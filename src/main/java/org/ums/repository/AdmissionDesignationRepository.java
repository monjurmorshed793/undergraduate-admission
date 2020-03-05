package org.ums.repository;

import org.ums.domain.AdmissionDesignation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AdmissionDesignation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdmissionDesignationRepository extends JpaRepository<AdmissionDesignation, Long>, JpaSpecificationExecutor<AdmissionDesignation> {

}
