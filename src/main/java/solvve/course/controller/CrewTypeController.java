package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.CrewTypeService;

import java.util.List;
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
    public CrewTypeReadDTO createCrewType(@RequestBody CrewTypeCreateDTO createDTO) {
        return crewTypeService.createCrewType(createDTO);
    }

    @PatchMapping("/{id}")
    public CrewTypeReadDTO patchCrewType(@PathVariable UUID id, @RequestBody CrewTypePatchDTO patch) {
        return crewTypeService.patchCrewType(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteCrewType(@PathVariable UUID id){
        crewTypeService.deleteCrewType(id);
    }

    @PutMapping("/{id}")
    public CrewTypeReadDTO putCrewType(@PathVariable UUID id, @RequestBody CrewTypePutDTO put) {
        return crewTypeService.updateCrewType(id, put);
    }

    @GetMapping
    public List<CrewTypeReadDTO> getCrewTypes(CrewTypeFilter filter) {
        return crewTypeService.getCrewTypes(filter);
    }
}
