package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantPatchDTO;
import solvve.course.dto.RoleReviewCompliantPutDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.service.RoleReviewRoleReviewCompliantService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role-reviews/{roleReviewId}/role-review-compliants")
public class RoleReviewRoleReviewCompliantController {

    @Autowired
    private RoleReviewRoleReviewCompliantService roleReviewCompliantService;

    @GetMapping
    public List<RoleReviewCompliantReadDTO> getRoleReviewSpoilerData(
            @PathVariable UUID roleReviewId) {
        return roleReviewCompliantService.getRoleReviewRoleReviewCompliant(roleReviewId);
    }

    @PostMapping
    public RoleReviewCompliantReadDTO createRoleReviewCompliant(
            @PathVariable UUID roleReviewId,
            @RequestBody @Valid RoleReviewCompliantCreateDTO createDTO) {
        return roleReviewCompliantService.createRoleReviewRoleReviewCompliant(roleReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReviewCompliantReadDTO patchRoleReviewCompliant(
            @PathVariable UUID roleReviewId,
            @PathVariable (value = "id") UUID id,
            @RequestBody @Valid RoleReviewCompliantPatchDTO patch) {
        return roleReviewCompliantService.patchRoleReviewRoleReviewCompliant(roleReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleReviewCompliant(@PathVariable UUID roleReviewId,
                                          @PathVariable (value = "id") UUID id) {
        roleReviewCompliantService.deleteRoleReviewRoleReviewCompliant(roleReviewId, id);
    }

    @PutMapping("/{id}")
    public RoleReviewCompliantReadDTO putRoleReviewCompliant(@PathVariable UUID roleReviewId,
                                                             @PathVariable (value = "id") UUID id,
                                                             @RequestBody @Valid RoleReviewCompliantPutDTO put) {
        return roleReviewCompliantService.updateRoleReviewRoleReviewCompliant(roleReviewId, id, put);
    }
}
