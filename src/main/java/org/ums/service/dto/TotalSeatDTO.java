package org.ums.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.ums.domain.TotalSeat} entity.
 */
public class TotalSeatDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer seat;

    private LocalDate createdOn;

    private LocalDate modifiedOn;

    private String modifiedBy;


    private Long facultyProgramId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getFacultyProgramId() {
        return facultyProgramId;
    }

    public void setFacultyProgramId(Long faProgramId) {
        this.facultyProgramId = faProgramId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TotalSeatDTO totalSeatDTO = (TotalSeatDTO) o;
        if (totalSeatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), totalSeatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TotalSeatDTO{" +
            "id=" + getId() +
            ", seat=" + getSeat() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", facultyProgramId=" + getFacultyProgramId() +
            "}";
    }
}
