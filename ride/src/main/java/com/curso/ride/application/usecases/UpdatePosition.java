package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.InputUpdatePosition;
import com.curso.ride.application.ports.PositionRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Position;
import com.curso.ride.domain.entity.Ride;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class UpdatePosition {

    private final RideRepository rideRepository;
    private final PositionRepository positionRepository;
    private final Clock clock;

    public UpdatePosition(RideRepository rideRepository, PositionRepository positionRepository, Clock clock) {
        this.rideRepository = rideRepository;
        this.positionRepository = positionRepository;
        this.clock = clock;
    }

    public void execute(InputUpdatePosition inputUpdatePosition) {
        Ride ride = rideRepository.getRideById(inputUpdatePosition.rideId());
        ride.update();
        Position position = Position.create(
                ride.getRideId(),
                inputUpdatePosition.latitude(),
                inputUpdatePosition.longitude(),
                clock
        );
        positionRepository.savePosition(position);
    }

}
