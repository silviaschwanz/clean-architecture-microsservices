package com.curso.ride.domain.service;

import com.curso.ride.domain.entity.Position;
import com.curso.ride.domain.vo.Coordinates;
import com.curso.ride.domain.vo.RideFinished;

import java.util.List;

public class RideFinishedCalculator {

    public static RideFinished calculate(List<Position> positions) {
        double totalDistance = 0.0;
        double totalFare = 0.0;
        FareCalculator fareCalculator;

        for (int i = 0; i < positions.size() - 1; i++) {
            Coordinates from = new Coordinates(positions.get(i).getLatitude(), positions.get(i).getLongitude());
            Coordinates to = new Coordinates(positions.get(i + 1).getLatitude(), positions.get(i + 1).getLongitude());

            double distance = calculateCordinates(from, to);
            totalDistance += distance;

            fareCalculator = FareCalculatorFactory.create(positions.get(i+1).getDate());
            double fare = fareCalculator.calculate(distance);
            totalFare += fare;
        }

        return new RideFinished(totalDistance, totalFare);
    }

    public static int calculateCordinates(Coordinates from, Coordinates to) {
        final double earthRadius = 6371.0;
        final double degreesToRadians = Math.PI / 180;
        double deltaLat = (to.latitude() - from.latitude()) * degreesToRadians;
        double deltaLon = (to.longitude() - from.longitude()) * degreesToRadians;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(from.latitude() * degreesToRadians)
                * Math.cos(to.latitude() * degreesToRadians)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return (int) Math.round(distance);
    }

}
