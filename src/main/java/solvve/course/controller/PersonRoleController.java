package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solvve.course.controller.security.PublicAccess;
import solvve.course.dto.RoleReadDTO;
import solvve.course.service.PersonRoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/person/{personId}/roles")
public class PersonRoleController {

    @Autowired
    private PersonRoleService personRoleService;

    @PublicAccess
    @GetMapping
    public List<RoleReadDTO> getRolesByPerson(@PathVariable UUID personId) {
        return personRoleService.getRolesByPerson(personId);
    }
}
