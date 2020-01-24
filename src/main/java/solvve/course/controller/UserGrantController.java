package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.UserGrantCreateDTO;
import solvve.course.dto.UserGrantPatchDTO;
import solvve.course.dto.UserGrantPutDTO;
import solvve.course.dto.UserGrantReadDTO;
import solvve.course.service.UserGrantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usergrants")
public class UserGrantController {

    @Autowired
    private UserGrantService userGrantService;

    @GetMapping("/{id}")
    public UserGrantReadDTO getGrants(@PathVariable UUID id) {
        return userGrantService.getGrants(id);
    }

    @PostMapping
    public UserGrantReadDTO createGrants(@RequestBody UserGrantCreateDTO createDTO){
        return userGrantService.createGrants(createDTO);
    }

    @PatchMapping("/{id}")
    public UserGrantReadDTO patchGrants(@PathVariable UUID id, @RequestBody UserGrantPatchDTO patch){
        return userGrantService.patchGrants(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteGrants(@PathVariable UUID id){
        userGrantService.deleteGrants(id);
    }

    @PutMapping("/{id}")
    public UserGrantReadDTO putGrants(@PathVariable UUID id, @RequestBody UserGrantPutDTO put){
        return userGrantService.putGrants(id, put);
    }
}
