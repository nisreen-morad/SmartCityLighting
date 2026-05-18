package com.smartcity;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class SmartCityGUI extends JFrame implements LightObserver {
    private StreetSensor sensor;
    
    // مصابيح الشوارع (Observers المنطقية)
    private StreetLight light1;
    private StreetLight light2;

    // عناصر الواجهة الرسومية
    private LightBulbPanel bulbPanel1;
    private LightBulbPanel bulbPanel2;
    
    private JLabel statusLabel1;
    private JLabel statusLabel2;
    
    private JComboBox<String> timeComboBox;
    private JCheckBox motionCheckBox;
    private JComboBox<String> strategyComboBox;
    
    private JLabel activeStrategyVal;
    private JLabel currentLoadVal;
    private JLabel energySavingsVal;

    // تفعيل مكتبة FlatLaf Dark Mode لعصرنة الواجهة الرسومية
    static {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("FlatLaf theme failed to initialize, falling back to default Swing look.");
        }
    }

    public SmartCityGUI() {
        // إعدادات النافذة الأساسية
        setTitle("🌿 Smart City Lighting - لوحة التحكم الذكية");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);

        // 1. إنشاء الحساس (Subject) وتجهيز المصابيح (Observers)
        sensor = new StreetSensor();
        light1 = new StreetLight(1);
        light2 = new StreetLight(2);

        // ربط جميع الملاحظين بالحساس
        sensor.addObserver(this);   // الواجهة نفسها لتحديث الرسومات
        sensor.addObserver(light1); // المصباح الأول (سيسجل الأحداث في الكونسول)
        sensor.addObserver(light2); // المصباح الثاني (سيسجل الأحداث في الكونسول)

        // 2. تصميم الواجهة وتقسيمها
        initControlPanel();   // اللوحة العلوية للتحكم
        initLightsDisplay();  // اللوحة الوسطى لعرض المصابيح المضيئة
        initStatsDashboard(); // اللوحة السفلية للإحصائيات الفورية

        // تفعيل الحالة الافتراضية
        updateState();
    }

    // إنشاء لوحة التحكم العلوية
    private void initControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        controlPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "لوحة التحكم والمحاكاة / Simulation Control", 
                TitledBorder.RIGHT, 
                TitledBorder.TOP, 
                new Font("Inter", Font.BOLD, 12)
        ));

        // اختيار الوقت
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.add(new JLabel("الوقت / Time:"));
        String[] times = {"Day", "Night"};
        timeComboBox = new JComboBox<>(times);
        timeComboBox.setPreferredSize(new Dimension(90, 30));
        timePanel.add(timeComboBox);

        // استشعار الحركة
        motionCheckBox = new JCheckBox("رصد حركة / Motion Detected");
        motionCheckBox.setFocusPainted(false);

        // اختيار استراتيجية الإضاءة (Strategy Pattern!)
        JPanel strategyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        strategyPanel.add(new JLabel("الاستراتيجية / Strategy:"));
        String[] strategies = {
            "Normal / الوضع العادي",
            "Eco Mode / توفير الطاقة",
            "Security / الأمان العالي"
        };
        strategyComboBox = new JComboBox<>(strategies);
        strategyComboBox.setPreferredSize(new Dimension(170, 30));
        strategyPanel.add(strategyComboBox);

        // زر التحديث
        JButton applyButton = new JButton("تحديث المحاكاة ⚡");
        applyButton.setPreferredSize(new Dimension(140, 32));
        applyButton.setFont(new Font("Inter", Font.BOLD, 12));
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> updateState());

        // إضافة العناصر للوحة التحكم
        controlPanel.add(timePanel);
        controlPanel.add(motionCheckBox);
        controlPanel.add(strategyPanel);
        controlPanel.add(applyButton);

        add(controlPanel, BorderLayout.NORTH);
    }

    // إنشاء لوحة عرض المصابيح
    private void initLightsDisplay() {
        JPanel mainDisplayPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainDisplayPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // المصباح الأول
        JPanel lightPanel1 = createLightCard("StreetLight 1");
        bulbPanel1 = (LightBulbPanel) lightPanel1.getClientProperty("bulbPanel");
        statusLabel1 = (JLabel) lightPanel1.getClientProperty("statusLabel");

        // المصباح الثاني
        JPanel lightPanel2 = createLightCard("StreetLight 2");
        bulbPanel2 = (LightBulbPanel) lightPanel2.getClientProperty("bulbPanel");
        statusLabel2 = (JLabel) lightPanel2.getClientProperty("statusLabel");

        mainDisplayPanel.add(lightPanel1);
        mainDisplayPanel.add(lightPanel2);

        add(mainDisplayPanel, BorderLayout.CENTER);
    }

    // كارت مخصص لكل مصباح لتنظيم تصميمه
    private JPanel createLightCard(String title) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(35, 35, 35));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 14));
        titleLabel.setForeground(new Color(180, 180, 180));

        // لوحة رسم المصباح المضيء بتأثير التوهج
        LightBulbPanel bulb = new LightBulbPanel();
        bulb.setPreferredSize(new Dimension(100, 100));
        bulb.setOpaque(false);

        // تسمية حالة السطوع والتشغيل
        JLabel statusLabel = new JLabel("سطوع المصباح: 0%", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        statusLabel.setForeground(Color.GRAY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(bulb, BorderLayout.CENTER);
        card.add(statusLabel, BorderLayout.SOUTH);

        // حفظ مرجع العناصر لتسهيل تحديثها لاحقاً
        card.putClientProperty("bulbPanel", bulb);
        card.putClientProperty("statusLabel", statusLabel);

        return card;
    }

    // لوحة الإحصائيات السفلية (Dashboard)
    private void initStatsDashboard() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), 
                "إحصائيات الطاقة اللحظية / Live Power Analytics", 
                TitledBorder.RIGHT, 
                TitledBorder.TOP, 
                new Font("Inter", Font.BOLD, 12)
        ));

        // الاستراتيجية المفعلة
        JPanel stratPanel = createStatBox("الاستراتيجية النشطة", "Normal");
        activeStrategyVal = (JLabel) stratPanel.getClientProperty("valLabel");

        // الحمل الحالي بالوات
        JPanel loadPanel = createStatBox("معدل الاستهلاك الكلي", "0 Watt");
        currentLoadVal = (JLabel) loadPanel.getClientProperty("valLabel");

        // نسبة التوفير في الطاقة
        JPanel savingsPanel = createStatBox("نسبة توفير الطاقة 🌿", "100%");
        energySavingsVal = (JLabel) savingsPanel.getClientProperty("valLabel");
        energySavingsVal.setForeground(new Color(46, 204, 113)); // لون أخضر افتراضي للتوفير الكامل

        statsPanel.add(stratPanel);
        statsPanel.add(loadPanel);
        statsPanel.add(savingsPanel);

        add(statsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatBox(String labelText, String defaultVal) {
        JPanel box = new JPanel(new GridLayout(2, 1, 5, 5));
        box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box.setBackground(new Color(28, 28, 28));

        JLabel title = new JLabel(labelText, SwingConstants.CENTER);
        title.setFont(new Font("Inter", Font.PLAIN, 12));
        title.setForeground(Color.GRAY);

        JLabel value = new JLabel(defaultVal, SwingConstants.CENTER);
        value.setFont(new Font("Inter", Font.BOLD, 16));
        value.setForeground(Color.WHITE);

        box.add(title);
        box.add(value);
        box.putClientProperty("valLabel", value);

        return box;
    }

    // استخراج الاستراتيجية المناسبة وتحديث الحساس
    private void updateState() {
        String timeOfDay = (String) timeComboBox.getSelectedItem();
        boolean motionDetected = motionCheckBox.isSelected();
        String selectedStratName = (String) strategyComboBox.getSelectedItem();

        LightingStrategy strategy;
        if (selectedStratName.contains("Eco")) {
            strategy = new EcoStrategy();
        } else if (selectedStratName.contains("Security")) {
            strategy = new SecurityStrategy();
        } else {
            strategy = new NormalStrategy();
        }

        // إطلاق إشعار التغيير للملاحظين (Observer Pattern + Strategy Pattern)
        sensor.setSensorData(timeOfDay, motionDetected, strategy);
    }

    // دالة تحديث الواجهة عند حدوث أي تغيير في الحساس (Observer Pattern)
    @Override
    public void update(String timeOfDay, boolean motionDetected, LightingStrategy strategy) {
        // حساب السطوع لكل مصباح بناءً على الاستراتيجية المفعلة
        int brightness = strategy.calculateBrightness(timeOfDay, motionDetected);

        // 1. تحديث الرسومات البيانية وتأثير التوهج للمصابيح
        bulbPanel1.setBrightness(brightness);
        bulbPanel2.setBrightness(brightness);

        // 2. تحديث نصوص الحالة للمصابيح
        String statusText = (brightness > 0) ? "نشط (" + brightness + "%) 💡" : "مطفأ (0%) 🌑";
        statusLabel1.setText("سطوع المصباح: " + statusText);
        statusLabel2.setText("سطوع المصباح: " + statusText);
        
        if (brightness > 0) {
            statusLabel1.setForeground(new Color(255, 215, 0));
            statusLabel2.setForeground(new Color(255, 215, 0));
        } else {
            statusLabel1.setForeground(Color.GRAY);
            statusLabel2.setForeground(Color.GRAY);
        }

        // 3. تحديث لوحة الإحصائيات (Dashboard)
        activeStrategyVal.setText(strategy.getStrategyName().split(" / ")[0]);

        // نفترض أن المصباح الواحد يستهلك 100 وات كحد أقصى (عند سطوع 100%)
        int totalLoad = (brightness * 2); // استهلاك المصباحين معاً
        currentLoadVal.setText(totalLoad + " Watt");

        // حساب نسبة توفير الطاقة مقارنة بالاستهلاك الأقصى (200 وات)
        int savingsPercent = 100 - (int)((totalLoad / 200.0) * 100);
        energySavingsVal.setText(savingsPercent + "%");

        // تغيير ألوان نسبة التوفير لتعطي مؤشراً تفاعلياً رائعاً
        if (savingsPercent >= 80) {
            energySavingsVal.setForeground(new Color(46, 204, 113)); // أخضر (توفير ممتاز)
        } else if (savingsPercent >= 50) {
            energySavingsVal.setForeground(new Color(241, 196, 15)); // أصفر (توفير متوسط)
        } else {
            energySavingsVal.setForeground(new Color(231, 76, 60)); // أحمر (توفير منخفض أو استهلاك كامل)
        }
    }

    // كلاس داخلي لرسم المصباح بتأثير التوهج المضيء (Dynamic Glow Effect)
    private static class LightBulbPanel extends JPanel {
        private int brightness = 0;

        public void setBrightness(int brightness) {
            this.brightness = brightness;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // تحسين جودة الرسم ومنع الحواف المتكسرة (Anti-Aliasing)
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 40;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            // 1. رسم تأثير التوهج المضيء بالتدريج الدائري (Radial Glow)
            if (brightness > 0) {
                float[] dist = {0.0f, 1.0f};
                // شفافية التوهج تعتمد مباشرة على نسبة السطوع المحسوبة من الاستراتيجية
                int alphaVal = (int) (brightness * 2.2); // حد أقصى 220 شفافية
                Color glowColor = new Color(255, 235, 100, alphaVal);
                Color outerColor = new Color(255, 235, 100, 0);

                RadialGradientPaint paint = new RadialGradientPaint(
                        new Point(width / 2, height / 2),
                        (diameter / 2.0f) + 20, 
                        dist, 
                        new Color[]{glowColor, outerColor}
                );
                g2d.setPaint(paint);
                g2d.fill(new Ellipse2D.Double(x - 20, y - 20, diameter + 40, diameter + 40));
            }

            // 2. رسم شكل جسم المصباح الداخلي الدائري
            Color bulbColor;
            if (brightness == 0) {
                bulbColor = new Color(60, 60, 60); // رمادي داكن (مطفأ)
            } else {
                // دمج درجات اللون الأصفر الفاقع والأبيض بناءً على شدة السطوع
                int red = 255;
                int green = 230 + (int)(brightness * 0.25);
                int blue = 100 + (int)(brightness * 1.5);
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;
                bulbColor = new Color(red, green, blue);
            }

            g2d.setColor(bulbColor);
            g2d.fill(new Ellipse2D.Double(x, y, diameter, diameter));

            // 3. رسم إطار المصباح لإعطاء تأثير ثلاثي الأبعاد
            g2d.setColor(brightness > 0 ? new Color(255, 240, 150) : new Color(90, 90, 90));
            g2d.setStroke(new BasicStroke(2.5f));
            g2d.draw(new Ellipse2D.Double(x, y, diameter, diameter));
        }
    }
}
