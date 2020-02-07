package solvve.course.repository;

import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;

import java.util.List;

public interface CompanyDetailsRepositoryCustom {

    List<CompanyDetails> findByFilter(CompanyDetailsFilter filter);
}
