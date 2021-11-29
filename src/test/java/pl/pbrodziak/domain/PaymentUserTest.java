package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class PaymentUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentUser.class);
        PaymentUser paymentUser1 = new PaymentUser();
        paymentUser1.setId(1L);
        PaymentUser paymentUser2 = new PaymentUser();
        paymentUser2.setId(paymentUser1.getId());
        assertThat(paymentUser1).isEqualTo(paymentUser2);
        paymentUser2.setId(2L);
        assertThat(paymentUser1).isNotEqualTo(paymentUser2);
        paymentUser1.setId(null);
        assertThat(paymentUser1).isNotEqualTo(paymentUser2);
    }
}
