package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.service.CrewTypeService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crewtype")
public class CrewTypeController {

    @Autowired
    private CrewTypeService crewTypeService;

    @GetMapping("/{id}")
    public CrewTypeReadDTO getCrewType(@PathVariable UUID id) {
        return crewTypeService.getCrewType(id);
    }

    @PostMapping
    public CrewTypeReadDTO createCrewType(@RequestBody CrewTypeCreateDTO createDTO){
        return crewTypeService.createCrewType(createDTO);
    }
}
