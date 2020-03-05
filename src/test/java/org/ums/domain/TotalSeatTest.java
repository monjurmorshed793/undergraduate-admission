package org.ums.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class TotalSeatTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TotalSeat.class);
        TotalSeat totalSeat1 = new TotalSeat();
        totalSeat1.setId(1L);
        TotalSeat totalSeat2 = new TotalSeat();
        totalSeat2.setId(totalSeat1.getId());
        assertThat(totalSeat1).isEqualTo(totalSeat2);
        totalSeat2.setId(2L);
        assertThat(totalSeat1).isNotEqualTo(totalSeat2);
        totalSeat1.setId(null);
        assertThat(totalSeat1).isNotEqualTo(totalSeat2);
    }
}
