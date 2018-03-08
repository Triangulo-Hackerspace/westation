package br.net.triangulohackerspace.westation.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Sensor entity. This class is used in SensorResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sensors?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SensorCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter topic;

    private IntegerFilter threshold;

    public SensorCriteria() {
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

    public StringFilter getTopic() {
        return topic;
    }

    public void setTopic(StringFilter topic) {
        this.topic = topic;
    }

    public IntegerFilter getThreshold() {
        return threshold;
    }

    public void setThreshold(IntegerFilter threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "SensorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (topic != null ? "topic=" + topic + ", " : "") +
                (threshold != null ? "threshold=" + threshold + ", " : "") +
            "}";
    }

}
