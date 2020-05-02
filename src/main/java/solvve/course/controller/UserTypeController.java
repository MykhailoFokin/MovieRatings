package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.Admin;
import solvve.course.dto.UserTypeCreateDTO;
import solvve.course.dto.UserTypePatchDTO;
import solvve.course.dto.UserTypePutDTO;
import solvve.course.dto.UserTypeReadDTO;
import solvve.course.service.UserTypeService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usertypes")
public class UserTypeController {

    @Autowired
    private UserTypeService userTypeService;

    @Admin
    @GetMapping("/{id}")
    public UserTypeReadDTO getUserTypes(@PathVariable UUID id) {
        return userTypeService.getUserTypes(id);
    }

    @Admin
    @PostMapping
    public UserTypeReadDTO createUserTypes(@RequestBody @Valid UserTypeCreateDTO createDTO) {
        return userTypeService.createUserTypes(createDTO);
    }

    @Admin
    @PatchMapping("/{id}")
    public UserTypeReadDTO patchUserTypes(@PathVariable UUID id, @RequestBody UserTypePatchDTO patch) {
        return userTypeService.patchUserTypes(id, patch);
    }

    @Admin
    @DeleteMapping("/{id}")
    public void deleteUserTypes(@PathVariable UUID id) {
        userTypeService.deleteUserTypes(id);
    }

    @Admin
    @PutMapping("/{id}")
    public UserTypeReadDTO putUserTypes(@PathVariable UUID id, @RequestBody @Valid UserTypePutDTO put) {
        return userTypeService.updateUserTypes(id, put);
    }
}
