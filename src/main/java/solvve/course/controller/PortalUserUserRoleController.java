package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.PortalUserUserRoleReadDTO;
import solvve.course.service.PortalUserUserRoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portal-user/{portalUserId}/user-roles")
public class PortalUserUserRoleController {

    @Autowired
    private PortalUserUserRoleService portalUserUserRoleService;

    @PublicAccess
    @GetMapping
    public List<PortalUserUserRoleReadDTO> getPortalUserUserRoles(@PathVariable UUID portalUserId) {
        return portalUserUserRoleService.getPortalUserUserRoles(portalUserId);
    }

    @AdminOrContentManager
    @PostMapping("/{id}")
    public List<PortalUserUserRoleReadDTO> addUserRoleToPortalUser(@PathVariable UUID portalUserId,
                                                             @PathVariable UUID id) {
        return portalUserUserRoleService.addUserRoleToPortalUser(portalUserId, id);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public List<PortalUserUserRoleReadDTO> deleteUserRole(@PathVariable UUID portalUserId, @PathVariable UUID id) {
        return portalUserUserRoleService.removeUserRoleFromPortalUser(portalUserId, id);
    }
}
