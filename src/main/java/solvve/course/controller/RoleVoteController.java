package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.service.RoleVoteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolevotes")
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

    @PatchMapping("/{id}")
    public RoleVoteReadDTO patchRoleVote(@PathVariable UUID id, @RequestBody RoleVotePatchDTO patch){
        return roleVoteService.patchRoleVote(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleVote(@PathVariable UUID id){
        roleVoteService.deleteRoleVote(id);
    }
}
