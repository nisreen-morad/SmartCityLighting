package com.smartcity;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // تشغيل الواجهة الرسومية بطريقة آمنة
        SwingUtilities.invokeLater(() -> {
            SmartCityGUI gui = new SmartCityGUI();
            gui.setVisible(true);
        });
    }
}
