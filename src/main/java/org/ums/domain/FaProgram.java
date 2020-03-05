package org.ums.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A FaProgram.
 */
@Entity
@Table(name = "fa_program")
public class FaProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("faPrograms")
    private Semester semester;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("faPrograms")
    private Faculty faculty;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("faPrograms")
    private Program program;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public FaProgram createdOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getModifiedOn() {
        return modifiedOn;
    }

    public FaProgram modifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public FaProgram modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Semester getSemester() {
        return semester;
    }

    public FaProgram semester(Semester semester) {
        this.semester = semester;
        return this;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public FaProgram faculty(Faculty faculty) {
        this.faculty = faculty;
        return this;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Program getProgram() {
        return program;
    }

    public FaProgram program(Program program) {
        this.program = program;
        return this;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FaProgram)) {
            return false;
        }
        return id != null && id.equals(((FaProgram) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FaProgram{" +
            "id=" + getId() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            "}";
    }
}
