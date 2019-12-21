package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.RoleService;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public RoleReadDTO getRole(@PathVariable UUID id) {
        return roleService.getRole(id);
    }
}
