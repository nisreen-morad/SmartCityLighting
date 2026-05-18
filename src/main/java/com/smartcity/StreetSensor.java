package com.smartcity;

import java.util.ArrayList;
import java.util.List;

public class StreetSensor {
    private List<LightObserver> observers;
    private String timeOfDay;
    private boolean motionDetected;

    public StreetSensor() {
        this.observers = new ArrayList<>();
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

    private void notifyObservers() {
        for (LightObserver observer : observers) {
            observer.update(timeOfDay, motionDetected);
        }
    }
}
