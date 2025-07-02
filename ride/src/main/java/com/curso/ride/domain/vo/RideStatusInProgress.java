package com.curso.ride.domain.vo;

public class RideStatusInProgress implements RideStatus{

    private String value;

    public RideStatusInProgress() {
        this.value = Status.IN_PROGRESS.toString();
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
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus update() {
        return this;
    }

    @Override
    public RideStatus finish() {
        return new RideStatusFinished();
    }

}
