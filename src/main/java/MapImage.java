import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapImage extends JPanel {
    private BufferedImage image;
    private double x;
    private double y;
    private double zoom;
    private double MAX_ZOOM = 2;
    private double MIN_ZOOM = 0.1;
    private double zoomIncrement = -1;
    private List<MouseEvent> clicks = new ArrayList<MouseEvent>();

    MapImage() {
        this.setSize(1024, 768);
        zoom = 1;
        try {
            image = ImageIO.read(new File("D:\\Projects\\Personal\\GameMap\\src\\main\\resources\\ThedasMap.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Backup original transform
        AffineTransform originalTransform = g2d.getTransform();

        g2d.translate(-(x * image.getWidth() * zoom), -(y * image.getHeight() * zoom));

        g2d.scale(zoom, zoom);

        // paint the image here with no scaling
        g2d.drawImage(image, 0, 0, null);
        for (int k = 0; k < clicks.size(); k++) {
            MouseEvent mouseEvent = clicks.get(k);
            MouseEvent lastClick = mouseEvent;
            if (k != 0) {
                lastClick = clicks.get(k - 1);
            }
            if (mouseEvent.isShiftDown()) {
                g2d.setColor(Color.CYAN);
            } else {
                g2d.setColor(Color.GREEN);
            }
            g2d.drawLine(mouseEvent.getX(), mouseEvent.getY(), lastClick.getX(), lastClick.getY());
        }

        g2d.setColor(Color.WHITE);
        int r = 100;



        /*
            2      3

        1             4

           6        5

         */

        for(int i = 0; i < 10; i++) {
            Polygon hex = new Polygon();
            hex.addPoint(-r, 0); // 1
            hex.addPoint((int)(-.5 * r), ((int)(Math.sqrt(3) / 2 * r))); // 2
            hex.addPoint((int)(.5 * r), ((int)(Math.sqrt(3) / 2 * r))); // 3
            hex.addPoint(r, 0); // 4
            hex.addPoint((int)(.5 * r), ((int)(Math.sqrt(3) / -2 * r))); // 5
            hex.addPoint((int)(-.5 * r), ((int)(Math.sqrt(3) / -2 * r))); // 6
            hex.addPoint(-r, 0); // 7 (1)
            hex.translate(0, r*2 * i);
            for (int k = 0; k < 34; k++) {
                g2d.drawPolygon(hex);
                hex.translate(r * 2, 0);
            }

        }
        // Restore original transform
        g2d.setTransform(originalTransform);
    }

    public void panImage(double x, double y) {
        this.x = x;
        this.y = y;
        super.repaint();
    }

    public void zoomImage(int i) {
        if (zoomIncrement == -1) {
            zoomIncrement = Math.abs(i);
        }
        zoom += (i / zoomIncrement) * .05;
        if (zoom > MAX_ZOOM) {
            zoom = MAX_ZOOM;
        }
        if (zoom < MIN_ZOOM) {
            zoom = MIN_ZOOM;
        }
        super.repaint();
    }

    public void addClick(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            clicks.clear();
        } else {
            System.out.println(e);
            clicks.add(e);
        }
        super.repaint();
    }
}
