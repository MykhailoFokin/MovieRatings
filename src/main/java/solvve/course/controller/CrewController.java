package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.CrewCreateDTO;
import solvve.course.dto.CrewPatchDTO;
import solvve.course.dto.CrewReadDTO;
import solvve.course.service.CrewService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @GetMapping("/{id}")
    public CrewReadDTO getCrew(@PathVariable UUID id) {
        return crewService.getCrew(id);
    }

    @PostMapping
    public CrewReadDTO createCrew(@RequestBody CrewCreateDTO createDTO) {
        return crewService.createCrew(createDTO);
    }

    @PatchMapping("/{id}")
    public CrewReadDTO patchCrew(@PathVariable UUID id, @RequestBody CrewPatchDTO patch){
        return crewService.patchCrew(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCrew(@PathVariable UUID id){
        crewService.deleteCrew(id);
    }
}
