package com.authorization.service.server.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping(value = "/")
public class AuthorizationController {

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping(value = {
            "me", "/principal"
    })
    public Principal me(@AuthenticationPrincipal Principal principal) {
        return principal;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal Principal principal) {
        model.addAttribute("principal", principal);
        return "home";
    }
}
