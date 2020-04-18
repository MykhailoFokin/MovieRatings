package solvve.course.client.themoviedb.dto;

import lombok.Data;

@Data
public class MovieCreditsCastReadDTO {

    private String castId;

    private String character;

    private String creditId;

    private String gender;

    private String id;

    private String name;

    private String order;
}
