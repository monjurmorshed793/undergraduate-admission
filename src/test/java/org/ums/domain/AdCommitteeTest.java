package org.ums.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class AdCommitteeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdCommittee.class);
        AdCommittee adCommittee1 = new AdCommittee();
        adCommittee1.setId(1L);
        AdCommittee adCommittee2 = new AdCommittee();
        adCommittee2.setId(adCommittee1.getId());
        assertThat(adCommittee1).isEqualTo(adCommittee2);
        adCommittee2.setId(2L);
        assertThat(adCommittee1).isNotEqualTo(adCommittee2);
        adCommittee1.setId(null);
        assertThat(adCommittee1).isNotEqualTo(adCommittee2);
    }
}
