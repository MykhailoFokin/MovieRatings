package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.MovieReadDTO;
import solvve.course.dto.PortalUserReadDTO;
import solvve.course.service.PortalUserMovieService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = PortalUserMovieController.class)
public class PortalUserMovieControllerTest extends BaseControllerTest {

    @MockBean
    private PortalUserMovieService portalUserMovieService;

    @Test
    public void testGetMovies() throws Exception {
        MovieReadDTO movieReadDTO = new MovieReadDTO();
        movieReadDTO.setId(UUID.randomUUID());
        PortalUserReadDTO portalUserReadDTO = new PortalUserReadDTO();
        portalUserReadDTO.setId(UUID.randomUUID());
        List<MovieReadDTO> movieReadDTOs = List.of(movieReadDTO);

        Instant startFrom = LocalDateTime.of(2019, 12, 23, 8, 0).toInstant(ZoneOffset.UTC);
        Instant startTo = LocalDateTime.of(2019, 12, 23, 9, 0).toInstant(ZoneOffset.UTC);

        Mockito.when(portalUserMovieService.getPortalUserMovies(portalUserReadDTO.getId(), startFrom, startTo))
                .thenReturn(movieReadDTOs);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/movies/{startFrom}/{startTo}",
                portalUserReadDTO.getId(), startFrom, startTo))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReadDTOs);

        Mockito.verify(portalUserMovieService).getPortalUserMovies(portalUserReadDTO.getId(), startFrom, startTo);
    }

    @Test
    public void testGetRecommendedMovies() throws Exception {
        MovieReadDTO movieReadDTO = new MovieReadDTO();
        movieReadDTO.setId(UUID.randomUUID());
        PortalUserReadDTO portalUserReadDTO = new PortalUserReadDTO();
        portalUserReadDTO.setId(UUID.randomUUID());
        List<MovieReadDTO> movieReadDTOs = List.of(movieReadDTO);

        Mockito.when(portalUserMovieService.getPortalUserRecommendedMovies(portalUserReadDTO.getId()))
                .thenReturn(movieReadDTOs);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/movies/recommendations",
                portalUserReadDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReadDTOs);

        Mockito.verify(portalUserMovieService).getPortalUserRecommendedMovies(portalUserReadDTO.getId());
    }
}
