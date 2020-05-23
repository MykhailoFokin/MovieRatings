package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.RegisteredUserAccess;
import solvve.course.controller.security.UserNotBlockedAccess;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.service.RolePortalUserRoleVoteService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role/{roleId}/portal-user/{id}/role-votes")
public class RolePortalUserRoleVoteController {

    @Autowired
    private RolePortalUserRoleVoteService rolePortalUserRoleVoteService;

    @RegisteredUserAccess
    @GetMapping
    public List<RoleVoteReadDTO> getRoleReviewSpoilerData(@PathVariable UUID roleId,
                                                          @PathVariable UUID id) {
        return rolePortalUserRoleVoteService.getRolesVotesByPortalUser(roleId, id);
    }

    @UserNotBlockedAccess
    @PostMapping
    public RoleVoteReadDTO createRoleReviewCompliant(@PathVariable UUID roleId,
                                                     @PathVariable UUID id,
                                                     @RequestBody @Valid RoleVoteCreateDTO createDTO) {
        return rolePortalUserRoleVoteService.createRoleVoteByPortalUser(id, roleId, createDTO);
    }
}
