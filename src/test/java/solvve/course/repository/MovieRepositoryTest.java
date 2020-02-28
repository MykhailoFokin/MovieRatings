package solvve.course.repository;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.*;
import solvve.course.dto.MovieFilter;
import solvve.course.service.MovieService;
import solvve.course.utils.TestObjectsFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(statements = {"delete from movie_prod_countries",
        "delete from movie_prod_companies",
        "delete from movie_prod_languages",
        "delete from language",
        "delete from genre",
        "delete from movie",
        "delete from country"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    @Autowired
    private MovieService movieService;

    @Test
    public void testSave() {
        Set<Country> sc = testObjectsFactory.createCountrySet();
        Movie m = new Movie();
        m.setMovieProdCountries(sc);
        m = movieRepository.save(m);
        assertNotNull(m.getId());
        assertTrue(movieRepository.findById(m.getId()).isPresent());
    }

    @Test
    public void testGetMovieByEmptyFilter() {
        Country c1 = testObjectsFactory.createCountry("Ukraine");
        Country c2 = testObjectsFactory.createCountry("Poland");
        Country c3 = testObjectsFactory.createCountry("Germany");
        Movie m1 = testObjectsFactory.createMovie(Set.of(c1));
        Movie m2 = testObjectsFactory.createMovie(Set.of(c1,c2));
        Movie m3 = testObjectsFactory.createMovie(Set.of(c1,c3));
        Movie m4 = testObjectsFactory.createMovie(Set.of(c2,c3));

        MovieFilter filter = new MovieFilter();
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m3.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByTitle() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setTitle("Title1");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMovieByYear() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setYear((short) 2015);
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByGenres() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Genre g1 = testObjectsFactory.createGenre(m1, MovieGenreType.ACTION);
        Genre g2 = testObjectsFactory.createGenre(m2, MovieGenreType.ACTION);
        testObjectsFactory.createGenre(m3, MovieGenreType.WAR);
        testObjectsFactory.createGenre(m4, MovieGenreType.WAR);

        MovieFilter filter = new MovieFilter();
        filter.setGenres(List.of(g1.getName(), g2.getName()));
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMovieByDescription() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setDescription("Description1");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByCompanyType() {
        CompanyDetails cd1 = testObjectsFactory.createCompanyDetails("Company1", "Overview",
                LocalDate.of(1990, 10, 10));
        CompanyDetails cd2 = testObjectsFactory.createCompanyDetails("Company2", "Overview",
                LocalDate.of(1991, 10, 10));
        MovieCompany mc1 = testObjectsFactory.createMovieCompany(cd1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany mc2 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.DISTRIBUTORS);
        MovieCompany mc3 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.OTHER_COMPANIES);
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                Set.of(mc1,mc3), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                Set.of(mc2,mc3), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                Set.of(mc1,mc2), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                Set.of(mc3), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setCompanyType(mc1.getMovieProductionType());
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByCompanyName() {
        CompanyDetails cd1 = testObjectsFactory.createCompanyDetails("Company1", "Overview",
                LocalDate.of(1990, 10, 10));
        CompanyDetails cd2 = testObjectsFactory.createCompanyDetails("Company2", "Overview",
                LocalDate.of(1991, 10, 10));
        MovieCompany mc1 = testObjectsFactory.createMovieCompany(cd1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany mc2 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.DISTRIBUTORS);
        MovieCompany mc3 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.PRODUCTION_COMPANIES);
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                Set.of(mc1,mc3), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                Set.of(mc2,mc3), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                Set.of(mc1,mc2), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                Set.of(mc3), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setCompanyName(cd1.getName());
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m3.getId());
    }

    @Test
    public void testGetMovieBySoundMix() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setSoundMix("WindowsMovieMaker");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByColour() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setColour("RGB");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByAspectRatio() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setAspectRatio("1:10");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId());
    }

    @Test
    public void testGetMovieByCamera() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setCamera("Panasonic");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByLaboratory() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                 new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                 new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                 new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                 new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setLaboratory("Lab3");
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByLanguage() {
        Language l1 = testObjectsFactory.createLanguage(LanguageType.DUTCH);
        Language l2 = testObjectsFactory.createLanguage(LanguageType.ENGLISH);
        Language l3 = testObjectsFactory.createLanguage(LanguageType.UKRAINIAN);
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                 new HashSet<>(), Set.of(l1,l3));
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), Set.of(l1,l2));
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), Set.of(l2,l3));
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                 new HashSet<>(), Set.of(l1,l3));

        MovieFilter filter = new MovieFilter();
        filter.setLanguage(l1.getName());
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByIsPublished() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setIsPublished(Boolean.TRUE);
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByTitles() {
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title3", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setTitles(List.of("Title2", "Title3"));
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByYears() {
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setYears(List.of((short) 2011, (short) 2015));
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m3.getId());
    }

    @Test
    public void testGetMovieByCompanyTypes() {
        CompanyDetails cd1 = testObjectsFactory.createCompanyDetails("Company1", "Overview",
                LocalDate.of(1990, 10, 10));
        CompanyDetails cd2 = testObjectsFactory.createCompanyDetails("Company2", "Overview",
                LocalDate.of(1991, 10, 10));
        MovieCompany mc1 = testObjectsFactory.createMovieCompany(cd1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany mc2 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.DISTRIBUTORS);
        MovieCompany mc3 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.OTHER_COMPANIES);
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                Set.of(mc1,mc3), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                Set.of(mc2,mc3), new HashSet<>());
        Movie m3 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                Set.of(mc1,mc2), new HashSet<>());
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                Set.of(mc3), new HashSet<>());

        MovieFilter filter = new MovieFilter();
        filter.setCompanyTypes(List.of(MovieProductionType.PRODUCTION_COMPANIES));
        testObjectsFactory.inTransaction(() -> {
            Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                    .containsExactlyInAnyOrder(m1.getId(), m3.getId());
        });
    }

    @Test
    public void testGetMovieByLanguages() {
        Language l1 = testObjectsFactory.createLanguage(LanguageType.DUTCH);
        Language l2 = testObjectsFactory.createLanguage(LanguageType.ENGLISH);
        Language l3 = testObjectsFactory.createLanguage(LanguageType.UKRAINIAN);
        Movie m1 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                new HashSet<>(), Set.of(l1,l3));
        Movie m2 = testObjectsFactory.createMovie(new HashSet<>(), "Title1", (short) 2015,
                "1:10", "Camera1, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                new HashSet<>(), Set.of(l1,l2));
        testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "WindowsMovieMaker", Boolean.TRUE,
                new HashSet<>(), Set.of(l2,l3));
        Movie m4 = testObjectsFactory.createMovie(new HashSet<>(), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                new HashSet<>(), Set.of(l1,l3));

        MovieFilter filter = new MovieFilter();
        filter.setLanguages(List.of(l1.getName()));
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m1.getId(), m2.getId(), m4.getId());
    }

    @Test
    public void testGetMovieByAllFilters() {
        Country c1 = testObjectsFactory.createCountry("Ukraine");
        Country c2 = testObjectsFactory.createCountry("Poland");
        Country c3 = testObjectsFactory.createCountry("Germany");
        CompanyDetails cd1 = testObjectsFactory.createCompanyDetails("Company1", "Overview",
                LocalDate.of(1990, 10, 10));
        CompanyDetails cd2 = testObjectsFactory.createCompanyDetails("Company2", "Overview",
                LocalDate.of(1991, 10, 10));
        MovieCompany mc1 = testObjectsFactory.createMovieCompany(cd1, MovieProductionType.PRODUCTION_COMPANIES);
        MovieCompany mc2 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.DISTRIBUTORS);
        MovieCompany mc3 = testObjectsFactory.createMovieCompany(cd2, MovieProductionType.OTHER_COMPANIES);
        Language l1 = testObjectsFactory.createLanguage(LanguageType.DUTCH);
        Language l2 = testObjectsFactory.createLanguage(LanguageType.ENGLISH);
        Language l3 = testObjectsFactory.createLanguage(LanguageType.UKRAINIAN);
        Movie m1 = testObjectsFactory.createMovie(Set.of(c1), "Title1", (short) 2011,
                "1:10", "Panasonic, Sony",
                "Black", "123", "Description1",
                "CaliforniaDreaming", "DolbySurround", Boolean.FALSE,
                Set.of(mc1,mc3), Set.of(l1,l3));
        Movie m2 = testObjectsFactory.createMovie(Set.of(c1,c2), "Title1", (short) 2015,
                "1:10", "Camera3, Sony",
                "Black, RGB", "133", "Description2",
                "CaliforniaDreaming", "WindowsMovieMaker", Boolean.FALSE,
                Set.of(mc2,mc3), Set.of(l1,l2));
        Movie m3 = testObjectsFactory.createMovie(Set.of(c1,c3), "Title2", (short) 2015,
                "1:15", "Camera3, Sony",
                "RGB", "123", "Description1",
                "Lab3", "DolbySurround", Boolean.TRUE,
                Set.of(mc1,mc2), Set.of(l2,l3));
        Movie m4 = testObjectsFactory.createMovie(Set.of(c2,c3), "Title2", (short) 2019,
                "1:11", "Panasonic, Camera3",
                "Red, Yellow, Green", "163", "Description4",
                "Lab3", "DolbySurround", Boolean.TRUE,
                Set.of(mc3), Set.of(l1,l3));
        Genre g1 = testObjectsFactory.createGenre(m1, MovieGenreType.ACTION);
        Genre g2 = testObjectsFactory.createGenre(m2, MovieGenreType.ACTION);
        Genre g3 = testObjectsFactory.createGenre(m3, MovieGenreType.WAR);
        Genre g4 = testObjectsFactory.createGenre(m4, MovieGenreType.WAR);

        MovieFilter filter = new MovieFilter();
        filter.setTitle("Title2");
        filter.setYear((short) 2015);
        filter.setLanguage(l2.getName());
        filter.setCompanyType(MovieProductionType.DISTRIBUTORS);
        filter.setIsPublished(Boolean.TRUE);
        filter.setCamera("Camera3");
        filter.setLaboratory("Lab3");
        filter.setSoundMix("DolbySurround");
        filter.setDescription("Description1");
        filter.setAspectRatio("1:15");
        filter.setColour("RGB");
        filter.setLanguages(List.of(l1.getName(), l2.getName(), l3.getName()));
        filter.setCompanyTypes(List.of(MovieProductionType.PRODUCTION_COMPANIES, MovieProductionType.DISTRIBUTORS));
        filter.setYears(List.of((short) 2011, (short) 2015, (short) 2019));
        filter.setTitles(List.of("Title1","Title2"));
        filter.setGenres(List.of(g3.getName()));
        Assertions.assertThat(movieService.getMovies(filter)).extracting("Id")
                .containsExactlyInAnyOrder(m3.getId());
    }

    @Test
    public void testCteatedAtIsSet() {
        Movie entity = testObjectsFactory.createMovie();

        Instant createdAtBeforeReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtBeforeReload);
        entity = movieRepository.findById(entity.getId()).get();

        Instant createdAtAfterReload = entity.getCreatedAt();
        Assert.assertNotNull(createdAtAfterReload);
        Assert.assertEquals(createdAtBeforeReload, createdAtAfterReload);
    }

    @Test
    public void testupdatedAtIsSet() {
        Movie entity = testObjectsFactory.createMovie();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);
        entity = movieRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertEquals(updatedAtBeforeReload, updatedAtAfterReload);
    }

    @Test
    public void testupdatedAtIsModified() {
        Movie entity = testObjectsFactory.createMovie();

        Instant updatedAtBeforeReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtBeforeReload);

        entity.setDescription("NewNameTest");
        movieRepository.save(entity);
        entity = movieRepository.findById(entity.getId()).get();

        Instant updatedAtAfterReload = entity.getUpdatedAt();
        Assert.assertNotNull(updatedAtAfterReload);
        Assert.assertTrue(updatedAtBeforeReload.compareTo(updatedAtAfterReload) < 1);
    }
}
