package com.curso.ride.application;

import com.curso.ride.application.dto.OutputRide;
import com.curso.ride.application.ports.PositionRepository;
import com.curso.ride.application.ports.RideRepository;
import com.curso.ride.application.usecases.GetRide;
import com.curso.ride.domain.entity.Ride;
import com.curso.ride.domain.vo.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRideTest {

    private RideRepository rideRepository;
    private PositionRepository positionRepository;
    private GetRide getRide;

    @BeforeEach
    void setUp() {
        rideRepository = mock(RideRepository.class);
        positionRepository = mock(PositionRepository.class);
        getRide = new GetRide(rideRepository, positionRepository);
    }

    @Test
    void shouldReturnRideOutputWhenRideExists() {
        UUID rideId = UUID.randomUUID();
        UUID passengerId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Ride ride = Ride.restore(
                rideId,
                passengerId,
                driverId,
                Status.REQUESTED.toString(),
                23.5,
                10.0, -10.0,
                20.0, -20.0,
                15.0,
                now
        );
        when(rideRepository.getRideById(rideId.toString())).thenReturn(ride);
        OutputRide output = getRide.execute(rideId.toString());
        assertEquals(rideId.toString(), output.rideId());
        assertEquals(passengerId.toString(), output.passengerId());
        assertEquals(10.0, output.fromLatitude());
        assertEquals(-10.0, output.fromLongitude());
        assertEquals(20.0, output.toLatitude());
        assertEquals(-20.0, output.toLongitude());
        assertEquals(Status.REQUESTED.toString(), output.status());
        verify(rideRepository).getRideById(rideId.toString());
    }

    @Test
    void shouldThrowExceptionWhenRideNotFound() {
        String rideId = "invalid-id";
        when(rideRepository.getRideById(rideId)).thenReturn(null);
        Exception exception = assertThrows(NullPointerException.class, () -> {
            getRide.execute(rideId);
        });
        assertNotNull(exception);
        verify(rideRepository).getRideById(rideId);
    }

}