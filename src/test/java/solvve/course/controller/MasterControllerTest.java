package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.Master;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MasterService;
import solvve.course.service.TranslationService;

import java.util.List;
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

    private MasterReadDTO fromExtendedToDTO(MasterReadExtendedDTO dto) {
        MasterReadDTO masterReadDTO = new MasterReadDTO();
        masterReadDTO.setId(dto.getId());
        masterReadDTO.setName(dto.getName());
        masterReadDTO.setPhone(dto.getPhone());
        masterReadDTO.setAbout(dto.getAbout());
        return masterReadDTO;
    }

    @Test
    public void testGetMaster() throws Exception {
        MasterReadExtendedDTO master = createMasterRead();

        Mockito.when(masterService.getMaster(master.getId())).thenReturn(master);

        String resultJson = mvc.perform(get("/api/v1/masters/{id}", master.getId()))
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

        String resultJson = mvc.perform(get("/api/v1/masters/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMasterWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/masters/{id}",wrongId))
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

        MasterReadDTO read = fromExtendedToDTO(createMasterRead());

        Mockito.when(masterService.createMaster(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/masters")
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

        MasterReadDTO read = fromExtendedToDTO(createMasterRead());

        Mockito.when(masterService.patchMaster(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/masters/{id}", read.getId().toString())
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

        mvc.perform(delete("/api/v1/masters/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(masterService).deleteMaster(id);
    }

    @Test
    public void testPutMaster() throws Exception {

        MasterPutDTO putDTO = new MasterPutDTO();
        putDTO.setName("MasterName");
        putDTO.setPhone("645768767");
        putDTO.setAbout("What about");

        MasterReadDTO read = fromExtendedToDTO(createMasterRead());

        Mockito.when(masterService.putMaster(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/masters/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MasterReadDTO actualMaster = objectMapper.readValue(resultJson, MasterReadDTO.class);
        Assert.assertEquals(read, actualMaster);
    }

    @Test
    public void testGetMasters() throws Exception {
        MasterFilter masterFilter = new MasterFilter();
        masterFilter.setName("MasterName");

        MasterReadDTO read = new MasterReadDTO();
        read.setName("MasterName");
        List<MasterReadDTO> expectedResult = List.of(read);
        Mockito.when(masterService.getMasters(masterFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/masters")
                .param("name", masterFilter.getName()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<MasterReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }

    private MasterReadExtendedDTO createMasterRead() {
        MasterReadExtendedDTO master = new MasterReadExtendedDTO();
        master.setId(UUID.randomUUID());
        master.setName("MasterName");
        master.setPhone("645768767");
        master.setAbout("What about");
        return master;
    }
}
