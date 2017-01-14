import javax.swing.*;
import java.text.DecimalFormat;

public class TripInfoForm {
    private JLabel distanceLabel;
    private JLabel travelTimeLabel;
    private JPanel infoPanel;
    private static final DecimalFormat format = new DecimalFormat("0.00");
    public JPanel getInfoPanel() {
        return infoPanel;
    }

    public void setTravelInfo(double distance, double travelTime) {
        distanceLabel.setText("Distance: " + format.format(distance) + " miles");
        travelTimeLabel.setText("Travel Time: " + format.format(travelTime) + " days");
    }
}
