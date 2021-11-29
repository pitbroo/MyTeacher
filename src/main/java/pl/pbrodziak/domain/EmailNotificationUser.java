package pl.pbrodziak.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailNotificationUser.
 */
@Entity
@Table(name = "email_notification_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmailNotificationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "emailNotificationUsers" }, allowSetters = true)
    private EmailNotification emailNotification;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailNotificationUser id(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public EmailNotificationUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EmailNotification getEmailNotification() {
        return this.emailNotification;
    }

    public EmailNotificationUser emailNotification(EmailNotification emailNotification) {
        this.setEmailNotification(emailNotification);
        return this;
    }

    public void setEmailNotification(EmailNotification emailNotification) {
        this.emailNotification = emailNotification;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotificationUser)) {
            return false;
        }
        return id != null && id.equals(((EmailNotificationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationUser{" +
            "id=" + getId() +
            "}";
    }
}
