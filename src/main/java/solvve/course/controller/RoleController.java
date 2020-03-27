package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleCreateDTO;
import solvve.course.dto.RolePatchDTO;
import solvve.course.dto.RolePutDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.RoleService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public RoleReadDTO getRole(@PathVariable UUID id) {
        return roleService.getRole(id);
    }

    @PostMapping
    public RoleReadDTO createRole(@RequestBody @Valid RoleCreateDTO createDTO) {
        return roleService.createRole(createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReadDTO patchRole(@PathVariable UUID id, @RequestBody @Valid RolePatchDTO patch) {
        return roleService.patchRole(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
    }

    @PutMapping("/{id}")
    public RoleReadDTO putRole(@PathVariable UUID id, @RequestBody @Valid RolePutDTO put) {
        return roleService.updateRole(id, put);
    }
}
