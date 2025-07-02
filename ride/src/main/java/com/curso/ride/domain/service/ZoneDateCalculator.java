package com.curso.ride.domain.service;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZoneDateCalculator {

    public static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    public static ZonedDateTime toZonedDateTime(Clock clock) {
        return ZonedDateTime.ofInstant(clock.instant(), ZONE_ID);
    }

}
