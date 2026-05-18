package com.smartcity;

public class NormalStrategy implements LightingStrategy {
    @Override
    public int calculateBrightness(String timeOfDay, boolean motionDetected) {
        if (timeOfDay.equalsIgnoreCase("Day")) {
            return 0; // مطفأ في النهار
        }
        
        // في الليل
        if (motionDetected) {
            return 100; // سطوع كامل عند وجود حركة
        } else {
            return 50; // سطوع متوسط عند عدم وجود حركة
        }
    }

    @Override
    public String getStrategyName() {
        return "Normal / الوضع العادي";
    }

    @Override
    public String getDescription() {
        return "الليل: 50% سطوع، وعند الحركة: 100%. النهار: مطفأ.";
    }
}
