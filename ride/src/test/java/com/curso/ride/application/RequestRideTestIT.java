package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.dto.RideInput;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.RequestRide;
import com.curso.ride.domain.entity.Account;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class RequestRideTestIT {

    @Autowired
    AccountRepositoryPostgres accountRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    private Flyway flyway;

    @Autowired
    RequestRide requestRide;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldRequestRide(){
        var account = accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        false,
                        "12345678"
                )
        );
        var rideInput = new RideInput(
                account.getAccountId(),
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476
        );
        var rideOutput = requestRide.execute(rideInput);
        assertEquals(account.getAccountId(), rideOutput.passengerId());
        Ride ride = rideRepository.getRideById(rideOutput.rideId());
        assertEquals(ride.getRideId(), rideOutput.rideId());
        assertEquals(ride.getFromLatitude(), rideOutput.fromLatitude());
        assertEquals(ride.getFromLongitude(), rideOutput.fromLongitude());
        assertEquals(ride.getToLatitude(), rideOutput.toLatitude());
        assertEquals(ride.getToLongitude(), rideOutput.toLongitude());
        assertEquals(Status.REQUESTED.toString(), rideOutput.status());
    }

    @Test
    void shouldThrowExceptionWhenRequestRideIsNotPassenger(){
        var account = accountRepository.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        true,
                        "12345678"
                )
        );
        var rideInput = new RideInput(
                account.getAccountId(),
                -27.584905257808835,
                -48.545022195325124,
                -27.496887588317275,
                -48.522234807851476
        );
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requestRide.execute(rideInput);
        });
        assertEquals("Must be a passenger", exception.getMessage());
    }

}