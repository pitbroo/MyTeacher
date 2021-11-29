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
 * Criteria class for the {@link pl.pbrodziak.domain.TimeShit} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.TimeShitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /time-shits?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TimeShitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter present;

    private LocalDateFilter date;

    private LongFilter userId;

    public TimeShitCriteria() {}

    public TimeShitCriteria(TimeShitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.present = other.present == null ? null : other.present.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public TimeShitCriteria copy() {
        return new TimeShitCriteria(this);
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

    public BooleanFilter getPresent() {
        return present;
    }

    public BooleanFilter present() {
        if (present == null) {
            present = new BooleanFilter();
        }
        return present;
    }

    public void setPresent(BooleanFilter present) {
        this.present = present;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TimeShitCriteria that = (TimeShitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(present, that.present) &&
            Objects.equals(date, that.date) &&
            Objects.equals(userId, that.userId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, present, date, userId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeShitCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (present != null ? "present=" + present + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }
}
