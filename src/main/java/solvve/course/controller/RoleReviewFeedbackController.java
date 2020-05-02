package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.service.RoleReviewFeedbackService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewfeedbacks")
public class RoleReviewFeedbackController {

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedback;

    @AdminOrModerator
    @GetMapping("/{id}")
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(@PathVariable UUID id) {
        return roleReviewFeedback.getRoleReviewFeedback(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(@RequestBody
                                                                  @Valid RoleReviewFeedbackCreateDTO createDTO) {
        return roleReviewFeedback.createRoleReviewFeedback(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(@PathVariable UUID id,
                                                             @RequestBody RoleReviewFeedbackPatchDTO patch) {
        return roleReviewFeedback.patchRoleReviewFeedback(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleReviewFeedback(@PathVariable UUID id) {
        roleReviewFeedback.deleteRoleReviewFeedback(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleReviewFeedbackReadDTO putRoleReviewFeedback(@PathVariable UUID id,
                                                           @RequestBody @Valid RoleReviewFeedbackPutDTO put) {
        return roleReviewFeedback.updateRoleReviewFeedback(id, put);
    }
}
