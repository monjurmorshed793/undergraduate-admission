package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.AdCommitteeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdCommittee} and its DTO {@link AdCommitteeDTO}.
 */
@Mapper(componentModel = "spring", uses = {SemesterMapper.class, FacultyMapper.class, AdmissionDesignationMapper.class, UserMapper.class})
public interface AdCommitteeMapper extends EntityMapper<AdCommitteeDTO, AdCommittee> {

    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(source = "semester.name", target = "semesterName")
    @Mapping(source = "faculty.id", target = "facultyId")
    @Mapping(source = "faculty.name", target = "facultyName")
    @Mapping(source = "designation.id", target = "designationId")
    @Mapping(source = "designation.name", target = "designationName")
    @Mapping(source = "user.id", target = "userId")
    AdCommitteeDTO toDto(AdCommittee adCommittee);

    @Mapping(source = "semesterId", target = "semester")
    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(source = "designationId", target = "designation")
    @Mapping(source = "userId", target = "user")
    AdCommittee toEntity(AdCommitteeDTO adCommitteeDTO);

    default AdCommittee fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdCommittee adCommittee = new AdCommittee();
        adCommittee.setId(id);
        return adCommittee;
    }
}
