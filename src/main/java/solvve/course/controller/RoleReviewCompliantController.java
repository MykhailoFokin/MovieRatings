package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.service.RoleReviewCompliantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewcompliants")
public class RoleReviewCompliantController {

    @Autowired
    private RoleReviewCompliantService roleReviewCompliant;

    @GetMapping("/{id}")
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(@PathVariable UUID id) {
        return roleReviewCompliant.getRoleReviewCompliant(id);
    }

    @PostMapping
    public RoleReviewCompliantReadDTO createRoleReviewCompliant(@RequestBody RoleReviewCompliantCreateDTO createDTO) {
        return roleReviewCompliant.createRoleReviewCompliant(createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(@PathVariable UUID id,
                                                               @RequestBody RoleReviewCompliantPatchDTO patch) {
        return roleReviewCompliant.patchRoleReviewCompliant(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleReviewCompliant(@PathVariable UUID id) {
        roleReviewCompliant.deleteRoleReviewCompliant(id);
    }

    @PutMapping("/{id}")
    public RoleReviewCompliantReadDTO putRoleReviewCompliant(@PathVariable UUID id,
                                                             @RequestBody RoleReviewCompliantPutDTO put) {
        return roleReviewCompliant.putRoleReviewCompliant(id, put);
    }
}
