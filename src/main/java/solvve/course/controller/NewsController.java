package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsPutDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.service.NewsService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PublicAccess
    @GetMapping("/{id}")
    public NewsReadDTO getNews(@PathVariable UUID id) {
        return newsService.getNews(id);
    }

    @AdminOrContentManager
    @PostMapping
    public NewsReadDTO createNews(@RequestBody @Valid NewsCreateDTO createDTO) {
        return newsService.createNews(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public NewsReadDTO patchNews(@PathVariable UUID id, @RequestBody @Valid NewsPatchDTO patch) {
        return newsService.patchNews(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable UUID id) {
        newsService.deleteNews(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public NewsReadDTO putNews(@PathVariable UUID id, @RequestBody @Valid NewsPutDTO put) {
        return newsService.updateNews(id, put);
    }
}
