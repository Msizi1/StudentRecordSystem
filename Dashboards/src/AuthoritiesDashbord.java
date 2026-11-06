/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java 
 */

import data.DataStore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.*;

/**
 * 
 */
public class AuthoritiesDashbord extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AuthoritiesDashbord.class.getName());
    private JLabel lblStudentsCount, lblCoursesCount, lblRegCount, lblFlagCount;
    private String username;

   
    public AuthoritiesDashbord() {
        this("authority1");
    }

    public AuthoritiesDashbord(String username) {
        this.username = username;
        initComponents();
        setupTables();
        buildCards();                       
        wireExtras();
        lblUser.setText("Authorities: " + username);
        jLabel4.setText("Welcome back, Faculty Authority");
        tblCourses.setBorder(BorderFactory.createTitledBorder("Master System Oversight Table (All Records)"));
        refreshAll();
        pack();                            
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new AuthoritiesDashbord().setVisible(true));
    }

   
    private DefaultTableModel readOnly(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private void setupTables() {
        tblStudents.setModel(readOnly("Student ID", "Full Name", "Course", "Email", "Gender",
                "Date of Birth", "Status", "Compliance Check"));
        tblEnrolments.setModel(readOnly("Student No", "Name", "Course Code", "Course", "Grade"));
        tblCourses.setModel(readOnly("Code", "Course", "Lecturer", "Registered", "Graded"));
    }

   
    private void wireExtras() {
        btnLogout.addActionListener(this::btnLogoutActionPerformed);
        btnAudit.addActionListener(this::btnAuditActionPerformed);
        txtSearch.addActionListener(this::btnSearchActionPerformed);   
        
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
                txtCourseCode.setText(tblEnrolments.getValueAt(r, 2).toString());
            }
        });
       
        tblCourses.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblCourses.getSelectedRow();
                if (r >= 0) txtCourseCode.setText(tblCourses.getValueAt(r, 0).toString());
            }
        });
    }

    
    private void buildCards() {
        pnlCards.setLayout(new java.awt.GridLayout(1, 4, 12, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(makeCard("System Students", lblStudentsCount = new JLabel("0")));
        pnlCards.add(makeCard("Institutional Courses", lblCoursesCount = new JLabel("0")));
        pnlCards.add(makeCard("Official Registrations", lblRegCount = new JLabel("0")));
        pnlCards.add(makeCard("Audit Flags", lblFlagCount = new JLabel("0")));
    }

 
    private JPanel makeCard(String title, JLabel value) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 229, 240)),
            BorderFactory.createEmptyBorder(14, 18, 14, 14)));

        JLabel t = new JLabel(title);
        t.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        t.setForeground(new Color(30, 30, 30));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(t, BorderLayout.CENTER);
        top.add(new JLabel(new DocIcon()), BorderLayout.EAST);

        value.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        value.setForeground(new Color(10, 46, 92));

        card.add(top, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

  
    private static class DocIcon implements javax.swing.Icon {
        @Override public int getIconWidth() { return 30; }
        @Override public int getIconHeight() { return 34; }
        @Override public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(196, 205, 220));
            g2.fillRoundRect(x + 7, y, 22, 28, 5, 5);     
            g2.setColor(new Color(226, 231, 240));
            g2.fillRoundRect(x, y + 5, 22, 28, 5, 5);    
            g2.setColor(new Color(170, 180, 196));
            g2.fillRect(x + 4, y + 13, 14, 2);
            g2.fillRect(x + 4, y + 18, 14, 2);
            g2.fillRect(x + 4, y + 23, 10, 2);
            g2.dispose();
        }
    }

    
    private boolean matchesSearch(String studentNumber) {
        String q = txtSearch.getText().trim().toLowerCase();
        return q.isEmpty() || studentNumber.toLowerCase().contains(q);
    }

    private List<Student> filteredStudents() {
        List<Student> result = new ArrayList<>();
        for (Student s : DataStore.students) {
            if (matchesSearch(s.getStudentNumber())) result.add(s);
        }
        return result;
    }

    private void doSearch() {
        refreshAll();
        if (!txtSearch.getText().trim().isEmpty() && tblStudents.getRowCount() == 0) {
            msg("No student number matching \"" + txtSearch.getText().trim() + "\".");
        }
    }

    private void refreshAll() {
        loadStudents(filteredStudents());
        loadEnrolments();
        loadCourses();
        lblStudentsCount.setText(String.valueOf(DataStore.students.size()));
        lblCoursesCount.setText(String.valueOf(DataStore.courses.size()));
        lblRegCount.setText(String.valueOf(DataStore.enrolments.size()));
        lblFlagCount.setText(String.valueOf(DataStore.countFlagged()));
    }

  
    private void loadStudents(List<Student> list) {
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();
        m.setRowCount(0);
        for (Student s : list) {
            StringBuilder courses = new StringBuilder();
            int enrolled = 0;
            for (Enrolment e : DataStore.enrolments) {
                if (!e.getStudentNumber().equals(s.getStudentNumber())) continue;
                enrolled++;
                if (courses.length() > 0) courses.append(", ");
                courses.append(e.getCourseCode());
            }
            String audit = DataStore.getAudit(s.getStudentNumber());
            m.addRow(new Object[]{
                s.getStudentNumber(),
                s.getFirstName() + " " + s.getLastName(),
                enrolled == 0 ? "-" : courses.toString(),
                s.getEmail(),
                s.getGender(),
                s.getDob(),
                enrolled == 0 ? "Unregistered" : "Registered",
                audit == null ? "Audit pending" : audit
            });
        }
    }

    private String gradeText(Enrolment e) {
        return DataStore.hasGrade(e.getStudentNumber(), e.getCourseCode())
                ? String.format("%.1f%%", e.getGrade()) : "Not graded";
    }

    private void loadEnrolments() {
        DefaultTableModel m = (DefaultTableModel) tblEnrolments.getModel();
        m.setRowCount(0);
        for (Enrolment e : DataStore.enrolments) {
            if (!matchesSearch(e.getStudentNumber())) continue;
            Student s = DataStore.findStudent(e.getStudentNumber());
            Course c = DataStore.findCourse(e.getCourseCode());
            m.addRow(new Object[]{e.getStudentNumber(),
                s == null ? "(unknown)" : s.getFirstName() + " " + s.getLastName(),
                e.getCourseCode(),
                c == null ? "(unknown)" : c.getName(),
                gradeText(e)});
        }
    }

    private void loadCourses() {
        DefaultTableModel m = (DefaultTableModel) tblCourses.getModel();
        m.setRowCount(0);
        for (Course c : DataStore.courses) {
            m.addRow(new Object[]{c.getCode(), c.getName(), c.getLecturerUsername(),
                DataStore.countEnrolled(c.getCode()), DataStore.countGraded(c.getCode())});
        }
    }

   
    private void msg(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

  
    private void btnAuditActionPerformed(java.awt.event.ActionEvent evt) {
        String sn = txtRegStudent.getText().trim();
        if (sn.isEmpty()) {
            msg("Enter a student number, or click a student row first.");
            return;
        }
        Student s = DataStore.findStudent(sn);
        if (s == null) {
            msg("Student not found.");
            return;
        }
        String result = DataStore.performAudit(s.getStudentNumber());

        StringBuilder sb = new StringBuilder("Audit: " + s.getStudentNumber() + " - "
                + s.getFirstName() + " " + s.getLastName() + "\n");
        sb.append("Result: ").append(result).append("\n\n");
        boolean any = false;
        for (Enrolment e : DataStore.enrolments) {
            if (!e.getStudentNumber().equals(s.getStudentNumber())) continue;
            any = true;
            Course c = DataStore.findCourse(e.getCourseCode());
            sb.append(e.getCourseCode()).append(" - ")
              .append(c == null ? "(unknown)" : c.getName())
              .append(": ").append(gradeText(e)).append("\n");
        }
        if (!any) sb.append("Not registered in any course.");
        JOptionPane.showMessageDialog(this, sb.toString(), "Audit",
                result.startsWith("Not in compliance") ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        refreshAll();
    }

    
    private void overrideStudent() {
        Object[] options = {"Edit details", "Change status", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "What do you want to override?", "Override",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) editStudentDetails();
        else if (choice == 1) changeRegistration();
    }

   
    private void changeRegistration() {
        String sn = txtRegStudent.getText().trim();
        String code = txtCourseCode.getText().trim().toUpperCase();
        if (sn.isEmpty() || code.isEmpty()) {
            msg("Enter both a student number and a course code.");
            return;
        }
        Student s = DataStore.findStudent(sn);
        if (s == null) {
            msg("Student not found.");
            return;
        }
        if (DataStore.findCourse(code) == null) {
            msg("Course not found.");
            return;
        }
        sn = s.getStudentNumber();
        boolean registered = DataStore.findEnrolment(sn, code) != null;

        Object[] options = {"Registered", "Unregistered", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                sn + " is currently " + (registered ? "registered" : "unregistered") + " in " + code
                + ".\nSet status to:", "Change status",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                options[registered ? 1 : 0]);
        if (choice != 0 && choice != 1) return;

        String err;
        if (choice == 0) {
            if (registered) {
                msg(sn + " is already registered in " + code + ".");
                return;
            }
            err = DataStore.register(sn, code);
        } else {
            if (!registered) {
                msg(sn + " is not registered in " + code + ".");
                return;
            }
            if (DataStore.hasGrade(sn, code)) { 
                int sure = JOptionPane.showConfirmDialog(this,
                        sn + " already has a grade in " + code + ".\nUnregistering will delete that grade. Continue?",
                        "Change status", JOptionPane.YES_NO_OPTION);
                if (sure != JOptionPane.YES_OPTION) return;
            }
            err = DataStore.deregister(sn, code);
        }
        if (err != null) {
            msg(err);
            return;
        }
        msg(sn + " is now " + (choice == 0 ? "registered" : "unregistered") + " in " + code
                + ". The audit for this student is pending again.");
        refreshAll();
    }

 
    private void editStudentDetails() {
        String sn = txtRegStudent.getText().trim();
        if (sn.isEmpty()) {
            msg("Enter a student number, or click a student row first.");
            return;
        }
        Student s = DataStore.findStudent(sn);
        if (s == null) {
            msg("Student not found.");
            return;
        }
        JTextField txtFirst = new JTextField(s.getFirstName(), 16);
        JTextField txtLast = new JTextField(s.getLastName(), 16);
        JTextField txtEmail = new JTextField(s.getEmail(), 16);
        JTextField txtDob = new JTextField(s.getDob(), 16);
        JComboBox<String> cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        if (s.getGender() != null) {
            boolean known = false;
            for (int i = 0; i < cmbGender.getItemCount(); i++) {
                if (cmbGender.getItemAt(i).equalsIgnoreCase(s.getGender())) { cmbGender.setSelectedIndex(i); known = true; }
            }
            if (!known) { cmbGender.addItem(s.getGender()); cmbGender.setSelectedItem(s.getGender()); }
        }

        JPanel form = new JPanel(new java.awt.GridLayout(0, 2, 8, 8));
        form.add(new JLabel("First name:"));       form.add(txtFirst);
        form.add(new JLabel("Last name:"));        form.add(txtLast);
        form.add(new JLabel("Email:"));            form.add(txtEmail);
        form.add(new JLabel("Gender:"));           form.add(cmbGender);
        form.add(new JLabel("Date of birth:"));    form.add(txtDob);
        form.add(new JLabel("(yyyy-mm-dd)"));      form.add(new JLabel(""));

        int ok = JOptionPane.showConfirmDialog(this, form, "Edit details: " + s.getStudentNumber(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        String err = DataStore.updateStudent(s.getStudentNumber(), txtFirst.getText(), txtLast.getText(),
                txtEmail.getText(), (String) cmbGender.getSelectedItem(), txtDob.getText());
        if (err != null) {
            msg(err);
            return;
        }
        msg("Details updated. This student now needs a new audit.");
        refreshAll();
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
       
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
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        scrEnrolment = new javax.swing.JScrollPane();
        tblEnrolments = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtRegStudent = new javax.swing.JTextField();
        btnAudit = new javax.swing.JButton();
        btnOverride = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCourseCode = new javax.swing.JTextField();
        pnlCards = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        tabs.addTab("Student Info", jPanel4);

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
                .addGap(20, 20, 20)
                .addComponent(scrEnrolment, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addComponent(scrEnrolment, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabs.addTab("Enrolments", jPanel3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        tabs.addTab("Courses", jPanel5);

        jPanel1.setBackground(new java.awt.Color(240, 244, 250));

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Student No.", "Name", "Email", "Gender", "Date of Birth", "Course", "Status", "Compliance Check"
            }
        ));
        jScrollPane1.setViewportView(tblStudents);

        tblCourses.setBorder(javax.swing.BorderFactory.createTitledBorder("Master System Oversight Table"));
        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Code", "Course", "Lecture", "Registered"
            }
        ));
        jScrollPane2.setViewportView(tblCourses);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtRegStudent.addActionListener(this::txtRegStudentActionPerformed);

        btnAudit.setText("Audit");

        btnOverride.setText("Override");
        btnOverride.addActionListener(this::btnOverrideActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Authority Control Panel");

        jLabel2.setText("Student Number");

        jLabel3.setText("Course Code");

        txtCourseCode.addActionListener(this::txtCourseCodeActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(109, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnOverride, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAudit, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtRegStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(txtCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtRegStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(txtCourseCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAudit, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOverride, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jLabel4.setText("Welcome back, Authorities");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(81, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 773, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtRegStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRegStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRegStudentActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        doSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnOverrideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverrideActionPerformed
        overrideStudent();
    }//GEN-LAST:event_btnOverrideActionPerformed

    private void txtCourseCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCourseCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCourseCodeActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAudit;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOverride;
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
    private javax.swing.JScrollPane scrEnrolment;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTable tblEnrolments;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtCourseCode;
    private javax.swing.JTextField txtRegStudent;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
