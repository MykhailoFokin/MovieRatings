package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.UserTypesCreateDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.service.UserTypesService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usertypes")
public class UserTypesController {

    @Autowired
    private UserTypesService userTypes;

    @GetMapping("/{id}")
    public UserTypesReadDTO getUserTypes(@PathVariable UUID id) {
        return userTypes.getUserTypes(id);
    }

    @PostMapping
    public UserTypesReadDTO createUserTypes(@RequestBody UserTypesCreateDTO createDTO){
        return userTypes.createUserTypes(createDTO);
    }
}
