package br.net.triangulohackerspace.westation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 25)
    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @NotNull
    @Size(max = 25)
    @Column(name = "topic", length = 25, nullable = false)
    private String topic;

    @Min(value = 1)
    @Max(value = 86400)
    @Column(name = "threshold")
    private Integer threshold;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sensor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public Sensor topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public Sensor threshold(Integer threshold) {
        this.threshold = threshold;
        return this;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sensor sensor = (Sensor) o;
        if (sensor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", topic='" + getTopic() + "'" +
            ", threshold=" + getThreshold() +
            "}";
    }
}
