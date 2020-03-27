package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CrewService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @GetMapping("/{id}")
    public CrewReadExtendedDTO getCrew(@PathVariable UUID id) {
        return crewService.getCrew(id);
    }

    @PostMapping
    public CrewReadDTO createCrew(@RequestBody @Valid CrewCreateDTO createDTO) {
        return crewService.createCrew(createDTO);
    }

    @PatchMapping("/{id}")
    public CrewReadDTO patchCrew(@PathVariable UUID id, @RequestBody @Valid CrewPatchDTO patch) {
        return crewService.patchCrew(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable UUID id) {
        crewService.deleteCrew(id);
    }

    @PutMapping("/{id}")
    public CrewReadDTO putCrew(@PathVariable UUID id, @RequestBody @Valid CrewPutDTO put) {
        return crewService.updateCrew(id, put);
    }

    @GetMapping
    public PageResult<CrewReadDTO> getCrews(CrewFilter filter, Pageable pageable) {
        return crewService.getCrews(filter, pageable);
    }
}
