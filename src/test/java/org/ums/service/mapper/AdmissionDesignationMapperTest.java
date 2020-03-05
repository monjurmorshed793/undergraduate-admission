package org.ums.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AdmissionDesignationMapperTest {

    private AdmissionDesignationMapper admissionDesignationMapper;

    @BeforeEach
    public void setUp() {
        admissionDesignationMapper = new AdmissionDesignationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(admissionDesignationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(admissionDesignationMapper.fromId(null)).isNull();
    }
}
