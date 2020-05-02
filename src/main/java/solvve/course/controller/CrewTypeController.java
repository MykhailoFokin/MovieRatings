package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.CrewTypeService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crewtypes")
public class CrewTypeController {

    @Autowired
    private CrewTypeService crewTypeService;

    @PublicAccess
    @GetMapping("/{id}")
    public CrewTypeReadDTO getCrewType(@PathVariable UUID id) {
        return crewTypeService.getCrewType(id);
    }

    @AdminOrContentManager
    @PostMapping
    public CrewTypeReadDTO createCrewType(@RequestBody @Valid CrewTypeCreateDTO createDTO) {
        return crewTypeService.createCrewType(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public CrewTypeReadDTO patchCrewType(@PathVariable UUID id, @RequestBody @Valid CrewTypePatchDTO patch) {
        return crewTypeService.patchCrewType(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteCrewType(@PathVariable UUID id) {
        crewTypeService.deleteCrewType(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public CrewTypeReadDTO putCrewType(@PathVariable UUID id, @RequestBody @Valid CrewTypePutDTO put) {
        return crewTypeService.updateCrewType(id, put);
    }

    @ApiPageable
    @PublicAccess
    @GetMapping
    public PageResult<CrewTypeReadDTO> getCrewTypes(CrewTypeFilter filter, Pageable pageable) {
        return crewTypeService.getCrewTypes(filter, pageable);
    }
}
