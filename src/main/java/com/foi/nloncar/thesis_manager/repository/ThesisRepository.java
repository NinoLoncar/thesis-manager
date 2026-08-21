package com.foi.nloncar.thesis_manager.repository;

import com.foi.nloncar.thesis_manager.model.Thesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThesisRepository extends JpaRepository<Thesis, Integer> {

	@Query("""
			select t from Thesis t
			where (:mentorId is null or t.mentor.id = :mentorId)
			and (:studentId is null or t.student.id = :studentId)
			and (:title is null or lower(t.title) like lower(concat('%', :title, '%')))
			and (:mentorName is null or lower(concat(t.mentor.firstName, ' ', t.mentor.lastName)) like lower(concat('%', :mentorName, '%')))
			and (:reserved is null or (:reserved = true and t.student is not null) or (:reserved = false and t.student is null))
			""")
	List<Thesis> search(@Param("mentorId") Integer mentorId,
						 @Param("studentId") Integer studentId,
						 @Param("title") String title,
						 @Param("mentorName") String mentorName,
						 @Param("reserved") Boolean reserved);

	boolean existsByMentorId(Integer mentorId);

	boolean existsByStudentId(Integer studentId);
}
