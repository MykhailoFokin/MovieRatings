package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.controller.validation.ControllerValidationUtil;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.service.RoleSpoilerDataService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolespoilerdata")
public class RoleSpoilerDataController {

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

    @AdminOrModerator
    @GetMapping("/{id}")
    public RoleSpoilerDataReadDTO getRoleSpoilerData(@PathVariable UUID id) {
        return roleSpoilerDataService.getRoleSpoilerData(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleSpoilerDataReadDTO createRoleSpoilerData(@RequestBody @Valid RoleSpoilerDataCreateDTO createDTO) {
        ControllerValidationUtil.validateLessThan(createDTO.getStartIndex(), createDTO.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.createRoleSpoilerData(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleSpoilerDataReadDTO patchRoleSpoilerData(@PathVariable UUID id,
                                                       @RequestBody RoleSpoilerDataPatchDTO patch) {
        ControllerValidationUtil.validateLessThan(patch.getStartIndex(), patch.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.patchRoleSpoilerData(id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleSpoilerData(@PathVariable UUID id) {
        roleSpoilerDataService.deleteRoleSpoilerData(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleSpoilerDataReadDTO putRoleSpoilerData(@PathVariable UUID id,
                                                     @RequestBody @Valid RoleSpoilerDataPutDTO put) {
        ControllerValidationUtil.validateLessThan(put.getStartIndex(), put.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.updateRoleSpoilerData(id, put);
    }
}
