package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.UserTypoRequestPatchDTO;
import solvve.course.dto.UserTypoRequestPutDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.ModeratorUserTypoRequestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderator/{moderatorId}/user-typo-requests")
public class ModeratorNewsUserTypoRequestController {

    @Autowired
    private ModeratorUserTypoRequestService moderatorUserTypoRequestService;

    @GetMapping
    public List<UserTypoRequestReadDTO> getUserTypoRequests(@PathVariable UUID moderatorId) {
        return moderatorUserTypoRequestService.getModeratorUserTypoRequests(moderatorId);
    }

    @PatchMapping("/{userTypoRequestId}")
    public UserTypoRequestReadDTO patchUserTypoRequest(@PathVariable UUID moderatorId,
                                                       @PathVariable UUID userTypoRequestId,
                                                       @RequestBody UserTypoRequestPatchDTO patch) {
        return moderatorUserTypoRequestService.patchUserTypoRequestByModerator(moderatorId, userTypoRequestId,
                patch);
    }

    @PutMapping("/{userTypoRequestId}")
    public UserTypoRequestReadDTO fixTypo(@PathVariable UUID moderatorId,
                                          @PathVariable UUID userTypoRequestId,
                                          @RequestBody UserTypoRequestPutDTO put) {
        return moderatorUserTypoRequestService.fixNewsTypo(moderatorId, userTypoRequestId, put);
    }
}
