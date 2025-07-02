package com.curso.ride.application.dto;

public record InputUpdatePosition(
        String rideId,
        double latitude,
        double longitude
) {
}
