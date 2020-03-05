package org.ums.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A TotalSeat.
 */
@Entity
@Table(name = "total_seat")
public class TotalSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "seat", nullable = false)
    private Integer seat;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "modified_on")
    private LocalDate modifiedOn;

    @Column(name = "modified_by")
    private String modifiedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("totalSeats")
    private FaProgram facultyProgram;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeat() {
        return seat;
    }

    public TotalSeat seat(Integer seat) {
        this.seat = seat;
        return this;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public TotalSeat createdOn(LocalDate createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDate getModifiedOn() {
        return modifiedOn;
    }

    public TotalSeat modifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
        return this;
    }

    public void setModifiedOn(LocalDate modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public TotalSeat modifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public FaProgram getFacultyProgram() {
        return facultyProgram;
    }

    public TotalSeat facultyProgram(FaProgram faProgram) {
        this.facultyProgram = faProgram;
        return this;
    }

    public void setFacultyProgram(FaProgram faProgram) {
        this.facultyProgram = faProgram;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TotalSeat)) {
            return false;
        }
        return id != null && id.equals(((TotalSeat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TotalSeat{" +
            "id=" + getId() +
            ", seat=" + getSeat() +
            ", createdOn='" + getCreatedOn() + "'" +
            ", modifiedOn='" + getModifiedOn() + "'" +
            ", modifiedBy='" + getModifiedBy() + "'" +
            "}";
    }
}
