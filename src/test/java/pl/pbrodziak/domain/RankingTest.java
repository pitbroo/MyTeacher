package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class RankingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ranking.class);
        Ranking ranking1 = new Ranking();
        ranking1.setId(1L);
        Ranking ranking2 = new Ranking();
        ranking2.setId(ranking1.getId());
        assertThat(ranking1).isEqualTo(ranking2);
        ranking2.setId(2L);
        assertThat(ranking1).isNotEqualTo(ranking2);
        ranking1.setId(null);
        assertThat(ranking1).isNotEqualTo(ranking2);
    }
}
