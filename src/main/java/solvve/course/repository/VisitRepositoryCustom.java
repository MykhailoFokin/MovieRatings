package solvve.course.repository;

import solvve.course.domain.Visit;
import solvve.course.dto.VisitFilter;

import java.util.List;

public interface VisitRepositoryCustom {

    List<Visit> findByFilter(VisitFilter filter);
}
