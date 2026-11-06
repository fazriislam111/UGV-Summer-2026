package edu.barishal.ghat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VesselManagerFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final VesselDao vesselDao = new VesselDao();

    private final JComboBox<VesselType> typeComboBox = new JComboBox<>(VesselType.values());
    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField capacityField = new JTextField();
    private final JTextField routeField = new JTextField();
    private final JTextField fuelField = new JTextField("0");
    private final JTextField locationField = new JTextField();
    private final JComboBox<Vessel> vesselComboBox = new JComboBox<>();
    private final JTextArea behaviorOutput = new JTextArea(5, 50);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {"ID", "Name", "Type", "Capacity", "Route", "Fuel (L)", "Location"},
            0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable vesselTable = new JTable(tableModel);

    public VesselManagerFrame() {
        super("Barishal Launch Ghat Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        vesselTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vesselTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });
        add(createTopPanel(), BorderLayout.NORTH);
        add(new JScrollPane(vesselTable), BorderLayout.CENTER);
        add(createBehaviorPanel(), BorderLayout.SOUTH);

        loadVessels(false);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 6));
        addField(formPanel, "Vessel type", typeComboBox);
        addField(formPanel, "Vessel ID", idField);
        addField(formPanel, "Name", nameField);
        addField(formPanel, "Passenger capacity", capacityField);
        addField(formPanel, "Route", routeField);
        addField(formPanel, "Fuel level (liters)", fuelField);
        addField(formPanel, "Current location", locationField);

        JButton addButton = new JButton("Add Vessel");
        addButton.addActionListener(event -> addVessel());
        JButton loadButton = new JButton("Load Vessels");
        loadButton.addActionListener(event -> loadVessels(true));
        JButton editButton = new JButton("Save Changes");
        editButton.addActionListener(event -> editSelectedVessel());
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(event -> deleteSelectedVessel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        topPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 0, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        return topPanel;
    }

    private JPanel createBehaviorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 6));
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Selected vessel:"));
        vesselComboBox.setPrototypeDisplayValue(
                new Launch("EXAMPLE", "Example vessel", 1, "Barishal - Dhaka", 0, "Barishal"));
        selectionPanel.add(vesselComboBox);

        JButton testButton = new JButton("Test Interface Behavior (+10 L)");
        testButton.addActionListener(event -> testSelectedVessel());
        selectionPanel.add(testButton);

        behaviorOutput.setEditable(false);
        behaviorOutput.setLineWrap(true);
        behaviorOutput.setWrapStyleWord(true);
        behaviorOutput.setText(
                "Choose a vessel, then test its shared Vessel and interface behavior.");
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        panel.add(selectionPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(behaviorOutput), BorderLayout.CENTER);
        return panel;
    }

    private void addField(JPanel panel, String label, java.awt.Component field) {
        panel.add(new JLabel(label + ":"));
        panel.add(field);
    }

    private void addVessel() {
        try {
            Vessel vessel = VesselFactory.create(
                    (VesselType) typeComboBox.getSelectedItem(),
                    idField.getText(),
                    nameField.getText(),
                    Integer.parseInt(capacityField.getText().trim()),
                    routeField.getText(),
                    Double.parseDouble(fuelField.getText().trim()),
                    locationField.getText());
            vesselDao.insert(vessel);
            loadVessels(false);
            selectVessel(vessel.getVesselId());
            clearForm();
            JOptionPane.showMessageDialog(this, "Vessel added successfully.");
        } catch (NumberFormatException exception) {
            showError("Capacity must be a whole number and fuel must be a number.");
        } catch (IllegalArgumentException | SQLException exception) {
            showError(exception.getMessage());
        }
    }

    private void editSelectedVessel() {
        int selectedViewRow = vesselTable.getSelectedRow();
        if (selectedViewRow < 0) {
            showError("Select a vessel in the table before editing.");
            return;
        }

        int selectedRow = vesselTable.convertRowIndexToModel(selectedViewRow);
        String originalVesselId = tableModel.getValueAt(selectedRow, 0).toString();
        try {
            Vessel vessel = readVesselFromForm();
            vesselDao.update(originalVesselId, vessel);
            loadVessels(false);
            selectTableVessel(vessel.getVesselId());
            JOptionPane.showMessageDialog(this, "Vessel updated successfully.");
        } catch (NumberFormatException exception) {
            showError("Capacity must be a whole number and fuel must be a number.");
        } catch (IllegalArgumentException | SQLException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteSelectedVessel() {
        int selectedViewRow = vesselTable.getSelectedRow();
        if (selectedViewRow < 0) {
            showError("Select a vessel in the table before deleting.");
            return;
        }

        int selectedRow = vesselTable.convertRowIndexToModel(selectedViewRow);
        String vesselId = tableModel.getValueAt(selectedRow, 0).toString();
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Delete vessel " + vesselId + " from the database?",
                "Confirm deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            vesselDao.delete(vesselId);
            loadVessels(false);
            clearForm();
            JOptionPane.showMessageDialog(this, "Vessel deleted successfully.");
        } catch (SQLException exception) {
            showError(exception.getMessage());
        }
    }

    private Vessel readVesselFromForm() {
        return VesselFactory.create(
                (VesselType) typeComboBox.getSelectedItem(),
                idField.getText(),
                nameField.getText(),
                Integer.parseInt(capacityField.getText().trim()),
                routeField.getText(),
                Double.parseDouble(fuelField.getText().trim()),
                locationField.getText());
    }

    private void loadVessels(boolean showSuccess) {
        try {
            List<Vessel> vessels = vesselDao.findAll();
            DefaultComboBoxModel<Vessel> comboModel = new DefaultComboBoxModel<>();
            tableModel.setRowCount(0);
            for (Vessel vessel : vessels) {
                comboModel.addElement(vessel);
                tableModel.addRow(new Object[] {
                    vessel.getVesselId(),
                    vessel.getName(),
                    vessel.getVesselType().getDisplayName(),
                    vessel.getCapacity(),
                    vessel.getRoute(),
                    String.format("%.2f", vessel.getFuelLevel()),
                    vessel.getCurrentLocation()
                });
            }
            vesselComboBox.setModel(comboModel);
            if (showSuccess) {
                JOptionPane.showMessageDialog(
                        this, "Loaded " + vessels.size() + " vessel(s) from the database.");
            }
        } catch (SQLException | IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void testSelectedVessel() {
        Vessel vessel = (Vessel) vesselComboBox.getSelectedItem();
        if (vessel == null) {
            showError("Load or add a vessel before testing its behavior.");
            return;
        }
        if (!(vessel instanceof Refuelable refuelable)
                || !(vessel instanceof Trackable trackable)) {
            showError("This vessel does not support the required interfaces.");
            return;
        }

        try {
            refuelable.refuel(10.0);
            vesselDao.updateOperationalData(vessel);
            behaviorOutput.setText(
                    "Shared Vessel method:\n" + vessel.getSummary()
                            + "\n\nRefuelable default method: added 10.00 L."
                            + "\nTrackable default method: " + trackable.getTrackingStatus());
            loadVessels(false);
            selectVessel(vessel.getVesselId());
        } catch (IllegalArgumentException | SQLException exception) {
            showError(exception.getMessage());
        }
    }

    private void selectVessel(String vesselId) {
        for (int index = 0; index < vesselComboBox.getItemCount(); index++) {
            Vessel vessel = vesselComboBox.getItemAt(index);
            if (vessel.getVesselId().equals(vesselId)) {
                vesselComboBox.setSelectedIndex(index);
                return;
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedViewRow = vesselTable.getSelectedRow();
        if (selectedViewRow < 0) {
            return;
        }

        int selectedRow = vesselTable.convertRowIndexToModel(selectedViewRow);
        String vesselId = tableModel.getValueAt(selectedRow, 0).toString();
        for (int index = 0; index < vesselComboBox.getItemCount(); index++) {
            Vessel vessel = vesselComboBox.getItemAt(index);
            if (vessel.getVesselId().equals(vesselId)) {
                typeComboBox.setSelectedItem(vessel.getVesselType());
                idField.setText(vessel.getVesselId());
                nameField.setText(vessel.getName());
                capacityField.setText(Integer.toString(vessel.getCapacity()));
                routeField.setText(vessel.getRoute());
                fuelField.setText(Double.toString(vessel.getFuelLevel()));
                locationField.setText(vessel.getCurrentLocation());
                vesselComboBox.setSelectedItem(vessel);
                return;
            }
        }
    }

    private void selectTableVessel(String vesselId) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (tableModel.getValueAt(row, 0).toString().equals(vesselId)) {
                int viewRow = vesselTable.convertRowIndexToView(row);
                vesselTable.setRowSelectionInterval(viewRow, viewRow);
                return;
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        capacityField.setText("");
        routeField.setText("");
        fuelField.setText("0");
        locationField.setText("");
        idField.requestFocusInWindow();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message == null || message.isBlank() ? "An unexpected error occurred." : message,
                "Unable to complete operation",
                JOptionPane.ERROR_MESSAGE);
    }
}
