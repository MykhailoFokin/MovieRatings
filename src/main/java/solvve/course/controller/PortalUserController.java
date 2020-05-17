package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.Admin;
import solvve.course.controller.security.AdminOrCurrentUser;
import solvve.course.dto.PortalUserCreateDTO;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserPutDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.service.PortalUserService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portalusers")
public class PortalUserController {

    @Autowired
    private PortalUserService portalUserService;

    @Admin
    @GetMapping("/{id}")
    public PortalUserReadDTO getPortalUser(@PathVariable UUID id) {
        return portalUserService.getPortalUser(id);
    }

    @Admin
    @PostMapping
    public PortalUserReadDTO createPortalUser(@RequestBody @Valid PortalUserCreateDTO createDTO) {
        return portalUserService.createPortalUser(createDTO);
    }

    @AdminOrCurrentUser
    @PatchMapping("/{id}")
    public PortalUserReadDTO patchPortalUser(@PathVariable UUID id, @RequestBody @Valid PortalUserPatchDTO patch) {
        return portalUserService.patchPortalUser(id, patch);
    }

    @Admin
    @DeleteMapping("/{id}")
    public void deletePortalUser(@PathVariable UUID id) {
        portalUserService.deletePortalUser(id);
    }

    @Admin
    @PutMapping("/{id}")
    public PortalUserReadDTO putPortalUser(@PathVariable UUID id, @RequestBody @Valid PortalUserPutDTO put) {
        return portalUserService.updatePortalUser(id, put);
    }
}
