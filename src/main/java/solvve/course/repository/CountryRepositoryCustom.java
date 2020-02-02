package solvve.course.repository;

import solvve.course.domain.Country;
import solvve.course.dto.CountryFilter;

import java.util.List;

public interface CountryRepositoryCustom {

    List<Country> findByFilter(CountryFilter filter);
}
