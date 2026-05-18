package com.smartcity;

public class StreetLight implements LightObserver {
    private int id;
    private boolean isOn;

    public StreetLight(int id) {
        this.id = id;
        this.isOn = false;
    }

    @Override
    public void update(String timeOfDay, boolean motionDetected) {
        // إذا كان الوقت ليلاً وهناك حركة، يتم تشغيل المصباح
        if (timeOfDay.equalsIgnoreCase("Night") && motionDetected) {
            turnOn();
        } 
        // غير ذلك (في النهار أو لا توجد حركة)، يتم إطفاؤه
        else {
            turnOff();
        }
    }

    private void turnOn() {
        if (!isOn) {
            isOn = true;
            System.out.println("💡 StreetLight " + id + ": Turned ON");
        }
    }

    private void turnOff() {
        if (isOn) {
            isOn = false;
            System.out.println("🌑 StreetLight " + id + ": Turned OFF");
        }
    }
}
