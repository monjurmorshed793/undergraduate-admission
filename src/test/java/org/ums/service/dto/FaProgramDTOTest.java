package org.ums.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class FaProgramDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaProgramDTO.class);
        FaProgramDTO faProgramDTO1 = new FaProgramDTO();
        faProgramDTO1.setId(1L);
        FaProgramDTO faProgramDTO2 = new FaProgramDTO();
        assertThat(faProgramDTO1).isNotEqualTo(faProgramDTO2);
        faProgramDTO2.setId(faProgramDTO1.getId());
        assertThat(faProgramDTO1).isEqualTo(faProgramDTO2);
        faProgramDTO2.setId(2L);
        assertThat(faProgramDTO1).isNotEqualTo(faProgramDTO2);
        faProgramDTO1.setId(null);
        assertThat(faProgramDTO1).isNotEqualTo(faProgramDTO2);
    }
}
