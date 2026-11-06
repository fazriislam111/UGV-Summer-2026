package edu.barishal.ghat;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                System.err.println("Using the default Swing appearance: " + exception.getMessage());
            }
            new VesselManagerFrame().setVisible(true);
        });
    }
}
