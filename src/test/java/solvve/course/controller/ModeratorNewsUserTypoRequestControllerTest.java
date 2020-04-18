package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.dto.UserTypoRequestPatchDTO;
import solvve.course.dto.UserTypoRequestPutDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.ModeratorUserTypoRequestService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ModeratorNewsUserTypoRequestController.class)
public class ModeratorNewsUserTypoRequestControllerTest extends BaseControllerTest {

    @MockBean
    private ModeratorUserTypoRequestService moderatorUserTypoRequestService;

    @Test
    public void testUserTypoRequests() throws Exception {
        List<UserTypoRequestReadDTO> userTypoRequests = List.of(generateObject(UserTypoRequestReadDTO.class));
        UUID moderatorId = userTypoRequests.get(0).getModeratorId();

        Mockito.when(moderatorUserTypoRequestService
                .getModeratorUserTypoRequests(moderatorId))
                .thenReturn(userTypoRequests);

        String resultJson = mvc.perform(get("/api/v1/moderator/{moderatorId}/user-typo-requests", moderatorId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<UserTypoRequestReadDTO> actualUserTypoRequests = objectMapper.readValue(resultJson,
                new TypeReference<List<UserTypoRequestReadDTO>>() {
                });
        Assert.assertEquals(actualUserTypoRequests, userTypoRequests);
    }

    @Test
    public void testPatchMovieReviewCompliant() throws Exception {

        UserTypoRequestPatchDTO patchDTO = generateObject(UserTypoRequestPatchDTO.class);

        UserTypoRequestReadDTO read = generateObject(UserTypoRequestReadDTO.class);

        Mockito.when(moderatorUserTypoRequestService
                .patchUserTypoRequestByModerator(read.getModeratorId(), read.getId(), patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moderator/{moderatorId}/user-typo-requests/{userTypoRequestId}",
                read.getModeratorId(), read.getId())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypoRequestReadDTO actualUserTypoRequest = objectMapper
                .readValue(resultJson, UserTypoRequestReadDTO.class);
        Assert.assertEquals(read, actualUserTypoRequest);
    }

    @Test
    public void testPutUserTypoRequest() throws Exception {

        UserTypoRequestPutDTO putDTO = generateObject(UserTypoRequestPutDTO.class);

        UserTypoRequestReadDTO read = generateObject(UserTypoRequestReadDTO.class);

        Mockito.when(moderatorUserTypoRequestService.
                fixNewsTypo(read.getModeratorId(), read.getId(), putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moderator/{moderatorId}/user-typo-requests/{userTypoRequestId}",
                read.getModeratorId(), read.getId())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypoRequestReadDTO actualUserTypoRequest = objectMapper
                .readValue(resultJson, UserTypoRequestReadDTO.class);
        Assert.assertEquals(read, actualUserTypoRequest);
    }
}
