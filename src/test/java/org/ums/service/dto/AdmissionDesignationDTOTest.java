package org.ums.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class AdmissionDesignationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdmissionDesignationDTO.class);
        AdmissionDesignationDTO admissionDesignationDTO1 = new AdmissionDesignationDTO();
        admissionDesignationDTO1.setId(1L);
        AdmissionDesignationDTO admissionDesignationDTO2 = new AdmissionDesignationDTO();
        assertThat(admissionDesignationDTO1).isNotEqualTo(admissionDesignationDTO2);
        admissionDesignationDTO2.setId(admissionDesignationDTO1.getId());
        assertThat(admissionDesignationDTO1).isEqualTo(admissionDesignationDTO2);
        admissionDesignationDTO2.setId(2L);
        assertThat(admissionDesignationDTO1).isNotEqualTo(admissionDesignationDTO2);
        admissionDesignationDTO1.setId(null);
        assertThat(admissionDesignationDTO1).isNotEqualTo(admissionDesignationDTO2);
    }
}
