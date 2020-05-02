package solvve.course.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.PublicAccess;

@RestController
public class HealthController {

    @GetMapping("/health")
    public void health() {
    }
}
