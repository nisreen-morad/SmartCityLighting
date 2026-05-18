package com.smartcity;

public interface LightObserver {
    void update(String timeOfDay, boolean motionDetected, LightingStrategy strategy);
}
