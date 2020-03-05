package org.ums.service.mapper;


import org.ums.domain.*;
import org.ums.service.dto.AdmissionDesignationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdmissionDesignation} and its DTO {@link AdmissionDesignationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AdmissionDesignationMapper extends EntityMapper<AdmissionDesignationDTO, AdmissionDesignation> {



    default AdmissionDesignation fromId(Long id) {
        if (id == null) {
            return null;
        }
        AdmissionDesignation admissionDesignation = new AdmissionDesignation();
        admissionDesignation.setId(id);
        return admissionDesignation;
    }
}
