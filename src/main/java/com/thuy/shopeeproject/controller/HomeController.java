package com.thuy.shopeeproject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class HomeController {
    @GetMapping("/home")
    // @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAnyAuthority('user')")
    public String home(Model model) {
        model.addAttribute("message", model);
        return "This is homepage";
    }
}
