package org.cps.labman.controller;

import org.cps.labman.dto.LocationDto;
import org.cps.labman.dto.TestDto;
import org.cps.labman.service.LocationService;
import org.cps.labman.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * This class manages laboratory management related activities like manage location and tests.
 */
@Controller
public class ManageLabController {

    private final LocationService locationService;
    private final TestService testService;

    public ManageLabController(LocationService locationService, TestService testService) {
        this.locationService = locationService;
        this.testService = testService;
    }

    @GetMapping("/manage-lab")
    public String showManageForm(Model model) {
        model.addAttribute("page", "manage-lab.html");
        model.addAttribute("location", new LocationDto());
        model.addAttribute("locations", locationService.findAll());
        model.addAttribute("test", new TestDto());
        model.addAttribute("tests", testService.findAll());
        return "main";
    }

    @PostMapping("/manage-lab/location/save")
    public String create(@Valid @ModelAttribute("location") LocationDto locationDto,
                         BindingResult result,
                         Model model) {
        locationService.create(locationDto);
        return "redirect:/manage-lab?success";
    }

    @GetMapping("/manage-lab/location/{id}/delete")
    public String deleteLocation(@PathVariable("id") Long id, Model model) {
        locationService.delete(id);
        return "redirect:/manage-lab?success";
    }

    @PostMapping("/manage-lab/test/save")
    public String create(@Valid @ModelAttribute("test") TestDto testDto,
                         BindingResult result,
                         Model model) {
        testService.create(testDto);
        return "redirect:/manage-lab?success";
    }

    @GetMapping("/manage-lab/test/{id}/delete")
    public String deleteTest(@PathVariable("id") Long id, Model model) {
        testService.delete(id);
        return "redirect:/manage-lab?success";
    }
}
