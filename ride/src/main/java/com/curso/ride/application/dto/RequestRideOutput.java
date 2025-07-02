package com.curso.ride.application.dto;

public record RequestRideOutput(
        String rideId,
        String passengerId,
        String driverId,
        double fromLatitude,
        double fromLongitude,
        double toLatitude,
        double toLongitude,
        String status
) {
}
