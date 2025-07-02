package com.curso.ride.application.ports;

import com.curso.ride.domain.entity.Position;

import java.util.List;

public interface PositionRepository {

    Position savePosition(Position position);
    List<Position> getPositionsByRideId(String rideId);

}
