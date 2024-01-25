package ramacciotti.beans_8_puzzle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    private PropertyChangeSupport propertyChange = new PropertyChangeSupport(this);

    public EightController() {
        this.setText("START");
//        propertyChange = new PropertyChangeSupport(this);
    }

    private int getPosOfLabel(int label) {
        for (int i = 0; i < 9; i++) {
            if (labels[i] == label) {
                return i;
            }
        }
        return -1;
    }

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

    public void handleLabel(PropertyChangeEvent evt) throws PropertyVetoException {
        System.out.println("CTRL executing handle label");
        int oldLabel = (int) evt.getOldValue();
        int newLabel = (int) evt.getNewValue();
        System.out.println("CTRL moving " + oldLabel + " to " + newLabel);

        int pos1 = getPosOfLabel(oldLabel);
        int pos2 = getPosOfLabel(newLabel);
        if (oldLabel == hole || !arePosAdjacent(pos1, pos2)) {
            this.setText("KO");
            throw new PropertyVetoException("Cannot switch tiles", evt);
        }

        this.setText("OK");
        this.myFirePropertyChange(evt.getPropertyName(), pos1, pos2);

        int temp = labels[pos1];
        labels[pos1] = labels[pos2];
        labels[pos2] = temp;

    }

    public void handleFlip(PropertyChangeEvent evt) throws PropertyVetoException {
        System.out.println("CTRL executing handle flip");
        if (labels[8] != hole) {
            throw new PropertyVetoException("Hole is not in last position", evt);
        }
        int temp = labels[0];
        labels[0] = labels[1];
        labels[1] = temp;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        System.out.println("CTRL executing vetoable change");
        switch (evt.getPropertyName()) {
            case "label" ->
                this.handleLabel(evt);
            case "flip" ->
                this.handleFlip(evt);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("CTRL executing property change");
        String propertyName = evt.getPropertyName();
        if (propertyName.equals("restart")) {
            labels = (int[]) evt.getNewValue();
            System.out.println(Arrays.toString(labels));
        }
    }

    public void myAddPropertyChangeListener(PropertyChangeListener l) {
        propertyChange.addPropertyChangeListener(l);
    }

    public void myRemovePropertyChangeListener(PropertyChangeListener l) {
        propertyChange.removePropertyChangeListener(l);
    }

    public void myFirePropertyChange(String propertyName, Object oldValue, Object newValue) {
        System.out.println("CTRL executing my fire property change");
        PropertyChangeEvent evt = new PropertyChangeEvent(propertyChange, propertyName, oldValue, newValue);
        for (PropertyChangeListener l : propertyChange.getPropertyChangeListeners()) {
            l.propertyChange(evt);
        }
    }

}
