package org.ums.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.ums.domain.TotalSeat} entity. This class is used
 * in {@link org.ums.web.rest.TotalSeatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /total-seats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TotalSeatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter seat;

    private LocalDateFilter createdOn;

    private LocalDateFilter modifiedOn;

    private StringFilter modifiedBy;

    private LongFilter facultyProgramId;

    public TotalSeatCriteria() {
    }

    public TotalSeatCriteria(TotalSeatCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.seat = other.seat == null ? null : other.seat.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
        this.facultyProgramId = other.facultyProgramId == null ? null : other.facultyProgramId.copy();
    }

    @Override
    public TotalSeatCriteria copy() {
        return new TotalSeatCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSeat() {
        return seat;
    }

    public void setSeat(IntegerFilter seat) {
        this.seat = seat;
    }

    public LocalDateFilter getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateFilter createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateFilter getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateFilter modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public StringFilter getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(StringFilter modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LongFilter getFacultyProgramId() {
        return facultyProgramId;
    }

    public void setFacultyProgramId(LongFilter facultyProgramId) {
        this.facultyProgramId = facultyProgramId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TotalSeatCriteria that = (TotalSeatCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(seat, that.seat) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedOn, that.modifiedOn) &&
            Objects.equals(modifiedBy, that.modifiedBy) &&
            Objects.equals(facultyProgramId, that.facultyProgramId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        seat,
        createdOn,
        modifiedOn,
        modifiedBy,
        facultyProgramId
        );
    }

    @Override
    public String toString() {
        return "TotalSeatCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (seat != null ? "seat=" + seat + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
                (facultyProgramId != null ? "facultyProgramId=" + facultyProgramId + ", " : "") +
            "}";
    }

}
