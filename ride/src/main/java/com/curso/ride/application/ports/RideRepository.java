package com.curso.ride.application.ports;

import com.curso.ride.domain.entity.Ride;

import java.util.List;

public interface RideRepository {

    Ride getRideById(String rideId);

    List<Ride> getRidesByPassanger(String passengerId);

    Ride saveRide(Ride ride);

    void update(Ride ride);

}
