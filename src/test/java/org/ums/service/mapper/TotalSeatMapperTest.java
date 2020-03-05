package org.ums.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TotalSeatMapperTest {

    private TotalSeatMapper totalSeatMapper;

    @BeforeEach
    public void setUp() {
        totalSeatMapper = new TotalSeatMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(totalSeatMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(totalSeatMapper.fromId(null)).isNull();
    }
}
