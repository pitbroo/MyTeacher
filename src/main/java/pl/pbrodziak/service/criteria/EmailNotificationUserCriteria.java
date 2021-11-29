package pl.pbrodziak.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pl.pbrodziak.domain.EmailNotificationUser} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.EmailNotificationUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-notification-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmailNotificationUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private LongFilter emailNotificationId;

    public EmailNotificationUserCriteria() {}

    public EmailNotificationUserCriteria(EmailNotificationUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.emailNotificationId = other.emailNotificationId == null ? null : other.emailNotificationId.copy();
    }

    @Override
    public EmailNotificationUserCriteria copy() {
        return new EmailNotificationUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getEmailNotificationId() {
        return emailNotificationId;
    }

    public LongFilter emailNotificationId() {
        if (emailNotificationId == null) {
            emailNotificationId = new LongFilter();
        }
        return emailNotificationId;
    }

    public void setEmailNotificationId(LongFilter emailNotificationId) {
        this.emailNotificationId = emailNotificationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmailNotificationUserCriteria that = (EmailNotificationUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(emailNotificationId, that.emailNotificationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, emailNotificationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (emailNotificationId != null ? "emailNotificationId=" + emailNotificationId + ", " : "") +
            "}";
    }
}
