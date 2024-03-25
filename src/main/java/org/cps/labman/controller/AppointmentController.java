package org.cps.labman.controller;

import org.cps.labman.dto.AppointmentDto;
import org.cps.labman.persistence.model.User;
import org.cps.labman.service.AppointmentService;
import org.cps.labman.service.LocationService;
import org.cps.labman.service.TestService;
import org.cps.labman.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * This class provider api mappings for appointment related features
 */
@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final LocationService locationService;
    private final UserService userService;
    private final TestService testService;

    public AppointmentController(AppointmentService appointmentService,
                                 LocationService locationService,
                                 UserService userService,
                                 TestService testService) {
        this.appointmentService = appointmentService;
        this.locationService = locationService;
        this.userService = userService;
        this.testService = testService;
    }

    @GetMapping("/appointment/create")
    public String showCreateForm(Model model) {
        model.addAttribute("page", "appointment.html");
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setStatus("SCHEDULED");
        model.addAttribute("appointment", appointmentDto);
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("tests", testService.findAll());
        List<String> appointmentTimes = List.of("9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM",
                "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");
        model.addAttribute("appointmentTimes", appointmentTimes);
        return "main";
    }

    @PostMapping("/appointment/user/save")
    public String create(@Valid @ModelAttribute("appointment") AppointmentDto appointmentDto,
                         BindingResult result,
                         Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(authentication.getName());
        appointmentService.createAppointment(user.getId(), appointmentDto);
        return "redirect:/appointment/create?success";
    }

    @PostMapping("/appointment/save")
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentDto appointmentDto,
                         BindingResult result,
                         Model model) {
        appointmentService.createAppointment(appointmentDto.getUserId(), appointmentDto);
        return "redirect:/appointment/manage?success";
    }

    @GetMapping("/appointment/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("page", "appointment.html");
        model.addAttribute("appointment", appointmentService.editAppointment(id));
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("tests", testService.findAll());
        List<String> appointmentTimes = List.of("9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM",
                "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM");
        model.addAttribute("appointmentTimes", appointmentTimes);
        return "main";
    }

    @GetMapping("/appointment/{id}/cancel")
    public String showCancelForm(@PathVariable("id") Long id, Model model) {
        appointmentService.deleteAppointment(id);
        return "redirect:/account?success";
    }

    @GetMapping("/appointment/manage")
    public String showManageAppointmentForm(Model model) {
        model.addAttribute("page", "manage-appointment.html");
        model.addAttribute("appointment", new AppointmentDto());
        return "main";
    }

    @GetMapping("/appointment/find")
    public String findAppointment(@RequestParam("id") Long id, Model model) {
        model.addAttribute("page", "manage-appointment.html");
        AppointmentDto appointmentDto = appointmentService.editAppointment(id);
        if (appointmentDto == null) {
            return "redirect:/appointment/manage?not_found";
        } else {
            model.addAttribute("appointment", appointmentDto);
            return "main";
        }
    }
}
