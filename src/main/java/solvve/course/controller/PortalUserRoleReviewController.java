package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.CurrentUser;
import solvve.course.dto.RoleReviewCreateDTO;
import solvve.course.dto.RoleReviewPatchDTO;
import solvve.course.dto.RoleReviewPutDTO;
import solvve.course.dto.RoleReviewReadDTO;
import solvve.course.service.PortalUserRoleReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portal-user/{portalUserId}/role-reviews")
public class PortalUserRoleReviewController {

    @Autowired
    private PortalUserRoleReviewService portalUserRoleReviewService;

    @CurrentUser
    @GetMapping
    public List<RoleReviewReadDTO> getRoleReviewSpoilerData(
            @PathVariable UUID portalUserId) {
        return portalUserRoleReviewService.getPortalUserRoleReview(portalUserId);
    }

    @CurrentUser
    @PostMapping
    public RoleReviewReadDTO createRoleReview(
            @PathVariable UUID portalUserId,
            @RequestBody @Valid RoleReviewCreateDTO createDTO) {
        return portalUserRoleReviewService.createPortalUserRoleReview(portalUserId, createDTO);
    }

    @CurrentUser
    @PatchMapping("/{id}")
    public RoleReviewReadDTO patchRoleReview(
            @PathVariable UUID portalUserId,
            @PathVariable (value = "id") UUID id,
            @RequestBody @Valid RoleReviewPatchDTO patch) {
        return portalUserRoleReviewService.patchPortalUserRoleReview(portalUserId, id, patch);
    }

    @CurrentUser
    @DeleteMapping("/{id}")
    public void deleteRoleReview(@PathVariable UUID portalUserId,
                                 @PathVariable (value = "id") UUID id) {
        portalUserRoleReviewService.deletePortalUserRoleReview(portalUserId, id);
    }

    @CurrentUser
    @PutMapping("/{id}")
    public RoleReviewReadDTO putRoleReview(@PathVariable UUID portalUserId,
                                           @PathVariable(value = "id") UUID id,
                                           @RequestBody @Valid RoleReviewPutDTO put) {
        return portalUserRoleReviewService.updatePortalUserRoleReview(portalUserId, id, put);
    }
}
