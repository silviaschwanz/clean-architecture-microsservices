package com.curso.ride.domain.vo;

public interface RideStatus {

    String getValue();
    RideStatus accept();
    RideStatus start();
    RideStatus update();
    RideStatus finish();

}
