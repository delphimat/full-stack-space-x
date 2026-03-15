package com.six.spacex.repository;
import com.six.spacex.domain.Mission;
import com.six.spacex.domain.Rocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository {
    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    public void saveRocket(Rocket rocket) {
        if (rocket == null) {
            throw new IllegalArgumentException("Rocket cannot be null");
        }

        rockets.put(rocket.getId(), rocket);
    }

    public Optional<Rocket> findRocketById(String id) {
        return Optional.ofNullable(rockets.get(id));
    }

    public void saveMission(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Mission cannot be null");
        }
        missions.put(mission.getName(), mission);
    }

    public Optional<Mission> findMissionByName(String name) {
        return Optional.ofNullable(missions.get(name));
    }

    public List<Mission> findAllMissions() {
        return new ArrayList<>(missions.values());
    }
}
