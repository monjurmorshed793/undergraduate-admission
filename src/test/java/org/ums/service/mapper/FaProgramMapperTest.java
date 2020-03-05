package org.ums.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FaProgramMapperTest {

    private FaProgramMapper faProgramMapper;

    @BeforeEach
    public void setUp() {
        faProgramMapper = new FaProgramMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(faProgramMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(faProgramMapper.fromId(null)).isNull();
    }
}
