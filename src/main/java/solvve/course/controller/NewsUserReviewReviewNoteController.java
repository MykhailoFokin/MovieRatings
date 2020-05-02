package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.dto.NewsUserReviewNoteCreateDTO;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.service.NewsUserReviewReviewNoteService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news/{newsId}/news-user-reviews/{newsReviewId}/news-user-review-notes")
public class NewsUserReviewReviewNoteController {

    @Autowired
    private NewsUserReviewReviewNoteService newsUserReviewReviewNoteService;

    @AdminOrContentManager
    @GetMapping
    public List<NewsUserReviewNoteReadDTO> getNewsUserReviewUserReviewNote(@PathVariable UUID newsId,
                                                                           @PathVariable UUID newsReviewId) {
        return newsUserReviewReviewNoteService.getNewsUserReviewUserReviewNote(newsId, newsReviewId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public NewsUserReviewNoteReadDTO createNewsUserReviewNote(@PathVariable UUID newsId,
                                                              @PathVariable UUID newsReviewId,
                                                              @RequestBody
                                                                  @Valid NewsUserReviewNoteCreateDTO createDTO) {
        return newsUserReviewReviewNoteService.createNewsUserReviewReviewNote(newsId, newsReviewId, createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public NewsUserReviewNoteReadDTO patchNewsUserReviewNote(@PathVariable UUID newsId,
                                                             @PathVariable UUID newsReviewId,
                                                             @PathVariable (value = "id") UUID id,
                                                             @RequestBody @Valid NewsUserReviewNotePatchDTO patch) {
        return newsUserReviewReviewNoteService.patchNewsUserReviewReviewNote(newsId, newsReviewId, id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteNewsUserReviewNote(@PathVariable UUID newsId,
                                         @PathVariable UUID newsReviewId,
                                         @PathVariable (value = "id") UUID id) {
        newsUserReviewReviewNoteService.deleteNewsUserReviewReviewNote(newsId, newsReviewId, id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public NewsUserReviewNoteReadDTO putNewsUserReviewNote(@PathVariable UUID newsId,
                                                           @PathVariable UUID newsReviewId,
                                                           @PathVariable (value = "id") UUID id,
                                                           @RequestBody @Valid NewsUserReviewNotePutDTO put) {
        return newsUserReviewReviewNoteService.updateNewsUserReviewReviewNote(newsId, newsReviewId, id, put);
    }
}
