package com.smartcity;

import java.util.ArrayList;
import java.util.List;

public class StreetSensor {
    private List<LightObserver> observers;
    private String timeOfDay;
    private boolean motionDetected;
    private LightingStrategy currentStrategy;

    public StreetSensor() {
        this.observers = new ArrayList<>();
        this.currentStrategy = new NormalStrategy(); // الاستراتيجية الافتراضية
    }

    public void addObserver(LightObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(LightObserver observer) {
        observers.remove(observer);
    }

    public void setSensorData(String timeOfDay, boolean motionDetected) {
        this.timeOfDay = timeOfDay;
        this.motionDetected = motionDetected;
        notifyObservers();
    }

    public void setSensorData(String timeOfDay, boolean motionDetected, LightingStrategy strategy) {
        this.timeOfDay = timeOfDay;
        this.motionDetected = motionDetected;
        this.currentStrategy = strategy;
        notifyObservers();
    }

    private void notifyObservers() {
        for (LightObserver observer : observers) {
            observer.update(timeOfDay, motionDetected, currentStrategy);
        }
    }

    public LightingStrategy getCurrentStrategy() {
        return currentStrategy;
    }
}
