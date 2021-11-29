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
 * Criteria class for the {@link pl.pbrodziak.domain.Task} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter pointGrade;

    private StringFilter content;

    private LocalDateFilter deadline;

    private LongFilter lessonId;

    private LongFilter taskSolvedId;

    public TaskCriteria() {}

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pointGrade = other.pointGrade == null ? null : other.pointGrade.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.deadline = other.deadline == null ? null : other.deadline.copy();
        this.lessonId = other.lessonId == null ? null : other.lessonId.copy();
        this.taskSolvedId = other.taskSolvedId == null ? null : other.taskSolvedId.copy();
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
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

    public LongFilter getPointGrade() {
        return pointGrade;
    }

    public LongFilter pointGrade() {
        if (pointGrade == null) {
            pointGrade = new LongFilter();
        }
        return pointGrade;
    }

    public void setPointGrade(LongFilter pointGrade) {
        this.pointGrade = pointGrade;
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

    public LongFilter getLessonId() {
        return lessonId;
    }

    public LongFilter lessonId() {
        if (lessonId == null) {
            lessonId = new LongFilter();
        }
        return lessonId;
    }

    public void setLessonId(LongFilter lessonId) {
        this.lessonId = lessonId;
    }

    public LongFilter getTaskSolvedId() {
        return taskSolvedId;
    }

    public LongFilter taskSolvedId() {
        if (taskSolvedId == null) {
            taskSolvedId = new LongFilter();
        }
        return taskSolvedId;
    }

    public void setTaskSolvedId(LongFilter taskSolvedId) {
        this.taskSolvedId = taskSolvedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskCriteria that = (TaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pointGrade, that.pointGrade) &&
            Objects.equals(content, that.content) &&
            Objects.equals(deadline, that.deadline) &&
            Objects.equals(lessonId, that.lessonId) &&
            Objects.equals(taskSolvedId, that.taskSolvedId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pointGrade, content, deadline, lessonId, taskSolvedId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pointGrade != null ? "pointGrade=" + pointGrade + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (deadline != null ? "deadline=" + deadline + ", " : "") +
            (lessonId != null ? "lessonId=" + lessonId + ", " : "") +
            (taskSolvedId != null ? "taskSolvedId=" + taskSolvedId + ", " : "") +
            "}";
    }
}
