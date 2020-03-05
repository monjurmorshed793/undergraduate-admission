package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.FaProgramDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FaProgram} and its DTO {@link FaProgramDTO}.
 */
@Mapper(componentModel = "spring", uses = {SemesterMapper.class, FacultyMapper.class, ProgramMapper.class})
public interface FaProgramMapper extends EntityMapper<FaProgramDTO, FaProgram> {

    @Mapping(source = "semester.id", target = "semesterId")
    @Mapping(source = "semester.name", target = "semesterName")
    @Mapping(source = "faculty.id", target = "facultyId")
    @Mapping(source = "faculty.name", target = "facultyName")
    @Mapping(source = "program.id", target = "programId")
    @Mapping(source = "program.name", target = "programName")
    FaProgramDTO toDto(FaProgram faProgram);

    @Mapping(source = "semesterId", target = "semester")
    @Mapping(source = "facultyId", target = "faculty")
    @Mapping(source = "programId", target = "program")
    FaProgram toEntity(FaProgramDTO faProgramDTO);

    default FaProgram fromId(Long id) {
        if (id == null) {
            return null;
        }
        FaProgram faProgram = new FaProgram();
        faProgram.setId(id);
        return faProgram;
    }
}
