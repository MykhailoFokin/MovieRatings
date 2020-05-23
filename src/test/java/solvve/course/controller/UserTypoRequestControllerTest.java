package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.UserTypoRequestService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = UserTypoRequestController.class)
public class UserTypoRequestControllerTest extends BaseControllerTest {

    @MockBean
    private UserTypoRequestService userTypoRequestService;

    @Test
    public void testGetUserTypoRequest() throws Exception {
        UserTypoRequestReadDTO userTypoRequest = generateObject(UserTypoRequestReadDTO.class);
        List<UserTypoRequestReadDTO> userTypoRequests = List.of(userTypoRequest);

        Mockito.when(userTypoRequestService.getUserTypoRequests()).thenReturn(userTypoRequests);

        String resultJson = mvc.perform(get("/api/v1/user-typo-requests"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<UserTypoRequestReadDTO> actualRequest = objectMapper.readValue(resultJson,
                new TypeReference<List<UserTypoRequestReadDTO>>(){});
        Assertions.assertThat(actualRequest).isEqualTo(userTypoRequests);

        Mockito.verify(userTypoRequestService).getUserTypoRequests();
    }
}
