package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.*;
import solvve.course.dto.RoleReadDTO;

import java.util.List;

public class MovieRolesServiceTest extends BaseTest {

    @Autowired
    private MovieRolesService movieRolesService;

    @Test
    public void testGetMovieReviewCompliant() {
        Movie movie = testObjectsFactory.createMovie();
        Person person = testObjectsFactory.createPerson();
        Role role = testObjectsFactory.createRole(person, movie);

        List<RoleReadDTO> readDTO = movieRolesService.getMovieRoles(movie.getId());
        Assertions.assertThat(readDTO).extracting("id").containsExactlyInAnyOrder(role.getId());
    }
}
