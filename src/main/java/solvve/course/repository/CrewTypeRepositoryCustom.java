package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.CrewType;
import solvve.course.dto.CrewTypeFilter;

public interface CrewTypeRepositoryCustom {

    Page<CrewType> findByFilter(CrewTypeFilter filter, Pageable pageable);
}
