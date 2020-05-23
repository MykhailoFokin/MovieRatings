package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.UserConfidenceType;
import solvve.course.dto.*;
import solvve.course.service.ModeratorPortalUserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = ModeratorPortalUserController.class)
public class ModeratorPortalUserControllerTest extends BaseControllerTest {

    @MockBean
    private ModeratorPortalUserService moderatorPortalUserService;

    @Test
    public void testGeBlockedPortalUsers() throws Exception {
        PortalUserReadDTO contentManagerDTO = generateObject(PortalUserReadDTO.class);
        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);
        portalUserReadDTO.setUserConfidence(UserConfidenceType.BLOCKED);
        List<PortalUserReadDTO> portalUsers = List.of(portalUserReadDTO);

        Mockito.when(moderatorPortalUserService.getBlockedPortalUsers(contentManagerDTO.getId()))
                .thenReturn(portalUsers);

        String resultJson = mvc.perform(get("/api/v1/moderator/{id}/portal-users",
                contentManagerDTO.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PortalUserReadDTO> actualPortalUsers = objectMapper.readValue(resultJson,
                new TypeReference<List<PortalUserReadDTO>>(){});
        Assertions.assertThat(actualPortalUsers).isEqualTo(portalUsers);

        Mockito.verify(moderatorPortalUserService).getBlockedPortalUsers(contentManagerDTO.getId());
    }

    @Test
    public void testBlockUnblockPortalUser() throws Exception {

        PortalUserReadDTO contentManagerDTO = generateObject(PortalUserReadDTO.class);
        PortalUserReadDTO portalUserReadDTO = generateObject(PortalUserReadDTO.class);
        portalUserReadDTO.setUserConfidence(UserConfidenceType.NORMAL);

        PortalUserPatchDTO patchDTO = new PortalUserPatchDTO();
        patchDTO.setUserConfidence(UserConfidenceType.BLOCKED);

        Mockito.when(moderatorPortalUserService.blockUnblockPortalUser(
                contentManagerDTO.getId(), portalUserReadDTO.getId(), patchDTO)).thenReturn(portalUserReadDTO);

        String resultJson = mvc.perform(patch(
                "/api/v1/moderator/{id}/portal-users/{id}"
                , contentManagerDTO.getId(), portalUserReadDTO.getId())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PortalUserReadDTO actualPortalUser = objectMapper.readValue(resultJson, PortalUserReadDTO.class);
        Assert.assertEquals(portalUserReadDTO, actualPortalUser);
    }
}
