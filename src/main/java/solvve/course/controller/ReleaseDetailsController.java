package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.ReleaseDetailsCreateDTO;
import solvve.course.dto.ReleaseDetailsPatchDTO;
import solvve.course.dto.ReleaseDetailsReadDTO;
import solvve.course.service.ReleaseDetailsService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/releasedetails")
public class ReleaseDetailsController {

    @Autowired
    private ReleaseDetailsService releaseDetailsService;

    @GetMapping("/{id}")
    public ReleaseDetailsReadDTO getReleaseDetails(@PathVariable UUID id) {
        return releaseDetailsService.getReleaseDetails(id);
    }

    @PostMapping
    public ReleaseDetailsReadDTO createReleaseDetails(@RequestBody ReleaseDetailsCreateDTO createDTO){
        return releaseDetailsService.createReleaseDetails(createDTO);
    }

    @PatchMapping("/{id}")
    public ReleaseDetailsReadDTO patchReleaseDetails(@PathVariable UUID id, @RequestBody ReleaseDetailsPatchDTO patch){
        return releaseDetailsService.patchReleaseDetails(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteReleaseDetails(@PathVariable UUID id){
        releaseDetailsService.deleteReleaseDetails(id);
    }
}
