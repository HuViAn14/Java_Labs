package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;

public class FunctionPlotter extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MARGIN = 50;
    private static final int STEP = 1;

    private double A = 1;
    private double B = 1;
    private double C = 1;
    private String functionType = "sin";

    public FunctionPlotter() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw axes
        g2.drawLine(MARGIN, HEIGHT / 2, WIDTH - MARGIN, HEIGHT / 2);
        g2.drawLine(WIDTH / 2, MARGIN, WIDTH / 2, HEIGHT - MARGIN);

        // Draw selected function
        switch (functionType) {
            case "sin":
                drawFunction(g2, this::sinFunction, Color.RED);
                break;
            case "cos":
                drawFunction(g2, this::cosFunction, Color.BLUE);
                break;
            case "tan":
                drawFunction(g2, this::tanFunction, Color.GREEN);
                break;
            case "parabola":
                drawFunction(g2, this::parabolaFunction, Color.MAGENTA);
                break;
            case "ellipse":
                drawFunction(g2, this::ellipseFunction, Color.ORANGE);
                break;
        }
    }

    private void drawFunction(Graphics2D g2, Function<Double, Double> function, Color color) {
        g2.setColor(color);
        for (int x = -WIDTH / 2; x < WIDTH / 2; x += STEP) {
            double x1 = x / 50.0;
            double y1 = function.apply(x1);
            double x2 = (x + STEP) / 50.0;
            double y2 = function.apply(x2);

            int px1 = toPixelX(x1);
            int py1 = toPixelY(y1);
            int px2 = toPixelX(x2);
            int py2 = toPixelY(y2);

            g2.drawLine(px1, py1, px2, py2);
        }
    }

    private int toPixelX(double x) {
        return (int) (WIDTH / 2 + x * 50);
    }

    private int toPixelY(double y) {
        return (int) (HEIGHT / 2 - y * 50);
    }

    private double sinFunction(double x) {
        return A + B * Math.sin(C * x);
    }

    private double cosFunction(double x) {
        return A + B * Math.cos(C * x);
    }

    private double tanFunction(double x) {
        return A + B * Math.tan(C * x);
    }

    private double parabolaFunction(double x) {
        return A * x * x + B * x + C;
    }

    private double ellipseFunction(double x) {
        double a = 5;
        double b = 3;
        if (Math.abs(x) > a) return 0;
        return Math.sqrt((1 - (x * x) / (a * a)) * (b * b));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Function Plotter");
        FunctionPlotter plotter = new FunctionPlotter();

        JPanel controlPanel = new JPanel();
        JTextField aField = new JTextField("1", 5);
        JTextField bField = new JTextField("1", 5);
        JTextField cField = new JTextField("1", 5);
        String[] functions = {"sin", "cos", "tan", "parabola", "ellipse"};
        JComboBox<String> functionList = new JComboBox<>(functions);
        JButton plotButton = new JButton("Plot");

        controlPanel.add(new JLabel("A:"));
        controlPanel.add(aField);
        controlPanel.add(new JLabel("B:"));
        controlPanel.add(bField);
        controlPanel.add(new JLabel("C:"));
        controlPanel.add(cField);
        controlPanel.add(functionList);
        controlPanel.add(plotButton);

        frame.setLayout(new BorderLayout());
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(plotter, BorderLayout.CENTER);

        plotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plotter.A = Double.parseDouble(aField.getText());
                plotter.B = Double.parseDouble(bField.getText());
                plotter.C = Double.parseDouble(cField.getText());
                plotter.functionType = (String) functionList.getSelectedItem();
                plotter.repaint();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @FunctionalInterface
    interface Function<T, R> {
        R apply(T t);
    }
}
