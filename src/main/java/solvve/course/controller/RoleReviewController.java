package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.service.RoleReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereview")
public class RoleReviewController {

    @Autowired
    private RoleReviewService roleReviewService;

    @GetMapping("/{id}")
    public RoleReviewReadDTO getRoleReview(@PathVariable UUID id) {
        return roleReviewService.getRoleReview(id);
    }

    @PostMapping
    public RoleReviewReadDTO createRoleReview(@RequestBody RoleReviewCreateDTO createDTO){
        return roleReviewService.createRoleReview(createDTO);
    }
}
