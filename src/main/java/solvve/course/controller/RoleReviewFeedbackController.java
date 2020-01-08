package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewFeedbackCreateDTO;
import solvve.course.dto.RoleReviewFeedbackReadDTO;
import solvve.course.service.RoleReviewFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewfeedback")
public class RoleReviewFeedbackController {

    @Autowired
    private RoleReviewFeedbackService roleReviewFeedback;

    @GetMapping("/{id}")
    public RoleReviewFeedbackReadDTO getRoleReviewFeedback(@PathVariable UUID id) {
        return roleReviewFeedback.getRoleReviewFeedback(id);
    }

    @PostMapping
    public RoleReviewFeedbackReadDTO createRoleReviewFeedback(@RequestBody RoleReviewFeedbackCreateDTO createDTO){
        return roleReviewFeedback.createRoleReviewFeedback(createDTO);
    }
}
