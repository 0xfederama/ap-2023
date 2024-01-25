package ramacciotti.beans_8_puzzle;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author federico
 */
public class EightTile extends JButton implements PropertyChangeListener {

    private int position;
    private int label;
    private final int hole = 9;

    public EightTile() {
    }

    public EightTile(int position) {
        this.position = position;
        this.label = position;
        this.updateBackgroundAndText();
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String getLabel() {
        return Integer.toString(label);
    }

    public void updateLabel(int newLabel) {
        label = newLabel;
        this.updateBackgroundAndText();
    }

    public void updateBackgroundAndText() {
        // Update background color and text based on label value
        if (label == 9) {
            this.setBackground(Color.GRAY);
            this.setText("");
        } else if (position == label) {
            this.setBackground(Color.GREEN);
            this.setText(String.valueOf(label));
        } else {
            this.setBackground(Color.YELLOW);
            this.setText(String.valueOf(label));
        }
    }

    /**
     * If not vetoed, move hole on this tile
     */
    public void click() {
        System.out.println("TILE clicked on " + label);
        try {
            this.fireVetoableChange("label", label, hole);

            this.updateLabel(hole);
            System.out.println("No one vetoed");
        } catch (PropertyVetoException e) {
            this.flashTile();
        }
    }

    /**
     * Flash the tile with red background for 0.3 sec
     */
    private void flashTile() {
        System.out.println("TILE executing flash");
        Color oldColor = this.getBackground();
        this.setBackground(Color.RED);

        Timer timer = new Timer(200, e -> this.setBackground(oldColor));
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Execute restart event
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("TILE executing property change");
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("restart")) {
            int[] newLabels = (int[]) evt.getNewValue();
            label = newLabels[position - 1];
            this.updateBackgroundAndText();
        } 
    }
    
}
