package com.curso.ride.infra.controller;

import com.curso.ride.application.dto.ErrorOutput;
import com.curso.ride.application.dto.RideInput;
import com.curso.ride.application.usecases.RequestRide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    RequestRide requestRide;

    @PostMapping()
    public ResponseEntity<?> getRide(@RequestBody RideInput input) {
        try {
            var rideOutput = requestRide.execute(input);
            return ResponseEntity.ok().body(rideOutput);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorOutput(e.getMessage()));
        }
    }

}


