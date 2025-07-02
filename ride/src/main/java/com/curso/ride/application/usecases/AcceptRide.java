package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.AcceptRideInput;
import com.curso.ride.application.dto.AcceptRideOutput;
import com.curso.ride.application.ports.AccountRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Account;
import com.curso.ride.domain.entity.Ride;
import org.springframework.stereotype.Service;

@Service
public class AcceptRide {

    private final AccountRepository accountRepository;

    private final RideRepository rideRepository;

    public AcceptRide(AccountRepository accountRepository, RideRepository rideRepository) {
        this.accountRepository = accountRepository;
        this.rideRepository = rideRepository;
    }

    public AcceptRideOutput execute(AcceptRideInput acceptRideInput) {
        Account account = accountRepository.getAccountById(acceptRideInput.driverId());
        if(!account.isDriver()) throw new RuntimeException("Must be a driver");
        Ride ride = rideRepository.getRideById(acceptRideInput.rideId());
        ride.accept(account.getAccountId());
        rideRepository.update(ride);
        return new AcceptRideOutput(ride.getRideId());
    }

}
