package com.six.spacex.service;

import com.six.spacex.domain.Mission;
import com.six.spacex.domain.MissionStatus;
import com.six.spacex.domain.Rocket;
import com.six.spacex.domain.RocketStatus;
import com.six.spacex.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DragonServiceTest {

    private InMemoryRepository repository;
    private DragonService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
        service = new DragonService(repository);
    }

    @Test
    void testCreateMission_ShouldSaveAndReturnScheduledMission() {
        Mission m = service.createMission("Mars");
        assertThat(m.getName()).isEqualTo("Mars");
        assertThat(m.getStatus()).isEqualTo(MissionStatus.SCHEDULED);
        assertThat(repository.findMissionByName("Mars")).isPresent();
    }

    @Test
    void testCreateRocket_ShouldSaveAndReturnOnGroundRocket() {
        Rocket r = service.createRocket("F1");
        assertThat(r.getId()).isEqualTo("F1");
        assertThat(r.getStatus()).isEqualTo(RocketStatus.ON_GROUND);
        assertThat(repository.findRocketById("F1")).isPresent();
    }

    @Test
    void testAssignRocketToMission_ShouldAssignAndChangeMissionStatus() {
        Mission m = service.createMission("Mars");
        Rocket r = service.createRocket("F1");

        service.assignRocketToMission("F1", "Mars");

        assertThat(m.getAssignedRockets()).containsExactly(r);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);
    }

    @Test
    void testAssignRocket_ToEndedMission_ShouldThrowException() {
        Mission m = service.createMission("Mars");
        service.createRocket("F1");
        service.assignRocketToMission("F1", "Mars");
        service.unassignRocketToMission("F1", "Mars"); // Mission is now ENDED
        assertThat(m.getStatus()).isEqualTo(MissionStatus.ENDED);

        assertThatThrownBy(() -> service.assignRocketToMission("F1", "Mars"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot assign rocket to an ENDED mission");
    }

    @Test
    void testAssignRocket_AlreadyAssigned_ShouldThrowException() {
        service.createMission("Mars");
        service.createMission("Moon");
        service.createRocket("F1");

        service.assignRocketToMission("F1", "Mars");

        assertThatThrownBy(() -> service.assignRocketToMission("F1", "Moon"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Rocket is already assigned to an active mission");
    }

    @Test
    void testChangeRocketStatus_ToInRepair_ShouldChangeMissionStatusToPending() {
        Mission m = service.createMission("Mars");
        Rocket r = service.createRocket("F1");
        service.assignRocketToMission("F1", "Mars");

        assertThat(m.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);

        service.updateRocketStatus("F1", RocketStatus.IN_REPAIR);

        assertThat(r.getStatus()).isEqualTo(RocketStatus.IN_REPAIR);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.PENDING);
    }

    @Test
    void testChangeRocketStatus_FromInRepair_ShouldChangeMissionStatusToInProgress() {
        Mission m = service.createMission("Mars");
        Rocket r = service.createRocket("F1");
        service.assignRocketToMission("F1", "Mars");

        service.updateRocketStatus("F1", RocketStatus.IN_REPAIR);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.PENDING);

        service.updateRocketStatus("F1", RocketStatus.ON_GROUND);
        assertThat(r.getStatus()).isEqualTo(RocketStatus.ON_GROUND);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);
    }

    @Test
    void testAssignRocket_AfterMissionEnded_ShouldBePossibleForNewMission() {
        Mission mars = service.createMission("Mars");
        service.createMission("Moon");
        Rocket r = service.createRocket("F1");

        service.assignRocketToMission("F1", "Mars");
        service.unassignRocketToMission("F1", "Mars");
        assertThat(mars.getStatus()).isEqualTo(MissionStatus.ENDED);

        service.assignRocketToMission("F1", "Moon");

        Mission moon = repository.findMissionByName("Moon").get();
        assertThat(moon.getAssignedRockets()).containsExactly(r);
    }

    @Test
    void testGetSummaryOfMissions_ShouldOrderCorrectly() {
        service.createMission("Mars"); // 2 rockets
        service.createMission("Moon"); // 1 rocket
        service.createMission("Alpha"); // 1 rocket
        service.createMission("Zeta"); // 1 rocket
        service.createMission("Empty"); // 0 rockets

        service.createRocket("R1");
        service.createRocket("R2");
        service.createRocket("R3");
        service.createRocket("R4");
        service.createRocket("R5");

        service.assignRocketToMission("R1", "Mars");
        service.assignRocketToMission("R2", "Mars");

        service.assignRocketToMission("R3", "Moon");
        service.assignRocketToMission("R4", "Alpha");
        service.assignRocketToMission("R5", "Zeta");

        List<Mission> summary = service.getMissionSummary();

        // Expected
        // Mars (2 rockets)
        // Zeta (1 rocket) - descending alphabetical order for missions with 1 rocket
        // Moon (1 rocket)
        // Alpha (1 rocket)
        // Empty (0 rockets)
        assertThat(summary).extracting(Mission::getName)
                .containsExactly("Mars", "Zeta", "Moon", "Alpha", "Empty");
    }

    @Test
    void testMissionStatusWithMultipleRockets_ShouldBePendingIfAtLeastOneIsInRepair() {
        Mission m = service.createMission("Multi-Rocket Mission");
        service.createRocket("R1");
        service.createRocket("R2");

        service.assignRocketToMission("R1", "Multi-Rocket Mission");
        service.assignRocketToMission("R2", "Multi-Rocket Mission");
        assertThat(m.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);

        service.updateRocketStatus("R1", RocketStatus.IN_REPAIR);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.PENDING);

        service.updateRocketStatus("R2", RocketStatus.IN_REPAIR);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.PENDING);

        service.updateRocketStatus("R1", RocketStatus.ON_GROUND);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.PENDING);

        service.updateRocketStatus("R2", RocketStatus.ON_GROUND);
        assertThat(m.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);
    }

    @Test
    void testUnassignRocketToMission_ShouldEndMissionIfInProgressAndLastRocket() {
        // Given
        Mission mission = service.createMission("Apollo 13");
        Rocket rocket = service.createRocket("Odyssey");
        service.assignRocketToMission(rocket.getId(), mission.getName());
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.IN_PROGRESS);

        // When
        service.unassignRocketToMission(rocket.getId(), mission.getName());

        // Then
        Mission endedMission = repository.findMissionByName("Apollo 13").get();
        assertThat(endedMission.getStatus()).isEqualTo(MissionStatus.ENDED);
        assertThat(endedMission.getAssignedRockets()).isEmpty();
    }

    @Test
    void testUnassignRocketToMission_ShouldScheduleMissionIfPendingAndLastRocket() {
        // Given
        Mission mission = service.createMission("Apollo 14");
        Rocket rocket = service.createRocket("Kitty Hawk");
        service.assignRocketToMission(rocket.getId(), mission.getName());
        service.updateRocketStatus(rocket.getId(), RocketStatus.IN_REPAIR);
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.PENDING);

        // When
        service.unassignRocketToMission(rocket.getId(), mission.getName());

        // Then
        assertThat(mission.getStatus()).isEqualTo(MissionStatus.SCHEDULED);
        assertThat(mission.getAssignedRockets()).isEmpty();
    }
}
