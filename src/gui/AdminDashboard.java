package gui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java 
 */

import data.DataStore;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 */
public class AdminDashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName());

    private final String username;
    private JLabel lblStudentsCount, lblCoursesCount, lblEnrolCount, lblUnregCount;
    private DefaultTableModel studentModel, enrolModel, moduleModel;
    private DefaultTableModel allStudentsModel, allSubjectsModel;

    public AdminDashboard(String username) {
        this.username = username;
        initComponents();
        setupTables();
        buildCards();
        wireExtras();
        lblUser.setText(username);
        jLabel4.setText("Welcome back, " + username);
        setMinimumSize(new java.awt.Dimension(1000, 650));
        pack();
        setLocationRelativeTo(null);
        refreshAll();
        loadAllStudents();
        loadAllSubjects();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    // ---------- setup ----------
    private DefaultTableModel readOnly(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

   private void setupTables() {
       allSubjectsModel = readOnly(
        "Subject Code",
        "Subject Name",
        "Programme",
        "Lecturer",
        "Students Enrolled"
);
    studentModel = readOnly(
            "Student Number", "Full Name", "Email", "Gender", "Date of Birth"
    );
    allStudentsModel = readOnly(
        "Student Number", "Full Name", "Email", "Gender", "Date of Birth");

    enrolModel = readOnly(
            "Student Number", "Full Name", "Subject Code", "Subject", "Grade"
    );

    moduleModel = readOnly(
            "Subject Code", "Subject", "Lecturer", "Students Enrolled"
    );

    tblStudents.setModel(studentModel);
    tblEnrolments.setModel(enrolModel);
    tblCourses.setModel(moduleModel);
    tblAllStudents.setModel(allStudentsModel);
    tblAllSubjects.setModel(allSubjectsModel);

    for (javax.swing.JTable t : new javax.swing.JTable[]{
            tblStudents, tblEnrolments, tblCourses, tblAllStudents,tblAllSubjects
    }) {
        t.setRowHeight(30);
        t.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
        t.setAutoCreateRowSorter(true);
    }
}

    private void wireExtras() {
        btnRegister.addActionListener(this::btnRegisterActionPerformed);
        btnDeregister.addActionListener(this::btnDeregisterActionPerformed);
        txtSearch.addActionListener(this::btnSearchActionPerformed);
        // click a row = fill the Register / Deregister boxes
        tblStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblStudents.getSelectedRow();
                if (r >= 0) txtRegStudent.setText(tblStudents.getValueAt(r, 0).toString());
            }
        });
        tblEnrolments.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblEnrolments.getSelectedRow();
                if (r < 0) return;
                txtRegStudent.setText(tblEnrolments.getValueAt(r, 0).toString());
                txtRegCourse.setText(tblEnrolments.getValueAt(r, 2).toString());
            }
        });
        tblCourses.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblCourses.getSelectedRow();
                if (r >= 0) txtRegCourse.setText(tblCourses.getValueAt(r, 0).toString());
            }
        });
    }

    // ---------- cards ----------
    private void buildCards() {
        pnlCards.setLayout(new java.awt.GridLayout(1, 4, 12, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(makeCard("Students", lblStudentsCount = new JLabel("0")));
        pnlCards.add(makeCard("Modules", lblCoursesCount = new JLabel("0")));
        pnlCards.add(makeCard("Enrolments", lblEnrolCount = new JLabel("0")));
        pnlCards.add(makeCard("Unregistered", lblUnregCount = new JLabel("0")));
    }

    private JPanel makeCard(String title, JLabel value) {
        JPanel card = new JPanel(new java.awt.GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(220, 229, 240)),
                javax.swing.BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        JLabel t = new JLabel(title);
        t.setForeground(new Color(91, 107, 128));
        value.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        value.setForeground(new Color(10, 46, 92));
        card.add(t);
        card.add(value);
        return card;
    }

    // ---------- loading ----------
    private boolean matchesSearch(String studentNumber) {
        String q = txtSearch.getText().trim().toLowerCase();
        return q.isEmpty() || studentNumber.toLowerCase().contains(q);
    }

    private int countUnregistered() {
        int n = 0;
        for (Student s : DataStore.students) {
            boolean found = false;
            for (Enrolment e : DataStore.enrolments) {
                if (e.getStudentNumber().equals(s.getStudentNumber())) { found = true; break; }
            }
            if (!found) n++;
        }
        return n;
    }

    private String fullName(String studentNumber) {
        Student s = DataStore.findStudent(studentNumber);
        return s == null ? "(unknown)" : s.getFirstName() + " " + s.getLastName();
    }

    private void refreshAll() {
        studentModel.setRowCount(0);
        for (Student s : DataStore.students) {
            if (!matchesSearch(s.getStudentNumber())) continue;
            studentModel.addRow(new Object[]{s.getStudentNumber(),
                s.getFirstName() + " " + s.getLastName(), s.getEmail(), s.getGender(), s.getDob()});
        }
        enrolModel.setRowCount(0);
        for (Enrolment e : DataStore.enrolments) {
            if (!matchesSearch(e.getStudentNumber())) continue;
            Subject m = DataStore.findModule(e.getModuleCode());
            String grade = DataStore.hasGrade(e.getStudentNumber(), e.getModuleCode())
                    ? String.format("%.1f%%", e.getGrade()) : "Not graded";
            enrolModel.addRow(new Object[]{e.getStudentNumber(), fullName(e.getStudentNumber()),
                e.getModuleCode(), m == null ? "(unknown)" : m.getName(), grade});
        }
        moduleModel.setRowCount(0);
        for (Subject m : DataStore.subjects) {
            moduleModel.addRow(new Object[]{m.getCode(), m.getName(),
                m.getLecturerUsername(), DataStore.countEnrolled(m.getCode())});
        }
        lblStudentsCount.setText(String.valueOf(DataStore.students.size()));
        lblCoursesCount.setText(String.valueOf(DataStore.subjects.size()));
        lblEnrolCount.setText(String.valueOf(DataStore.enrolments.size()));
        lblUnregCount.setText(String.valueOf(countUnregistered()));
    }
    private void loadAllStudents() {
    allStudentsModel.setRowCount(0);

    for (Student s : DataStore.students) {
        allStudentsModel.addRow(new Object[]{
            s.getStudentNumber(),
            s.getFirstName() + " " + s.getLastName(),
            s.getEmail(),
            s.getGender(),
            s.getDob()
        });
    }
}
    private void loadAllSubjects() {
    allSubjectsModel.setRowCount(0);

    for (Subject subject : DataStore.subjects) {

        Course programme = DataStore.findProgramme(
                subject.getCourseCode()
        );

        String programmeName = programme == null
                ? "(unknown programme)"
                : programme.getName();

        allSubjectsModel.addRow(new Object[]{
            subject.getCode(),
            subject.getName(),
            programmeName,
            subject.getLecturerUsername(),
            DataStore.countEnrolled(subject.getCode())
        });
    }
}
    // ---------- helpers ----------
    private void warn(String text) {
        JOptionPane.showMessageDialog(this, text, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String action, Exception ex) {
        logger.log(java.util.logging.Level.SEVERE, action + " failed", ex);
        JOptionPane.showMessageDialog(this, "Could not " + action + ". Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Asks for the demographic details of a NEW student. Returns null if cancelled. */
    private String[] askNewStudentDetails(String studentNumber) {
        JTextField first = new JTextField(16), last = new JTextField(16),
                   email = new JTextField(16), dob = new JTextField(16);
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JPanel form = new JPanel(new java.awt.GridLayout(0, 2, 8, 8));
        form.add(new JLabel("First name:"));    form.add(first);
        form.add(new JLabel("Last name:"));     form.add(last);
        form.add(new JLabel("Email:"));         form.add(email);
        form.add(new JLabel("Gender:"));        form.add(gender);
        form.add(new JLabel("Date of birth:")); form.add(dob);
        form.add(new JLabel("(yyyy-mm-dd)"));   form.add(new JLabel(""));
        int ok = JOptionPane.showConfirmDialog(this, form,
                "New student " + studentNumber + " - enter details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return null;
        return new String[]{first.getText(), last.getText(), email.getText(),
            (String) gender.getSelectedItem(), dob.getText()};
    }

    private String describe(Student s) {
        return s.getStudentNumber() + " - " + s.getFirstName() + " " + s.getLastName()
                + "\nEmail: " + s.getEmail() + "\nGender: " + s.getGender()
                + "\nDate of birth: " + s.getDob();
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtRegCourse = new javax.swing.JTextField();
        txtRegStudent = new javax.swing.JTextField();
        btnDeregister = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlCards = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        scrAllStudents = new javax.swing.JScrollPane();
        tblAllStudents = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scrEnrolment = new javax.swing.JScrollPane();
        tblEnrolments = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        scrAllSubjects = new javax.swing.JScrollPane();
        tblAllSubjects = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlHeader.setBackground(new java.awt.Color(1, 130, 196));
        pnlHeader.setForeground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images.jpg"))); // NOI18N
        lblTitle.setText("Student Records");

        btnLogout.setBackground(new java.awt.Color(1, 130, 196));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Log out");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(1, 130, 196));
        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTitle)
                .addGap(40, 40, 40)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSearch)
                .addGap(18, 18, 18)
                .addComponent(lblUser)
                .addGap(18, 18, 18)
                .addComponent(btnLogout)
                .addGap(18, 18, 18))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLogout)
                    .addComponent(btnSearch)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        tabs.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jPanel1.setBackground(new java.awt.Color(240, 244, 250));

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student No.", "Name", "Email", "Gender", "Date of Birth"
            }
        ));
        jScrollPane1.setViewportView(tblStudents);

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Module Code", "Module", "Lecture", "Registered"
            }
        ));
        jScrollPane2.setViewportView(tblCourses);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtRegCourse.addActionListener(this::txtRegCourseActionPerformed);

        txtRegStudent.addActionListener(this::txtRegStudentActionPerformed);

        btnDeregister.setText("Deregister");
        btnDeregister.addActionListener(this::btnDeregisterActionPerformed);

        btnRegister.setText("Register");
        btnRegister.addActionListener(this::btnRegisterActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Register / Deregister");

        jLabel2.setText("Student Number");

        jLabel3.setText("Module Code");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel3)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRegStudent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                            .addComponent(txtRegCourse, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeregister)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeregister, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout pnlCardsLayout = new javax.swing.GroupLayout(pnlCards);
        pnlCards.setLayout(pnlCardsLayout);
        pnlCardsLayout.setHorizontalGroup(
            pnlCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlCardsLayout.setVerticalGroup(
            pnlCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Welcome back, Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                        .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabs.addTab("Dashboard", jPanel1);

        tblAllStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student Number", "Name", "Email", "Gender", "Date of Birth"
            }
        ));
        scrAllStudents.setViewportView(tblAllStudents);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrAllStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrAllStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("All Students", jPanel4);

        tblEnrolments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student No", "Name", "Course Code", "Course"
            }
        ));
        scrEnrolment.setViewportView(tblEnrolments);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrEnrolment, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(scrEnrolment, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Enrolments", jPanel3);

        tblAllSubjects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Module Code", "Module", "Programme", "Lecturer", "Students Enrolled"
            }
        ));
        scrAllSubjects.setViewportView(tblAllSubjects);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrAllSubjects, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrAllSubjects, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        tabs.addTab("Modules", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabs))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtRegStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRegStudentActionPerformed
        txtRegCourse.requestFocusInWindow();
    }//GEN-LAST:event_txtRegStudentActionPerformed

    private void txtRegCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRegCourseActionPerformed
        btnRegister.doClick();
    }//GEN-LAST:event_txtRegCourseActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            refreshAll();
            if (!txtSearch.getText().trim().isEmpty() && tblStudents.getRowCount() == 0) {
                warn("No student number matching \"" + txtSearch.getText().trim() + "\".");
            }
        } catch (Exception ex) {
            showError("search", ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        try {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                dispose();
            }
        } catch (Exception ex) {
            showError("log out", ex);
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
         try {
            String sn = txtRegStudent.getText().trim();
            String code = txtRegCourse.getText().trim().toUpperCase();
            if (sn.isEmpty() || code.isEmpty()) {
                warn("Enter a student number and a module code.");
                return;
            }
            if (!sn.matches("\\d{9}")) {
                warn("Student number must contain exactly 9 digits.");
                return;
            }
            if (DataStore.findModule(code) == null) {
                warn("Module not found. Enter a valid module code.");
                return;
            }
            boolean isNew = DataStore.findStudent(sn) == null;
            if (isNew) {
                String[] d = askNewStudentDetails(sn);
                if (d == null) return;                       // cancelled
                String err = DataStore.addStudent(sn, d[0], d[1], d[2], d[3], d[4]);
                if (err != null) { warn(err); return; }
            }
            String err = DataStore.register(sn, code);
            if (err != null) { warn(err); refreshAll(); return; }
            JOptionPane.showMessageDialog(this, (isNew ? "New student added and registered for " : "Registered for ")
                    + code + ":\n\n" + describe(DataStore.findStudent(sn)));
            txtRegStudent.setText("");
            txtRegCourse.setText("");
            refreshAll();
        } catch (Exception ex) {
            showError("register the student", ex);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnDeregisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeregisterActionPerformed
        // TODO add your handling code here:
        try {
            String sn = txtRegStudent.getText().trim();
            String code = txtRegCourse.getText().trim().toUpperCase();
            if (sn.isEmpty() || code.isEmpty()) {
                warn("Enter a student number and a module code.");
                return;
            }
            Student s = DataStore.findStudent(sn);
            if (s == null) { warn("Student not found."); return; }
            if (DataStore.findEnrolment(sn, code) == null) {
                warn("This student is not registered for " + code + ".");
                return;
            }
            String text = "Deregister this student from " + code + "?\n\n" + describe(s);
            if (DataStore.hasGrade(sn, code)) text += "\n\nThey already have a grade; it will be deleted.";
            int choice = JOptionPane.showConfirmDialog(this, text, "Deregister", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;
            String err = DataStore.deregister(sn, code);
            if (err != null) { warn(err); return; }
            JOptionPane.showMessageDialog(this, "Student deregistered.");
            txtRegStudent.setText("");
            txtRegCourse.setText("");
            refreshAll();
        } catch (Exception ex) {
            showError("deregister the student", ex);
        }
    }//GEN-LAST:event_btnDeregisterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeregister;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JScrollPane scrAllStudents;
    private javax.swing.JScrollPane scrAllSubjects;
    private javax.swing.JScrollPane scrEnrolment;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblAllStudents;
    private javax.swing.JTable tblAllSubjects;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTable tblEnrolments;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtRegCourse;
    private javax.swing.JTextField txtRegStudent;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
