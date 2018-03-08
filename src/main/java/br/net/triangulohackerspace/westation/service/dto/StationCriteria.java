package br.net.triangulohackerspace.westation.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;





/**
 * Criteria class for the Station entity. This class is used in StationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private IntegerFilter port;

    private StringFilter sufix;

    private BigDecimalFilter longitude;

    private BigDecimalFilter latitude;

    private StringFilter ip;

    private LongFilter sensorId;

    public StationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getPort() {
        return port;
    }

    public void setPort(IntegerFilter port) {
        this.port = port;
    }

    public StringFilter getSufix() {
        return sufix;
    }

    public void setSufix(StringFilter sufix) {
        this.sufix = sufix;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getIp() {
        return ip;
    }

    public void setIp(StringFilter ip) {
        this.ip = ip;
    }

    public LongFilter getSensorId() {
        return sensorId;
    }

    public void setSensorId(LongFilter sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String toString() {
        return "StationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (port != null ? "port=" + port + ", " : "") +
                (sufix != null ? "sufix=" + sufix + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (ip != null ? "ip=" + ip + ", " : "") +
                (sensorId != null ? "sensorId=" + sensorId + ", " : "") +
            "}";
    }

}
