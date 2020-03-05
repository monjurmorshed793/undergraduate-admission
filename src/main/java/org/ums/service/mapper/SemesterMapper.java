package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.SemesterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Semester} and its DTO {@link SemesterDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SemesterMapper extends EntityMapper<SemesterDTO, Semester> {



    default Semester fromId(Long id) {
        if (id == null) {
            return null;
        }
        Semester semester = new Semester();
        semester.setId(id);
        return semester;
    }
}
