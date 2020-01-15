package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.UserTypesCreateDTO;
import solvve.course.dto.UserTypesPatchDTO;
import solvve.course.dto.UserTypesReadDTO;
import solvve.course.service.UserTypesService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usertypes")
public class UserTypesController {

    @Autowired
    private UserTypesService userTypesService;

    @GetMapping("/{id}")
    public UserTypesReadDTO getUserTypes(@PathVariable UUID id) {
        return userTypesService.getUserTypes(id);
    }

    @PostMapping
    public UserTypesReadDTO createUserTypes(@RequestBody UserTypesCreateDTO createDTO){
        return userTypesService.createUserTypes(createDTO);
    }

    @PatchMapping("/{id}")
    public UserTypesReadDTO patchUserTypes(@PathVariable UUID id, @RequestBody UserTypesPatchDTO patch){
        return userTypesService.patchUserTypes(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteUserTypes(@PathVariable UUID id){
        userTypesService.deleteUserTypes(id);
    }
}
