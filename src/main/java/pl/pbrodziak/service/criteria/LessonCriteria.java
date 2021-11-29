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
 * Criteria class for the {@link pl.pbrodziak.domain.Lesson} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.LessonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lessons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LessonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter status;

    private LocalDateFilter dateStart;

    private LocalDateFilter dateEnd;

    private StringFilter classroomOrAddres;

    private LongFilter courseId;

    private LongFilter taskId;

    public LessonCriteria() {}

    public LessonCriteria(LessonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.dateEnd = other.dateEnd == null ? null : other.dateEnd.copy();
        this.classroomOrAddres = other.classroomOrAddres == null ? null : other.classroomOrAddres.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
    }

    @Override
    public LessonCriteria copy() {
        return new LessonCriteria(this);
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

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LocalDateFilter getDateStart() {
        return dateStart;
    }

    public LocalDateFilter dateStart() {
        if (dateStart == null) {
            dateStart = new LocalDateFilter();
        }
        return dateStart;
    }

    public void setDateStart(LocalDateFilter dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateFilter getDateEnd() {
        return dateEnd;
    }

    public LocalDateFilter dateEnd() {
        if (dateEnd == null) {
            dateEnd = new LocalDateFilter();
        }
        return dateEnd;
    }

    public void setDateEnd(LocalDateFilter dateEnd) {
        this.dateEnd = dateEnd;
    }

    public StringFilter getClassroomOrAddres() {
        return classroomOrAddres;
    }

    public StringFilter classroomOrAddres() {
        if (classroomOrAddres == null) {
            classroomOrAddres = new StringFilter();
        }
        return classroomOrAddres;
    }

    public void setClassroomOrAddres(StringFilter classroomOrAddres) {
        this.classroomOrAddres = classroomOrAddres;
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

    public LongFilter getTaskId() {
        return taskId;
    }

    public LongFilter taskId() {
        if (taskId == null) {
            taskId = new LongFilter();
        }
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LessonCriteria that = (LessonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(dateEnd, that.dateEnd) &&
            Objects.equals(classroomOrAddres, that.classroomOrAddres) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(taskId, that.taskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, dateStart, dateEnd, classroomOrAddres, courseId, taskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LessonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
            (dateEnd != null ? "dateEnd=" + dateEnd + ", " : "") +
            (classroomOrAddres != null ? "classroomOrAddres=" + classroomOrAddres + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }
}
