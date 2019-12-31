package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleVoteCompliantCreateDTO;
import solvve.course.dto.RoleVoteCompliantReadDTO;
import solvve.course.service.RoleVoteCompliantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolevotecompliant")
public class RoleVoteCompliantController {

    @Autowired
    private RoleVoteCompliantService roleVoteCompliant;

    @GetMapping("/{id}")
    public RoleVoteCompliantReadDTO getRoleVoteCompliant(@PathVariable UUID id) {
        return roleVoteCompliant.getRoleVoteCompliant(id);
    }

    @PostMapping
    public RoleVoteCompliantReadDTO createRoleVoteCompliant(@RequestBody RoleVoteCompliantCreateDTO createDTO){
        return roleVoteCompliant.createRoleVoteCompliant(createDTO);
    }
}
