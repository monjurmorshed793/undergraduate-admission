package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.TotalSeatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TotalSeat} and its DTO {@link TotalSeatDTO}.
 */
@Mapper(componentModel = "spring", uses = {FaProgramMapper.class})
public interface TotalSeatMapper extends EntityMapper<TotalSeatDTO, TotalSeat> {

    @Mapping(source = "facultyProgram.id", target = "facultyProgramId")
    TotalSeatDTO toDto(TotalSeat totalSeat);

    @Mapping(source = "facultyProgramId", target = "facultyProgram")
    TotalSeat toEntity(TotalSeatDTO totalSeatDTO);

    default TotalSeat fromId(Long id) {
        if (id == null) {
            return null;
        }
        TotalSeat totalSeat = new TotalSeat();
        totalSeat.setId(id);
        return totalSeat;
    }
}
