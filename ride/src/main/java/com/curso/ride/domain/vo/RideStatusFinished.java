package com.curso.ride.domain.vo;

public class RideStatusFinished implements RideStatus{

    private final String value;

    public RideStatusFinished() {
        this.value = Status.COMPLETED.toString();
    }

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
        throw new RuntimeException("Invalid status");
    }

    @Override
    public RideStatus finish() {
        throw new RuntimeException("Invalid status");
    }
}
