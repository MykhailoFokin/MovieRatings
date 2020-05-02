package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.documentation.ApiPageable;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.*;
import solvve.course.service.CrewService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @PublicAccess
    @GetMapping("/{id}")
    public CrewReadExtendedDTO getCrew(@PathVariable UUID id) {
        return crewService.getCrew(id);
    }

    @AdminOrContentManager
    @PostMapping
    public CrewReadDTO createCrew(@RequestBody @Valid CrewCreateDTO createDTO) {
        return crewService.createCrew(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public CrewReadDTO patchCrew(@PathVariable UUID id, @RequestBody @Valid CrewPatchDTO patch) {
        return crewService.patchCrew(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable UUID id) {
        crewService.deleteCrew(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public CrewReadDTO putCrew(@PathVariable UUID id, @RequestBody @Valid CrewPutDTO put) {
        return crewService.updateCrew(id, put);
    }

    @ApiPageable
    @PublicAccess
    @GetMapping
    public PageResult<CrewReadDTO> getCrews(CrewFilter filter, Pageable pageable) {
        return crewService.getCrews(filter, pageable);
    }
}
