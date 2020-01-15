package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.PersonsCreateDTO;
import solvve.course.dto.PersonsPatchDTO;
import solvve.course.dto.PersonsReadDTO;
import solvve.course.service.PersonsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonsController {

    @Autowired
    private PersonsService personsService;

    @GetMapping("/{id}")
    public PersonsReadDTO getPersons(@PathVariable UUID id) {
        return personsService.getPersons(id);
    }

    @PostMapping
    public PersonsReadDTO createPersons(@RequestBody PersonsCreateDTO createDTO){
        return personsService.createPersons(createDTO);
    }

    @PatchMapping("/{id}")
    public PersonsReadDTO patchPersons(@PathVariable UUID id, @RequestBody PersonsPatchDTO patch){
        return personsService.patchPersons(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deletePersons(@PathVariable UUID id){
        personsService.deletePersons(id);
    }
}
