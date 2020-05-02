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
import solvve.course.service.RoleReviewSpoilerDataService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/role-reviews/{roleReviewId}/role-spoiler-datas")
public class RoleReviewSpoilerDataController {

    @Autowired
    private RoleReviewSpoilerDataService roleSpoilerDataService;

    @AdminOrModerator
    @GetMapping
    public List<RoleSpoilerDataReadDTO> getRoleReviewSpoilerData(
            @PathVariable UUID roleReviewId) {
        return roleSpoilerDataService.getRoleReviewSpoilerData(roleReviewId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RoleSpoilerDataReadDTO createRoleSpoilerData(@PathVariable UUID roleReviewId,
                                                        @RequestBody @Valid RoleSpoilerDataCreateDTO createDTO) {
        ControllerValidationUtil.validateLessThan(createDTO.getStartIndex(), createDTO.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.createRoleReviewSpoilerData(roleReviewId, createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public RoleSpoilerDataReadDTO patchRoleSpoilerData(@PathVariable UUID roleReviewId,
                                                       @PathVariable (value = "id") UUID id,
                                                       @RequestBody RoleSpoilerDataPatchDTO patch) {
        ControllerValidationUtil.validateLessThan(patch.getStartIndex(), patch.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.patchRoleReviewSpoilerData(roleReviewId, id, patch);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteRoleSpoilerData(@PathVariable UUID roleReviewId,
                                      @PathVariable (value = "id") UUID id) {
        roleSpoilerDataService.deleteRoleReviewSpoilerData(roleReviewId, id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public RoleSpoilerDataReadDTO putRoleSpoilerData(@PathVariable UUID roleReviewId,
                                                     @PathVariable (value = "id") UUID id,
                                                     @RequestBody @Valid RoleSpoilerDataPutDTO put) {
        ControllerValidationUtil.validateLessThan(put.getStartIndex(), put.getEndIndex(),
                "startIndex", "endIndex");
        return roleSpoilerDataService.updateRoleReviewSpoilerData(roleReviewId, id, put);
    }
}
