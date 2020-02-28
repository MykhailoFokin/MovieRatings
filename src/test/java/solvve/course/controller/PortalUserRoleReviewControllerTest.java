package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import solvve.course.domain.RoleReview;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.PortalUserRoleReviewService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PortalUserRoleReviewController.class)
@ActiveProfiles("test")
public class PortalUserRoleReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortalUserRoleReviewService portalUserRoleReviewService;

    @Test
    public void testGetRoleReview() throws Exception {
        PortalUserReadDTO portalUserReadDTO = createPortalUserReadDTO();
        List<RoleReviewReadDTO> roleReview = List.of(createRoleReview(portalUserReadDTO.getId()));

        Mockito.when(portalUserRoleReviewService.getPortalUserRoleReview(portalUserReadDTO.getId()))
                .thenReturn(roleReview);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews",
                portalUserReadDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<RoleReviewReadDTO> actualRole = objectMapper.readValue(resultJson,
                new TypeReference<List<RoleReviewReadDTO>>(){});
        Assertions.assertThat(actualRole).isEqualTo(roleReview);

        Mockito.verify(portalUserRoleReviewService).getPortalUserRoleReview(portalUserReadDTO.getId());
    }

    @Test
    public void testGetRoleReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleReview.class,wrongId);
        Mockito.when(portalUserRoleReviewService.getPortalUserRoleReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews",
                wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetRoleReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/portal-user/{portalUserId}/role-reviews"
                , wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = createPortalUserReadDTO();

        RoleReviewCreateDTO create = new RoleReviewCreateDTO();
        create.setTextReview("Review");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        create.setPortalUserId(portalUserReadDTO.getId());
        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());

        Mockito.when(portalUserRoleReviewService.createPortalUserRoleReview(portalUserReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/portal-user/{portalUserId}/role-reviews",
                portalUserReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assertions.assertThat(actualRoleReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = createPortalUserReadDTO();

        RoleReviewPatchDTO patchDTO = new RoleReviewPatchDTO();
        patchDTO.setTextReview("Review");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        patchDTO.setPortalUserId(portalUserReadDTO.getId());
        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());

        Mockito.when(portalUserRoleReviewService.patchPortalUserRoleReview(portalUserReadDTO.getId(),
                read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/portal-user/{portalUserId}/role-reviews/{id}"
                ,portalUserReadDTO.getId() ,read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    @Test
    public void testDeleteRoleReview() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/portal-user/{portalUserId}/role-reviews/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(portalUserRoleReviewService).deletePortalUserRoleReview(id, id);
    }

    @Test
    public void testPutRoleReview() throws Exception {

        PortalUserReadDTO portalUserReadDTO = createPortalUserReadDTO();

        RoleReviewPutDTO putDTO = new RoleReviewPutDTO();
        putDTO.setTextReview("Review");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        putDTO.setPortalUserId(portalUserReadDTO.getId());

        RoleReviewReadDTO read = createRoleReview(portalUserReadDTO.getId());

        Mockito.when(portalUserRoleReviewService.updatePortalUserRoleReview(portalUserReadDTO.getId(),
                read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/portal-user/{portalUserId}/role-reviews/{id}"
                , portalUserReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleReviewReadDTO actualRoleReview = objectMapper
                .readValue(resultJson, RoleReviewReadDTO.class);
        Assert.assertEquals(read, actualRoleReview);
    }

    private RoleReviewReadDTO createRoleReview(UUID portalUserId) {
        RoleReviewReadDTO roleReviewReadDTO = new RoleReviewReadDTO();
        roleReviewReadDTO.setId(UUID.randomUUID());
        roleReviewReadDTO.setTextReview("Review");
        roleReviewReadDTO.setPortalUserId(portalUserId);
        return roleReviewReadDTO;
    }

    private PortalUserReadDTO createPortalUserReadDTO() {
        PortalUserReadDTO portalUserReadDTO = new PortalUserReadDTO();
        portalUserReadDTO.setId(UUID.randomUUID());
        return portalUserReadDTO;
    }
}
