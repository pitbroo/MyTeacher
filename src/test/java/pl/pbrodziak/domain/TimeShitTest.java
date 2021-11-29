package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class TimeShitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeShit.class);
        TimeShit timeShit1 = new TimeShit();
        timeShit1.setId(1L);
        TimeShit timeShit2 = new TimeShit();
        timeShit2.setId(timeShit1.getId());
        assertThat(timeShit1).isEqualTo(timeShit2);
        timeShit2.setId(2L);
        assertThat(timeShit1).isNotEqualTo(timeShit2);
        timeShit1.setId(null);
        assertThat(timeShit1).isNotEqualTo(timeShit2);
    }
}
