package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.dto.UserTypoRequestCreateDTO;
import solvve.course.dto.UserTypoRequestPatchDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.UserTypoRequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news/{newsId}/user-typo-requests")
public class NewsUserTypoRequestController {

    @Autowired
    private UserTypoRequestService userTypoRequestService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/{requesterId}")
    public List<UserTypoRequestReadDTO> getUserTypoRequests(@PathVariable UUID newsId,
                                                            @PathVariable UUID requesterId) {
        return userTypoRequestService.getUserTypoRequestsByNewsAndRequester(newsId, requesterId);
    }

    @PreAuthorize("hasAnyAuthority('CONTENTMANAGER')")
    @GetMapping
    public List<UserTypoRequestReadDTO> getUserTypoRequests(@PathVariable UUID newsId) {
        return userTypoRequestService.getUserTypoRequestsForNews(newsId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public UserTypoRequestReadDTO createUserTypoRequest(@PathVariable UUID newsId,
                                                        @RequestBody
                                                        @Valid UserTypoRequestCreateDTO createDTO) {
        return userTypoRequestService.createUserTypoRequest(newsId, createDTO);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/{requesterId}/{id}")
    public void deleteUserTypoRequest(@PathVariable UUID newsId,
                                      @PathVariable UUID requesterId,
                                      @PathVariable (value = "id") UUID id) {
        userTypoRequestService.deleteUserTypoRequest(newsId, requesterId, id);
    }

    @AdminOrContentManager
    @PatchMapping("/{userTypoRequestId}")
    public UserTypoRequestReadDTO patchUserTypoRequests(@PathVariable UUID newsId,
                                                        @PathVariable UUID userTypoRequestId,
                                                        @RequestBody @Valid UserTypoRequestPatchDTO patch) {
        return userTypoRequestService.patchUserTypoRequest(newsId, userTypoRequestId, patch);
    }
}
