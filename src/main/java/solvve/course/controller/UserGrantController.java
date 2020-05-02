package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.controller.security.Admin;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.service.UserGrantService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usergrants")
public class UserGrantController {

    @Autowired
    private UserGrantService userGrantService;

    @Admin
    @GetMapping("/{id}")
    public UserGrantReadDTO getGrants(@PathVariable UUID id) {
        return userGrantService.getGrants(id);
    }

    @Admin
    @PostMapping
    public UserGrantReadDTO createGrants(@RequestBody @Valid UserGrantCreateDTO createDTO) {
        return userGrantService.createGrants(createDTO);
    }
    
    @Admin
    @PatchMapping("/{id}")
    public UserGrantReadDTO patchGrants(@PathVariable UUID id, @RequestBody @Valid UserGrantPatchDTO patch) {
        return userGrantService.patchGrants(id, patch);
    }

    @Admin
    @DeleteMapping("/{id}")
    public void deleteGrants(@PathVariable UUID id) {
        userGrantService.deleteGrants(id);
    }

    @Admin
    @PutMapping("/{id}")
    public UserGrantReadDTO putGrants(@PathVariable UUID id, @RequestBody @Valid UserGrantPutDTO put) {
        return userGrantService.updateGrants(id, put);
    }
}
