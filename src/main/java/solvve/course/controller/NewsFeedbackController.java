package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrModerator;
import solvve.course.dto.NewsFeedbackCreateDTO;
import solvve.course.dto.NewsFeedbackPatchDTO;
import solvve.course.dto.NewsFeedbackPutDTO;
import solvve.course.dto.NewsFeedbackReadDTO;
import solvve.course.service.NewsFeedbackService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news-feedbacks")
public class NewsFeedbackController {

    @Autowired
    private NewsFeedbackService newsFeedbackService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/{id}")
    public NewsFeedbackReadDTO getNewsFeedback(@PathVariable UUID id) {
        return newsFeedbackService.getNewsFeedback(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public NewsFeedbackReadDTO createNewsFeedback(@RequestBody @Valid NewsFeedbackCreateDTO createDTO) {
        return newsFeedbackService.createNewsFeedback(createDTO);
    }

    @AdminOrModerator
    @PatchMapping("/{id}")
    public NewsFeedbackReadDTO patchNewsFeedback(@PathVariable UUID id, @RequestBody NewsFeedbackPatchDTO patch) {
        return newsFeedbackService.patchNewsFeedback(id, patch);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/{id}")
    public void deleteNewsFeedback(@PathVariable UUID id) {
        newsFeedbackService.deleteNewsFeedback(id);
    }

    @AdminOrModerator
    @PutMapping("/{id}")
    public NewsFeedbackReadDTO putNewsFeedback(@PathVariable UUID id, @RequestBody @Valid NewsFeedbackPutDTO put) {
        return newsFeedbackService.updateNewsFeedback(id, put);
    }
}
