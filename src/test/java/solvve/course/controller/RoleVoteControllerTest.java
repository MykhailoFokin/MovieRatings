package solvve.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.RoleVote;
import solvve.course.domain.UserVoteRatingType;
import solvve.course.dto.RoleVoteCreateDTO;
import solvve.course.dto.RoleVoteReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.RoleVoteService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RoleVoteController.class)
@ActiveProfiles("test")
public class RoleVoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleVoteService roleVoteService;

    @Test
    public void testGetRoleVote() throws Exception {
        RoleVoteReadDTO roleVote = new RoleVoteReadDTO();
        roleVote.setId(UUID.randomUUID());
        //roleVote.setRoleId(roleReadDTO.getId());
        //roleVote.setUserId(portalUserReadDTO.getId());
        roleVote.setRating(UserVoteRatingType.valueOf("R9"));

        Mockito.when(roleVoteService.getRoleVote(roleVote.getId())).thenReturn(roleVote);

        String resultJson = mvc.perform(get("/api/v1/rolevote/{id}", roleVote.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        RoleVoteReadDTO actualMovie = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(roleVote);

        Mockito.verify(roleVoteService).getRoleVote(roleVote.getId());
    }

    @Test
    public void testGetRoleVoteWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(RoleVote.class,wrongId);
        Mockito.when(roleVoteService.getRoleVote(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/rolevote/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testGetRoleVoteWrongFormatId() throws Exception {
        String illegalArgumentString = "123";
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Invalid UUID string: " + illegalArgumentString);

        UUID wrongId = UUID.fromString(illegalArgumentString);
    }

    @Test
    public void testCreateRoleVote() throws Exception {

        RoleVoteCreateDTO create = new RoleVoteCreateDTO();
        //create.setRoleId(roleReadDTO.getId());
        //create.setUserId(portalUserReadDTO.getId());
        create.setRating(UserVoteRatingType.valueOf("R9"));

        RoleVoteReadDTO read = new RoleVoteReadDTO();
        read.setId(UUID.randomUUID());
        //read.setRoleId(roleReadDTO.getId());
        //read.setUserId(portalUserReadDTO.getId());
        read.setRating(UserVoteRatingType.valueOf("R9"));

        Mockito.when(roleVoteService.createRoleVote(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/rolevote")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RoleVoteReadDTO actualRoleVote = objectMapper.readValue(resultJson, RoleVoteReadDTO.class);
        Assertions.assertThat(actualRoleVote).isEqualToComparingFieldByField(read);
    }
}
