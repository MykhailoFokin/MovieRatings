package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.MasterCreateDTO;
import solvve.course.dto.MasterPatchDTO;
import solvve.course.dto.MasterReadDTO;
import solvve.course.service.MasterService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @GetMapping("/{id}")
    public MasterReadDTO getMaster(@PathVariable UUID id) {
        return masterService.getMaster(id);
    }

    @PostMapping
    public MasterReadDTO createMaster(@RequestBody MasterCreateDTO createDTO){
        return masterService.createMaster(createDTO);
    }

    @PatchMapping("/{id}")
    public MasterReadDTO patchMaster(@PathVariable UUID id, @RequestBody MasterPatchDTO patch){
        return masterService.patchMaster(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMaster(@PathVariable UUID id){
        masterService.deleteMaster(id);
    }
}
