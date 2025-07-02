package com.curso.ride.application.dto;

import com.curso.ride.domain.entity.Position;

import java.util.List;

public record OutputRide(
        String rideId,
        String passengerId,
        String driverId,
        double fromLatitude,
        double fromLongitude,
        double toLatitude,
        double toLongitude,
        String status,
        List<Position> positions,
        double distance,
        double fare
) {
}
