package com.smartcity;

public class EcoStrategy implements LightingStrategy {
    @Override
    public int calculateBrightness(String timeOfDay, boolean motionDetected) {
        if (timeOfDay.equalsIgnoreCase("Day")) {
            return 0; // مطفأ في النهار
        }
        
        // في الليل
        if (motionDetected) {
            return 100; // سطوع كامل عند وجود حركة
        } else {
            return 10; // سطوع منخفض جداً (10%) لتوفير الطاقة القصوى
        }
    }

    @Override
    public String getStrategyName() {
        return "Eco Mode / توفير الطاقة";
    }

    @Override
    public String getDescription() {
        return "الليل: 10% سطوع، وعند الحركة: 100% لتوفير طاقة أقصى. النهار: مطفأ.";
    }
}
