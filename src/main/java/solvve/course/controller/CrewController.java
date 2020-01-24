package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CrewService;

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
    public CrewReadExtendedDTO createCrew(@RequestBody CrewCreateDTO createDTO) {
        return crewService.createCrew(createDTO);
    }

    @PatchMapping("/{id}")
    public CrewReadExtendedDTO patchCrew(@PathVariable UUID id, @RequestBody CrewPatchDTO patch){
        return crewService.patchCrew(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable UUID id){
        crewService.deleteCrew(id);
    }

    @PutMapping("/{id}")
    public CrewReadExtendedDTO putCrew(@PathVariable UUID id, @RequestBody CrewPutDTO put){
        return crewService.putCrew(id, put);
    }
}
