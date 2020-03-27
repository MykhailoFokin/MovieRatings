package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.CompanyDetailsFilter;

public interface CompanyDetailsRepositoryCustom {

    Page<CompanyDetails> findByFilter(CompanyDetailsFilter filter, Pageable pageable);
}
