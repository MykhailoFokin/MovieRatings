package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.service.RoleVoteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolevote")
public class RoleVoteController {

    @Autowired
    private RoleVoteService roleVoteService;

    @GetMapping("/{id}")
    public RoleVoteReadDTO getRoleVote(@PathVariable UUID id) {
        return roleVoteService.getRoleVote(id);
    }

    @PostMapping
    public RoleVoteReadDTO createRoleVote(@RequestBody RoleVoteCreateDTO createDTO){
        return roleVoteService.createRoleVote(createDTO);
    }
}
