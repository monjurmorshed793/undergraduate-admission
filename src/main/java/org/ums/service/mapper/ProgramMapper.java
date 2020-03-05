package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.ProgramDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Program} and its DTO {@link ProgramDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {



    default Program fromId(Long id) {
        if (id == null) {
            return null;
        }
        Program program = new Program();
        program.setId(id);
        return program;
    }
}
