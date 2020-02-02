package solvve.course.repository;

import solvve.course.domain.Master;
import solvve.course.dto.MasterFilter;

import java.util.List;

public interface MasterRepositoryCustom {

    List<Master> findByFilter(MasterFilter filter);
}
