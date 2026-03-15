package com.six.spacex.domain;

import java.util.*;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status;
    private final Set<Rocket> assignedRockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.assignedRockets = new HashSet<>();
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

    public Set<Rocket> getAssignedRockets() {
        return Collections.unmodifiableSet(assignedRockets);
    }

    public void assignRocket(Rocket rocket) {
        this.assignedRockets.add(rocket);
    }

    public void unassignRocket(Rocket rocket) {
        this.assignedRockets.remove(rocket);
    }
}
