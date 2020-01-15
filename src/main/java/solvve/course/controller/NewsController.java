package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.NewsCreateDTO;
import solvve.course.dto.NewsPatchDTO;
import solvve.course.dto.NewsReadDTO;
import solvve.course.service.NewsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/{id}")
    public NewsReadDTO getNews(@PathVariable UUID id) {
        return newsService.getNews(id);
    }

    @PostMapping
    public NewsReadDTO createNews(@RequestBody NewsCreateDTO createDTO){
        return newsService.createNews(createDTO);
    }

    @PatchMapping("/{id}")
    public NewsReadDTO patchNews(@PathVariable UUID id, @RequestBody NewsPatchDTO patch){
        return newsService.patchNews(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable UUID id){
        newsService.deleteNews(id);
    }
}
