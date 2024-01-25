package ramacciotti.beans_8_puzzle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Arrays;
import javax.swing.JLabel;

/**
 *
 * @author federico
 */
public class EightController extends JLabel implements VetoableChangeListener, PropertyChangeListener {

    private final int hole = 9;
    private int[] labels;

    public EightController() {
        this.setText("START");
    }

    /**
     * Given the label, find the position of the label in the array
     *
     * @param label
     * @return index of the label in the array, -1 if not found
     */
    private int getPosOfLabel(int label) {
        for (int i = 0; i < 9; i++) {
            if (this.labels[i] == label) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Given two positions in the array, find if they're adjacent
     *
     * @param pos1
     * @param pos2
     * @return true if pos1 adj pos, false otherwise
     */
    private boolean arePosAdjacent(int pos1, int pos2) {
        switch (pos1) {
            case 0:
                return (pos2 == 1 || pos2 == 3);
            case 1:
                return (pos2 == 0 || pos2 == 2 || pos2 == 4);
            case 2:
                return (pos2 == 1 || pos2 == 5);
            case 3:
                return (pos2 == 0 || pos2 == 4 || pos2 == 6);
            case 4:
                return (pos2 == 1 || pos2 == 3 || pos2 == 5 || pos2 == 7);
            case 5:
                return (pos2 == 2 || pos2 == 4 || pos2 == 8);
            case 6:
                return (pos2 == 3 || pos2 == 7);
            case 7:
                return (pos2 == 4 || pos2 == 6 || pos2 == 8);
            case 8:
                return (pos2 == 5 || pos2 == 7);
        }
        return false;
    }

    /**
     * Handle change of label based on event
     *
     * @param evt
     * @throws PropertyVetoException if clicked on hole OR if old and hole are
     * not adjacent
     */
    private void handleLabel(PropertyChangeEvent evt) throws PropertyVetoException {
        int oldLabel = (int) evt.getOldValue();
        int newLabel = (int) evt.getNewValue();
        System.out.println("CTRL moving " + oldLabel + " to " + newLabel);

        int pos1 = getPosOfLabel(oldLabel);
        int pos2 = getPosOfLabel(newLabel);

        // veto if not ok
        if (oldLabel == hole || !arePosAdjacent(pos1, pos2)) {
            this.setText("KO");
            throw new PropertyVetoException("Cannot switch tiles", evt);
        }

        // set text and notify all listeners
        this.setText("OK");
        this.firePropertyChange(evt.getPropertyName(), pos1, pos2);

        int temp = this.labels[pos1];
        this.labels[pos1] = this.labels[pos2];
        this.labels[pos2] = temp;

    }

    /**
     * Handle flip based on event
     *
     * @param evt
     * @throws PropertyVetoException if hole is not in last position
     */
    private void handleFlip(PropertyChangeEvent evt) throws PropertyVetoException {
        if (this.labels[8] != hole) {
            System.out.println("CTRL cannot execute flip");
            throw new PropertyVetoException("Hole is not in last position", evt);
        }

        this.firePropertyChange("label", 0, 1);

        int temp = this.labels[0];
        this.labels[0] = this.labels[1];
        this.labels[1] = temp;
    }

    /**
     * Handle vetoable changes (label and flip)
     *
     * @param evt
     * @throws PropertyVetoException according to handle label and flip
     */
    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        System.out.println("CTRL executing vetoable change (label or flip)");
        switch (evt.getPropertyName()) {
            case "label" ->
                this.handleLabel(evt);
            case "flip" ->
                this.handleFlip(evt);
        }
    }

    /**
     * Handle property change (restart)
     *
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("CTRL executing property change");
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("restart")) {
            this.labels = (int[]) evt.getNewValue();
            System.out.println(Arrays.toString(this.labels));
        }
    }

}
