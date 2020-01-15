package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.GrantsCreateDTO;
import solvve.course.dto.GrantsPatchDTO;
import solvve.course.dto.GrantsReadDTO;
import solvve.course.service.GrantsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grants")
public class GrantsController {

    @Autowired
    private GrantsService grantsService;

    @GetMapping("/{id}")
    public GrantsReadDTO getGrants(@PathVariable UUID id) {
        return grantsService.getGrants(id);
    }

    @PostMapping
    public GrantsReadDTO createGrants(@RequestBody GrantsCreateDTO createDTO){
        return grantsService.createGrants(createDTO);
    }

    @PatchMapping("/{id}")
    public GrantsReadDTO patchGrants(@PathVariable UUID id, @RequestBody GrantsPatchDTO patch){
        return grantsService.patchGrants(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteGrants(@PathVariable UUID id){
        grantsService.deleteGrants(id);
    }
}
