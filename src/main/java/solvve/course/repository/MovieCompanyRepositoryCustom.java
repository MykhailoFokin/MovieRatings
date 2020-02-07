package solvve.course.repository;

import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieCompanyFilter;

import java.util.List;

public interface MovieCompanyRepositoryCustom {

    List<MovieCompany> findByFilter(MovieCompanyFilter movieCompanyFilter);
}
