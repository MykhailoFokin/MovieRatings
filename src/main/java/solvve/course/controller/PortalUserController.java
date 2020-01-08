package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.service.PortalUserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portalusers")
public class PortalUserController {

    @Autowired
    private PortalUserService portalUserService;

    @GetMapping("/{id}")
    public PortalUserReadDTO getPortalUsers(@PathVariable UUID id) {
        return portalUserService.getPortalUsers(id);
    }

    @PostMapping
    public PortalUserReadDTO createPortalUsers(@RequestBody PortalUserCreateDTO createDTO){
        return portalUserService.createPortalUsers(createDTO);
    }
}
