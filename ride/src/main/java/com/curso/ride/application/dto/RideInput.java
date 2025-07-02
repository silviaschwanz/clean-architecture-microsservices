package com.curso.ride.application.dto;

public record RideInput(
        String passengerId,
        Double fromLat,
        Double fromLongit,
        Double toLat,
        Double toLongit
) {

}
