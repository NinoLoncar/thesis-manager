package com.foi.nloncar.thesis_manager.rest.thesis;

import com.foi.nloncar.thesis_manager.dto.request.CreateThesisRequest;
import com.foi.nloncar.thesis_manager.dto.resource.ThesisDto;
import com.foi.nloncar.thesis_manager.dto.request.UpdateThesisRequest;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.exception.ValidationException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisReservation;
import com.foi.nloncar.thesis_manager.model.ThesisStatus;
import com.foi.nloncar.thesis_manager.model.ThesisType;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisReservationRepository;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ThesisService {

	private final ThesisRepository thesisRepository;
	private final UserRepository userRepository;
	private final ThesisReservationRepository reservationRepository;

	public ThesisService(ThesisRepository thesisRepository, UserRepository userRepository,
						  ThesisReservationRepository reservationRepository) {
		this.thesisRepository = thesisRepository;
		this.userRepository = userRepository;
		this.reservationRepository = reservationRepository;
	}

	public ThesisDto createThesis(CreateThesisRequest request, Integer mentorId) {
		validateTitle(request.title());
		ThesisType type = parseType(request.type());

		User mentor = userRepository.findById(mentorId).orElseThrow(
				() -> new NotFoundException("Mentor not found"));

		Thesis thesis = new Thesis(
				request.title(),
				request.abstractText(),
				type,
				ThesisStatus.PROPOSED,
				null,
				mentor
		);

		Thesis saved = saveThesis(thesis);
		return toDto(saved, null);
	}

	public Thesis saveThesis(Thesis thesis) {
		return thesisRepository.save(thesis);
	}

	public List<ThesisDto> getAllTheses(Integer mentorId, Integer studentId, String title, String mentorName,
										 Boolean reserved) {
		return thesisRepository.search(mentorId, studentId, title, mentorName, reserved).stream()
				.map(thesis -> toDto(thesis, null))
				.toList();
	}

	public void deleteThesis(Integer id) {
		Thesis thesis = thesisRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		if (thesis.getStudent() != null) {
			throw new ValidationException("Cannot delete a thesis that has a student assigned to it");
		}

		if (reservationRepository.existsByThesisId(id)) {
			throw new ValidationException("Cannot delete a thesis that has reservations");
		}

		thesisRepository.deleteById(id);
	}

	public ThesisDto getThesisById(Integer id, Integer currentUserId) {
		Thesis thesis = thesisRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Thesis not found"));
		return toDto(thesis, currentUserId);
	}

	public ThesisDto updateThesis(Integer id, UpdateThesisRequest request) {
		validateTitle(request.title());

		Thesis thesis = thesisRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Thesis not found"));

		thesis.setTitle(request.title());
		thesis.setAbstractText(request.abstractText());

		Thesis saved = saveThesis(thesis);
		return toDto(saved, null);
	}

	private static final int TITLE_MAX_LENGTH = 255;

	private void validateTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new ValidationException("Title is required");
		}
		if (title.length() > TITLE_MAX_LENGTH) {
			throw new ValidationException("Title must be at most " + TITLE_MAX_LENGTH + " characters");
		}
	}

	private ThesisType parseType(String type) {
		try {
			return ThesisType.valueOf(type);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new ValidationException("Invalid thesis type");
		}
	}

	private ThesisDto toDto(Thesis thesis, Integer currentUserId) {
		User student = thesis.getStudent();

		ThesisReservation myReservation = currentUserId != null
				? reservationRepository.findByThesisId(thesis.getId()).stream()
						.filter(reservation -> reservation.getStudent().getId().equals(currentUserId))
						.max(Comparator.comparing(ThesisReservation::getId))
						.orElse(null)
				: null;

		return new ThesisDto(
				thesis.getId(),
				thesis.getTitle(),
				thesis.getAbstractText(),
				thesis.getType().name(),
				thesis.getStatus().name(),
				thesis.getMentor().getId(),
				thesis.getMentor().fullName(),
				student != null ? student.getId() : null,
				student != null ? student.fullName() : null,
				myReservation != null ? myReservation.getId() : null,
				myReservation != null ? myReservation.getStatus().name() : null
		);
	}
}
