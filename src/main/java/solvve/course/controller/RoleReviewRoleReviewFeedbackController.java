package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.service.RoleReviewRoleReviewFeedbackService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role-reviews/{roleReviewId}/role-review-feedbacks")
public class RoleReviewRoleReviewFeedbackController {

    @Autowired
    private RoleReviewRoleReviewFeedbackService roleReviewFeedbackService;

    @GetMapping
    public List<RoleReviewFeedbackReadDTO> getRoleReviewSpoilerData(
            @PathVariable UUID roleReviewId) {
        return roleReviewFeedbackService.getRoleReviewRoleReviewFeedback(roleReviewId);
    }

    @PostMapping
    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(@PathVariable UUID roleReviewId,
                                                              @RequestBody RoleReviewFeedbackCreateDTO createDTO) {
        return roleReviewFeedbackService.createRoleReviewRoleReviewFeedback(roleReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(@PathVariable UUID roleReviewId,
                                                             @PathVariable (value = "id") UUID id,
                                                             @RequestBody RoleReviewFeedbackPatchDTO patch) {
        return roleReviewFeedbackService.patchRoleReviewRoleReviewFeedback(roleReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleReviewFeedback(@PathVariable UUID roleReviewId,
                                         @PathVariable (value = "id") UUID id) {
        roleReviewFeedbackService.deleteRoleReviewRoleReviewFeedback(roleReviewId, id);
    }

    @PutMapping("/{id}")
    public RoleReviewFeedbackReadDTO putRoleReviewFeedback(@PathVariable UUID roleReviewId,
                                                           @PathVariable (value = "id") UUID id,
                                                           @RequestBody RoleReviewFeedbackPutDTO put) {
        return roleReviewFeedbackService.updateRoleReviewRoleReviewFeedback(roleReviewId, id, put);
    }
}
