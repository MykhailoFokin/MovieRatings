package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.RoleService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PublicAccess
    @GetMapping("/{id}")
    public RoleReadDTO getRole(@PathVariable UUID id) {
        return roleService.getRole(id);
    }

    @AdminOrContentManager
    @PostMapping
    public RoleReadDTO createRole(@RequestBody @Valid RoleCreateDTO createDTO) {
        return roleService.createRole(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public RoleReadDTO patchRole(@PathVariable UUID id, @RequestBody @Valid RolePatchDTO patch) {
        return roleService.patchRole(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public RoleReadDTO putRole(@PathVariable UUID id, @RequestBody @Valid RolePutDTO put) {
        return roleService.updateRole(id, put);
    }

    @PublicAccess
    @GetMapping("/leader-board")
    public List<RoleInLeaderBoardReadDTO> getMovieLeaderBoard() {
        return roleService.getRolesLeaderBoard();
    }
}
