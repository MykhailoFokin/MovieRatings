package solvve.course.client.themoviedb.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieReadShortDTO {

    private String id;

    private String title;

    private String originalTitle;

    private Boolean adult;

    private String originalLanguage;

    private List<Integer> genreIds;

    private String overview;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;
}
