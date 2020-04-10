package solvve.course;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import solvve.course.utils.TestObjectsFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {"delete from visit","delete from news_user_review_note", "delete from news_user_review",
        "delete from news","delete from movie_review_compliant",
        "delete from movie_review_feedback","delete from movie_spoiler_data","delete from movie_vote",
        "delete from movie_review","delete from genre","delete from crew_type","delete from crew",
        "delete from movie_company","delete from role_review_compliant","delete from role_review_feedback",
        "delete from role_vote","delete from role_spoiler_data","delete from role_review","delete from role",
        "delete from user_grant","delete from person","delete from country",
        "delete from company_details","delete from portal_user","delete from user_type",
        "delete from release_detail","delete from movie"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseTest {

    @Autowired
    protected TestObjectsFactory testObjectsFactory;
}
