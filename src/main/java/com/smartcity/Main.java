package com.smartcity;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Singleton pattern verification test
        System.out.println("\n=======================================================");
        System.out.println("[Singleton Pattern Verification]");
        System.out.println("=======================================================");
        
        StreetSensor s1 = StreetSensor.getInstance();
        StreetSensor s2 = StreetSensor.getInstance();
        
        System.out.println("-> Sensor 1 instance HashCode: " + s1.hashCode());
        System.out.println("-> Sensor 2 instance HashCode: " + s2.hashCode());
        
        if (s1 == s2) {
            System.out.println("SUCCESS: Both references point to the exact same instance!");
            System.out.println("STATUS: Singleton pattern works successfully.");
        } else {
            System.out.println("FAILURE: Multiple sensor instances detected.");
        }
        System.out.println("=======================================================\n");

        // تشغيل الواجهة الرسومية بطريقة آمنة
        SwingUtilities.invokeLater(() -> {
            SmartCityGUI gui = new SmartCityGUI();
            gui.setVisible(true);
        });
    }
}
