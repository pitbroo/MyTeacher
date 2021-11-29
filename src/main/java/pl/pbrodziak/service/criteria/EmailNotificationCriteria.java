package pl.pbrodziak.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pl.pbrodziak.domain.EmailNotification} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.EmailNotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /email-notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmailNotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter content;

    private LocalDateFilter time;

    private StringFilter teacher;

    private LongFilter emailNotificationUserId;

    public EmailNotificationCriteria() {}

    public EmailNotificationCriteria(EmailNotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.teacher = other.teacher == null ? null : other.teacher.copy();
        this.emailNotificationUserId = other.emailNotificationUserId == null ? null : other.emailNotificationUserId.copy();
    }

    @Override
    public EmailNotificationCriteria copy() {
        return new EmailNotificationCriteria(this);
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

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public LocalDateFilter getTime() {
        return time;
    }

    public LocalDateFilter time() {
        if (time == null) {
            time = new LocalDateFilter();
        }
        return time;
    }

    public void setTime(LocalDateFilter time) {
        this.time = time;
    }

    public StringFilter getTeacher() {
        return teacher;
    }

    public StringFilter teacher() {
        if (teacher == null) {
            teacher = new StringFilter();
        }
        return teacher;
    }

    public void setTeacher(StringFilter teacher) {
        this.teacher = teacher;
    }

    public LongFilter getEmailNotificationUserId() {
        return emailNotificationUserId;
    }

    public LongFilter emailNotificationUserId() {
        if (emailNotificationUserId == null) {
            emailNotificationUserId = new LongFilter();
        }
        return emailNotificationUserId;
    }

    public void setEmailNotificationUserId(LongFilter emailNotificationUserId) {
        this.emailNotificationUserId = emailNotificationUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmailNotificationCriteria that = (EmailNotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            Objects.equals(time, that.time) &&
            Objects.equals(teacher, that.teacher) &&
            Objects.equals(emailNotificationUserId, that.emailNotificationUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, time, teacher, emailNotificationUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailNotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (time != null ? "time=" + time + ", " : "") +
            (teacher != null ? "teacher=" + teacher + ", " : "") +
            (emailNotificationUserId != null ? "emailNotificationUserId=" + emailNotificationUserId + ", " : "") +
            "}";
    }
}
