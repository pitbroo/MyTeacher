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
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "point_grade")
    private Long pointGrade;

    @Column(name = "content")
    private String content;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "course", "tasks" }, allowSetters = true)
    private Lesson lesson;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "task" }, allowSetters = true)
    private Set<TaskSolved> taskSolveds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Task id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Task title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPointGrade() {
        return this.pointGrade;
    }

    public Task pointGrade(Long pointGrade) {
        this.pointGrade = pointGrade;
        return this;
    }

    public void setPointGrade(Long pointGrade) {
        this.pointGrade = pointGrade;
    }

    public String getContent() {
        return this.content;
    }

    public Task content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public Task deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public byte[] getAttachment() {
        return this.attachment;
    }

    public Task attachment(byte[] attachment) {
        this.attachment = attachment;
        return this;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return this.attachmentContentType;
    }

    public Task attachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
        return this;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

    public Lesson getLesson() {
        return this.lesson;
    }

    public Task lesson(Lesson lesson) {
        this.setLesson(lesson);
        return this;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Set<TaskSolved> getTaskSolveds() {
        return this.taskSolveds;
    }

    public Task taskSolveds(Set<TaskSolved> taskSolveds) {
        this.setTaskSolveds(taskSolveds);
        return this;
    }

    public Task addTaskSolved(TaskSolved taskSolved) {
        this.taskSolveds.add(taskSolved);
        taskSolved.setTask(this);
        return this;
    }

    public Task removeTaskSolved(TaskSolved taskSolved) {
        this.taskSolveds.remove(taskSolved);
        taskSolved.setTask(null);
        return this;
    }

    public void setTaskSolveds(Set<TaskSolved> taskSolveds) {
        if (this.taskSolveds != null) {
            this.taskSolveds.forEach(i -> i.setTask(null));
        }
        if (taskSolveds != null) {
            taskSolveds.forEach(i -> i.setTask(this));
        }
        this.taskSolveds = taskSolveds;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", pointGrade=" + getPointGrade() +
            ", content='" + getContent() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", attachment='" + getAttachment() + "'" +
            ", attachmentContentType='" + getAttachmentContentType() + "'" +
            "}";
    }
}
