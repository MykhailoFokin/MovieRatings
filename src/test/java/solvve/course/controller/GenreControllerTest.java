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
import solvve.course.domain.Genre;
import solvve.course.domain.MovieGenreType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.GenreService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = GenreController.class)
@ActiveProfiles("test")
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @Test
    public void testGetGenres() throws Exception {
        GenreReadDTO genre = createGenresRead();

        Mockito.when(genreService.getGenre(genre.getId())).thenReturn(genre);

        String resultJson = mvc.perform(get("/api/v1/genres/{id}", genre.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GenreReadDTO actualMovie = objectMapper.readValue(resultJson, GenreReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(genre);

        Mockito.verify(genreService).getGenre(genre.getId());
    }

    @Test
    public void testGetGenresWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Genre.class,wrongId);
        Mockito.when(genreService.getGenre(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/genres/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetGenresWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/genres/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateGenres() throws Exception {

        GenreCreateDTO create = new GenreCreateDTO();
        create.setName(MovieGenreType.ACTION);

        GenreReadDTO read = createGenresRead();

        Mockito.when(genreService.createGenre(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/genres")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GenreReadDTO actualGenres = objectMapper.readValue(resultJson, GenreReadDTO.class);
        Assertions.assertThat(actualGenres).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchGenres() throws Exception {

        GenrePatchDTO patchDTO = new GenrePatchDTO();
        patchDTO.setName(MovieGenreType.ACTION);

        GenreReadDTO read = createGenresRead();

        Mockito.when(genreService.patchGenre(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/genres/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GenreReadDTO actualGenres = objectMapper.readValue(resultJson, GenreReadDTO.class);
        Assert.assertEquals(read, actualGenres);
    }

    @Test
    public void testDeleteGenres() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/genres/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(genreService).deleteGenre(id);
    }

    @Test
    public void testPutGenres() throws Exception {

        GenrePutDTO putDTO = new GenrePutDTO();
        putDTO.setName(MovieGenreType.ACTION);

        GenreReadDTO read = createGenresRead();

        Mockito.when(genreService.putGenre(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/genres/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        GenreReadDTO actualGenres = objectMapper.readValue(resultJson, GenreReadDTO.class);
        Assert.assertEquals(read, actualGenres);
    }

    @Test
    public void testGetGenresFilter() throws Exception {
        GenreFilter genreFilter = new GenreFilter();
        genreFilter.setGenres(List.of(MovieGenreType.ADVENTURE));

        GenreReadDTO read = new GenreReadDTO();
        read.setName(MovieGenreType.ADVENTURE);
        List<GenreReadDTO> expectedResult = List.of(read);
        Mockito.when(genreService.getGenres(genreFilter)).thenReturn(expectedResult);

        String resultJson = mvc.perform(get("/api/v1/genres")
                .param("genres", "ADVENTURE"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<GenreReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(expectedResult, actualResult);
    }

    private GenreReadDTO createGenresRead() {
        GenreReadDTO dto = new GenreReadDTO();
        dto.setId(UUID.randomUUID());
        dto.setName(MovieGenreType.ACTION);
        return dto;
    }
}
