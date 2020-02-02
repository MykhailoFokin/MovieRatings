package solvve.course.repository;

import solvve.course.domain.Crew;
import solvve.course.dto.CrewFilter;

import java.util.List;

public interface CrewRepositoryCustom {

    List<Crew> findByFilter(CrewFilter filter);
}
