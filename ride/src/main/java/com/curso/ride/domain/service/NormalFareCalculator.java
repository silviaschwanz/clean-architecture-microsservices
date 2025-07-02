package com.curso.ride.domain.service;

public class NormalFareCalculator implements FareCalculator {

    @Override
    public double calculate(double distance) {
        return distance * 2.1;
    }

}
