package com.foi.nloncar.thesis_manager.rest.reservation;

import com.foi.nloncar.thesis_manager.dto.request.CreateReservationRequest;
import com.foi.nloncar.thesis_manager.dto.resource.MessageResponse;
import com.foi.nloncar.thesis_manager.dto.resource.ThesisReservationDto;
import com.foi.nloncar.thesis_manager.exception.AuthorizationException;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisReservation;
import com.foi.nloncar.thesis_manager.model.ThesisReservationStatus;
import com.foi.nloncar.thesis_manager.model.ThesisStatus;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisReservationRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import com.foi.nloncar.thesis_manager.rest.thesis.ThesisService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThesisReservationService {

	private final ThesisReservationRepository reservationRepository;
	private final ThesisRepository thesisRepository;
	private final UserRepository userRepository;
	private final ThesisService thesisService;

	public ThesisReservationService(ThesisReservationRepository reservationRepository,
									 ThesisRepository thesisRepository,
									 UserRepository userRepository,
									 ThesisService thesisService) {
		this.reservationRepository = reservationRepository;
		this.thesisRepository = thesisRepository;
		this.userRepository = userRepository;
		this.thesisService = thesisService;
	}

	public ThesisReservationDto createReservation(CreateReservationRequest request, Integer studentId) {
		validateCreate(request, studentId);

		User student = userRepository.findById(studentId).orElseThrow(
				() -> new NotFoundException("Student not found"));
		Thesis thesis = thesisRepository.findById(request.thesisId()).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		ThesisReservation reservation = new ThesisReservation(student, thesis, ThesisReservationStatus.PENDING);

		ThesisReservation saved = saveReservation(reservation);
		return toDto(saved);
	}

	public ThesisReservation saveReservation(ThesisReservation reservation) {
		return reservationRepository.save(reservation);
	}

	public List<ThesisReservationDto> getReservationsForThesis(Integer thesisId) {
		return reservationRepository.findByThesisId(thesisId).stream().map(this::toDto).toList();
	}

	public MessageResponse approveReservation(Integer id, Integer currentUserId) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		validateDecision(reservation, currentUserId);

		reservation.setStatus(ThesisReservationStatus.APPROVED);
		saveReservation(reservation);

		Thesis thesis = reservation.getThesis();
		thesis.setStudent(reservation.getStudent());
		thesis.setReservedAt(LocalDateTime.now());
		thesis.setStatus(ThesisStatus.IN_PROGRESS);
		thesisService.saveThesis(thesis);

		denyOtherPendingReservations(thesis.getId(), reservation.getId());

		return new MessageResponse("Reservation approved");
	}

	public MessageResponse denyReservation(Integer id, Integer currentUserId) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		validateDecision(reservation, currentUserId);

		reservation.setStatus(ThesisReservationStatus.DENIED);
		saveReservation(reservation);
		return new MessageResponse("Reservation denied");
	}

	public MessageResponse cancelReservation(Integer id, Integer currentUserId) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		validateCancel(reservation, currentUserId);

		reservation.setStatus(ThesisReservationStatus.CANCELED);
		saveReservation(reservation);
		return new MessageResponse("Reservation canceled");
	}

	private void denyOtherPendingReservations(Integer thesisId, Integer approvedReservationId) {
		List<ThesisReservation> pending = reservationRepository.findByThesisId(thesisId).stream()
				.filter(reservation -> !reservation.getId().equals(approvedReservationId))
				.filter(reservation -> reservation.getStatus() == ThesisReservationStatus.PENDING)
				.toList();

		for (ThesisReservation reservation : pending) {
			reservation.setStatus(ThesisReservationStatus.DENIED);
			saveReservation(reservation);
		}
	}

	private void validateCreate(CreateReservationRequest request, Integer studentId) {
		boolean hasActiveReservation = reservationRepository.findByStudentId(studentId).stream()
				.anyMatch(reservation -> reservation.getStatus() == ThesisReservationStatus.PENDING
						|| reservation.getStatus() == ThesisReservationStatus.APPROVED);

		if (hasActiveReservation) {
			throw new ValidationException("You already have a pending or approved reservation");
		}

		Thesis thesis = thesisRepository.findById(request.thesisId()).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		if (thesis.getStudent() != null) {
			throw new ValidationException("This thesis is already reserved");
		}
	}

	private void validateCancel(ThesisReservation reservation, Integer currentUserId) {
		if (!reservation.getStudent().getId().equals(currentUserId)) {
			throw new AuthorizationException("You can only cancel your own reservation");
		}

		if (reservation.getStatus() != ThesisReservationStatus.PENDING) {
			throw new ValidationException("Only pending reservations can be canceled");
		}
	}

	private void validateDecision(ThesisReservation reservation, Integer currentUserId) {
		if (!reservation.getThesis().getMentor().getId().equals(currentUserId)) {
			throw new AuthorizationException("Only the thesis's mentor can approve or deny reservations");
		}

		if (reservation.getStatus() != ThesisReservationStatus.PENDING) {
			throw new ValidationException("Only pending reservations can be approved or denied");
		}
	}

	private ThesisReservationDto toDto(ThesisReservation reservation) {
		return new ThesisReservationDto(
				reservation.getId(),
				reservation.getThesis().getId(),
				reservation.getThesis().getTitle(),
				reservation.getStudent().getId(),
				reservation.getStudent().fullName(),
				reservation.getStatus().name()
		);
	}
}
