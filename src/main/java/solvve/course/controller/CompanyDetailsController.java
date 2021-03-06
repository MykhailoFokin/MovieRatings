package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.CompanyDetailsService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companydetails")
public class CompanyDetailsController {

    @Autowired
    private CompanyDetailsService companyDetailsService;

    @PublicAccess
    @GetMapping("/{id}")
    public CompanyDetailsReadDTO getCompanyDetail(@PathVariable UUID id) {
        return companyDetailsService.getCompanyDetails(id);
    }

    @AdminOrContentManager
    @PostMapping
    public CompanyDetailsReadDTO createCompanyDetails(@RequestBody @Valid CompanyDetailsCreateDTO createDTO) {
        return companyDetailsService.createCompanyDetails(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public CompanyDetailsReadDTO patchCompanyDetails(@PathVariable UUID id,
                                                     @RequestBody @Valid CompanyDetailsPatchDTO patch) {
        return companyDetailsService.patchCompanyDetails(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteCompanyDetails(@PathVariable UUID id) {
        companyDetailsService.deleteCompanyDetails(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public CompanyDetailsReadDTO putCompanyDetails(@PathVariable UUID id,
                                                   @RequestBody @Valid CompanyDetailsPutDTO put) {
        return companyDetailsService.updateCompanyDetails(id, put);
    }

    @ApiPageable
    @PublicAccess
    @GetMapping
    public PageResult<CompanyDetailsReadDTO> getCompanyDetails(CompanyDetailsFilter filter,
                                                               @ApiIgnore Pageable pageable) {
        return companyDetailsService.getCompanyDetails(filter, pageable);
    }
}
