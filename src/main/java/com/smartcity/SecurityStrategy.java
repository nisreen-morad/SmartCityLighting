package com.smartcity;

public class SecurityStrategy implements LightingStrategy {
    @Override
    public int calculateBrightness(String timeOfDay, boolean motionDetected) {
        if (timeOfDay.equalsIgnoreCase("Day")) {
            return 0; // مطفأ في النهار
        }
        
        // في الليل
        return 100; // سطوع كامل 100% دائماً في الليل لتأمين المنطقة بغض النظر عن الحركة
    }

    @Override
    public String getStrategyName() {
        return "Security / وضع الأمان العالي";
    }

    @Override
    public String getDescription() {
        return "الليل: 100% سطوع دائم لحماية الشارع. النهار: مطفأ.";
    }
}
