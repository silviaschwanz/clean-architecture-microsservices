package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.dto.AcceptRideInput;
import com.curso.ride.application.dto.AcceptRideOutput;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.AcceptRide;
import com.curso.ride.data.AccountFactory;
import com.curso.ride.data.RideFactory;
import com.curso.ride.domain.entity.Ride;
import com.curso.ride.domain.vo.Status;
import com.curso.ride.infra.repository.AccountRepositoryPostgres;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class AcceptRideTestIT {

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    AcceptRide acceptRide;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T12:00:00Z");
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shouldAcceptRide(){
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        AcceptRideOutput acceptRideOutput = acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        Ride rideAccepted = rideRepository.getRideById(acceptRideOutput.rideId());
        assertEquals(Status.ACCEPTED.toString(), rideAccepted.getStatus());
        assertEquals(newRide.getRideId(), rideAccepted.getRideId());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotRequested(){
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        AcceptRideInput acceptRideInput = new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId());
        acceptRide.execute(acceptRideInput);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            acceptRide.execute(acceptRideInput);
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }

}