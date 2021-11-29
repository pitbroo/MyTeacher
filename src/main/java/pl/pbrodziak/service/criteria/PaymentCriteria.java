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
 * Criteria class for the {@link pl.pbrodziak.domain.Payment} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter deadline;

    private LocalDateFilter date;

    private LongFilter courseId;

    private LongFilter paymentUserId;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deadline = other.deadline == null ? null : other.deadline.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.paymentUserId = other.paymentUserId == null ? null : other.paymentUserId.copy();
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
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

    public LocalDateFilter getDeadline() {
        return deadline;
    }

    public LocalDateFilter deadline() {
        if (deadline == null) {
            deadline = new LocalDateFilter();
        }
        return deadline;
    }

    public void setDeadline(LocalDateFilter deadline) {
        this.deadline = deadline;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public LongFilter courseId() {
        if (courseId == null) {
            courseId = new LongFilter();
        }
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getPaymentUserId() {
        return paymentUserId;
    }

    public LongFilter paymentUserId() {
        if (paymentUserId == null) {
            paymentUserId = new LongFilter();
        }
        return paymentUserId;
    }

    public void setPaymentUserId(LongFilter paymentUserId) {
        this.paymentUserId = paymentUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deadline, that.deadline) &&
            Objects.equals(date, that.date) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(paymentUserId, that.paymentUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deadline, date, courseId, paymentUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (deadline != null ? "deadline=" + deadline + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (paymentUserId != null ? "paymentUserId=" + paymentUserId + ", " : "") +
            "}";
    }
}
