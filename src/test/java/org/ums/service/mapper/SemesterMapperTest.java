package org.ums.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SemesterMapperTest {

    private SemesterMapper semesterMapper;

    @BeforeEach
    public void setUp() {
        semesterMapper = new SemesterMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(semesterMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(semesterMapper.fromId(null)).isNull();
    }
}
