package com.foi.nloncar.thesis_manager.rest.reservation;

import com.foi.nloncar.thesis_manager.annotation.RequiresPermission;
import com.foi.nloncar.thesis_manager.dto.request.CreateReservationRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ThesisReservationController {

	private final ThesisReservationService reservationService;

	public ThesisReservationController(ThesisReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	@RequiresPermission("RESERVATION_CREATE")
	public ResponseEntity<?> createReservation(@RequestBody CreateReservationRequest request, HttpSession session) {
		Integer studentId = (Integer) session.getAttribute("userId");
		return ResponseEntity.ok().body(reservationService.createReservation(request, studentId));
	}

	@GetMapping
	@RequiresPermission("RESERVATION_MANAGE")
	public ResponseEntity<?> getReservations(@RequestParam(name = "thesisId") Integer thesisId) {
		return ResponseEntity.ok().body(reservationService.getReservationsForThesis(thesisId));
	}

	@PutMapping("/{id}/approve")
	@RequiresPermission("RESERVATION_MANAGE")
	public ResponseEntity<?> approveReservation(@PathVariable("id") Integer id, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		return ResponseEntity.ok().body(reservationService.approveReservation(id, currentUserId));
	}

	@PutMapping("/{id}/deny")
	@RequiresPermission("RESERVATION_MANAGE")
	public ResponseEntity<?> denyReservation(@PathVariable("id") Integer id, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		return ResponseEntity.ok().body(reservationService.denyReservation(id, currentUserId));
	}

	@PutMapping("/{id}/cancel")
	@RequiresPermission("RESERVATION_CREATE")
	public ResponseEntity<?> cancelReservation(@PathVariable("id") Integer id, HttpSession session) {
		Integer currentUserId = (Integer) session.getAttribute("userId");
		return ResponseEntity.ok().body(reservationService.cancelReservation(id, currentUserId));
	}
}
