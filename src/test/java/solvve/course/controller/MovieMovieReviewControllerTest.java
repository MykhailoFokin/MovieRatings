package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.service.MovieMovieReviewService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = MovieMovieReviewController.class)
public class MovieMovieReviewControllerTest extends BaseControllerTest {

    @MockBean
    private MovieMovieReviewService movieMovieReviewService;

    @Test
    public void testGetMovieReviews() throws Exception {
        UUID movieId = UUID.randomUUID();

        List<MovieReviewReadDTO> movies = List.of(generateObject(MovieReviewReadDTO.class));

        Mockito.when(movieMovieReviewService.getMovieReviews(movieId)).thenReturn(movies);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/movie-reviews", movieId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewReadDTO> actualcompanies = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewReadDTO>>(){});
        Assertions.assertThat(actualcompanies).isEqualTo(movies);

        Mockito.verify(movieMovieReviewService).getMovieReviews(movieId);
    }

    @Test
    public void testGetMovieReviewsUnModerated() throws Exception {
        UUID movieId = UUID.randomUUID();

        List<MovieReviewReadDTO> movies = List.of(generateObject(MovieReviewReadDTO.class));

        Mockito.when(movieMovieReviewService.getUnModeratedMovieReviews(movieId)).thenReturn(movies);

        String resultJson = mvc.perform(get("/api/v1/movies/{movieId}/movie-reviews/unmoderated", movieId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewReadDTO> actualcompanies = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewReadDTO>>(){});
        Assertions.assertThat(actualcompanies).isEqualTo(movies);

        Mockito.verify(movieMovieReviewService).getUnModeratedMovieReviews(movieId);
    }
}
