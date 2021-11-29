package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class CourseUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseUser.class);
        CourseUser courseUser1 = new CourseUser();
        courseUser1.setId(1L);
        CourseUser courseUser2 = new CourseUser();
        courseUser2.setId(courseUser1.getId());
        assertThat(courseUser1).isEqualTo(courseUser2);
        courseUser2.setId(2L);
        assertThat(courseUser1).isNotEqualTo(courseUser2);
        courseUser1.setId(null);
        assertThat(courseUser1).isNotEqualTo(courseUser2);
    }
}
