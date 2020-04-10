package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Language;
import solvve.course.domain.LanguageType;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieLanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.LinkDuplicatedException;
import solvve.course.repository.LanguageRepository;
import solvve.course.repository.MovieRepository;

import java.util.List;
import java.util.UUID;

public class MovieLanguageServiceTest extends BaseTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MovieLanguageService movieLanguageService;

    @Test
    public void testAddLanguageToMovie() {
        Movie movie = testObjectsFactory.createMovie();
        UUID languageId = languageRepository.findMovieLanguageByType(LanguageType.ARABIAN);

        List<MovieLanguageReadDTO> res = movieLanguageService.addLanguageToMovie(movie.getId(), languageId);

        MovieLanguageReadDTO expectedRead = new MovieLanguageReadDTO();
        expectedRead.setId(languageId);
        expectedRead.setName(LanguageType.ARABIAN);
        Assertions.assertThat(res).containsExactlyInAnyOrder(expectedRead);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterSave = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterSave.getMovieProdLanguages()).extracting(Language::getId)
                    .containsExactlyInAnyOrder(languageId);
        });
    }

    @Test
    public void testDuplicatedLanguage() {
        Movie movie = testObjectsFactory.createMovie();
        UUID languageId = languageRepository.findMovieLanguageByType(LanguageType.JAPANESE);

        movieLanguageService.addLanguageToMovie(movie.getId(), languageId);
        Assertions.assertThatThrownBy(() -> {
            movieLanguageService.addLanguageToMovie(movie.getId(), languageId);
        }).isInstanceOf(LinkDuplicatedException.class);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongMovieId() {
        UUID wrongMovieId = UUID.randomUUID();
        UUID languageId = languageRepository.findMovieLanguageByType(LanguageType.JAPANESE);
        movieLanguageService.addLanguageToMovie(wrongMovieId, languageId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testWrongLanguageId() {
        Movie movie = testObjectsFactory.createMovie();
        UUID languageId = UUID.randomUUID();
        movieLanguageService.addLanguageToMovie(movie.getId(), languageId);
    }

    @Test
    public void testRemoveLanguageFromMovie() {
        UUID languageId1 = languageRepository.findMovieLanguageByType(LanguageType.ARABIAN);
        UUID languageId2 = languageRepository.findMovieLanguageByType(LanguageType.FRENCH);
        Movie movie = testObjectsFactory.createMovieWithLanguages(List.of(languageId1, languageId2));

        movieLanguageService.removeLanguageFromMovie(movie.getId(), languageId1);

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assertions.assertThat(movieAfterRemove.getMovieProdLanguages()).extracting(Language::getId)
                    .containsExactlyInAnyOrder(languageId2);
        });
    }

    @Test
    public void testAddAndThenRemoveLanguageFromMovie() {
        Movie movie = testObjectsFactory.createMovie();
        UUID languageId = languageRepository.findMovieLanguageByType(LanguageType.JAPANESE);
        movieLanguageService.addLanguageToMovie(movie.getId(), languageId);

        List<MovieLanguageReadDTO> remainingLanguages = movieLanguageService.removeLanguageFromMovie(movie.getId(),
                languageId);
        Assert.assertTrue(remainingLanguages.isEmpty());

        testObjectsFactory.inTransaction(() -> {
            Movie movieAfterRemove = movieRepository.findById(movie.getId()).get();
            Assert.assertTrue(movieAfterRemove.getMovieProdLanguages().isEmpty());
        });
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotAddedLanguage() {
        Movie movie = testObjectsFactory.createMovie();
        UUID languageId = languageRepository.findMovieLanguageByType(LanguageType.JAPANESE);

        movieLanguageService.removeLanguageFromMovie(movie.getId(), languageId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRemoveNotExistedLanguage() {
        Movie movie = testObjectsFactory.createMovie();
        movieLanguageService.removeLanguageFromMovie(movie.getId(), UUID.randomUUID());
    }

    @Test
    public void testGetMovieLanguages() {
        UUID languageId1 = languageRepository.findMovieLanguageByType(LanguageType.ARABIAN);
        UUID languageId2 = languageRepository.findMovieLanguageByType(LanguageType.FRENCH);
        Movie movie = testObjectsFactory.createMovieWithLanguages(List.of(languageId1, languageId2));

        Assertions.assertThat(movieLanguageService.getMovieLanguages(movie.getId()))
                .extracting(MovieLanguageReadDTO::getId)
                .containsExactlyInAnyOrder(languageId1, languageId2);
    }
}
