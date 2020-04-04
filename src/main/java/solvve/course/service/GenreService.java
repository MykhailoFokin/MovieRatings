package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Genre;
import solvve.course.dto.*;
import solvve.course.repository.GenreRepository;

import java.util.UUID;

@Service
public class GenreService extends AbstractService {

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public GenreReadDTO getGenre(UUID id) {
        Genre genre = repositoryHelper.getByIdRequired(Genre.class, id);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public GenreReadDTO createGenre(GenreCreateDTO create) {
        Genre genre = translationService.translate(create, Genre.class);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public GenreReadDTO patchGenre(UUID id, GenrePatchDTO patch) {
        Genre genre = repositoryHelper.getByIdRequired(Genre.class, id);

        translationService.map(patch, genre);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public void deleteGenre(UUID id) {
        genreRepository.delete(repositoryHelper.getByIdRequired(Genre.class, id));
    }

    public GenreReadDTO updateGenre(UUID id, GenrePutDTO put) {
        Genre genre = repositoryHelper.getByIdRequired(Genre.class, id);

        translationService.updateEntity(put, genre);

        genre = genreRepository.save(genre);
        return translationService.translate(genre, GenreReadDTO.class);
    }

    public PageResult<GenreReadDTO> getGenres(GenreFilter genreFilter, Pageable pageable) {
        Page<Genre> genres = genreRepository.findByFilter(genreFilter, pageable);
        return translationService.toPageResult(genres, GenreReadDTO.class);
    }
}
