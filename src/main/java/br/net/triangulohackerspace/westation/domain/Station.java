package br.net.triangulohackerspace.westation.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Station.
 */
@Entity
@Table(name = "station")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 60)
    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 65535)
    @Column(name = "port", nullable = false)
    private Integer port;

    @Size(max = 20)
    @Column(name = "sufix", length = 20)
    private String sufix;

    @Column(name = "longitude", precision=18, scale=15)
    private BigDecimal longitude;

    @Column(name = "latitude", precision=18, scale=15)
    private BigDecimal latitude;

    @NotNull
    @Size(min = 7, max = 15)
    @Column(name = "ip", length = 15, nullable = false)
    private String ip;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "station_sensor",
               joinColumns = @JoinColumn(name="stations_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="sensors_id", referencedColumnName="id"))
    private Set<Sensor> sensors = new HashSet<>();

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

    public Station name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPort() {
        return port;
    }

    public Station port(Integer port) {
        this.port = port;
        return this;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSufix() {
        return sufix;
    }

    public Station sufix(String sufix) {
        this.sufix = sufix;
        return this;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Station longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public Station latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getIp() {
        return ip;
    }

    public Station ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public Station sensors(Set<Sensor> sensors) {
        this.sensors = sensors;
        return this;
    }

    public Station addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        return this;
    }

    public Station removeSensor(Sensor sensor) {
        this.sensors.remove(sensor);
        return this;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
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
        Station station = (Station) o;
        if (station.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Station{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", port=" + getPort() +
            ", sufix='" + getSufix() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", ip='" + getIp() + "'" +
            "}";
    }
}
