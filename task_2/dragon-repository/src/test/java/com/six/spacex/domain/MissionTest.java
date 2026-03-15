package com.six.spacex.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MissionTest {

    @Test
    void testMissionCreation_shouldHaveScheduledStatusInitially()
    {
        Mission mission = new Mission("Mars-1");

        assertThat(mission.getName()).isEqualTo("Mars-1");
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.SCHEDULED);
        assertThat(mission.getAssignedRockets()).isEmpty();
    }
}
