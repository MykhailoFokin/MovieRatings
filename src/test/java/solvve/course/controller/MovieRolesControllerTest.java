package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.MovieReadDTO;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.MovieRolesService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = MovieRolesController.class)
public class MovieRolesControllerTest extends BaseControllerTest {

    @MockBean
    private MovieRolesService movieRolesService;

    @Test
    public void testGeBlockedPortalUsers() throws Exception {
        MovieReadDTO movieDTO = generateObject(MovieReadDTO.class);
        RoleReadDTO roleDTO = generateObject(RoleReadDTO.class);
        List<RoleReadDTO> roles = List.of(roleDTO);

        Mockito.when(movieRolesService.getMovieRoles(movieDTO.getId())).thenReturn(roles);

        String resultJson = mvc.perform(get("/api/v1/movie/{movieId}/roles",
                movieDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReadDTO> actualRoles = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReadDTO>>(){});
        Assertions.assertThat(actualRoles).isEqualTo(roles);

        Mockito.verify(movieRolesService).getMovieRoles(movieDTO.getId());
    }
}
