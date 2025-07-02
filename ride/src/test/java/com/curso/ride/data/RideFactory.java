package com.curso.ride.data;

import com.curso.ride.domain.entity.Ride;
import com.curso.ride.domain.vo.Status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class RideFactory {

    public static final UUID RIDE_ID = UUID.randomUUID();

    public static Ride createRide(String accountPassanger, Clock fixedClock) {
        return Ride.create(
                accountPassanger,
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476,
                fixedClock
        );
    }

    public static Ride restoreRide(String accountPassanger, String driverID, LocalDateTime date) {
        return Ride.restore(
                RIDE_ID,
                UUID.fromString(accountPassanger),
                UUID.fromString(driverID),
                Status.IN_PROGRESS.toString(),
                0,
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476,
                0,
                date
        );
    }
}
