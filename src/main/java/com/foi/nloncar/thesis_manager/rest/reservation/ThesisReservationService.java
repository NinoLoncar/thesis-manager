package com.foi.nloncar.thesis_manager.rest.reservation;

import com.foi.nloncar.thesis_manager.dto.request.CreateReservationRequest;
import com.foi.nloncar.thesis_manager.dto.resource.ThesisReservationDto;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisReservation;
import com.foi.nloncar.thesis_manager.model.ThesisReservationStatus;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisReservationRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThesisReservationService {

	private final ThesisReservationRepository reservationRepository;
	private final ThesisRepository thesisRepository;
	private final UserRepository userRepository;

	public ThesisReservationService(ThesisReservationRepository reservationRepository,
									 ThesisRepository thesisRepository,
									 UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.thesisRepository = thesisRepository;
		this.userRepository = userRepository;
	}

	public ThesisReservationDto createReservation(CreateReservationRequest request, Integer studentId) {
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

	public ThesisReservationDto approveReservation(Integer id) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		reservation.setStatus(ThesisReservationStatus.APPROVED);
		ThesisReservation saved = saveReservation(reservation);

		Thesis thesis = reservation.getThesis();
		thesis.setStudent(reservation.getStudent());
		thesis.setReservedAt(LocalDateTime.now());
		thesisRepository.save(thesis);

		denyOtherPendingReservations(thesis.getId(), reservation.getId());

		return toDto(saved);
	}

	public ThesisReservationDto denyReservation(Integer id) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		reservation.setStatus(ThesisReservationStatus.DENIED);
		return toDto(saveReservation(reservation));
	}

	public ThesisReservationDto cancelReservation(Integer id) {
		ThesisReservation reservation = reservationRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Reservation not found"));

		reservation.setStatus(ThesisReservationStatus.CANCELED);
		return toDto(saveReservation(reservation));
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
