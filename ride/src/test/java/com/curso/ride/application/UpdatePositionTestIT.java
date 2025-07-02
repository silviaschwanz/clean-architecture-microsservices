package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.dto.AcceptRideInput;
import com.curso.ride.application.dto.InputStartRide;
import com.curso.ride.application.dto.InputUpdatePosition;
import com.curso.ride.application.dto.OutputRide;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.AcceptRide;
import com.curso.ride.application.usecases.GetRide;
import com.curso.ride.application.usecases.StartRide;
import com.curso.ride.application.usecases.UpdatePosition;
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
public class UpdatePositionTestIT {

    @Autowired
    UpdatePosition updatePosition;

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    AcceptRide acceptRide;

    @Autowired
    StartRide startRide;

    @Autowired
    GetRide getRide;

    private static final Instant FIXED_NORMAL_INSTANT   = Instant.parse("2025-01-10T13:00:00Z"); // 10/01/2025 10:00 BRT
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
        fixedClock = Clock.fixed(FIXED_NORMAL_INSTANT, ZoneId.systemDefault());
    }

    @Test
    void shoudUpdatePosition() {
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        startRide.execute(new InputStartRide(newRide.getRideId()));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));

        OutputRide rideOutput = getRide.execute(newRide.getRideId());
        assertEquals(30, rideOutput.distance());
        assertEquals(Status.IN_PROGRESS.toString(), rideOutput.status());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotAccepted(){
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }

}
