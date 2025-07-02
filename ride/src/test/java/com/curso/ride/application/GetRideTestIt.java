package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.dto.OutputRide;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.GetRide;
import com.curso.ride.domain.entity.Ride;
import com.curso.ride.domain.vo.Status;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class GetRideTestIt {

    @Autowired
    private Flyway flyway;

    @Autowired
    private GetRide getRide;

    @Autowired
    private RideRepository rideRepository;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldRetrieveRideById() {
        UUID rideId = UUID.randomUUID();
        UUID passengerId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Ride ride = Ride.restore(
                rideId,
                passengerId,
                driverId,
                Status.REQUESTED.toString(),
                0.0,
                10.0, -10.0,
                20.0, -20.0,
                0.0,
                LocalDateTime.now()
        );
        rideRepository.saveRide(ride);
        OutputRide output = getRide.execute(rideId.toString());
        assertNotNull(output);
        assertEquals(rideId.toString(), output.rideId());
        assertEquals(passengerId.toString(), output.passengerId());
        assertEquals(Status.REQUESTED.toString(), output.status());
        assertEquals(10.0, output.fromLatitude());
        assertEquals(-10.0, output.fromLongitude());
        assertEquals(20.0, output.toLatitude());
        assertEquals(-20.0, output.toLongitude());
    }

}
