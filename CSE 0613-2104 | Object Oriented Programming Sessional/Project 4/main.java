import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class main {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/";

    private static final String DB_NAME = "barishal_doctor_db";

    private static final String DB_USER = "root";

    private static final String DB_PASSWORD = "iamfazri111";


    private Hospital hospital;
    private JFrame frame;

    private JComboBox<Doctor> doctorComboBox;
    private JTextField patientNameField;

    private JTextField dateField;
    private JTextField timeField;

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    private JLabel statusLabel;


    public main() {

        hospital = new Hospital(
                "Barishal General Hospital");

        SwingUtilities.invokeLater(this::createGUI);
        startBackgroundInitialization();
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            main app = new main();

        });

    }


    private void initializeDatabase() {

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver");

            System.out.println(
                    "MySQL Driver Loaded");

        } catch (ClassNotFoundException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "MySQL Connector/J missing");

            System.exit(0);
        }

        createDatabase();

        createTables();

        insertSampleData();

    }

    private void createDatabase() {

        try (Connection conn = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);

                Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE DATABASE IF NOT EXISTS "
                            + DB_NAME);

        } catch (SQLException e) {

            showError(e);
        }

    }

    private void createTables() {

        String[] sql = {

                """
                        CREATE TABLE IF NOT EXISTS departments(
                            department_id INT AUTO_INCREMENT PRIMARY KEY,
                            department_name VARCHAR(100)
                        )
                        """,

                """
                        CREATE TABLE IF NOT EXISTS doctors(
                            doctor_id INT AUTO_INCREMENT PRIMARY KEY,
                            doctor_name VARCHAR(100),
                            specialty VARCHAR(100),
                            department_id INT,
                            FOREIGN KEY(department_id)
                            REFERENCES departments(department_id)
                        )
                        """,

                """
                        CREATE TABLE IF NOT EXISTS patients(
                            patient_id INT AUTO_INCREMENT PRIMARY KEY,
                            patient_name VARCHAR(100),
                            phone VARCHAR(20)
                        )
                        """,

                """
                        CREATE TABLE IF NOT EXISTS appointments(

                            appointment_id INT AUTO_INCREMENT PRIMARY KEY,

                            doctor_id INT,

                            patient_id INT,

                            appointment_date VARCHAR(20),

                            appointment_time VARCHAR(20),

                            notes VARCHAR(200),


                            FOREIGN KEY(doctor_id)
                            REFERENCES doctors(doctor_id),


                            FOREIGN KEY(patient_id)
                            REFERENCES patients(patient_id)

                        )
                        """
        };

        try (Connection conn = getConnection();

                Statement stmt = conn.createStatement()) {

            for (String s : sql) {

                stmt.execute(s);

            }

        } catch (SQLException e) {

            showError(e);

        }

    }

    private void insertSampleData() {

        try (Connection conn = getConnection()) {

            ResultSet rs = conn.createStatement()
                    .executeQuery(
                            "SELECT COUNT(*) FROM doctors");

            rs.next();

            if (rs.getInt(1) == 0) {

                PreparedStatement dept = conn.prepareStatement(

                        "INSERT INTO departments(department_name) VALUES(?)",

                        Statement.RETURN_GENERATED_KEYS);

                dept.setString(
                        1,
                        "General Medicine");

                dept.executeUpdate();

                ResultSet key = dept.getGeneratedKeys();

                key.next();

                int departmentID = key.getInt(1);

                PreparedStatement doc = conn.prepareStatement(
                        """
                                INSERT INTO doctors
                                (doctor_name,specialty,department_id)
                                VALUES(?,?,?)
                                """);

                doc.setString(
                        1,
                        "Dr. Rahim");

                doc.setString(
                        2,
                        "Cardiology");

                doc.setInt(
                        3,
                        departmentID);

                doc.executeUpdate();

                doc.setString(
                        1,
                        "Dr. Sultana");

                doc.setString(
                        2,
                        "Pediatrics");

                doc.executeUpdate();

                PreparedStatement patient = conn.prepareStatement(

                        """
                                INSERT INTO patients
                                (patient_name,phone)
                                VALUES(?,?)
                                """);

                patient.setString(
                        1,
                        "Rafiq Ahmed");

                patient.setString(
                        2,
                        "01711111111");

                patient.executeUpdate();

                patient.setString(
                        1,
                        "Mina Akter");

                patient.setString(
                        2,
                        "01822222222");

                patient.executeUpdate();

            }

        } catch (SQLException e) {

            showError(e);

        }
    }


    private void createGUI() {
        frame = new JFrame(hospital.getName() + " Appointment System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 600));
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JTextArea report = new JTextArea(buildRelationshipReport());
        report.setEditable(false);
        JScrollPane reportScroll = new JScrollPane(report);
        reportScroll.setBorder(BorderFactory.createTitledBorder("System Relationship Report"));
        mainPanel.add(reportScroll, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Book an Appointment"));

        formPanel.add(new JLabel("Doctor:"));
        doctorComboBox = new JComboBox<>();
        formPanel.add(doctorComboBox);

        formPanel.add(new JLabel("Patient:"));
        patientNameField = new JTextField();
        formPanel.add(patientNameField);

        formPanel.add(new JLabel("Date YYYY-MM-DD:"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Time HH:MM:"));
        timeField = new JTextField();
        formPanel.add(timeField);

        JButton bookButton = new JButton("Book Appointment");
        bookButton.addActionListener(e -> bookAppointment());
        formPanel.add(bookButton);

        statusLabel = new JLabel("Ready");
        formPanel.add(statusLabel);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        String[] columns = { "ID", "Doctor", "Patient", "Date", "Time" };
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(appointmentTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Appointment List"));
        mainPanel.add(tableScroll, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void startBackgroundInitialization() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                initializeDatabase();
                return null;
            }

            @Override
            protected void done() {
                loadDoctorsAndPatientsAsync();
                loadAppointmentsAsync();
            }
        }.execute();
    }


    private void loadDoctorsAndPatientsAsync() {
        new SwingWorker<List<Doctor>, Void>() {
            List<Patient> patients;

            @Override
            protected List<Doctor> doInBackground() throws Exception {
                try (Connection conn = getConnection()) {
                    List<Doctor> doctors = readDoctors(conn);
                    return doctors;
                }
            }

            @Override
            protected void done() {
                try {
                    List<Doctor> doctors = get();
                    if (doctorComboBox != null) {
                        doctorComboBox.removeAllItems();
                        for (Doctor d : doctors) {
                            doctorComboBox.addItem(d);
                        }
                    }

                    if (statusLabel != null) {
                        statusLabel.setText("Doctors loaded");
                    }
                } catch (Exception e) {
                    showError(e);
                }
            }
        }.execute();
    }


    private void loadAppointmentsAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                List<Object[]> rows = new ArrayList<>();

                try (Connection conn = getConnection();

                        PreparedStatement ps = conn.prepareStatement(
                                """
                                        SELECT
                                        a.appointment_id,
                                        d.doctor_name,
                                        p.patient_name,
                                        a.appointment_date,
                                        a.appointment_time

                                        FROM appointments a

                                        JOIN doctors d
                                        ON a.doctor_id=d.doctor_id

                                        JOIN patients p
                                        ON a.patient_id=p.patient_id

                                        ORDER BY a.appointment_id
                                        """);

                        ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        rows.add(new Object[] {
                                rs.getInt("appointment_id"),
                                rs.getString("doctor_name"),
                                rs.getString("patient_name"),
                                rs.getString("appointment_date"),
                                rs.getString("appointment_time")
                        });
                    }
                }

                return rows;
            }

            @Override
            protected void done() {
                try {
                    List<Object[]> rows = get();
                    if (tableModel != null) {
                        tableModel.setRowCount(0);
                        for (Object[] row : rows) {
                            tableModel.addRow(row);
                        }
                    }
                } catch (Exception e) {
                    showError(e);
                }
            }
        }.execute();
    }


    private void bookAppointment() {

        Doctor doctor = (Doctor) doctorComboBox.getSelectedItem();
        String patientName = patientNameField.getText().trim();

        if (doctor == null || patientName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Select doctor and enter patient name");

            return;

        }

        String date = dateField.getText();
        String time = timeField.getText();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (Connection conn = getConnection();
                        PreparedStatement ps = conn.prepareStatement(
                                """
                                        INSERT INTO appointments
                                        (
                                        doctor_id,
                                        patient_id,
                                        appointment_date,
                                        appointment_time,
                                        notes
                                        )
                                        VALUES(?,?,?,?,?)
                                        """)) {
                    ps.setInt(1, doctor.id);
                    ps.setInt(2, insertOrGetPatient(conn, patientName));
                    ps.setString(3, date);
                    ps.setString(4, time);
                    ps.setString(5, "Booked from Java");
                    ps.executeUpdate();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    if (statusLabel != null) {
                        statusLabel.setText("Appointment booked");
                    }
                    if (dateField != null) {
                        dateField.setText("");
                    }
                    if (timeField != null) {
                        timeField.setText("");
                    }
                    loadAppointmentsAsync();
                } catch (Exception e) {
                    showError(e);
                }
            }
        }.execute();

    }


    private Connection getConnection() throws SQLException {

        System.out.println("Connecting to MySQL...");

        Connection conn = DriverManager.getConnection(
                DB_URL
                        + DB_NAME
                        + "?useSSL=false"
                        + "&allowPublicKeyRetrieval=true"
                        + "&serverTimezone=UTC"
                        + "&connectTimeout=5000",
                DB_USER,
                DB_PASSWORD);

        System.out.println("MySQL Connected!");

        return conn;
    }


    private List<Doctor> readDoctors(Connection conn)
            throws SQLException {

        List<Doctor> list = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(
                """
                        SELECT
                        d.doctor_id,
                        d.doctor_name,
                        d.specialty,
                        dp.department_name

                        FROM doctors d

                        LEFT JOIN departments dp

                        ON d.department_id =
                        dp.department_id
                        """);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Department dept = new Department(
                    rs.getString(
                            "department_name"));

            Doctor doctor = new Doctor(

                    rs.getInt(
                            "doctor_id"),

                    rs.getString(
                            "doctor_name"),

                    rs.getString(
                            "specialty"),

                    dept

            );

            list.add(doctor);

        }

        return list;

    }


    private int insertOrGetPatient(Connection conn, String patientName) throws SQLException {
        try (PreparedStatement check = conn.prepareStatement(
                "SELECT patient_id FROM patients WHERE patient_name = ?")) {
            check.setString(1, patientName);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("patient_id");
                }
            }
        }

        try (PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO patients (patient_name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, patientName);
            insert.setString(2, "");
            insert.executeUpdate();
            try (ResultSet keys = insert.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not create patient record");
    }


    private String buildRelationshipReport() {

        return

        "Relationship Report\n\n"

                +

                "1. Composition:\n"
                +
                "Hospital contains Department objects.\n\n"

                +

                "2. Aggregation:\n"
                +
                "Department manages Doctor objects.\n\n"

                +

                "3. Association:\n"
                +
                "Patient connects with Appointment.\n\n"

                +

                "4. One-to-many:\n"
                +
                "One doctor can have many appointments.\n\n"

                +

                "5. Many-to-many:\n"
                +
                "Doctors and patients connect through appointments table.\n\n"

                +

                "6. Database:\n"
                +
                "Foreign keys maintain relationships.";

    }

    private void showError(Exception e) {

        e.printStackTrace();

        Runnable task = () -> JOptionPane.showMessageDialog(
                frame,
                e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);

        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }

    }


    static class Hospital {

        String name;

        List<Department> departments = new ArrayList<>();

        Hospital(String name) {

            this.name = name;

        }

        String getName() {

            return name;

        }

    }

    static class Department {

        String name;

        Department(String name) {

            this.name = name;

        }

        public String toString() {

            return name;

        }

    }

    static class Doctor {

        int id;

        String name;

        String specialty;

        Department department;

        List<Appointment> appointments = new ArrayList<>();

        Doctor(
                int id,
                String name,
                String specialty,
                Department department) {

            this.id = id;

            this.name = name;

            this.specialty = specialty;

            this.department = department;

        }

        public String toString() {

            return name
                    +
                    " - "
                    +
                    specialty;

        }

    }

    static class Patient {

        int id;

        String name;

        String phone;

        List<Appointment> appointments = new ArrayList<>();

        Patient(
                int id,
                String name,
                String phone) {

            this.id = id;

            this.name = name;

            this.phone = phone;

        }

        public String toString() {

            return name;

        }

    }

    static class Appointment {

        Doctor doctor;

        Patient patient;

        String date;

        String time;

        Appointment(

                Doctor doctor,

                Patient patient,

                String date,

                String time

        ) {

            this.doctor = doctor;

            this.patient = patient;

            this.date = date;

            this.time = time;

        }

    }

}