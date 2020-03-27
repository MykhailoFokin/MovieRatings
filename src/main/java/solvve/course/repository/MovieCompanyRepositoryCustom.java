package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.MovieCompany;
import solvve.course.dto.MovieCompanyFilter;

public interface MovieCompanyRepositoryCustom {

    Page<MovieCompany> findByFilter(MovieCompanyFilter movieCompanyFilter, Pageable pageable);
}
