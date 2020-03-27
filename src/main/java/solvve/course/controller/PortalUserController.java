package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public PortalUserReadDTO getPortalUser(@PathVariable UUID id) {
        return portalUserService.getPortalUser(id);
    }

    @PostMapping
    public PortalUserReadDTO createPortalUser(@RequestBody @Valid PortalUserCreateDTO createDTO) {
        return portalUserService.createPortalUser(createDTO);
    }

    @PatchMapping("/{id}")
    public PortalUserReadDTO patchPortalUser(@PathVariable UUID id, @RequestBody @Valid PortalUserPatchDTO patch) {
        return portalUserService.patchPortalUser(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deletePortalUser(@PathVariable UUID id) {
        portalUserService.deletePortalUser(id);
    }

    @PutMapping("/{id}")
    public PortalUserReadDTO putPortalUser(@PathVariable UUID id, @RequestBody @Valid PortalUserPutDTO put) {
        return portalUserService.updatePortalUser(id, put);
    }
}
