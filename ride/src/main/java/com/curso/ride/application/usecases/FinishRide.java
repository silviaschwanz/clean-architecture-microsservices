package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.InputFinishRide;
import com.curso.ride.application.dto.OutputRide;
import com.curso.ride.application.ports.PositionRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Position;
import com.curso.ride.domain.entity.Ride;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinishRide {

    private final RideRepository rideRepository;
    private final PositionRepository positionRepository;

    public FinishRide(RideRepository rideRepository, PositionRepository positionRepository) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
    }

    public OutputRide execute(InputFinishRide inputFinishRide) {
        Ride ride = rideRepository.getRideById(inputFinishRide.rideId());
        List<Position> positions = positionRepository.getPositionsByRideId(ride.getRideId());
        ride.finish(positions);
        rideRepository.update(ride);
        return new OutputRide(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getFromLongitude(),
                ride.getStatus(),
                positions,
                ride.getDistance(),
                ride.getFare()
        );
    }

}
