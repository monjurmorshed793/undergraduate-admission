package org.ums.repository;

import org.ums.domain.FaProgram;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FaProgram entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FaProgramRepository extends JpaRepository<FaProgram, Long>, JpaSpecificationExecutor<FaProgram> {

}
