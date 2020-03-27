package solvve.course.controller;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HealthController.class)
public class HealthControllerTest extends BaseControllerTest {

    @Test
    public void testHealth() throws Exception {
        mvc.perform(get("/health")).andExpect(status().isOk());
    }
}
