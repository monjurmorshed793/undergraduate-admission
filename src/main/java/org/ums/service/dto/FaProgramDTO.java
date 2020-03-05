package org.ums.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.ums.domain.FaProgram} entity.
 */
public class FaProgramDTO implements Serializable {

    private Long id;

    private LocalDate createdOn;

    private LocalDate modifiedOn;

    private String modifiedBy;


    private Long semesterId;

    private String semesterName;

    private Long facultyId;

    private String facultyName;

    private Long programId;

    private String programName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FaProgramDTO faProgramDTO = (FaProgramDTO) o;
        if (faProgramDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), faProgramDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FaProgramDTO{" +
            "id=" + getId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            ", semesterId=" + getSemesterId() +
            ", semesterName='" + getSemesterName() + "'" +
            ", facultyId=" + getFacultyId() +
            ", facultyName='" + getFacultyName() + "'" +
            ", programId=" + getProgramId() +
            ", programName='" + getProgramName() + "'" +
            "}";
    }
}
