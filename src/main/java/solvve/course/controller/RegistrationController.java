package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.service.PortalUserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    @Autowired
    private PortalUserService portalUserService;

    @PostMapping
    public PortalUserReadDTO createPortalUser(@RequestBody @Valid PortalUserCreateDTO createDTO) {
        return portalUserService.createPortalUser(createDTO);
    }
}
