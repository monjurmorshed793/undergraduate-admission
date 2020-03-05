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
 * Criteria class for the {@link org.ums.domain.Program} entity. This class is used
 * in {@link org.ums.web.rest.ProgramResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /programs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProgramCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter programId;

    private StringFilter name;

    private LocalDateFilter createdOn;

    private LocalDateFilter modifiedOn;

    private StringFilter modifiedBy;

    public ProgramCriteria() {
    }

    public ProgramCriteria(ProgramCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.programId = other.programId == null ? null : other.programId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
    }

    @Override
    public ProgramCriteria copy() {
        return new ProgramCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getProgramId() {
        return programId;
    }

    public void setProgramId(IntegerFilter programId) {
        this.programId = programId;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProgramCriteria that = (ProgramCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(programId, that.programId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedOn, that.modifiedOn) &&
            Objects.equals(modifiedBy, that.modifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        programId,
        name,
        createdOn,
        modifiedOn,
        modifiedBy
        );
    }

    @Override
    public String toString() {
        return "ProgramCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (programId != null ? "programId=" + programId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
            "}";
    }

}
