package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.ReleaseDetailCreateDTO;
import solvve.course.dto.ReleaseDetailPatchDTO;
import solvve.course.dto.ReleaseDetailPutDTO;
import solvve.course.dto.ReleaseDetailReadDTO;
import solvve.course.service.ReleaseDetailService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/releasedetails")
public class ReleaseDetailController {

    @Autowired
    private ReleaseDetailService releaseDetailService;

    @GetMapping("/{id}")
    public ReleaseDetailReadDTO getReleaseDetails(@PathVariable UUID id) {
        return releaseDetailService.getReleaseDetails(id);
    }

    @PostMapping
    public ReleaseDetailReadDTO createReleaseDetails(@RequestBody ReleaseDetailCreateDTO createDTO) {
        return releaseDetailService.createReleaseDetails(createDTO);
    }

    @PatchMapping("/{id}")
    public ReleaseDetailReadDTO patchReleaseDetails(@PathVariable UUID id, @RequestBody ReleaseDetailPatchDTO patch) {
        return releaseDetailService.patchReleaseDetails(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteReleaseDetails(@PathVariable UUID id) {
        releaseDetailService.deleteReleaseDetails(id);
    }

    @PutMapping("/{id}")
    public ReleaseDetailReadDTO putReleaseDetails(@PathVariable UUID id, @RequestBody ReleaseDetailPutDTO put) {
        return releaseDetailService.putReleaseDetails(id, put);
    }
}
