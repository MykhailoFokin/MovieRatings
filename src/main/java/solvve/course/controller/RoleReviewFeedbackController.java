package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackPatchDTO;
import solvve.course.dto.RoleReviewFeedbackPutDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.service.RoleReviewFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewfeedbacks")
public class RoleReviewFeedbackController {

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedback;

    @GetMapping("/{id}")
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(@PathVariable UUID id) {
        return roleReviewFeedback.getRoleReviewFeedback(id);
    }

    @PostMapping
    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(@RequestBody RoleReviewFeedbackCreateDTO createDTO) {
        return roleReviewFeedback.createRoleReviewFeedback(createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReviewFeedbackReadDTO patchRoleReviewFeedback(@PathVariable UUID id, @RequestBody RoleReviewFeedbackPatchDTO patch) {
        return roleReviewFeedback.patchRoleReviewFeedback(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleReviewFeedback(@PathVariable UUID id) {
        roleReviewFeedback.deleteRoleReviewFeedback(id);
    }

    @PutMapping("/{id}")
    public RoleReviewFeedbackReadDTO putRoleReviewFeedback(@PathVariable UUID id, @RequestBody RoleReviewFeedbackPutDTO put) {
        return roleReviewFeedback.putRoleReviewFeedback(id, put);
    }
}
