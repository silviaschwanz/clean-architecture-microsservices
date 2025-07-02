package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.OutputRide;
import com.curso.ride.application.ports.PositionRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Position;
import com.curso.ride.domain.entity.Ride;
import com.curso.ride.domain.service.RideFinishedCalculator;
import com.curso.ride.domain.vo.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRide {

    private final RideRepository rideRepository;

    private final PositionRepository positionRepository;

    public GetRide(RideRepository rideRepository, PositionRepository positionRepository) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
    }

    public OutputRide execute(String rideId) {
        Ride ride = rideRepository.getRideById(rideId);
        List<Position> positions = positionRepository.getPositionsByRideId(rideId);
        double distance;
        if(ride.getStatus().equals(Status.COMPLETED.toString())) {
            distance = ride.getDistance();
        } else {
            distance = RideFinishedCalculator.calculate(positions).distance();
        }
        return new OutputRide(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getToLongitude(),
                ride.getStatus(),
                positions,
                distance,
                ride.getFare()
        );
    }

}
