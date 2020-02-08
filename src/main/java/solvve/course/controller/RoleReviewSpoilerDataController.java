package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataPutDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.service.RoleSpoilerDataService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolereviews/{rolereviewid}/rolespoilerdatas")
public class RoleReviewSpoilerDataController {

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

    @GetMapping
    public List<RoleSpoilerDataReadDTO> getRoleReviewSpoilerData(
            @PathVariable(value = "rolereviewid") UUID roleReviewId) {
        return roleSpoilerDataService.getRoleReviewSpoilerData(roleReviewId);
    }

    @PostMapping
    public RoleSpoilerDataReadDTO createRoleSpoilerData(@PathVariable (value = "rolereviewid") UUID roleReviewId,
                                                        @RequestBody RoleSpoilerDataCreateDTO createDTO) {
        return roleSpoilerDataService.createRoleReviewSpoilerData(roleReviewId, createDTO);
    }

    @PatchMapping("/{id}")
    public RoleSpoilerDataReadDTO patchRoleSpoilerData(@PathVariable (value = "rolereviewid") UUID roleReviewId,
                                                       @PathVariable (value = "id") UUID id,
                                                       @RequestBody RoleSpoilerDataPatchDTO patch) {
        return roleSpoilerDataService.patchRoleReviewSpoilerData(roleReviewId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleSpoilerData(@PathVariable (value = "rolereviewid") UUID roleReviewId,
                                      @PathVariable (value = "id") UUID id) {
        roleSpoilerDataService.deleteRoleReviewSpoilerData(roleReviewId, id);
    }

    @PutMapping("/{id}")
    public RoleSpoilerDataReadDTO putRoleSpoilerData(@PathVariable (value = "rolereviewid") UUID roleReviewId,
                                                     @PathVariable (value = "id") UUID id,
                                                     @RequestBody RoleSpoilerDataPutDTO put) {
        return roleSpoilerDataService.putRoleReviewSpoilerData(roleReviewId, id, put);
    }
}
