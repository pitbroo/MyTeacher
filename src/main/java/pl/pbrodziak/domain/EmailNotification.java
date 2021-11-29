package pl.pbrodziak.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EmailNotification.
 */
@Entity
@Table(name = "email_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EmailNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private LocalDate time;

    @Column(name = "teacher")
    private String teacher;

    @OneToMany(mappedBy = "emailNotification")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "emailNotification" }, allowSetters = true)
    private Set<EmailNotificationUser> emailNotificationUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailNotification id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public EmailNotification content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getTime() {
        return this.time;
    }

    public EmailNotification time(LocalDate time) {
        this.time = time;
        return this;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getTeacher() {
        return this.teacher;
    }

    public EmailNotification teacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Set<EmailNotificationUser> getEmailNotificationUsers() {
        return this.emailNotificationUsers;
    }

    public EmailNotification emailNotificationUsers(Set<EmailNotificationUser> emailNotificationUsers) {
        this.setEmailNotificationUsers(emailNotificationUsers);
        return this;
    }

    public EmailNotification addEmailNotificationUser(EmailNotificationUser emailNotificationUser) {
        this.emailNotificationUsers.add(emailNotificationUser);
        emailNotificationUser.setEmailNotification(this);
        return this;
    }

    public EmailNotification removeEmailNotificationUser(EmailNotificationUser emailNotificationUser) {
        this.emailNotificationUsers.remove(emailNotificationUser);
        emailNotificationUser.setEmailNotification(null);
        return this;
    }

    public void setEmailNotificationUsers(Set<EmailNotificationUser> emailNotificationUsers) {
        if (this.emailNotificationUsers != null) {
            this.emailNotificationUsers.forEach(i -> i.setEmailNotification(null));
        }
        if (emailNotificationUsers != null) {
            emailNotificationUsers.forEach(i -> i.setEmailNotification(this));
        }
        this.emailNotificationUsers = emailNotificationUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailNotification)) {
            return false;
        }
        return id != null && id.equals(((EmailNotification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotification{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", time='" + getTime() + "'" +
            ", teacher='" + getTeacher() + "'" +
            "}";
    }
}
