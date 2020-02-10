package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CompanyDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companydetails")
public class CompanyDetailsController {

    @Autowired
    private CompanyDetailsService companyDetailsService;

    @GetMapping("/{id}")
    public CompanyDetailsReadDTO getCompanyDetail(@PathVariable UUID id) {
        return companyDetailsService.getCompanyDetails(id);
    }

    @PostMapping
    public CompanyDetailsReadDTO createCompanyDetails(@RequestBody CompanyDetailsCreateDTO createDTO) {
        return companyDetailsService.createCompanyDetails(createDTO);
    }

    @PatchMapping("/{id}")
    public CompanyDetailsReadDTO patchCompanyDetails(@PathVariable UUID id,
                                                     @RequestBody CompanyDetailsPatchDTO patch) {
        return companyDetailsService.patchCompanyDetails(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyDetails(@PathVariable UUID id) {
        companyDetailsService.deleteCompanyDetails(id);
    }

    @PutMapping("/{id}")
    public CompanyDetailsReadDTO putCompanyDetails(@PathVariable UUID id, @RequestBody CompanyDetailsPutDTO put) {
        return companyDetailsService.updateCompanyDetails(id, put);
    }

    @GetMapping
    public List<CompanyDetailsReadDTO> getCompanyDetails(CompanyDetailsFilter filter) {
        return companyDetailsService.getCompanyDetails(filter);
    }
}
