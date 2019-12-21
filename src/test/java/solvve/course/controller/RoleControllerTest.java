package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.RoleService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleController.class)
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

        String resultJson = mvc.perform(get("/api/v1/movie/{id}", role.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        RoleReadDTO actualMovie = objectMapper.readValue(resultJson, RoleReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(role);

        Mockito.verify(roleService).getRole(role.getId());
    }
}
