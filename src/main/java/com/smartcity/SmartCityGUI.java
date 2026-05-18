package com.smartcity;

import javax.swing.*;
import java.awt.*;

public class SmartCityGUI extends JFrame implements LightObserver {
    private StreetSensor sensor;
    private JPanel lightBulb1;
    private JPanel lightBulb2;

    public SmartCityGUI() {
        // إعدادات النافذة الأساسية
        setTitle("Smart City Lighting Simulation");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // لمركزة النافذة في الشاشة

        // 1. إنشاء الحساس (Subject) وربط النافذة (Observer) به
        sensor = new StreetSensor();
        sensor.addObserver(this); 

        // 2. لوحة التحكم العلوية (لاختيار الوقت والحركة)
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] times = {"Day", "Night"};
        JComboBox<String> timeComboBox = new JComboBox<>(times);
        JCheckBox motionCheckBox = new JCheckBox("Motion Detected");
        JButton applyButton = new JButton("Apply / تحديث");

        controlPanel.add(new JLabel("Time / الوقت:"));
        controlPanel.add(timeComboBox);
        controlPanel.add(motionCheckBox);
        controlPanel.add(applyButton);

        add(controlPanel, BorderLayout.NORTH);

        // 3. لوحة المصابيح في المنتصف
        JPanel lightsContainer = new JPanel();
        lightsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        // إنشاء مصباحين
        JPanel lightPanel1 = createLightPanel("StreetLight 1");
        lightBulb1 = (JPanel) lightPanel1.getComponent(0); // حفظ الجزء المضيء لتغيير لونه

        JPanel lightPanel2 = createLightPanel("StreetLight 2");
        lightBulb2 = (JPanel) lightPanel2.getComponent(0);

        lightsContainer.add(lightPanel1);
        lightsContainer.add(lightPanel2);

        add(lightsContainer, BorderLayout.CENTER);

        // 4. برمجة زر التحديث
        applyButton.addActionListener(e -> {
            String timeOfDay = (String) timeComboBox.getSelectedItem();
            boolean motionDetected = motionCheckBox.isSelected();
            
            // عند تغيير البيانات في الحساس، سيقوم تلقائياً باستدعاء دالة update لجميع الـ Observers
            sensor.setSensorData(timeOfDay, motionDetected);
        });

        // تشغيل الواجهة بالحالة الافتراضية
        update("Day", false);
    }

    // دالة مساعدة لإنشاء شكل المصباح
    private JPanel createLightPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        
        JPanel bulb = new JPanel();
        bulb.setPreferredSize(new Dimension(80, 80));
        bulb.setBackground(Color.DARK_GRAY); // اللون الافتراضي (مطفأ)
        bulb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        panel.add(bulb, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);
        return panel;
    }

    // هذه الدالة من واجهة LightObserver، يتم استدعاؤها تلقائياً عند تغيير الحساس
    @Override
    public void update(String timeOfDay, boolean motionDetected) {
        Color lightColor;
        
        // منطق التشغيل: إذا كان الليل وهناك حركة يضيء المصباح باللون الأصفر
        if (timeOfDay.equalsIgnoreCase("Night") && motionDetected) {
            lightColor = Color.YELLOW; // تشغيل (ON)
        } else {
            lightColor = Color.DARK_GRAY; // إطفاء (OFF)
        }

        // تغيير لون المصابيح في الواجهة
        lightBulb1.setBackground(lightColor);
        lightBulb2.setBackground(lightColor);
    }
}
