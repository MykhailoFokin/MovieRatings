package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.RoleSpoilerDataCreateDTO;
import solvve.course.dto.RoleSpoilerDataPatchDTO;
import solvve.course.dto.RoleSpoilerDataReadDTO;
import solvve.course.service.RoleSpoilerDataService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolespoilerdata")
public class RoleSpoilerDataController {

    @Autowired
    private RoleSpoilerDataService roleSpoilerDataService;

    @GetMapping("/{id}")
    public RoleSpoilerDataReadDTO getRoleSpoilerData(@PathVariable UUID id) {
        return roleSpoilerDataService.getRoleSpoilerData(id);
    }

    @PostMapping
    public RoleSpoilerDataReadDTO createRoleSpoilerData(@RequestBody RoleSpoilerDataCreateDTO createDTO){
        return roleSpoilerDataService.createRoleSpoilerData(createDTO);
    }

    @PatchMapping("/{id}")
    public RoleSpoilerDataReadDTO patchRoleSpoilerData(@PathVariable UUID id, @RequestBody RoleSpoilerDataPatchDTO patch){
        return roleSpoilerDataService.patchRoleSpoilerData(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleSpoilerData(@PathVariable UUID id){
        roleSpoilerDataService.deleteRoleSpoilerData(id);
    }
}
