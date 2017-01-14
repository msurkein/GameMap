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
    private TripInfoForm tripInfoForm;

    MapImage() {
        this.setSize(1024, 768);
        zoom = 1;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream("ThedasMap.jpg"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Backup original transform
        AffineTransform originalTransform = g2d.getTransform();

        g2d.translate(-(x * image.getWidth() * zoom), -(y * image.getHeight() * zoom));

        //g2d.scale(zoom, zoom);

        // paint the image here with no scaling
        g2d.drawImage(image, 0, 0, null);
        double distance = 0;
        double travelTime = 0;
        for (int k = 0; k < clicks.size(); k++) {
            MouseEvent mouseEvent = clicks.get(k);
            MouseEvent lastClick = mouseEvent;
            if (k != 0) {
                lastClick = clicks.get(k - 1);
            }
            double travelMultiplier = 2.0;
            if (mouseEvent.isShiftDown()) {
                // horseback
                g2d.setColor(Color.CYAN);
                travelMultiplier = 1.0;
            } else if (mouseEvent.isControlDown()) {
                g2d.setColor(Color.MAGENTA);
                travelMultiplier = 0.5;
            } else {
                g2d.setColor(Color.GREEN);
            }
            double sqrt = Math.sqrt(Math.pow(mouseEvent.getX() - lastClick.getX(), 2) + Math.pow(mouseEvent.getY() - lastClick.getY(), 2)) / 140;
            distance += (sqrt * 35);
            BasicStroke stroke;
            if(mouseEvent.getButton() == MouseEvent.BUTTON3) {
                travelMultiplier *= 2;
                stroke = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1, new float[]{10F}, 0.0F);
            }
            else {
                stroke = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1);;
            }
            travelTime += sqrt * travelMultiplier;
            g2d.setStroke(stroke);
            g2d.drawLine(mouseEvent.getX(), mouseEvent.getY(), lastClick.getX(), lastClick.getY());
        }
        if (tripInfoForm != null) {
            tripInfoForm.setTravelInfo(distance, travelTime);
        }
        g2d.setColor(Color.WHITE);
/*
        int r = 100;

        *//*
            2      3

        1             4

           6        5

         *//*

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
                if(k % 2 == 0) {
                    hex.translate(r, r);
                }
                else {
                    hex.translate(-r, -r);
                }
                g2d.drawPolygon(hex);
                hex.translate(r * 2, 0);
            }

        }*/
        // Restore original transform
        g2d.setTransform(originalTransform);
    }

    public void panImage(double x, double y) {
        this.x = x;
        this.y = y;
        super.repaint();
    }

    public void zoomImage(int i) {
        /*zoom += (i / zoomIncrement);
        if (zoom > MAX_ZOOM) {
            zoom = MAX_ZOOM;
        }
        if (zoom < MIN_ZOOM) {
            zoom = MIN_ZOOM;
        }
        super.repaint();*/
    }

    public void addClick(MouseEvent e, double xModifier, double yModifier) {
        MouseEvent newEvent = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX() + (int)(image.getWidth() * xModifier) , e.getY() + (int)(image.getHeight() * yModifier), e.getClickCount(), e.isPopupTrigger(), e.getButton());
        clicks.add(newEvent);
        super.repaint();
    }

    public void setTripInfoForm(TripInfoForm tripInfoForm) {
        this.tripInfoForm = tripInfoForm;
    }

    public void removeLastClick() {
        if(!clicks.isEmpty()) {
            clicks.remove(clicks.size() - 1);
            super.repaint();
        }
    }

    public void clearClicks() {
        clicks.clear();
        super.repaint();
    }
}
