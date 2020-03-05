package org.ums.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class AdCommitteeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdCommitteeDTO.class);
        AdCommitteeDTO adCommitteeDTO1 = new AdCommitteeDTO();
        adCommitteeDTO1.setId(1L);
        AdCommitteeDTO adCommitteeDTO2 = new AdCommitteeDTO();
        assertThat(adCommitteeDTO1).isNotEqualTo(adCommitteeDTO2);
        adCommitteeDTO2.setId(adCommitteeDTO1.getId());
        assertThat(adCommitteeDTO1).isEqualTo(adCommitteeDTO2);
        adCommitteeDTO2.setId(2L);
        assertThat(adCommitteeDTO1).isNotEqualTo(adCommitteeDTO2);
        adCommitteeDTO1.setId(null);
        assertThat(adCommitteeDTO1).isNotEqualTo(adCommitteeDTO2);
    }
}
