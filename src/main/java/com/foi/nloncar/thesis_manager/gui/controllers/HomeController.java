package com.foi.nloncar.thesis_manager.gui.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("currentUserId", session.getAttribute("userId"));
        return "home";
    }
}
