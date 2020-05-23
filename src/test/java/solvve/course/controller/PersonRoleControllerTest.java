package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.PersonRoleService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = PersonRoleController.class)
public class PersonRoleControllerTest extends BaseControllerTest {

    @MockBean
    private PersonRoleService personRoleService;

    @Test
    public void testGetMovieReviews() throws Exception {
        UUID personId = UUID.randomUUID();

        List<RoleReadDTO> roles = List.of(generateObject(RoleReadDTO.class));

        Mockito.when(personRoleService.getRolesByPerson(personId)).thenReturn(roles);

        String resultJson = mvc.perform(get("/api/v1/person/{personId}/roles", personId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReadDTO> actualRoles = objectMapper.readValue(resultJson, new TypeReference<List<RoleReadDTO>>(){});
        Assertions.assertThat(actualRoles).isEqualTo(roles);

        Mockito.verify(personRoleService).getRolesByPerson(personId);
    }
}
