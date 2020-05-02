package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.service.RoleReviewService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviews")
public class RoleReviewController {

    @Autowired
    private RoleReviewService roleReviewService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public RoleReviewReadDTO getRoleReview(@PathVariable UUID id) {
        return roleReviewService.getRoleReview(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleReviewReadDTO createRoleReview(@RequestBody @Valid RoleReviewCreateDTO createDTO) {
        return roleReviewService.createRoleReview(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleReviewReadDTO patchRoleReview(@PathVariable UUID id, @RequestBody @Valid RoleReviewPatchDTO patch) {
        return roleReviewService.patchRoleReview(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleReview(@PathVariable UUID id) {
        roleReviewService.deleteRoleReview(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleReviewReadDTO putRoleReview(@PathVariable UUID id, @RequestBody @Valid RoleReviewPutDTO put) {
        return roleReviewService.updateRoleReview(id, put);
    }
}
