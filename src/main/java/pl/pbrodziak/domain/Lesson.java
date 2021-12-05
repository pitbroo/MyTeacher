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
 * A Lesson.
 */
@Entity
@Table(name = "lesson")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Column(name = "classroom_or_addres")
    private String classroomOrAddres;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "payment", "courseUsers" }, allowSetters = true)
    private Course course;

    @OneToMany(mappedBy = "lesson")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lesson", "taskSolveds" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lesson id(Long id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public Lesson status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDateStart() {
        return this.dateStart;
    }

    public Lesson dateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public Lesson setName(String name) {
        this.name = name;
        return this;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return this.dateEnd;
    }

    public Lesson dateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getClassroomOrAddres() {
        return this.classroomOrAddres;
    }

    public Lesson classroomOrAddres(String classroomOrAddres) {
        this.classroomOrAddres = classroomOrAddres;
        return this;
    }

    public void setClassroomOrAddres(String classroomOrAddres) {
        this.classroomOrAddres = classroomOrAddres;
    }

    public Course getCourse() {
        return this.course;
    }

    public Lesson course(Course course) {
        this.setCourse(course);
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public Lesson tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Lesson addTask(Task task) {
        this.tasks.add(task);
        task.setLesson(this);
        return this;
    }

    public Lesson removeTask(Task task) {
        this.tasks.remove(task);
        task.setLesson(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setLesson(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setLesson(this));
        }
        this.tasks = tasks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lesson)) {
            return false;
        }
        return id != null && id.equals(((Lesson) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lesson{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", classroomOrAddres='" + getClassroomOrAddres() + "'" +
            "}";
    }
}
