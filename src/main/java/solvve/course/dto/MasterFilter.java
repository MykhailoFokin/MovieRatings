package solvve.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class MasterFilter {

    private String name;

    private String phone;

    private String about;

    private List<String> names;

    private List<String> phones;

    private List<String> abouts;
}
