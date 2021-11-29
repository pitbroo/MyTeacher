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
 * Criteria class for the {@link pl.pbrodziak.domain.TaskSolved} entity. This class is used
 * in {@link pl.pbrodziak.web.rest.TaskSolvedResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /task-solveds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskSolvedCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter pointGrade;

    private StringFilter content;

    private LocalDateFilter deadline;

    private LocalDateFilter sendDay;

    private StringFilter answer;

    private LongFilter userId;

    private LongFilter taskId;

    public TaskSolvedCriteria() {}

    public TaskSolvedCriteria(TaskSolvedCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pointGrade = other.pointGrade == null ? null : other.pointGrade.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.deadline = other.deadline == null ? null : other.deadline.copy();
        this.sendDay = other.sendDay == null ? null : other.sendDay.copy();
        this.answer = other.answer == null ? null : other.answer.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
    }

    @Override
    public TaskSolvedCriteria copy() {
        return new TaskSolvedCriteria(this);
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

    public LocalDateFilter getSendDay() {
        return sendDay;
    }

    public LocalDateFilter sendDay() {
        if (sendDay == null) {
            sendDay = new LocalDateFilter();
        }
        return sendDay;
    }

    public void setSendDay(LocalDateFilter sendDay) {
        this.sendDay = sendDay;
    }

    public StringFilter getAnswer() {
        return answer;
    }

    public StringFilter answer() {
        if (answer == null) {
            answer = new StringFilter();
        }
        return answer;
    }

    public void setAnswer(StringFilter answer) {
        this.answer = answer;
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
        final TaskSolvedCriteria that = (TaskSolvedCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pointGrade, that.pointGrade) &&
            Objects.equals(content, that.content) &&
            Objects.equals(deadline, that.deadline) &&
            Objects.equals(sendDay, that.sendDay) &&
            Objects.equals(answer, that.answer) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(taskId, that.taskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pointGrade, content, deadline, sendDay, answer, userId, taskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskSolvedCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pointGrade != null ? "pointGrade=" + pointGrade + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (deadline != null ? "deadline=" + deadline + ", " : "") +
            (sendDay != null ? "sendDay=" + sendDay + ", " : "") +
            (answer != null ? "answer=" + answer + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (taskId != null ? "taskId=" + taskId + ", " : "") +
            "}";
    }
}
