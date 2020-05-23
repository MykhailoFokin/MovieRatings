package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.service.ModeratorMovieReviewCompliantService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderator/{moderatorId}/movie-review-compliants")
public class ModeratorMovieReviewCompliantController {

    @Autowired
    private ModeratorMovieReviewCompliantService moderatorMovieReviewCompliantService;

    @AdminOrModerator
    @GetMapping
    public List<MovieReviewCompliantReadDTO> getCreatedMovieReviewCompliants(@PathVariable UUID moderatorId) {
        return moderatorMovieReviewCompliantService.getCreatedMovieReviewCompliants(moderatorId);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public MovieReviewCompliantReadDTO patchMovieReviewCompliantInReview(
            @PathVariable UUID moderatorId,
            @PathVariable (value = "id") UUID id,
            @RequestBody @Valid MovieReviewCompliantPatchDTO patch) {
        return moderatorMovieReviewCompliantService.patchMovieReviewCompliantByModeratedStatus(id, patch, moderatorId);
    }

    @AdminOrModerator
    @DeleteMapping("/{id}")
    public void deleteMovieReviewCompliantAndBanUser(@PathVariable UUID moderatorId,
                                                     @PathVariable (value = "id") UUID id) {
        moderatorMovieReviewCompliantService.deleteMovieReviewByCompliantByModerator(moderatorId, id);
    }
}
