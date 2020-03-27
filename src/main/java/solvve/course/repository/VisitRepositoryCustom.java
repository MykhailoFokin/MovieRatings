package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Visit;
import solvve.course.dto.VisitFilter;

public interface VisitRepositoryCustom {

    Page<Visit> findByFilter(VisitFilter filter, Pageable pageable);
}
