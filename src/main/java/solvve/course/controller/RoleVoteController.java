package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
import solvve.course.dto.RoleVotePutDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.service.RoleVoteService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolevotes")
public class RoleVoteController {

    @Autowired
    private RoleVoteService roleVoteService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public RoleVoteReadDTO getRoleVote(@PathVariable UUID id) {
        return roleVoteService.getRoleVote(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleVoteReadDTO createRoleVote(@RequestBody @Valid RoleVoteCreateDTO createDTO) {
        return roleVoteService.createRoleVote(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleVoteReadDTO patchRoleVote(@PathVariable UUID id, @RequestBody RoleVotePatchDTO patch) {
        return roleVoteService.patchRoleVote(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleVote(@PathVariable UUID id) {
        roleVoteService.deleteRoleVote(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleVoteReadDTO putRoleVote(@PathVariable UUID id, @RequestBody @Valid RoleVotePutDTO put) {
        return roleVoteService.updateRoleVote(id, put);
    }
}
