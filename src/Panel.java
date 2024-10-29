import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Panel extends JPanel implements MouseMotionListener, KeyListener {
    private double deltaTime;
    private int scale = 175;
    private double zoomFactor = 1.0;
    private final double zoomStep = 0.1;
    private AffineTransform transform;
    private AffineTransform Default;
    private int lastX = 0, lastY = 0;
    private double increment = 0.003125;
    Thread ticker;
    ArrayList<Double> x;
    ArrayList<Double> y;
    int xOffset;
    int yOffset;
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

        ticker = new Thread(new Ticker());
        ticker.start();
        addMouseMotionListener(this);
        addKeyListener(this);
        requestFocusInWindow();
        setFocusable(true);
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
    Panel()
    {
        ticker = new Thread(new Ticker());
        ticker.start();
        addMouseMotionListener(this);
        addKeyListener(this);
        requestFocusInWindow();
        setFocusable(true);
        setPreferredSize(new Dimension(750, 750));
        setOpaque(true);
    }

    private void update() {
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        System.out.println(zoomFactor);
        g2d.translate(xOffset,yOffset);
        g2d.scale(zoomFactor,zoomFactor);
        g2d.translate(-xOffset,-yOffset);
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

            max = (Math.max(Complex.runIterations(new Complex(x.get(i) / scale, y.get(i) / scale)),max));
            g.setColor(generateColor(Complex.runIterations(new Complex(x.get(i) / scale, y.get(i) / scale))));
            g.fillOval((int) (x.get(i) + (750 / 2) -1), (int) ((750 / 2) - y.get(i)  -1), 2, 2);
            g.fillOval((int) (x.get(i) + (750 / 2) -1), (int) ((750 / 2) - (-1*y.get(i))-1 ), 2, 2);
        }
    }
    private void paintPoint2(Graphics2D g)
    {

        int max = -1;
            for (double real = -2; real < 2; real += increment) {
                for (double imaginary = -2; imaginary < 2; imaginary += increment) {

                    if (new Complex(real, imaginary).getMagnitude() < 2.0d) {
                        g.setColor(generateColor(Complex.runIterations(new Complex(real, imaginary))));
                        g.fillOval((int) ((real*scale) + (750 / 2) - 2), (int) ((750 / 2) - (imaginary*scale) - 2), 2, 2);
                    }
                }
            }


    }

    private void drawAxises(Graphics2D g) {
        //g.transform(Default);
        g.drawLine(375, 0, 375, 750);
        g.drawLine(0, 375, 750, 375);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xOffset = e.getX();
        yOffset = e.getY();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {


        if (e.getKeyCode() == KeyEvent.VK_W) {
            zoomFactor += zoomStep;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            zoomFactor -= zoomStep;
            if (zoomFactor < 0.1) {
                zoomFactor = 0.1;
            }
        }
        System.out.println(zoomFactor);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
