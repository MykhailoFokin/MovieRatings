package solvve.course.client.themoviedb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieReadDTO {

    private String id;

    private String originalTitle;

    private String title;

    private Boolean adult;

    private String originalLanguage;

    private List<Integer> genreIds;

    private String overview;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    private Long budget;

    private String homepage;

    private String imdbId;

    private Long revenue;

    private Integer runtime;

    private String tagline;
}
