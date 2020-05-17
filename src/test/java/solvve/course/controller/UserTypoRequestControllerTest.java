package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.UserTypoRequest;
import solvve.course.dto.UserTypoRequestCreateDTO;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.UserTypoRequestService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = NewsUserTypoRequestController.class)
public class UserTypoRequestControllerTest extends BaseControllerTest {

    @MockBean
    private UserTypoRequestService userTypoRequestService;

    @Test
    public void testGetUserTypoRequest() throws Exception {
        UserTypoRequestReadDTO userTypoRequest = generateObject(UserTypoRequestReadDTO.class);
        List<UserTypoRequestReadDTO> userTypoRequests = List.of(userTypoRequest);

        Mockito.when(userTypoRequestService.getUserTypoRequests(userTypoRequest.getNewsId(),
                userTypoRequest.getRequesterId()))
                .thenReturn(userTypoRequests);

        String resultJson = mvc.perform(get("/api/v1/news/{newsId}/user-typo-requests/{requesterId}",
                userTypoRequest.getNewsId(), userTypoRequest.getRequesterId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<UserTypoRequestReadDTO> actualRequest = objectMapper.readValue(resultJson,
                new TypeReference<List<UserTypoRequestReadDTO>>(){});
        Assertions.assertThat(actualRequest).isEqualTo(userTypoRequests);

        Mockito.verify(userTypoRequestService).getUserTypoRequests(userTypoRequest.getNewsId(),
                userTypoRequest.getRequesterId());
    }

    @Test
    public void testGetUserTypoRequestWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(UserTypoRequest.class,wrongId);
        Mockito.when(userTypoRequestService.getUserTypoRequests(wrongId, wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/news/{newsId}/user-typo-requests/{requesterId}",
                wrongId, wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetUserTypoRequestWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/news/{newsId}/user-typo-requests/{requesterId}",
                wrongId, wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateUserTypoRequest() throws Exception {

        UserTypoRequestCreateDTO create = generateObject(UserTypoRequestCreateDTO.class);

        UserTypoRequestReadDTO read = generateObject(UserTypoRequestReadDTO.class);

        Mockito.when(userTypoRequestService.
                createUserTypoRequest(create.getNewsId(), create.getRequesterId(), create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/news/{newsId}/user-typo-requests/{requesterId}",
                create.getNewsId(), create.getRequesterId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserTypoRequestReadDTO actualUserTypoRequest = objectMapper
                .readValue(resultJson, UserTypoRequestReadDTO.class);
        Assertions.assertThat(actualUserTypoRequest).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testDeleteUserTypoRequest() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/news/{newsId}/user-typo-requests/{requesterId}/{id}",
                id.toString(), id.toString(), id.toString())).
                andExpect(status().isOk());

        Mockito.verify(userTypoRequestService).deleteUserTypoRequest(id, id, id);
    }

    @Test
    public void testCreateUserTypoRequestValidationFailed() throws Exception {
        UserTypoRequestCreateDTO create = new UserTypoRequestCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/news/{newsId}/user-typo-requests/{requesterId}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(userTypoRequestService, Mockito.never()).createUserTypoRequest(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
