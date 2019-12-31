package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleVoteFeedbackCreateDTO;
import solvve.course.dto.RoleVoteFeedbackReadDTO;
import solvve.course.service.RoleVoteFeedbackService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolevotefeedback")
public class RoleVoteFeedbackController {

    @Autowired
    private RoleVoteFeedbackService roleVoteFeedback;

    @GetMapping("/{id}")
    public RoleVoteFeedbackReadDTO getRoleVoteFeedback(@PathVariable UUID id) {
        return roleVoteFeedback.getRoleVoteFeedback(id);
    }

    @PostMapping
    public RoleVoteFeedbackReadDTO createRoleVoteFeedback(@RequestBody RoleVoteFeedbackCreateDTO createDTO){
        return roleVoteFeedback.createRoleVoteFeedback(createDTO);
    }
}
