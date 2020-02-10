package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.CompanyDetails;

import java.util.UUID;

@Repository
public interface CompanyDetailsRepository
        extends CrudRepository<CompanyDetails, UUID>, CompanyDetailsRepositoryCustom {
}
