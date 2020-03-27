package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Country;
import solvve.course.dto.CountryFilter;

public interface CountryRepositoryCustom {

    Page<Country> findByFilter(CountryFilter filter, Pageable pageable);
}
