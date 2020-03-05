package org.ums.repository;

import org.ums.domain.AdCommittee;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the AdCommittee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdCommitteeRepository extends JpaRepository<AdCommittee, Long>, JpaSpecificationExecutor<AdCommittee> {

    @Query("select adCommittee from AdCommittee adCommittee where adCommittee.user.login = ?#{principal.username}")
    List<AdCommittee> findByUserIsCurrentUser();

}
