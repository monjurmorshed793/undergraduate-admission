package org.ums.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class SemesterDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemesterDTO.class);
        SemesterDTO semesterDTO1 = new SemesterDTO();
        semesterDTO1.setId(1L);
        SemesterDTO semesterDTO2 = new SemesterDTO();
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
        semesterDTO2.setId(semesterDTO1.getId());
        assertThat(semesterDTO1).isEqualTo(semesterDTO2);
        semesterDTO2.setId(2L);
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
        semesterDTO1.setId(null);
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
    }
}
