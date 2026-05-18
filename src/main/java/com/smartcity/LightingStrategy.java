package com.smartcity;

public interface LightingStrategy {
    /**
     * حساب نسبة سطوع المصباح (من 0% إلى 100%) بناءً على الوقت ورصد الحركة.
     */
    int calculateBrightness(String timeOfDay, boolean motionDetected);
    
    /**
     * الحصول على اسم الاستراتيجية لعرضه في الواجهة.
     */
    String getStrategyName();

    /**
     * الحصول على وصف بسيط للاستراتيجية.
     */
    String getDescription();
}
