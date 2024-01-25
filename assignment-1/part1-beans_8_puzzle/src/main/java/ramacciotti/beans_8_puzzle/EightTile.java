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

    /**
     * Update the label, the background and the text of the tile
     *
     * @param newLabel
     */
    private void updateTile(int newLabel) {
        this.label = newLabel;
        this.updateBackgroundAndText();
    }

    /**
     * Upgrade background and text, based on the value of label
     */
    private void updateBackgroundAndText() {
        if (this.label == 9) {
            this.setBackground(Color.GRAY);
            this.setText("");
        } else if (this.position == this.label) {
            this.setBackground(Color.GREEN);
            this.setText(String.valueOf(this.label));
        } else {
            this.setBackground(Color.YELLOW);
            this.setText(String.valueOf(this.label));
        }
    }

    /**
     * If not vetoed, move hole on this tile
     */
    public void click() {
        System.out.println("TILE clicked on " + this.label);
        try {
            this.fireVetoableChange("label", this.label, hole);
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
     * Property change triggered: - restart: get new label from labels array,
     * update tile - label: swap old label and new label
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("TILE property change");
        String propertyName = evt.getPropertyName();

        if (propertyName.equals("restart")) {

            int[] newLabels = (int[]) evt.getNewValue();
            this.updateTile(newLabels[position - 1]);

        } else if (propertyName.equals("label")) {
            // swap the labels oldlabel and newlabel

            int oldLabel = (int) evt.getOldValue();
            int newLabel = (int) evt.getNewValue();

            // if we are in the oldlabel, swap with new
            if (oldLabel == this.label) {
                this.updateTile(newLabel);
            } else // if we are in the newlabel, swap with old
            if (newLabel == this.label) {
                this.updateTile(oldLabel);
            }

        }
    }

}
