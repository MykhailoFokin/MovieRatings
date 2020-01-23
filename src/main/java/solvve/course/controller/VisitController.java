package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.VisitCreateDTO;
import solvve.course.dto.VisitPatchDTO;
import solvve.course.dto.VisitReadDTO;
import solvve.course.service.VisitService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/visit")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping("/{id}")
    public VisitReadDTO getVisit(@PathVariable UUID id) {
        return visitService.getVisit(id);
    }

    @PostMapping
    public VisitReadDTO createVisit(@RequestBody VisitCreateDTO createDTO){
        return visitService.createVisit(createDTO);
    }

    @PatchMapping("/{id}")
    public VisitReadDTO patchVisit(@PathVariable UUID id, @RequestBody VisitPatchDTO patch){
        return visitService.patchVisit(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable UUID id){
        visitService.deleteVisit(id);
    }
}
