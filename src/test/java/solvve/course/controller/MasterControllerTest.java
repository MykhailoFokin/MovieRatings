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
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Master;
import solvve.course.dto.MasterCreateDTO;
import solvve.course.dto.MasterPatchDTO;
import solvve.course.dto.MasterReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MasterService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MasterController.class)
@ActiveProfiles("test")
public class MasterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MasterService masterService;

    private MasterReadDTO createMasterRead() {
        MasterReadDTO master = new MasterReadDTO();
        master.setId(UUID.randomUUID());
        master.setName("MasterName");
        master.setPhone("645768767");
        master.setAbout("What about");
        return master;
    }

    @Test
    public void testGetMaster() throws Exception {
        MasterReadDTO master = createMasterRead();

        Mockito.when(masterService.getMaster(master.getId())).thenReturn(master);

        String resultJson = mvc.perform(get("/api/v1/master/{id}", master.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MasterReadDTO actualMovie = objectMapper.readValue(resultJson, MasterReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(master);

        Mockito.verify(masterService).getMaster(master.getId());
    }

    @Test
    public void testGetMasterWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Master.class,wrongId);
        Mockito.when(masterService.getMaster(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/master/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMasterWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/master/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMaster() throws Exception {

        MasterCreateDTO create = new MasterCreateDTO();
        create.setName("MasterName");
        create.setPhone("645768767");
        create.setAbout("What about");

        MasterReadDTO read = createMasterRead();

        Mockito.when(masterService.createMaster(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/master")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MasterReadDTO actualMaster = objectMapper.readValue(resultJson, MasterReadDTO.class);
        Assertions.assertThat(actualMaster).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMaster() throws Exception {

        MasterPatchDTO patchDTO = new MasterPatchDTO();
        patchDTO.setName("MasterName");
        patchDTO.setPhone("645768767");
        patchDTO.setAbout("What about");

        MasterReadDTO read = createMasterRead();

        Mockito.when(masterService.patchMaster(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/master/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MasterReadDTO actualMaster = objectMapper.readValue(resultJson, MasterReadDTO.class);
        Assert.assertEquals(read, actualMaster);
    }

    @Test
    public void testDeleteMaster() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/master/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(masterService).deleteMaster(id);
    }
}
