package com.foi.nloncar.thesis_manager.rest.thesis;

import com.foi.nloncar.thesis_manager.dto.CreateThesisRequest;
import com.foi.nloncar.thesis_manager.dto.ThesisDto;
import com.foi.nloncar.thesis_manager.exception.NotFoundException;
import com.foi.nloncar.thesis_manager.model.Thesis;
import com.foi.nloncar.thesis_manager.model.ThesisStatus;
import com.foi.nloncar.thesis_manager.model.ThesisType;
import com.foi.nloncar.thesis_manager.model.User;
import com.foi.nloncar.thesis_manager.repository.ThesisRepository;
import com.foi.nloncar.thesis_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThesisService {

	private final ThesisRepository thesisRepository;
	private final UserRepository userRepository;

	public ThesisService(ThesisRepository thesisRepository, UserRepository userRepository) {
		this.thesisRepository = thesisRepository;
		this.userRepository = userRepository;
	}

	public ThesisDto createThesis(CreateThesisRequest request, Integer mentorId) {
		User mentor = userRepository.findById(mentorId).orElseThrow(
				() -> new NotFoundException("Mentor not found"));

		Thesis thesis = new Thesis(
				request.title(),
				request.abstractText(),
				ThesisType.valueOf(request.type()),
				ThesisStatus.PROPOSED,
				null,
				mentor
		);

		Thesis saved = saveThesis(thesis);
		return toDto(saved);
	}

	public Thesis saveThesis(Thesis thesis) {
		return thesisRepository.save(thesis);
	}

	public List<ThesisDto> getAllTheses(Integer mentorId) {
		List<Thesis> theses = mentorId != null
				? thesisRepository.findByMentorId(mentorId)
				: thesisRepository.findAll();

		return theses.stream().map(this::toDto).toList();
	}

	private ThesisDto toDto(Thesis thesis) {
		User student = thesis.getStudent();

		return new ThesisDto(
				thesis.getId(),
				thesis.getTitle(),
				thesis.getAbstractText(),
				thesis.getType().name(),
				thesis.getStatus().name(),
				thesis.getMentor().getId(),
				thesis.getMentor().fullName(),
				student != null ? student.getId() : null,
				student != null ? student.fullName() : null
		);
	}
}
