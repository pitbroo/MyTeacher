package pl.pbrodziak.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Long value;

    @Column(name = "price")
    private Float price;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "insreuctor")
    private String insreuctor;

    @JsonIgnoreProperties(value = { "course", "paymentUsers" }, allowSetters = true)
    @OneToOne(mappedBy = "course")
    private Payment payment;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "course", "tasks" }, allowSetters = true)
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "course" }, allowSetters = true)
    private Set<CourseUser> courseUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return this.value;
    }

    public Course value(Long value) {
        this.value = value;
        return this;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Float getPrice() {
        return this.price;
    }

    public Course price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategory() {
        return this.category;
    }

    public Course category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsreuctor() {
        return this.insreuctor;
    }

    public Course insreuctor(String insreuctor) {
        this.insreuctor = insreuctor;
        return this;
    }

    public void setInsreuctor(String insreuctor) {
        this.insreuctor = insreuctor;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public Course payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public void setPayment(Payment payment) {
        if (this.payment != null) {
            this.payment.setCourse(null);
        }
        if (payment != null) {
            payment.setCourse(this);
        }
        this.payment = payment;
    }

    public Set<Lesson> getLessons() {
        return this.lessons;
    }

    public Course lessons(Set<Lesson> lessons) {
        this.setLessons(lessons);
        return this;
    }

    public Course addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setCourse(this);
        return this;
    }

    public Course removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setCourse(null);
        return this;
    }

    public void setLessons(Set<Lesson> lessons) {
        if (this.lessons != null) {
            this.lessons.forEach(i -> i.setCourse(null));
        }
        if (lessons != null) {
            lessons.forEach(i -> i.setCourse(this));
        }
        this.lessons = lessons;
    }

    public Set<CourseUser> getCourseUsers() {
        return this.courseUsers;
    }

    public Course courseUsers(Set<CourseUser> courseUsers) {
        this.setCourseUsers(courseUsers);
        return this;
    }

    public Course addCourseUser(CourseUser courseUser) {
        this.courseUsers.add(courseUser);
        courseUser.setCourse(this);
        return this;
    }

    public Course removeCourseUser(CourseUser courseUser) {
        this.courseUsers.remove(courseUser);
        courseUser.setCourse(null);
        return this;
    }

    public void setCourseUsers(Set<CourseUser> courseUsers) {
        if (this.courseUsers != null) {
            this.courseUsers.forEach(i -> i.setCourse(null));
        }
        if (courseUsers != null) {
            courseUsers.forEach(i -> i.setCourse(this));
        }
        this.courseUsers = courseUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            ", price=" + getPrice() +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", insreuctor='" + getInsreuctor() + "'" +
            "}";
    }
}
