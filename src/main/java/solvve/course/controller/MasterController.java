package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.*;
import solvve.course.service.MasterService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/masters")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @GetMapping("/{id}")
    public MasterReadExtendedDTO getMaster(@PathVariable UUID id) {
        return masterService.getMaster(id);
    }

    @PostMapping
    public MasterReadDTO createMaster(@RequestBody MasterCreateDTO createDTO) {
        return masterService.createMaster(createDTO);
    }

    @PatchMapping("/{id}")
    public MasterReadDTO patchMaster(@PathVariable UUID id, @RequestBody MasterPatchDTO patch) {
        return masterService.patchMaster(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteMaster(@PathVariable UUID id){
        masterService.deleteMaster(id);
    }

    @PutMapping("/{id}")
    public MasterReadDTO putMaster(@PathVariable UUID id, @RequestBody MasterPutDTO put) {
        return masterService.updateMaster(id, put);
    }

    @GetMapping
    public List<MasterReadDTO> getVisits(MasterFilter filter) {
        return masterService.getMasters(filter);
    }
}
