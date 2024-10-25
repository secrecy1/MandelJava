import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Panel extends JPanel {
    private double deltaTime;
    private int scale = 175;
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private AffineTransform transform;
    private AffineTransform Default;
    private int lastX = 0, lastY = 0;
    ArrayList<Double> x;
    ArrayList<Double> y;
    Color[] colors;

    Panel(Complex c) {
        //setUpZoom();
        x = new ArrayList<>();
        y = new ArrayList<>();
        x.add(c.getReal() * scale);
        y.add(c.getImaginary() * scale);
        setPreferredSize(new Dimension(750, 750));
        setForeground(new Color(151, 151, 151));
        setOpaque(true);
        setBackground(new Color(151, 151, 151));
    }

    Panel(ArrayList<Complex> c) {
       // setUpZoom();
        colors = new Color[]{Color.red, Color.orange, Color.yellow, Color.green, Color.blue, new Color(100, 0, 255),Color.black,Color.black};
        x = new ArrayList<>();
        y = new ArrayList<>();
        for (int i = 0; i < c.size(); i++) {
            x.add(c.get(i).getReal() * scale);
            y.add(c.get(i).getImaginary() * scale);
        }
        setPreferredSize(new Dimension(750, 750));
        //setForeground(new Color(151, 151, 151));
        setOpaque(true);
        //setBackground(new Color(151, 151, 151));
    }
    private void setUpZoom() {
        transform = new AffineTransform();
        Default = new AffineTransform();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                double zoomAmount = 1.0;
                if (keyCode == KeyEvent.VK_UP) { // Zoom in
                    zoomAmount = 1.1;
                } else if (keyCode == KeyEvent.VK_DOWN) { // Zoom out
                    zoomAmount = 0.9;
                }
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                    zoomFactor *= zoomAmount;
                    zoomFactor = Math.max(zoomFactor, zoomStep);
                    double offsetX = (lastX - getWidth() / 2.0) * (1 - zoomAmount);
                    double offsetY = (lastY - getHeight() / 2.0) * (1 - zoomAmount);
                    transform.translate(offsetX, offsetY);
                    transform.scale(zoomAmount, zoomAmount);
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - lastX;
                int deltaY = e.getY() - lastY;
                transform.translate(deltaX, deltaY);
                lastX = e.getX();
                lastY = e.getY();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }


    private void update() {
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
       /* try {
            g2d.transform(transform);
        }catch (Exception e)
        {
            System.out.println(e);
        } */
        drawAxises(g2d);
        paintPoint(g2d);
    }
    public Color generateColor(int n) {
        float hue = (n * 0.618033988749895f) % 1.0f; // Using the golden ratio for spacing
        return Color.getHSBColor(hue, 0.9f, 0.9f); // High saturation and brightness for distinct colors
    }
    private void paintPoint(Graphics2D g) {
        //g.transform(Default);
        int max = -1;
        for (int i = 0; i < x.size(); i++) {
            System.out.println(Math.max(Complex.runIterations(new Complex(x.get(i) / scale, y.get(i) / scale)),max));
            max = (Math.max(Complex.runIterations(new Complex(x.get(i) / scale, y.get(i) / scale)),max));
            g.setColor(generateColor(Complex.runIterations(new Complex(x.get(i) / scale, y.get(i) / scale))));
            g.fillOval((int) (x.get(i) + (750 / 2) - 2), (int) ((750 / 2) - y.get(i) - 2), 2, 2);
        }
    }

    private void drawAxises(Graphics2D g) {
        //g.transform(Default);
        g.drawLine(375, 0, 375, 750);
        g.drawLine(0, 375, 750, 375);
    }

    private class Ticker implements Runnable {
        @Override
        public void run() {
            tick();
        }

        public synchronized void tick() {
            double updateRate = 1.0d / 60.0d;
            double accumulator = 0;
            double lastFrameTime = 0.0;
            while (true) {
                double time = Time.getTime();
                deltaTime = time - lastFrameTime;
                accumulator += deltaTime;
                lastFrameTime = time;
                while (accumulator > updateRate) {
                    update();
                    repaint();
                    accumulator -= updateRate;
                }
            }
        }
    }
}
