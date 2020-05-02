package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.PersonCreateDTO;
import solvve.course.dto.PersonPatchDTO;
import solvve.course.dto.PersonPutDTO;
import solvve.course.dto.PersonReadDTO;
import solvve.course.service.PersonService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PublicAccess
    @GetMapping("/{id}")
    public PersonReadDTO getPersons(@PathVariable UUID id) {
        return personService.getPersons(id);
    }

    @AdminOrContentManager
    @PostMapping
    public PersonReadDTO createPersons(@RequestBody @Valid PersonCreateDTO createDTO) {
        return personService.createPersons(createDTO);
    }

    @AdminOrContentManager
    @PatchMapping("/{id}")
    public PersonReadDTO patchPersons(@PathVariable UUID id, @RequestBody @Valid PersonPatchDTO patch) {
        return personService.patchPersons(id, patch);
    }

    @AdminOrContentManager
    @DeleteMapping("/{id}")
    public void deletePersons(@PathVariable UUID id) {
        personService.deletePersons(id);
    }

    @AdminOrContentManager
    @PutMapping("/{id}")
    public PersonReadDTO putPersons(@PathVariable UUID id, @RequestBody @Valid PersonPutDTO put) {
        return personService.updatePersons(id, put);
    }
}
