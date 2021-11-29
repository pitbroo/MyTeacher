package pl.pbrodziak.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TaskSolved.
 */
@Entity
@Table(name = "task_solved")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TaskSolved implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_grade")
    private Long pointGrade;

    @Column(name = "content")
    private String content;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "send_day")
    private LocalDate sendDay;

    @Column(name = "answer")
    private String answer;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "lesson", "taskSolveds" }, allowSetters = true)
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskSolved id(Long id) {
        this.id = id;
        return this;
    }

    public Long getPointGrade() {
        return this.pointGrade;
    }

    public TaskSolved pointGrade(Long pointGrade) {
        this.pointGrade = pointGrade;
        return this;
    }

    public void setPointGrade(Long pointGrade) {
        this.pointGrade = pointGrade;
    }

    public String getContent() {
        return this.content;
    }

    public TaskSolved content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public TaskSolved deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getSendDay() {
        return this.sendDay;
    }

    public TaskSolved sendDay(LocalDate sendDay) {
        this.sendDay = sendDay;
        return this;
    }

    public void setSendDay(LocalDate sendDay) {
        this.sendDay = sendDay;
    }

    public String getAnswer() {
        return this.answer;
    }

    public TaskSolved answer(String answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public byte[] getAttachment() {
        return this.attachment;
    }

    public TaskSolved attachment(byte[] attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return this.attachmentContentType;
    }

    public TaskSolved attachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
        return this;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

    public User getUser() {
        return this.user;
    }

    public TaskSolved user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return this.task;
    }

    public TaskSolved task(Task task) {
        this.setTask(task);
        return this;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskSolved)) {
            return false;
        }
        return id != null && id.equals(((TaskSolved) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaskSolved{" +
            "id=" + getId() +
            ", pointGrade=" + getPointGrade() +
            ", content='" + getContent() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", sendDay='" + getSendDay() + "'" +
            ", answer='" + getAnswer() + "'" +
            ", attachment='" + getAttachment() + "'" +
            ", attachmentContentType='" + getAttachmentContentType() + "'" +
            "}";
    }
}
