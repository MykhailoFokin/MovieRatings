package solvve.course.repository;

import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeFilter;

import java.util.List;

public interface CrewTypeRepositoryCustom {

    List<CrewType> findByFilter(CrewTypeFilter filter);
}
