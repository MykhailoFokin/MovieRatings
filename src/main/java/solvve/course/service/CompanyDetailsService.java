package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.CompanyDetails;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.CompanyDetailsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyDetailsService {

    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public CompanyDetailsReadDTO getCompanyDetails(UUID id) {
        CompanyDetails companyDetails = getCompanyDetailsRequired(id);
        return translationService.toRead(companyDetails);
    }

    public CompanyDetailsReadDTO createCompanyDetails(CompanyDetailsCreateDTO create) {
        CompanyDetails companyDetails = translationService.toEntity(create);

        companyDetails = companyDetailsRepository.save(companyDetails);
        return translationService.toRead(companyDetails);
    }

    public CompanyDetailsReadDTO patchCompanyDetails(UUID id, CompanyDetailsPatchDTO patch) {
        CompanyDetails companyDetails = getCompanyDetailsRequired(id);

        translationService.patchEntity(patch, companyDetails);

        companyDetails = companyDetailsRepository.save(companyDetails);
        return translationService.toRead(companyDetails);
    }

    public void deleteCompanyDetails(UUID id) {
        companyDetailsRepository.delete(getCompanyDetailsRequired(id));
    }

    public CompanyDetailsReadDTO putCompanyDetails(UUID id, CompanyDetailsPutDTO put) {
        CompanyDetails companyDetails = getCompanyDetailsRequired(id);

        translationService.putEntity(put, companyDetails);

        companyDetails = companyDetailsRepository.save(companyDetails);
        return translationService.toRead(companyDetails);
    }

    public List<CompanyDetailsReadDTO> getCompanyDetails(CompanyDetailsFilter filter) {
        List<CompanyDetails> companyDetailss = companyDetailsRepository.findByFilter(filter);
        return companyDetailss.stream().map(translationService::toRead).collect(Collectors.toList());
    }

    private CompanyDetails getCompanyDetailsRequired(UUID id) {
        return companyDetailsRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(CompanyDetails.class, id);
        });
    }
}
