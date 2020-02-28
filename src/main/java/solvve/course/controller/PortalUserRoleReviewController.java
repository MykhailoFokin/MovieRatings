package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.service.PortalUserRoleReviewService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portal-user/{portalUserId}/role-reviews")
public class PortalUserRoleReviewController {

    @Autowired
    private PortalUserRoleReviewService portalUserRoleReviewService;

    @GetMapping
    public List<RoleReviewReadDTO> getRoleReviewSpoilerData(
            @PathVariable UUID portalUserId) {
        return portalUserRoleReviewService.getPortalUserRoleReview(portalUserId);
    }

    @PostMapping
    public RoleReviewReadDTO createRoleReview(
            @PathVariable UUID portalUserId,
            @RequestBody RoleReviewCreateDTO createDTO) {
        return portalUserRoleReviewService.createPortalUserRoleReview(portalUserId, createDTO);
    }

    @PatchMapping("/{id}")
    public RoleReviewReadDTO patchRoleReview(
            @PathVariable UUID portalUserId,
            @PathVariable (value = "id") UUID id,
            @RequestBody RoleReviewPatchDTO patch) {
        return portalUserRoleReviewService.patchPortalUserRoleReview(portalUserId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleReview(@PathVariable UUID portalUserId,
                                 @PathVariable (value = "id") UUID id) {
        portalUserRoleReviewService.deletePortalUserRoleReview(portalUserId, id);
    }

    @PutMapping("/{id}")
    public RoleReviewReadDTO putRoleReview(@PathVariable UUID portalUserId,
                                           @PathVariable(value = "id") UUID id,
                                           @RequestBody RoleReviewPutDTO put) {
        return portalUserRoleReviewService.updatePortalUserRoleReview(portalUserId, id, put);
    }
}
