package pl.pbrodziak.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.pbrodziak.web.rest.TestUtil;

class EmailNotificationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailNotificationUser.class);
        EmailNotificationUser emailNotificationUser1 = new EmailNotificationUser();
        emailNotificationUser1.setId(1L);
        EmailNotificationUser emailNotificationUser2 = new EmailNotificationUser();
        emailNotificationUser2.setId(emailNotificationUser1.getId());
        assertThat(emailNotificationUser1).isEqualTo(emailNotificationUser2);
        emailNotificationUser2.setId(2L);
        assertThat(emailNotificationUser1).isNotEqualTo(emailNotificationUser2);
        emailNotificationUser1.setId(null);
        assertThat(emailNotificationUser1).isNotEqualTo(emailNotificationUser2);
    }
}
