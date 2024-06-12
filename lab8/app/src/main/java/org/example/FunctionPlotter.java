package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Function;

public class FunctionPlotter extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MARGIN = 50;
    private double scale = 50;
    private double A = 1;
    private double B = 1;
    private double C = 1;
    private String functionType = "sin";
    private Point origin = new Point(WIDTH / 2, HEIGHT / 2);

    public FunctionPlotter() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseWheelListener(new ZoomHandler());
        addMouseListener(new DragHandler());
        addMouseMotionListener(new DragHandler());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        // Draw axes
        g2.drawLine(0, origin.y, WIDTH, origin.y);
        g2.drawLine(origin.x, 0, origin.x, HEIGHT);

        drawGrid(g2);

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
            case "ctan":
                drawFunction(g2, this::ctanFunction, Color.PINK);
                break;
            case "parabola":
                drawFunction(g2, this::parabolaFunction, Color.MAGENTA);
                break;
            case "ellipse":
                drawEllipse(g2, Color.ORANGE);
                break;
        }
    }

    private void drawGrid(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.LIGHT_GRAY);

        int step = (int) scale;

        for (int x = origin.x % step; x < WIDTH; x += step) {
            g2.drawLine(x, 0, x, HEIGHT);
            g2.setColor(Color.BLACK);
            g2.drawString(String.format("%.1f", (x - origin.x) / scale), x, origin.y + 15);
            g2.setColor(Color.LIGHT_GRAY);
        }
        for (int y = origin.y % step; y < HEIGHT; y += step) {
            g2.drawLine(0, y, WIDTH, y);
            g2.setColor(Color.BLACK);
            g2.drawString(String.format("%.1f", (origin.y - y) / scale), origin.x + 5, y);
            g2.setColor(Color.LIGHT_GRAY);
        }
    }

    private void drawFunction(Graphics2D g2, Function<Double, Double> function, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));
        for (int x = -WIDTH / 2; x < WIDTH / 2; x++) {
            double x1 = x / scale;
            double y1 = function.apply(x1);
            double x2 = (x + 1) / scale;
            double y2 = function.apply(x2);

            int px1 = toPixelX(x1);
            int py1 = toPixelY(y1);
            int px2 = toPixelX(x2);
            int py2 = toPixelY(y2);

            g2.drawLine(px1, py1, px2, py2);
        }
    }

    private void drawEllipse(Graphics2D g2, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(2));

        double step = 1.0 / scale; // Smaller step for smoother ellipse

        for (double t = -A; t <= A; t += step) {
            double x1 = t;
            double[] y1 = ellipseFunction(x1);
            double x2 = t + step;
            double[] y2 = ellipseFunction(x2);

            int px1 = toPixelX(x1);
            int py1Upper = toPixelY(y1[0]);
            int py1Lower = toPixelY(y1[1]);
            int px2 = toPixelX(x2);
            int py2Upper = toPixelY(y2[0]);
            int py2Lower = toPixelY(y2[1]);

            g2.drawLine(px1, py1Upper, px2, py2Upper);
            g2.drawLine(px1, py1Lower, px2, py2Lower);
        }
    }

    private int toPixelX(double x) {
        return origin.x + (int) (x * scale);
    }

    private int toPixelY(double y) {
        return origin.y - (int) (y * scale);
    }

    private double sinFunction(double x) {
        return A * Math.sin(B * x + C);
    }

    private double cosFunction(double x) {
        return A * Math.cos(B * x + C);
    }

    private double tanFunction(double x) {
        return A * Math.tan(B * x + C);
    }

    private double ctanFunction(double x) {
        return A / Math.tan(B * x + C);
    }

    private double parabolaFunction(double x) {
        return A * x * x + B * x + C;
    }

    private double[] ellipseFunction(double x) {
        double a = A; // semi-major axis
        double b = B; // semi-minor axis
        if (Math.abs(x) > a) return new double[]{0, 0}; // out of ellipse bounds
        double yUpper = Math.sqrt((1 - (x * x) / (a * a)) * (b * b));
        double yLower = -yUpper;
        return new double[]{yUpper, yLower};
    }

    private class ZoomHandler extends MouseAdapter {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double delta = 1.1;
            if (e.getPreciseWheelRotation() < 0) {
                scale *= delta;
            } else {
                scale /= delta;
            }
            repaint();
        }
    }

    private class DragHandler extends MouseAdapter {
        private Point lastPoint;

        @Override
        public void mousePressed(MouseEvent e) {
            lastPoint = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currentPoint = e.getPoint();
            origin.translate(currentPoint.x - lastPoint.x, currentPoint.y - lastPoint.y);
            lastPoint = currentPoint;
            repaint();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Function Plotter");
        FunctionPlotter plotter = new FunctionPlotter();

        JPanel controlPanel = new JPanel();
        JTextField aField = new JTextField("1", 5);
        JTextField bField = new JTextField("1", 5);
        JTextField cField = new JTextField("1", 5);
        String[] functions = {"sin", "cos", "tan", "ctan", "parabola", "ellipse"};
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

        plotButton.addActionListener(e -> {
            plotter.A = Double.parseDouble(aField.getText());
            plotter.B = Double.parseDouble(bField.getText());
            plotter.C = Double.parseDouble(cField.getText());
            plotter.functionType = (String) functionList.getSelectedItem();
            plotter.repaint();
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
