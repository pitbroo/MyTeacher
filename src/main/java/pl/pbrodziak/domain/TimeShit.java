package pl.pbrodziak.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimeShit.
 */
@Entity
@Table(name = "time_shit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeShit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "present")
    private Boolean present;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeShit id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getPresent() {
        return this.present;
    }

    public TimeShit present(Boolean present) {
        this.present = present;
        return this;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public TimeShit date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return this.user;
    }

    public TimeShit user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeShit)) {
            return false;
        }
        return id != null && id.equals(((TimeShit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeShit{" +
            "id=" + getId() +
            ", present='" + getPresent() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
