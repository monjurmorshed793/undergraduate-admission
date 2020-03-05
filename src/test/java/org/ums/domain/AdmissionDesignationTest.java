package org.ums.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class AdmissionDesignationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdmissionDesignation.class);
        AdmissionDesignation admissionDesignation1 = new AdmissionDesignation();
        admissionDesignation1.setId(1L);
        AdmissionDesignation admissionDesignation2 = new AdmissionDesignation();
        admissionDesignation2.setId(admissionDesignation1.getId());
        assertThat(admissionDesignation1).isEqualTo(admissionDesignation2);
        admissionDesignation2.setId(2L);
        assertThat(admissionDesignation1).isNotEqualTo(admissionDesignation2);
        admissionDesignation1.setId(null);
        assertThat(admissionDesignation1).isNotEqualTo(admissionDesignation2);
    }
}
