package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.Genre;
import solvve.course.domain.MovieGenreType;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.GenreService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = GenreController.class)
public class GenreControllerTest extends BaseControllerTest {

    @MockBean
    private GenreService genreService;

    @Test
    public void testGetGenres() throws Exception {
        GenreReadDTO genre = generateObject(GenreReadDTO.class);

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

        GenreCreateDTO create = generateObject(GenreCreateDTO.class);

        GenreReadDTO read = generateObject(GenreReadDTO.class);

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

        GenrePatchDTO patchDTO = generateObject(GenrePatchDTO.class);

        GenreReadDTO read = generateObject(GenreReadDTO.class);

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

        GenrePutDTO putDTO = generateObject(GenrePutDTO.class);

        GenreReadDTO read = generateObject(GenreReadDTO.class);
        read.setMovieId(putDTO.getMovieId());

        Mockito.when(genreService.updateGenre(read.getId(),putDTO)).thenReturn(read);

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
        PageResult<GenreReadDTO> resultPage = new PageResult<>();
        resultPage.setData(List.of(read));
        Mockito.when(genreService.getGenres(genreFilter, PageRequest.of(0, defaultPageSize))).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/genres")
                .param("genres", "ADVENTURE"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        PageResult<GenreReadDTO> actualResult = objectMapper.readValue(resultJson, new TypeReference<>() {
        });
        Assert.assertEquals(resultPage, actualResult);
    }

    @Test
    public void testCreateGenreValidationFailed() throws Exception {
        GenreCreateDTO create = new GenreCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/genres")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(genreService, Mockito.never()).createGenre(ArgumentMatchers.any());
    }

    @Test
    public void testPutGenreValidationFailed() throws Exception {
        GenrePutDTO put = new GenrePutDTO();

        String resultJson = mvc.perform(put("/api/v1/genres/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(genreService, Mockito.never()).updateGenre(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testGetGenresWithPagingAndSorting() throws Exception {
        GenreReadDTO read = generateObject(GenreReadDTO.class);
        GenreFilter filter = new GenreFilter();
        filter.setMovieId(read.getMovieId());

        int page = 1;
        int size = 25;

        PageResult<GenreReadDTO> resultPage = new PageResult<>();
        resultPage.setPage(page);
        resultPage.setPageSize(size);
        resultPage.setTotalElements(100);
        resultPage.setTotalPages(4);
        resultPage.setData(List.of(read));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        Mockito.when(genreService.getGenres(filter, pageRequest)).thenReturn(resultPage);

        String resultJson = mvc.perform(get("/api/v1/genres")
                .param("movieId", filter.getMovieId().toString())
                .param("page", Integer.toString(page))
                .param("size", Integer.toString(size))
                .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PageResult<GenreReadDTO> actualPage = objectMapper.readValue(resultJson,
                new TypeReference<PageResult<GenreReadDTO>>() {
                });
        Assert.assertEquals(resultPage, actualPage);
    }
}
