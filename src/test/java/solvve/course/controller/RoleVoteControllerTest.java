package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.RoleVote;
import solvve.course.domain.UserVoteRatingType;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVotePatchDTO;
import solvve.course.dto.RoleVotePutDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.RoleVoteService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = RoleVoteController.class)
public class RoleVoteControllerTest extends BaseControllerTest {

    @MockBean
    private RoleVoteService roleVoteService;

    @Test
    public void testGetRoleVote() throws Exception {
        RoleVoteReadDTO roleVote = generateObject(RoleVoteReadDTO.class);

        Mockito.when(roleVoteService.getRoleVote(roleVote.getId())).thenReturn(roleVote);

        String resultJson = mvc.perform(get("/api/v1/rolevotes/{id}", roleVote.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualMovie = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleVote);

        Mockito.verify(roleVoteService).getRoleVote(roleVote.getId());
    }

    @Test
    public void testGetRoleVoteWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleVote.class,wrongId);
        Mockito.when(roleVoteService.getRoleVote(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolevotes/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleVoteWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/rolevotes/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleVote() throws Exception {

        RoleVoteCreateDTO create = generateObject(RoleVoteCreateDTO.class);

        RoleVoteReadDTO read = generateObject(RoleVoteReadDTO.class);

        Mockito.when(roleVoteService.createRoleVote(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolevotes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualRoleVote = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assertions.assertThat(actualRoleVote).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleVote() throws Exception {

        RoleVotePatchDTO patchDTO = generateObject(RoleVotePatchDTO.class);

        RoleVoteReadDTO read = generateObject(RoleVoteReadDTO.class);

        Mockito.when(roleVoteService.patchRoleVote(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/rolevotes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualRoleVote = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assert.assertEquals(read, actualRoleVote);
    }

    @Test
    public void testDeleteRoleVote() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/rolevotes/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(roleVoteService).deleteRoleVote(id);
    }

    @Test
    public void testPutRoleVote() throws Exception {

        RoleVotePutDTO putDTO = generateObject(RoleVotePutDTO.class);

        RoleVoteReadDTO read = generateObject(RoleVoteReadDTO.class);

        Mockito.when(roleVoteService.updateRoleVote(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/rolevotes/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualRoleVote = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assert.assertEquals(read, actualRoleVote);
    }

    @Test
    public void testCreateRoleVoteValidationFailed() throws Exception {
        RoleVoteCreateDTO create = new RoleVoteCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/rolevotes")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleVoteService, Mockito.never()).createRoleVote(ArgumentMatchers.any());
    }

    @Test
    public void testPutRoleVoteValidationFailed() throws Exception {
        RoleVotePutDTO put = new RoleVotePutDTO();

        String resultJson = mvc.perform(put("/api/v1/rolevotes/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(roleVoteService, Mockito.never()).updateRoleVote(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
