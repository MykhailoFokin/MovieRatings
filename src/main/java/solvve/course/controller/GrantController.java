package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.GrantCreateDTO;
import solvve.course.dto.GrantPatchDTO;
import solvve.course.dto.GrantReadDTO;
import solvve.course.service.GrantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grants")
public class GrantController {

    @Autowired
    private GrantService grantService;

    @GetMapping("/{id}")
    public GrantReadDTO getGrants(@PathVariable UUID id) {
        return grantService.getGrants(id);
    }

    @PostMapping
    public GrantReadDTO createGrants(@RequestBody GrantCreateDTO createDTO){
        return grantService.createGrants(createDTO);
    }

    @PatchMapping("/{id}")
    public GrantReadDTO patchGrants(@PathVariable UUID id, @RequestBody GrantPatchDTO patch){
        return grantService.patchGrants(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteGrants(@PathVariable UUID id){
        grantService.deleteGrants(id);
    }
}
