package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.CurrentUser;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.service.PortalUserNewsUserReviewNoteService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/portal-user/{portalUserId}/news-user-review-notes")
public class PortalUserNewsUserReviewNoteController {

    @Autowired
    private PortalUserNewsUserReviewNoteService portalUserNewsUserReviewNoteService;

    @CurrentUser
    @GetMapping
    public List<NewsUserReviewNoteReadDTO> getNewsUserReviewUserReviewNote(@PathVariable UUID portalUserId) {
        return portalUserNewsUserReviewNoteService.getModeratorUserReviewNotes(portalUserId);
    }

    @CurrentUser
    @PatchMapping("/{id}")
    public NewsUserReviewNoteReadDTO patchNewsUserReviewNote(@PathVariable UUID portalUserId,
                                                             @PathVariable UUID newsUserReviewNoteId,
                                                             @RequestBody @Valid NewsUserReviewNotePatchDTO patch) {
        return portalUserNewsUserReviewNoteService.patchPortalUserNewsUserReviewNote(portalUserId,
                newsUserReviewNoteId, patch);
    }

    @CurrentUser
    @PutMapping("/{id}")
    public NewsUserReviewNoteReadDTO putNewsUserReviewNote(@PathVariable UUID portalUserId,
                                                           @PathVariable UUID newsUserReviewNoteId,
                                                           @RequestBody @Valid NewsUserReviewNotePutDTO put) {
        return portalUserNewsUserReviewNoteService.updatePortalUserNewsUserReviewNote(portalUserId,
                newsUserReviewNoteId, put);
    }
}
