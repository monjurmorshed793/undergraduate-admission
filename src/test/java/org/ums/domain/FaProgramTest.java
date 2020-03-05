package org.ums.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.ums.web.rest.TestUtil;

public class FaProgramTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FaProgram.class);
        FaProgram faProgram1 = new FaProgram();
        faProgram1.setId(1L);
        FaProgram faProgram2 = new FaProgram();
        faProgram2.setId(faProgram1.getId());
        assertThat(faProgram1).isEqualTo(faProgram2);
        faProgram2.setId(2L);
        assertThat(faProgram1).isNotEqualTo(faProgram2);
        faProgram1.setId(null);
        assertThat(faProgram1).isNotEqualTo(faProgram2);
    }
}
