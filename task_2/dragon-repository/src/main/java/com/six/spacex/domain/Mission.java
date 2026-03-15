package com.six.spacex.domain;

import java.util.*;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status;
    private final List<Rocket> assignedRockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.assignedRockets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public List<Rocket> getAssignedRockets() {
        return Collections.unmodifiableList(assignedRockets);
    }

    public void assignRocket(Rocket rocket) {
        if (!this.assignedRockets.contains(rocket)) {
            this.assignedRockets.add(rocket);
        }
    }
}
