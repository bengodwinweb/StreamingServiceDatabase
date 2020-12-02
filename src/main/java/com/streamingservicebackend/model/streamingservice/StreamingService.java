package com.streamingservicebackend.model.streamingservice;

import java.util.Objects;

public class StreamingService {

    private String id;
    private String name;
    private double monthlySubscriptionPrice;
    private int freeTrialLength; // in days
    private boolean showsAds;

    public StreamingService() { }

    public StreamingService(String id, String name, double subscriptionPrice, int trialLength, boolean showsAds) {
        this.id = id;
        this.name = name;
        this.monthlySubscriptionPrice = subscriptionPrice;
        this.freeTrialLength = trialLength;
        this.showsAds = showsAds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlySubscriptionPrice() {
        return monthlySubscriptionPrice;
    }

    public void setMonthlySubscriptionPrice(double monthlySubscriptionPrice) {
        this.monthlySubscriptionPrice = monthlySubscriptionPrice;
    }

    public int getFreeTrialLength() {
        return freeTrialLength;
    }

    public void setFreeTrialLength(int freeTrialLength) {
        this.freeTrialLength = freeTrialLength;
    }

    public boolean getShowsAds() {
        return showsAds;
    }

    public void setShowsAds(boolean showsAds) {
        this.showsAds = showsAds;
    }

    @Override
    public String toString() {
        return String.format("Service Name: %s, Id: %s, Base Price: $%,.2f, , Free Trial Days: %d, Ads: %s", getName(), getId(), getMonthlySubscriptionPrice(), getFreeTrialLength(), getShowsAds());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamingService that = (StreamingService) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
