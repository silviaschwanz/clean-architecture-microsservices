package com.curso.ride.domain.vo;

public class RideStatusFactory {

    public static RideStatus create(String status) {
        if(status.equals(Status.REQUESTED.toString())) return new RideStatusRequested();
        if(status.equals(Status.ACCEPTED.toString())) return new RideStatusAccepted();
        if(status.equals(Status.IN_PROGRESS.toString())) return new RideStatusInProgress();
        if(status.equals(Status.COMPLETED.toString())) return new RideStatusFinished();
        throw new RuntimeException("Invalid Status");
    }
}
