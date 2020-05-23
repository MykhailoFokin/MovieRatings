package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.PortalUserPatchDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.service.ModeratorPortalUserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderator/{id}/portal-users")
public class ModeratorPortalUserController {

    @Autowired
    private ModeratorPortalUserService moderatorPortalUserService;

    @AdminOrModerator
    @GetMapping
    public List<PortalUserReadDTO> getBlockedPortalUsers(@PathVariable UUID id) {
        return moderatorPortalUserService.getBlockedPortalUsers(id);
    }

    @AdminOrModerator
    @PatchMapping("/{portalUserId}")
    public PortalUserReadDTO blockUnblockPortalUser(@PathVariable UUID id,
                                             @PathVariable UUID portalUserId,
                                             @RequestBody @Valid PortalUserPatchDTO patch) {
        return moderatorPortalUserService.blockUnblockPortalUser(id, portalUserId, patch);
    }
}
