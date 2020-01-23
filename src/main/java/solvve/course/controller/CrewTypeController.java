package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.CrewTypeCreateDTO;
import solvve.course.dto.CrewTypePatchDTO;
import solvve.course.dto.CrewTypeReadDTO;
import solvve.course.service.CrewTypeService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/crewtypes")
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

    @PatchMapping("/{id}")
    public CrewTypeReadDTO patchCrewType(@PathVariable UUID id, @RequestBody CrewTypePatchDTO patch){
        return crewTypeService.patchCrewType(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCrewType(@PathVariable UUID id){
        crewTypeService.deleteCrewType(id);
    }
}
