package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.VisitService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping("/{id}")
    public VisitReadExtendedDTO getVisit(@PathVariable UUID id) {
        return visitService.getVisit(id);
    }

    @PostMapping
    public VisitReadExtendedDTO createVisit(@RequestBody VisitCreateDTO createDTO){
        return visitService.createVisit(createDTO);
    }

    @PatchMapping("/{id}")
    public VisitReadExtendedDTO patchVisit(@PathVariable UUID id, @RequestBody VisitPatchDTO patch){
        return visitService.patchVisit(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteVisit(@PathVariable UUID id){
        visitService.deleteVisit(id);
    }

    @PutMapping("/{id}")
    public VisitReadExtendedDTO putVisit(@PathVariable UUID id, @RequestBody VisitPutDTO put){
        return visitService.putVisit(id, put);
    }
}
