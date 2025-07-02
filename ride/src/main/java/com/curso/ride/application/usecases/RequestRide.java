package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.RequestRideOutput;
import com.curso.ride.application.dto.RideInput;
import com.curso.ride.application.ports.AccountRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.domain.entity.Account;
import com.curso.ride.domain.entity.Ride;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class RequestRide {

    private final AccountRepository accountRepository;

    private final RideRepository repository;

    private final Clock clock;

    public RequestRide(AccountRepository accountRepository, RideRepository rideRepository, Clock clock) {
        this.accountRepository = accountRepository;
        this.repository = rideRepository;
        this.clock = clock;
    }

    public RequestRideOutput execute(RideInput input) {
        Account account = accountRepository.getAccountById(input.passengerId());
        if(!account.isPassenger()) {
            throw new RuntimeException("Must be a passenger");
        }
        Ride ride = repository.saveRide(
                Ride.create(
                        account.getAccountId(),
                        input.fromLat(),
                        input.fromLongit(),
                        input.toLat(),
                        input.toLongit(),
                        clock
                )
        );
        return new RequestRideOutput(
                ride.getRideId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getFromLatitude(),
                ride.getFromLongitude(),
                ride.getToLatitude(),
                ride.getToLongitude(),
                ride.getStatus()
        );
    }

}
