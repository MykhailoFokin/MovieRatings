package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.dto.NewsUserReviewCreateDTO;
import solvve.course.dto.NewsUserReviewPatchDTO;
import solvve.course.dto.NewsUserReviewPutDTO;
import solvve.course.dto.NewsUserReviewReadDTO;
import solvve.course.service.NewsUserReviewService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news-user-reviews")
public class NewsUserReviewController {

    @Autowired
    private NewsUserReviewService newsUserReviewService;

    @AdminOrContentManager
    @GetMapping("/{id}")
    public NewsUserReviewReadDTO getNewsUserReview(@PathVariable UUID id) {
        return newsUserReviewService.getNewsUserReview(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public NewsUserReviewReadDTO createNewsUserReview(@RequestBody @Valid NewsUserReviewCreateDTO createDTO) {
        return newsUserReviewService.createNewsUserReview(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public NewsUserReviewReadDTO patchNewsUserReview(@PathVariable UUID id,
                                                     @RequestBody NewsUserReviewPatchDTO patch) {
        return newsUserReviewService.patchNewsUserReview(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteNewsUserReview(@PathVariable UUID id) {
        newsUserReviewService.deleteNewsUserReview(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public NewsUserReviewReadDTO putNewsUserReview(@PathVariable UUID id,
                                                   @RequestBody @Valid NewsUserReviewPutDTO put) {
        return newsUserReviewService.updateNewsUserReview(id, put);
    }
}
