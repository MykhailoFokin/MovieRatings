package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Crew;
import solvve.course.dto.CrewFilter;

public interface CrewRepositoryCustom {

    Page<Crew> findByFilter(CrewFilter filter, Pageable pageable);
}
