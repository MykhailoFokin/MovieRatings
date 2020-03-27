package solvve.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private List<T> data;

    private int page;

    private int pageSize;

    private int totalPages;

    private long totalElements;
}
