package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.service.PersonService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    public PersonReadDTO getPersons(@PathVariable UUID id) {
        return personService.getPersons(id);
    }

    @PostMapping
    public PersonReadDTO createPersons(@RequestBody PersonCreateDTO createDTO) {
        return personService.createPersons(createDTO);
    }

    @PatchMapping("/{id}")
    public PersonReadDTO patchPersons(@PathVariable UUID id, @RequestBody PersonPatchDTO patch) {
        return personService.patchPersons(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deletePersons(@PathVariable UUID id) {
        personService.deletePersons(id);
    }

    @PutMapping("/{id}")
    public PersonReadDTO putPersons(@PathVariable UUID id, @RequestBody PersonPutDTO put) {
        return personService.putPersons(id, put);
    }
}
