package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.RoleSpoilerData;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleSpoilerDataService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleSpoilerDataController.class)
@ActiveProfiles("test")
public class RoleSpoilerDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleSpoilerDataService roleSpoilerDataService;

    private RoleSpoilerDataReadDTO createRoleSpoilerDataRead() {
        RoleSpoilerDataReadDTO roleSpoilerData = new RoleSpoilerDataReadDTO();
        roleSpoilerData.setId(UUID.randomUUID());
        roleSpoilerData.setStartIndex(100);
        roleSpoilerData.setEndIndex(150);
        return roleSpoilerData;
    }

    @Test
    public void testGetRoleSpoilerData() throws Exception {
        RoleSpoilerDataReadDTO roleSpoilerData = createRoleSpoilerDataRead();

        Mockito.when(roleSpoilerDataService.getRoleSpoilerData(roleSpoilerData.getId())).thenReturn(roleSpoilerData);

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}", roleSpoilerData.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualMovie = objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleSpoilerData);

        Mockito.verify(roleSpoilerDataService).getRoleSpoilerData(roleSpoilerData.getId());
    }

    @Test
    public void testGetRoleSpoilerDataWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleSpoilerData.class,wrongId);
        Mockito.when(roleSpoilerDataService.getRoleSpoilerData(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleSpoilerDataWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolespoilerdata/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleSpoilerData() throws Exception {

        RoleSpoilerDataCreateDTO create = new RoleSpoilerDataCreateDTO();
        create.setStartIndex(100);
        create.setEndIndex(150);

        RoleSpoilerDataReadDTO read = createRoleSpoilerDataRead();

        Mockito.when(roleSpoilerDataService.createRoleSpoilerData(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolespoilerdata")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assertions.assertThat(actualRoleSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleSpoilerData() throws Exception {

        RoleSpoilerDataPatchDTO patchDTO = new RoleSpoilerDataPatchDTO();
        patchDTO.setStartIndex(100);
        patchDTO.setEndIndex(150);

        RoleSpoilerDataReadDTO read = createRoleSpoilerDataRead();

        Mockito.when(roleSpoilerDataService.patchRoleSpoilerData(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolespoilerdata/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleSpoilerDataReadDTO actualRoleSpoilerData = objectMapper.readValue(resultJson, RoleSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualRoleSpoilerData);
    }

    @Test
    public void testDeleteRoleSpoilerData() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolespoilerdata/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleSpoilerDataService).deleteRoleSpoilerData(id);
    }
}
