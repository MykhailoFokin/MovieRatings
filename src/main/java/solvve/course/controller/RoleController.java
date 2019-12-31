package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleCreateDTO;
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

    @PostMapping
    public RoleReadDTO createRole(@RequestBody RoleCreateDTO createDTO) {
        return roleService.createRole(createDTO);
    }
}
