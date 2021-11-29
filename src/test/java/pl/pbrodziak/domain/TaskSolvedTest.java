package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class TaskSolvedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskSolved.class);
        TaskSolved taskSolved1 = new TaskSolved();
        taskSolved1.setId(1L);
        TaskSolved taskSolved2 = new TaskSolved();
        taskSolved2.setId(taskSolved1.getId());
        assertThat(taskSolved1).isEqualTo(taskSolved2);
        taskSolved2.setId(2L);
        assertThat(taskSolved1).isNotEqualTo(taskSolved2);
        taskSolved1.setId(null);
        assertThat(taskSolved1).isNotEqualTo(taskSolved2);
    }
}
