package org.cps.labman.controller;

import org.cps.labman.dto.UserDto;
import org.cps.labman.persistence.model.User;
import org.cps.labman.service.AppointmentService;
import org.cps.labman.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * This class helps to manage authorization and authentication
 */
@Controller
public class AuthController {
    private UserService userService;
    final private AppointmentService appointmentService;

    @Value("${spring.application.name}")
    String appName;

    public AuthController(UserService userService,
                          AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = {"/", "index"})
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/account")
    public String accountPage(Model model) {
        model.addAttribute("page", "account.html");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("appointments", appointmentService.findAppointments(user.getId()));
        return "main";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result,
                               Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.createUser(userDto);
        return "redirect:/register?success";
    }
}