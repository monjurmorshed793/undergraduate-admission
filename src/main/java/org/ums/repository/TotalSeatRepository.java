package org.ums.repository;

import org.ums.domain.TotalSeat;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TotalSeat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TotalSeatRepository extends JpaRepository<TotalSeat, Long>, JpaSpecificationExecutor<TotalSeat> {

}
