package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewCompliantCreateDTO;
import solvve.course.dto.RoleReviewCompliantReadDTO;
import solvve.course.service.RoleReviewCompliantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviewcompliant")
public class RoleReviewCompliantController {

    @Autowired
    private RoleReviewCompliantService roleReviewCompliant;

    @GetMapping("/{id}")
    public RoleReviewCompliantReadDTO getRoleReviewCompliant(@PathVariable UUID id) {
        return roleReviewCompliant.getRoleReviewCompliant(id);
    }

    @PostMapping
    public RoleReviewCompliantReadDTO createRoleReviewCompliant(@RequestBody RoleReviewCompliantCreateDTO createDTO){
        return roleReviewCompliant.createRoleReviewCompliant(createDTO);
    }
}
