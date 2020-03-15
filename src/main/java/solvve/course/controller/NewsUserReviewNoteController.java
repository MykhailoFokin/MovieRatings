package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.NewsUserReviewNoteCreateDTO;
import solvve.course.dto.NewsUserReviewNotePatchDTO;
import solvve.course.dto.NewsUserReviewNotePutDTO;
import solvve.course.dto.NewsUserReviewNoteReadDTO;
import solvve.course.service.NewsUserReviewNoteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news-user-review-notes")
public class NewsUserReviewNoteController {

    @Autowired
    private NewsUserReviewNoteService newsUserReviewNoteService;

    @GetMapping("/{id}")
    public NewsUserReviewNoteReadDTO getNewsUserReviewNote(@PathVariable UUID id) {
        return newsUserReviewNoteService.getNewsUserReviewNote(id);
    }

    @PostMapping
    public NewsUserReviewNoteReadDTO createNewsUserReviewNote(@RequestBody NewsUserReviewNoteCreateDTO createDTO) {
        return newsUserReviewNoteService.createNewsUserReviewNote(createDTO);
    }

    @PatchMapping("/{id}")
    public NewsUserReviewNoteReadDTO patchNewsUserReviewNote(@PathVariable UUID id,
                                                             @RequestBody NewsUserReviewNotePatchDTO patch) {
        return newsUserReviewNoteService.patchNewsUserReviewNote(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteNewsUserReviewNote(@PathVariable UUID id) {
        newsUserReviewNoteService.deleteNewsUserReviewNote(id);
    }

    @PutMapping("/{id}")
    public NewsUserReviewNoteReadDTO putNewsUserReviewNote(@PathVariable UUID id,
                                                           @RequestBody NewsUserReviewNotePutDTO put) {
        return newsUserReviewNoteService.updateNewsUserReviewNote(id, put);
    }
}
