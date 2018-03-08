package br.net.triangulohackerspace.westation.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Data entity. This class is used in DataResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /data?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ZonedDateTimeFilter serverTime;

    private LongFilter timestamp;

    private StringFilter value;

    private LongFilter stationId;

    private LongFilter sensorId;

    public DataCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getServerTime() {
        return serverTime;
    }

    public void setServerTime(ZonedDateTimeFilter serverTime) {
        this.serverTime = serverTime;
    }

    public LongFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LongFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getValue() {
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public LongFilter getStationId() {
        return stationId;
    }

    public void setStationId(LongFilter stationId) {
        this.stationId = stationId;
    }

    public LongFilter getSensorId() {
        return sensorId;
    }

    public void setSensorId(LongFilter sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public String toString() {
        return "DataCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serverTime != null ? "serverTime=" + serverTime + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (stationId != null ? "stationId=" + stationId + ", " : "") +
                (sensorId != null ? "sensorId=" + sensorId + ", " : "") +
            "}";
    }

}
