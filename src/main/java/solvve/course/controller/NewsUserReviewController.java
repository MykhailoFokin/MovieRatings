package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.NewsUserReviewCreateDTO;
import solvve.course.dto.NewsUserReviewPatchDTO;
import solvve.course.dto.NewsUserReviewPutDTO;
import solvve.course.dto.NewsUserReviewReadDTO;
import solvve.course.service.NewsUserReviewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news-user-reviews")
public class NewsUserReviewController {

    @Autowired
    private NewsUserReviewService newsUserReviewService;

    @GetMapping("/{id}")
    public NewsUserReviewReadDTO getNewsUserReview(@PathVariable UUID id) {
        return newsUserReviewService.getNewsUserReview(id);
    }

    @PostMapping
    public NewsUserReviewReadDTO createNewsUserReview(@RequestBody NewsUserReviewCreateDTO createDTO) {
        return newsUserReviewService.createNewsUserReview(createDTO);
    }

    @PatchMapping("/{id}")
    public NewsUserReviewReadDTO patchNewsUserReview(@PathVariable UUID id, @RequestBody NewsUserReviewPatchDTO patch) {
        return newsUserReviewService.patchNewsUserReview(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteNewsUserReview(@PathVariable UUID id) {
        newsUserReviewService.deleteNewsUserReview(id);
    }

    @PutMapping("/{id}")
    public NewsUserReviewReadDTO putNewsUserReview(@PathVariable UUID id, @RequestBody NewsUserReviewPutDTO put) {
        return newsUserReviewService.updateNewsUserReview(id, put);
    }
}
