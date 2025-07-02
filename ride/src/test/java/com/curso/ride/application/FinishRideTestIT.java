package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.dto.*;
import com.curso.ride.application.ports.PositionRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.AcceptRide;
import com.curso.ride.application.usecases.FinishRide;
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
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class FinishRideTestIT {

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    AcceptRide acceptRide;

    @Autowired
    StartRide startRide;

    @Autowired
    FinishRide finishRide;

    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private static final Instant FIXED_NIGHT_INSTANT =
            LocalDateTime.of(2025, 3, 2, 23, 30).atZone(ZONE_ID).toInstant();

    private static final Instant FIXED_NORMAL_INSTANT =
            LocalDateTime.of(2025, 3, 2, 13, 30).atZone(ZONE_ID).toInstant();

    private static final Instant FIXED_ESPECIAL_DAY =
            LocalDateTime.of(2025, 3, 1, 23, 30).atZone(ZONE_ID).toInstant();

    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldFinishRideWithNormalFare() {
        fixedClock = Clock.fixed(FIXED_NORMAL_INSTANT, ZONE_ID);
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        startRide.execute(new InputStartRide(newRide.getRideId()));

        UpdatePosition updatePosition = new UpdatePosition(rideRepository, positionRepository, fixedClock);
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));

        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        OutputRide outputRide = finishRide.execute(inputFinishRide);
        assertEquals(30, outputRide.distance());
        assertEquals(Status.COMPLETED.toString(), outputRide.status());
        assertEquals(63, outputRide.fare());
    }

    @Test
    void shouldFinishRideWithOvernightFare() {
        fixedClock = Clock.fixed(FIXED_NIGHT_INSTANT, ZONE_ID);
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        startRide.execute(new InputStartRide(newRide.getRideId()));

        UpdatePosition updatePosition = new UpdatePosition(rideRepository, positionRepository, fixedClock);
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));

        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        OutputRide outputRide = finishRide.execute(inputFinishRide);
        assertEquals(30, outputRide.distance());
        assertEquals(Status.COMPLETED.toString(), outputRide.status());
        assertEquals(117, outputRide.fare());
    }

    @Test
    void shouldFinishRideWithEspecialDayFare() {
        fixedClock = Clock.fixed(FIXED_ESPECIAL_DAY, ZONE_ID);
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        startRide.execute(new InputStartRide(newRide.getRideId()));

        UpdatePosition updatePosition = new UpdatePosition(rideRepository, positionRepository, fixedClock);
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.584905257808835,-48.545022195325124));
        updatePosition.execute(new InputUpdatePosition(newRide.getRideId(),-27.496887588317275,-48.522234807851476));

        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        OutputRide outputRide = finishRide.execute(inputFinishRide);
        assertEquals(30, outputRide.distance());
        assertEquals(Status.COMPLETED.toString(), outputRide.status());
        assertEquals(30, outputRide.fare());
    }

    @Test
    void shouldThrowExceptionWhenStatusRideIsNotInProgress(){
        fixedClock = Clock.fixed(FIXED_NORMAL_INSTANT, ZoneId.systemDefault());
        var accountPassanger = accountRepository.saveAccount(AccountFactory.createAccountPassanger());
        var accountDriver = accountRepository.saveAccount(AccountFactory.createAccountDriver());
        Ride newRide = RideFactory.createRide(accountPassanger.getAccountId(), fixedClock);
        rideRepository.saveRide(newRide);
        acceptRide.execute(new AcceptRideInput(newRide.getRideId(), accountDriver.getAccountId()));
        InputFinishRide inputFinishRide = new InputFinishRide(newRide.getRideId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            finishRide.execute(inputFinishRide);
        });
        assertNotNull(exception);
        assertEquals("Invalid status", exception.getMessage());
    }

}
