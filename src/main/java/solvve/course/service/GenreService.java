package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Genre;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GenreRepository;

import java.util.UUID;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public GenreReadDTO getGenre(UUID id) {
        Genre genre = getGenreRequired(id);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public GenreReadDTO createGenre(GenreCreateDTO create) {
        Genre genre = translationService.translate(create, Genre.class);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public GenreReadDTO patchGenre(UUID id, GenrePatchDTO patch) {
        Genre genre = getGenreRequired(id);

        translationService.map(patch, genre);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public void deleteGenre(UUID id) {
        genreRepository.delete(getGenreRequired(id));
    }

    public GenreReadDTO updateGenre(UUID id, GenrePutDTO put) {
        Genre genre = getGenreRequired(id);

        translationService.updateEntity(put, genre);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public PageResult<GenreReadDTO> getGenres(GenreFilter genreFilter, Pageable pageable) {
        Page<Genre> genres = genreRepository.findByFilter(genreFilter, pageable);
        return translationService.toPageResult(genres, GenreReadDTO.class);
    }

    private Genre getGenreRequired(UUID id) {
        return genreRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Genre.class, id);
        });
    }
}
