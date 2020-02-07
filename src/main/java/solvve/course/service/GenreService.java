package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Genre;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.GenreRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public GenreReadDTO getGenre(UUID id) {
        Genre genre = getGenreRequired(id);
        return translationService.toRead(genre);
    }

    public GenreReadDTO createGenre(GenreCreateDTO create) {
        Genre genre = translationService.toEntity(create);

        genre = genreRepository.save(genre);
        return translationService.toRead(genre);
    }

    public GenreReadDTO patchGenre(UUID id, GenrePatchDTO patch) {
        Genre genre = getGenreRequired(id);

        translationService.patchEntity(patch, genre);

        genre = genreRepository.save(genre);
        return translationService.toRead(genre);
    }

    public void deleteGenre(UUID id) {
        genreRepository.delete(getGenreRequired(id));
    }

    public GenreReadDTO putGenre(UUID id, GenrePutDTO put) {
        Genre genre = getGenreRequired(id);

        translationService.putEntity(put, genre);

        genre = genreRepository.save(genre);
        return translationService.toRead(genre);
    }

    public List<GenreReadDTO> getGenres(GenreFilter genreFilter) {
        List<Genre> genreList = genreRepository.findByFilter(genreFilter);
        return genreList.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    private Genre getGenreRequired(UUID id) {
        return genreRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Genre.class, id);
        });
    }
}
