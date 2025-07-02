package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.InputStartRide;
import com.curso.ride.application.dto.OutputStartRide;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Ride;
import org.springframework.stereotype.Service;

@Service
public class StartRide {

    private final RideRepository rideRepository;

    public StartRide(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public OutputStartRide execute(InputStartRide inputStartRide) {
        Ride ride = rideRepository.getRideById(inputStartRide.rideId());
        ride.start();
        rideRepository.update(ride);
        return new OutputStartRide(ride.getRideId());
    }

}
