package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.PortalUsersCreateDTO;
import solvve.course.dto.PortalUsersReadDTO;
import solvve.course.service.PortalUsersService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portalusers")
public class PortalUsersController {

    @Autowired
    private PortalUsersService portalUsersService;

    @GetMapping("/{id}")
    public PortalUsersReadDTO getPortalUsers(@PathVariable UUID id) {
        return portalUsersService.getPortalUsers(id);
    }

    @PostMapping
    public PortalUsersReadDTO createPortalUsers(@RequestBody PortalUsersCreateDTO createDTO){
        return portalUsersService.createPortalUsers(createDTO);
    }
}
