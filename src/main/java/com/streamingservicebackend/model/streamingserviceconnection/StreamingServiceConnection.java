package com.streamingservicebackend.model.streamingserviceconnection;

public class StreamingServiceConnection {

    private final String id;
    private final String serviceName;
    private double rentalPrice;

    public StreamingServiceConnection(String id, String serviceName, Double rentalPrice) {
        this.id = id;
        this.serviceName = serviceName;
        this.rentalPrice = rentalPrice;
    }

    public String getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }
}
