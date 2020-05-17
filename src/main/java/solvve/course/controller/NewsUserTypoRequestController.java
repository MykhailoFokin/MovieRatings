package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.UserTypoRequestCreateDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.UserTypoRequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news/{newsId}/user-typo-requests/{requesterId}")
public class NewsUserTypoRequestController {

    @Autowired
    private UserTypoRequestService userTypoRequestService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public List<UserTypoRequestReadDTO> getUserTypoRequests(@PathVariable UUID newsId,
                                                            @PathVariable UUID requesterId) {
        return userTypoRequestService.getUserTypoRequests(newsId, requesterId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public UserTypoRequestReadDTO createUserTypoRequest(@PathVariable UUID newsId,
                                                        @PathVariable UUID requesterId,
                                                        @RequestBody
                                                        @Valid UserTypoRequestCreateDTO createDTO) {
        return userTypoRequestService.createUserTypoRequest(newsId, requesterId, createDTO);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/{id}")
    public void deleteUserTypoRequest(@PathVariable UUID newsId,
                                      @PathVariable UUID requesterId,
                                      @PathVariable (value = "id") UUID id) {
        userTypoRequestService.deleteUserTypoRequest(newsId, requesterId, id);
    }
}
