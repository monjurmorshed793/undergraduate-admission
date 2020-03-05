package org.ums.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import org.ums.domain.enumeration.SemesterStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link org.ums.domain.Semester} entity. This class is used
 * in {@link org.ums.web.rest.SemesterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /semesters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SemesterCriteria implements Serializable, Criteria {
    /**
     * Class for filtering SemesterStatus
     */
    public static class SemesterStatusFilter extends Filter<SemesterStatus> {

        public SemesterStatusFilter() {
        }

        public SemesterStatusFilter(SemesterStatusFilter filter) {
            super(filter);
        }

        @Override
        public SemesterStatusFilter copy() {
            return new SemesterStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter semesterId;

    private StringFilter name;

    private StringFilter shortName;

    private SemesterStatusFilter status;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LocalDateFilter createdOn;

    private LocalDateFilter modifiedOn;

    private StringFilter modifiedBy;

    public SemesterCriteria() {
    }

    public SemesterCriteria(SemesterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.semesterId = other.semesterId == null ? null : other.semesterId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.shortName = other.shortName == null ? null : other.shortName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.createdOn = other.createdOn == null ? null : other.createdOn.copy();
        this.modifiedOn = other.modifiedOn == null ? null : other.modifiedOn.copy();
        this.modifiedBy = other.modifiedBy == null ? null : other.modifiedBy.copy();
    }

    @Override
    public SemesterCriteria copy() {
        return new SemesterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(IntegerFilter semesterId) {
        this.semesterId = semesterId;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getShortName() {
        return shortName;
    }

    public void setShortName(StringFilter shortName) {
        this.shortName = shortName;
    }

    public SemesterStatusFilter getStatus() {
        return status;
    }

    public void setStatus(SemesterStatusFilter status) {
        this.status = status;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
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
        final SemesterCriteria that = (SemesterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(semesterId, that.semesterId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(shortName, that.shortName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(modifiedOn, that.modifiedOn) &&
            Objects.equals(modifiedBy, that.modifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        semesterId,
        name,
        shortName,
        status,
        startDate,
        endDate,
        createdOn,
        modifiedOn,
        modifiedBy
        );
    }

    @Override
    public String toString() {
        return "SemesterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (semesterId != null ? "semesterId=" + semesterId + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (shortName != null ? "shortName=" + shortName + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (modifiedOn != null ? "modifiedOn=" + modifiedOn + ", " : "") +
                (modifiedBy != null ? "modifiedBy=" + modifiedBy + ", " : "") +
            "}";
    }

}
