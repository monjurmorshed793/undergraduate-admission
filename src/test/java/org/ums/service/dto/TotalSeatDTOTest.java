package org.ums.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class TotalSeatDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TotalSeatDTO.class);
        TotalSeatDTO totalSeatDTO1 = new TotalSeatDTO();
        totalSeatDTO1.setId(1L);
        TotalSeatDTO totalSeatDTO2 = new TotalSeatDTO();
        assertThat(totalSeatDTO1).isNotEqualTo(totalSeatDTO2);
        totalSeatDTO2.setId(totalSeatDTO1.getId());
        assertThat(totalSeatDTO1).isEqualTo(totalSeatDTO2);
        totalSeatDTO2.setId(2L);
        assertThat(totalSeatDTO1).isNotEqualTo(totalSeatDTO2);
        totalSeatDTO1.setId(null);
        assertThat(totalSeatDTO1).isNotEqualTo(totalSeatDTO2);
    }
}
