package org.ums.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AdCommitteeMapperTest {

    private AdCommitteeMapper adCommitteeMapper;

    @BeforeEach
    public void setUp() {
        adCommitteeMapper = new AdCommitteeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(adCommitteeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(adCommitteeMapper.fromId(null)).isNull();
    }
}
