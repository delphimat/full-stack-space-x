package com.six.spacex.repository;

import com.six.spacex.domain.Mission;
import com.six.spacex.domain.Rocket;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryRepositoryTest {
    private InMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
    }

    @Test
    void testSaveAndFindRocket()
    {
        Rocket rocket = new Rocket("F9-01");
        repository.saveRocket(rocket);

        Optional<Rocket> found = repository.findRocketById("F9-01");
        assertThat(found).isPresent().contains(rocket);
    }

    @Test
    void testSaveAndFindMission()
    {
        Mission mission = new Mission("Mars-1");
        repository.saveMission(mission);

        Optional<Mission> found = repository.findMissionByName("Mars-1");
        assertThat(found).isPresent().contains(mission);
    }

    @Test
    void testFIndAllMissions()
    {
        Mission m1 = new Mission("M1");
        Mission m2 = new Mission("M2");
        repository.saveMission(m1);
        repository.saveMission(m2);

        Assertions.assertThat(repository.findAllMissions()).containsExactlyInAnyOrder(m1, m2);
    }

}
