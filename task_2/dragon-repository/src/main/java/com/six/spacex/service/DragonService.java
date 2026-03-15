package com.six.spacex.service;

import com.six.spacex.domain.Mission;
import com.six.spacex.domain.MissionStatus;
import com.six.spacex.domain.Rocket;
import com.six.spacex.domain.RocketStatus;
import com.six.spacex.repository.InMemoryRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DragonService {

    private final InMemoryRepository repository;

    public DragonService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public Mission createMission(String name) {
        Mission mission = new Mission(name);
        repository.saveMission(mission);
        return mission;
    }

    public Rocket createRocket(String id) {
        Rocket rocket = new Rocket(id);
        repository.saveRocket(rocket);
        return rocket;
    }

    public void assignRocketToMission(String rocketId, String missionName) {
        Rocket rocket = repository.findRocketById(rocketId)
                .orElseThrow(() -> new IllegalArgumentException("Rocket not found"));
        Mission mission = repository.findMissionByName(missionName)
                .orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        if (mission.getStatus() == MissionStatus.ENDED) {
            throw new IllegalStateException("Cannot assign rocket to an ENDED mission");
        }

        // Check if rocket is assigned to any active mission
        for (Mission m : repository.findAllMissions()) {
            if (m.getStatus() != MissionStatus.ENDED && m.getAssignedRockets().contains(rocket)) {
                if (m.getName().equals(mission.getName())) {
                    throw new IllegalStateException("Rocket is already assigned to this mission");
                }
                throw new IllegalStateException("Rocket is already assigned to an active mission");
            }
        }

        mission.assignRocket(rocket);
        evaluateMissionStatus(mission);
    }

    public void updateRocketStatus(String rocketId, RocketStatus newStatus) {
        Rocket rocket = repository.findRocketById(rocketId).orElseThrow(() -> new IllegalArgumentException("Rocket not found"));

        rocket.setStatus(newStatus);

        for (Mission mission : repository.findAllMissions()) {
            if (mission.getStatus() != MissionStatus.ENDED && mission.getAssignedRockets().contains(rocket)) {
                evaluateMissionStatus(mission);
            }
        }
    }

    public void updateMissionStatus(String missionName, MissionStatus newStatus) {
        Mission mission = repository.findMissionByName(missionName).orElseThrow(() -> new IllegalArgumentException("Mission not found"));

        if (mission.getStatus() == MissionStatus.ENDED) {
            throw new IllegalStateException("Cannot change the status of an ENDED mission");
        }

        mission.setStatus(newStatus);

        if (newStatus != MissionStatus.ENDED) {
            evaluateMissionStatus(mission);
        }
    }

    private void evaluateMissionStatus(Mission mission) {
        if (mission.getStatus() == MissionStatus.ENDED) {
            return;
        }

        if (mission.getAssignedRockets().isEmpty()) {
            mission.setStatus(MissionStatus.SCHEDULED);
            return;
        }

        boolean hasRocketInRepair = mission
                .getAssignedRockets()
                .stream()
                .anyMatch(r -> r.getStatus() == RocketStatus.IN_REPAIR);

        if (hasRocketInRepair) {
            mission.setStatus(MissionStatus.PENDING);
        } else {
            mission.setStatus(MissionStatus.IN_PROGRESS);
        }
    }

    public List<Mission> getMissionSummary() {
        return repository.findAllMissions().stream()
                .sorted(
                        Comparator.comparingInt((Mission m) -> m.getAssignedRockets().size()).reversed()
                                .thenComparing(Mission::getName, Comparator.reverseOrder())
                )
                .collect(Collectors.toList());
    }
}