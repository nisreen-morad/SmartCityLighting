package com.smartcity;

public class StreetLight implements LightObserver {
    private int id;
    private boolean isOn;
    private int brightness; // نسبة السطوع من 0 إلى 100

    public StreetLight(int id) {
        this.id = id;
        this.isOn = false;
        this.brightness = 0;
    }

    @Override
    public void update(String timeOfDay, boolean motionDetected, LightingStrategy strategy) {
        // حساب السطوع ديناميكياً باستخدام نمط الاستراتيجية (Strategy Pattern)
        this.brightness = strategy.calculateBrightness(timeOfDay, motionDetected);
        this.isOn = (this.brightness > 0);
        
        System.out.println("⚡ Mapped Event: StreetLight " + id + 
                           " | Strategy: " + strategy.getStrategyName() + 
                           " | Brightness: " + brightness + "%" + 
                           " | Status: " + (isOn ? "ON" : "OFF"));
    }

    public int getId() {
        return id;
    }

    public boolean isOn() {
        return isOn;
    }

    public int getBrightness() {
        return brightness;
    }
}
