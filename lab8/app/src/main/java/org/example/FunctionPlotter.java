package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
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
    private final List<PlotFunction> functions = new ArrayList<>();

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

        // Draw all functions
        for (PlotFunction plotFunction : functions) {
            if (plotFunction.type.equals("ellipse")) {
                drawEllipse(g2, plotFunction);
            } else {
                drawFunction(g2, plotFunction.function, plotFunction.color);
            }
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
        g2.setStroke(new BasicStroke(3));  // Increase line width to 3
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

    private void drawEllipse(Graphics2D g2, PlotFunction plotFunction) {
        double a = plotFunction.A;
        double b = plotFunction.B;
        g2.setColor(plotFunction.color);
        g2.setStroke(new BasicStroke(3));  // Increase line width to 3

        for (int x = (int) (-a * scale); x < (int) (a * scale); x++) {
            double x1 = x / scale;
            double[] y1 = ellipseFunction(x1, a, b);
            double x2 = (x + 1) / scale;
            double[] y2 = ellipseFunction(x2, a, b);

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

    private double[] ellipseFunction(double x, double a, double b) {
        if (Math.abs(x) > a) return new double[]{Double.NaN, Double.NaN}; // out of ellipse bounds
        double yUpper = Math.sqrt((1 - (x * x) / (a * a)) * (b * b));
        double yLower = -yUpper;
        return new double[]{yUpper, yLower};
    }

    private void addFunction(String type, double a, double b, double c) {
        Function<Double, Double> function;
        Color color;
        switch (type) {
            case "sin":
                function = (x) -> a * Math.sin(b * x + c);
                color = new Color(128, 0, 0);  // Dark Red
                break;
            case "cos":
                function = (x) -> a * Math.cos(b * x + c);
                color = new Color(0, 0, 128);  // Dark Blue
                break;
            case "tan":
                function = (x) -> a * Math.tan(b * x + c);
                color = new Color(0, 128, 0);  // Dark Green
                break;
            case "ctan":
                function = (x) -> a / Math.tan(b * x + c);
                color = new Color(128, 0, 128);  // Dark Pink
                break;
            case "parabola":
                function = (x) -> a * x * x + b * x + c;
                color = new Color(128, 0, 128);  // Dark Magenta
                break;
            case "ellipse":
                function = (x) -> Double.NaN; // Not used, special case
                color = new Color(255, 165, 0);  // Dark Orange
                functions.add(new PlotFunction(type, a, b, c, color));
                repaint();
                return;
            default:
                throw new IllegalArgumentException("Unsupported function type: " + type);
        }
        functions.add(new PlotFunction(type, function, a, b, c, color));
        repaint();
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
        JButton addButton = new JButton("Add");
        JButton clearButton = new JButton("Clear");

        controlPanel.add(new JLabel("A:"));
        controlPanel.add(aField);
        controlPanel.add(new JLabel("B:"));
        controlPanel.add(bField);
        controlPanel.add(new JLabel("C:"));
        controlPanel.add(cField);
        controlPanel.add(functionList);
        controlPanel.add(plotButton);
        controlPanel.add(addButton);
        controlPanel.add(clearButton);

        frame.setLayout(new BorderLayout());
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(plotter, BorderLayout.CENTER);

        plotButton.addActionListener(e -> {
            plotter.functions.clear();
            plotter.A = Double.parseDouble(aField.getText());
            plotter.B = Double.parseDouble(bField.getText());
            plotter.C = Double.parseDouble(cField.getText());
            plotter.functionType = (String) functionList.getSelectedItem();
            plotter.addFunction(plotter.functionType, plotter.A, plotter.B, plotter.C);
        });

        addButton.addActionListener(e -> {
            plotter.A = Double.parseDouble(aField.getText());
            plotter.B = Double.parseDouble(bField.getText());
            plotter.C = Double.parseDouble(cField.getText());
            plotter.functionType = (String) functionList.getSelectedItem();
            plotter.addFunction(plotter.functionType, plotter.A, plotter.B, plotter.C);
        });

        clearButton.addActionListener(e -> {
            plotter.functions.clear();
            plotter.repaint();
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static class PlotFunction {
        String type;
        Function<Double, Double> function;
        double A, B, C;
        Color color;

        PlotFunction(String type, Function<Double, Double> function, double A, double B, double C, Color color) {
            this.type = type;
            this.function = function;
            this.A = A;
            this.B = B;
            this.C = C;
            this.color = color;
        }

        PlotFunction(String type, double A, double B, double C, Color color) {
            this.type = type;
            this.A = A;
            this.B = B;
            this.C = C;
            this.color = color;
        }
    }
}
