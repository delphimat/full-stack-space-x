package com.six.spacex.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RocketTest {
    @Test
    void testRocketCreation_shouldHaveOnGroundStatusInit()
    {
        Rocket rocket = new Rocket("Falcon-1");

        assertThat(rocket.getId()).isEqualTo("Falcon-1");
        assertThat(rocket.getStatus()).isEqualTo(RocketStatus.ON_GROUND);
    }

    @Test
    void testChangeStatus_shouldUpdateStatus() {
        Rocket rocket = new Rocket("Falcon-9");
        rocket.setStatus(RocketStatus.IN_SPACE);

        assertThat(rocket.getStatus()).isEqualTo(RocketStatus.IN_SPACE);
    }
}
