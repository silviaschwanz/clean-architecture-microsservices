package com.curso.ride.domain.vo;

public class RideStatusAccepted implements RideStatus{

    private String value;

    public RideStatusAccepted() {
        this.value = Status.ACCEPTED.toString();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public RideStatus accept() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus start() {
        return new RideStatusInProgress();
    }

    @Override
    public RideStatus update() {
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus finish() {
        throw new RuntimeException("Invalid status");
    }
}
