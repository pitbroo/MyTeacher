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
 * Criteria class for the {@link pl.pbrodziak.domain.Course} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter value;

    private FloatFilter price;

    private StringFilter category;

    private StringFilter description;

    private LongFilter userId;

    private LongFilter paymentId;

    private LongFilter courseUserId;

    public CourseCriteria() {}

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.courseUserId = other.courseUserId == null ? null : other.courseUserId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getValue() {
        return value;
    }

    public LongFilter value() {
        if (value == null) {
            value = new LongFilter();
        }
        return value;
    }

    public void setValue(LongFilter value) {
        this.value = value;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public StringFilter getCategory() {
        return category;
    }

    public StringFilter category() {
        if (category == null) {
            category = new StringFilter();
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public LongFilter getCourseUserId() {
        return courseUserId;
    }

    public LongFilter courseUserId() {
        if (courseUserId == null) {
            courseUserId = new LongFilter();
        }
        return courseUserId;
    }

    public void setCourseUserId(LongFilter courseUserId) {
        this.courseUserId = courseUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(value, that.value) &&
            Objects.equals(price, that.price) &&
            Objects.equals(category, that.category) &&
            Objects.equals(description, that.description) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(courseUserId, that.courseUserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, price, category, description, userId, paymentId, courseUserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (courseUserId != null ? "courseUserId=" + courseUserId + ", " : "") +
            "}";
    }
}
