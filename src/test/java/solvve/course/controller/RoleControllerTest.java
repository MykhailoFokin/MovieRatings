package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.Role;
import solvve.course.dto.RoleReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleController.class)
@ActiveProfiles("test")
public class RoleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleService roleService;

    @Test
    public void testGetRole() throws Exception {
        RoleReadDTO role = new RoleReadDTO();
        role.setId(UUID.randomUUID());
        role.setTitle("Actor");
        role.setRoleType("Main_Role");
        role.setDescription("Description test");

        Mockito.when(roleService.getRole(role.getId())).thenReturn(role);

        String resultJson = mvc.perform(get("/api/v1/role/{id}", role.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        RoleReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(role);

        Mockito.verify(roleService).getRole(role.getId());
    }

    @Test
    public void testGetRoleWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Role.class,wrongId);
        Mockito.when(roleService.getRole(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/role/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetRoleWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }
}
