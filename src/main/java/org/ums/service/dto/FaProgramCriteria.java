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
 * Criteria class for the {@link org.ums.domain.FaProgram} entity. This class is used
 * in {@link org.ums.web.rest.FaProgramResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fa-programs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FaProgramCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter createdOn;

    private LocalDateFilter modifiedOn;

    private StringFilter modifiedBy;

    private LongFilter semesterId;

    private LongFilter facultyId;

    private LongFilter programId;

    public FaProgramCriteria() {
    }

    public FaProgramCriteria(FaProgramCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
        this.semesterId = other.semesterId == null ? null : other.semesterId.copy();
        this.facultyId = other.facultyId == null ? null : other.facultyId.copy();
        this.programId = other.programId == null ? null : other.programId.copy();
    }

    @Override
    public FaProgramCriteria copy() {
        return new FaProgramCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public LongFilter getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(LongFilter semesterId) {
        this.semesterId = semesterId;
    }

    public LongFilter getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(LongFilter facultyId) {
        this.facultyId = facultyId;
    }

    public LongFilter getProgramId() {
        return programId;
    }

    public void setProgramId(LongFilter programId) {
        this.programId = programId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FaProgramCriteria that = (FaProgramCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedOn, that.modifiedOn) &&
            Objects.equals(modifiedBy, that.modifiedBy) &&
            Objects.equals(semesterId, that.semesterId) &&
            Objects.equals(facultyId, that.facultyId) &&
            Objects.equals(programId, that.programId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdOn,
        modifiedOn,
        modifiedBy,
        semesterId,
        facultyId,
        programId
        );
    }

    @Override
    public String toString() {
        return "FaProgramCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
                (semesterId != null ? "semesterId=" + semesterId + ", " : "") +
                (facultyId != null ? "facultyId=" + facultyId + ", " : "") +
                (programId != null ? "programId=" + programId + ", " : "") +
            "}";
    }

}
