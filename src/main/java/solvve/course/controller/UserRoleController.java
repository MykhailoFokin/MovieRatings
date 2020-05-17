package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.Admin;
import solvve.course.domain.UserGroupType;
import solvve.course.dto.*;
import solvve.course.service.UserRoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @Admin
    @GetMapping("/{id}")
    public UserRoleReadDTO getUserRoles(@PathVariable UUID id) {
        return userRoleService.getUserRoles(id);
    }

    @Admin
    @GetMapping
    public List<UserRoleReadDTO> getUserRoles() {
        return userRoleService.getUserRoles();
    }

    @Admin
    @GetMapping("/role/{userGroupType}")
    public UserRoleReadDTO getUserRolesByGroupType(@PathVariable UserGroupType userGroupType) {
        return userRoleService.getUserRolesByUserGroupType(userGroupType);
    }
}
