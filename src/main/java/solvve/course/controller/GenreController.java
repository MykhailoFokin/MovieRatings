package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.GenreService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PublicAccess
    @GetMapping("/{id}")
    public GenreReadDTO getGenre(@PathVariable UUID id) {
        return genreService.getGenre(id);
    }

    @AdminOrContentManager
    @PostMapping
    public GenreReadDTO createGenre(@RequestBody @Valid GenreCreateDTO createDTO) {
        return genreService.createGenre(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public GenreReadDTO patchGenre(@PathVariable UUID id, @RequestBody GenrePatchDTO patch) {
        return genreService.patchGenre(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public GenreReadDTO putGenre(@PathVariable UUID id, @RequestBody @Valid GenrePutDTO put) {
        return genreService.updateGenre(id, put);
    }

    @ApiPageable
    @PublicAccess
    @GetMapping
    public PageResult<GenreReadDTO> getGenres(GenreFilter genreFilter, @ApiIgnore Pageable pageable) {
        return genreService.getGenres(genreFilter, pageable);
    }
}
