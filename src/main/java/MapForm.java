import javax.swing.*;
import java.awt.event.*;

public class MapForm {
    private JPanel mapPanel;
    private JScrollBar verticalScrollBar;
    private JScrollBar horizontalScrollBar;

    MapForm() {
        addAdjustmentListener(horizontalScrollBar);
        addAdjustmentListener(verticalScrollBar);
        mapPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                getMapPanel().zoomImage(e.getWheelRotation());
            }
        });
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                getMapPanel().addClick(e, (double)horizontalScrollBar.getValue() / (double)horizontalScrollBar.getMaximum(), (double)verticalScrollBar.getValue() / (double)verticalScrollBar.getMaximum());
            }
        });

    }

    private void addAdjustmentListener(JScrollBar scrollBar) {
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                getMapPanel().panImage((double)horizontalScrollBar.getValue() / (double)horizontalScrollBar.getMaximum(), (double)verticalScrollBar.getValue() / (double)verticalScrollBar.getMaximum());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MapForm");
        final MapForm mapForm = new MapForm();
        frame.setContentPane(mapForm.getMapPanel());
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    mapForm.getMapPanel().removeLastClick();
                }
                if(e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    mapForm.getMapPanel().clearClicks();
                }
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        JFrame distanceFrame = new JFrame("DistanceForm");
        TripInfoForm tripInfoForm = new TripInfoForm();
        distanceFrame.setContentPane(tripInfoForm.getInfoPanel());
        distanceFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        distanceFrame.pack();
        distanceFrame.setVisible(true);
        mapForm.getMapPanel().setTripInfoForm(tripInfoForm);
    }

    private void createUIComponents() {
        mapPanel = new MapImage();
    }

    private MapImage getMapPanel() {
        return ((MapImage)mapPanel);
    }

}
