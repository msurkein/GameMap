import javax.swing.*;
import java.awt.event.*;

public class MapForm {
    private JPanel mapPanel;
    private JScrollBar verticalScrollBar;
    private JScrollBar horizontalScrollBar;

    public MapForm() {
        addAdjustmentListener(horizontalScrollBar);
        addAdjustmentListener(verticalScrollBar);
        mapPanel.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                getMapPanel().zoomImage(e.getScrollAmount() * e.getWheelRotation());
            }
        });
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getMapPanel().addClick(e);
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
        frame.setContentPane(new MapForm().mapPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        mapPanel = new MapImage();
    }

    private MapImage getMapPanel() {
        return ((MapImage)mapPanel);
    }

}
