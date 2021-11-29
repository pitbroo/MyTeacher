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
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "date")
    private LocalDate date;

    @JsonIgnoreProperties(value = { "payment", "lessons", "courseUsers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Course course;

    @OneToMany(mappedBy = "payment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "payment", "user" }, allowSetters = true)
    private Set<PaymentUser> paymentUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public Payment deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Payment date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Course getCourse() {
        return this.course;
    }

    public Payment course(Course course) {
        this.setCourse(course);
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<PaymentUser> getPaymentUsers() {
        return this.paymentUsers;
    }

    public Payment paymentUsers(Set<PaymentUser> paymentUsers) {
        this.setPaymentUsers(paymentUsers);
        return this;
    }

    public Payment addPaymentUser(PaymentUser paymentUser) {
        this.paymentUsers.add(paymentUser);
        paymentUser.setPayment(this);
        return this;
    }

    public Payment removePaymentUser(PaymentUser paymentUser) {
        this.paymentUsers.remove(paymentUser);
        paymentUser.setPayment(null);
        return this;
    }

    public void setPaymentUsers(Set<PaymentUser> paymentUsers) {
        if (this.paymentUsers != null) {
            this.paymentUsers.forEach(i -> i.setPayment(null));
        }
        if (paymentUsers != null) {
            paymentUsers.forEach(i -> i.setPayment(this));
        }
        this.paymentUsers = paymentUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", deadline='" + getDeadline() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
