package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.service.RoleReviewCompliantService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewcompliants")
public class RoleReviewCompliantController {

    @Autowired
    private RoleReviewCompliantService roleReviewCompliant;

    @AdminOrModerator
    @GetMapping("/{id}")
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(@PathVariable UUID id) {
        return roleReviewCompliant.getRoleReviewCompliant(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleReviewCompliantReadDTO createRoleReviewCompliant(@RequestBody
                                                                    @Valid RoleReviewCompliantCreateDTO createDTO) {
        return roleReviewCompliant.createRoleReviewCompliant(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(@PathVariable UUID id,
                                                               @RequestBody @Valid RoleReviewCompliantPatchDTO patch) {
        return roleReviewCompliant.patchRoleReviewCompliant(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleReviewCompliant(@PathVariable UUID id) {
        roleReviewCompliant.deleteRoleReviewCompliant(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleReviewCompliantReadDTO putRoleReviewCompliant(@PathVariable UUID id,
                                                             @RequestBody @Valid RoleReviewCompliantPutDTO put) {
        return roleReviewCompliant.updateRoleReviewCompliant(id, put);
    }
}
