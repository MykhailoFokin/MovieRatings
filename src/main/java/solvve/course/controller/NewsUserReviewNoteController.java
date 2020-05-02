package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.dto.NewsUserReviewNoteCreateDTO;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.service.NewsUserReviewNoteService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news-user-review-notes")
public class NewsUserReviewNoteController {

    @Autowired
    private NewsUserReviewNoteService newsUserReviewNoteService;

    @AdminOrContentManager
    @GetMapping("/{id}")
    public NewsUserReviewNoteReadDTO getNewsUserReviewNote(@PathVariable UUID id) {
        return newsUserReviewNoteService.getNewsUserReviewNote(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public NewsUserReviewNoteReadDTO createNewsUserReviewNote(@RequestBody
                                                                  @Valid NewsUserReviewNoteCreateDTO createDTO) {
        return newsUserReviewNoteService.createNewsUserReviewNote(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public NewsUserReviewNoteReadDTO patchNewsUserReviewNote(@PathVariable UUID id,
                                                             @RequestBody @Valid NewsUserReviewNotePatchDTO patch) {
        return newsUserReviewNoteService.patchNewsUserReviewNote(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteNewsUserReviewNote(@PathVariable UUID id) {
        newsUserReviewNoteService.deleteNewsUserReviewNote(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public NewsUserReviewNoteReadDTO putNewsUserReviewNote(@PathVariable UUID id,
                                                           @RequestBody @Valid NewsUserReviewNotePutDTO put) {
        return newsUserReviewNoteService.updateNewsUserReviewNote(id, put);
    }
}
