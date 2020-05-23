package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.AdminOrContentManager;
import solvve.course.dto.UserTypoRequestReadDTO;
import solvve.course.service.UserTypoRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-typo-requests")
public class UserTypoRequestController {

    @Autowired
    private UserTypoRequestService userTypoRequestService;

    @AdminOrContentManager
    @GetMapping
    public List<UserTypoRequestReadDTO> getUserTypoRequests() {
        return userTypoRequestService.getUserTypoRequests();
    }
}
