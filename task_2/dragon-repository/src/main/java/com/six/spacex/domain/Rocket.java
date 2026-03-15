package com.six.spacex.domain;

public class Rocket {
    private final String id;
    private RocketStatus status;

    public Rocket(String id) {
        this.id = id;
        this.status = RocketStatus.ON_GROUND;
    }

    public String getId() {
        return id;
    }

    public RocketStatus getStatus() {
        return status;
    }

    public void setStatus(RocketStatus status) {
        this.status = status;
    }
}
