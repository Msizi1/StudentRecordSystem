package gui;

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
public class AuthorityDashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AuthorityDashboard.class.getName());
    private JLabel lblStudentsCount, lblCoursesCount, lblRegCount, lblFlagCount;
    private String username;

    /**
     *
     */

    public AuthorityDashboard(String username) {
        this.username = username;
        initComponents();
        setupTables();
        tidyTables();
        buildCards();                       
        wireExtras();
        lblUser.setText("" + username);
        jLabel4.setText("Welcome back, "+ username);
        tblDashboardEnrolments.setBorder(BorderFactory.createTitledBorder("Modules (All Records)"));
        refreshAll();
        setMinimumSize(new java.awt.Dimension(1000, 650));
        pack();
        setLocationRelativeTo(null);
        loadSubjectOptions();
        loadProgrammes();
        loadAllSubjects();
    }

   public static void main(String args[]) {

    java.awt.EventQueue.invokeLater(() -> {

        new LoginForm().setVisible(true);

    });
}
   
    private DefaultTableModel readOnly(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private void setupTables() {
        tblAllStudents.setModel(readOnly("Student ID", "Full Name", "Modules", "Email", "Gender",
                "Date of Birth", "Status", "Compliance Check"));
        tblAllEnrolments.setModel(readOnly("Student No", "Name", "Module Code", "Module", "Grade"));
        tblDashboardEnrolments.setModel(readOnly("Module Code", "Module", "Lecturer", "Registered", "Graded"));
    }

   
    private void tidyTables() {
        for (javax.swing.JTable t : new javax.swing.JTable[]{tblAllStudents, tblAllEnrolments, tblDashboardEnrolments}) {
            t.setRowHeight(30);
            t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        }
    }

    private void wireExtras() {
        btnLogout.addActionListener(this::btnLogoutActionPerformed);
        txtSearch.addActionListener(this::btnSearchActionPerformed);   
        
        btnAuthorityGrade.addActionListener(evt -> editSelectedGrade());
        
        tblAllStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                int r = tblAllStudents.getSelectedRow();
                if (r >= 0) txtAuthorityStudent.setText(tblAllStudents.getValueAt(r, 0).toString());
            }
        });
       
        tblAllEnrolments.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int r = tblAllEnrolments.getSelectedRow();

        if (r < 0) {
            return;
        }

        txtAuthorityStudent.setText(
                tblAllEnrolments.getValueAt(r, 0).toString()
        );

        cmbAuthoritySubject.setSelectedItem(
                tblAllEnrolments.getValueAt(r, 2).toString()
        );
    }
});
       
        tblDashboardEnrolments.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int r = tblDashboardEnrolments.getSelectedRow();

        if (r >= 0) {
            cmbAuthoritySubject.setSelectedItem(
                    tblDashboardEnrolments.getValueAt(r, 0).toString()
            );
        }
    }
});
    }

    
    private void buildCards() {
        pnlCards.setLayout(new java.awt.GridLayout(1, 4, 12, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(makeCard("System Students", lblStudentsCount = new JLabel("0")));
        pnlCards.add(makeCard("Modules", lblCoursesCount = new JLabel("0")));
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
        try {
        refreshAll();
        if (!txtSearch.getText().trim().isEmpty() && tblAllStudents.getRowCount() == 0) {
            msg("No student number matching \"" + txtSearch.getText().trim() + "\".");
        }
        } catch (Exception ex) { showError("search", ex); }
    }

    private void refreshAll() {
        loadStudents(filteredStudents());
        loadEnrolments();
        loadCourses();
        lblStudentsCount.setText(String.valueOf(DataStore.students.size()));
        lblCoursesCount.setText(String.valueOf(DataStore.subjects.size()));
        lblRegCount.setText(String.valueOf(DataStore.enrolments.size()));
        lblFlagCount.setText(String.valueOf(DataStore.countFlagged()));
    }

  
    private void loadStudents(List<Student> list) {
        DefaultTableModel m = (DefaultTableModel) tblAllStudents.getModel();
        m.setRowCount(0);
        for (Student s : list) {
            StringBuilder courses = new StringBuilder();
            int enrolled = 0;
            for (Enrolment e : DataStore.enrolments) {
                if (!e.getStudentNumber().equals(s.getStudentNumber())) continue;
                enrolled++;
                if (courses.length() > 0) courses.append(", ");
                courses.append(e.getModuleCode());
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
        return DataStore.hasGrade(e.getStudentNumber(), e.getModuleCode())
                ? String.format("%.1f%%", e.getGrade()) : "Not graded";
    }

    private void loadEnrolments() {
        DefaultTableModel m = (DefaultTableModel) tblAllEnrolments.getModel();
        m.setRowCount(0);
        for (Enrolment e : DataStore.enrolments) {
            if (!matchesSearch(e.getStudentNumber())) continue;
            Student s = DataStore.findStudent(e.getStudentNumber());
            Subject c = DataStore.findModule(e.getModuleCode());
            m.addRow(new Object[]{e.getStudentNumber(),
                s == null ? "(unknown)" : s.getFirstName() + " " + s.getLastName(),
                e.getModuleCode(),
                c == null ? "(unknown)" : c.getName(),
                gradeText(e)});
        }
    }
    
    private void loadSubjectOptions() {
    cmbAuthoritySubject.removeAllItems();

    for (Subject subject : DataStore.subjects) {
        cmbAuthoritySubject.addItem(subject.getCode());
    }
}
    private void editSelectedGrade() {
    String studentNumber = txtAuthorityStudent.getText().trim();
    String subjectCode = getSelectedSubjectCode();
    String gradeInput = txtAuthorityGrade.getText().trim();

    if (studentNumber.isEmpty()) {
        msg("Please enter a student number or select a student from the table.");
        return;
    }

    if (subjectCode.isEmpty()) {
        msg("Please select a subject.");
        return;
    }

    if (gradeInput.isEmpty()) {
        msg("Please enter a grade.");
        return;
    }

    Student student = DataStore.findStudent(studentNumber);

    if (student == null) {
        msg("Student not found.");
        return;
    }

    String actualStudentNumber = student.getStudentNumber();

    if (DataStore.findEnrolment(actualStudentNumber, subjectCode) == null) {
        msg("This student is not registered for " + subjectCode + ".");
        return;
    }

    double grade;

    try {
        grade = Double.parseDouble(gradeInput);
    } catch (NumberFormatException ex) {
        msg("Please enter a valid numerical grade.");
        return;
    }

    String error = DataStore.updateGrade(
            actualStudentNumber,
            subjectCode,
            grade
    );

    if (error != null) {
        msg(error);
        return;
    }

    msg("Grade successfully updated.");
    txtAuthorityGrade.setText("");
    refreshAll();
}
    
    private void loadCourses() {
        DefaultTableModel m = (DefaultTableModel) tblDashboardEnrolments.getModel();
        m.setRowCount(0);
        for (Subject c : DataStore.subjects) {
            m.addRow(new Object[]{c.getCode(), c.getName(), c.getLecturerUsername(),
                DataStore.countEnrolled(c.getCode()), DataStore.countGraded(c.getCode())});
        }
    }
    
    private void loadProgrammes() {
    jTable1.setModel(readOnly(
            "Programme Code",
            "Programme Name",
            "Number of Subjects"
    ));

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    for (Course course : DataStore.courses) {
        int subjectCount = 0;

        for (Subject subject : DataStore.subjects) {
            if (subject.getCourseCode().equalsIgnoreCase(course.getCode())) {
                subjectCount++;
            }
        }

        model.addRow(new Object[]{
            course.getCode(),
            course.getName(),
            subjectCount
        });
    }
}
    
    private void loadAllSubjects() {
    tblAllSubjects.setModel(readOnly(
            "Subject Code",
            "Subject Name",
            "Programme Code",
            "Programme Name",
            "Lecturer Username"
    ));

    DefaultTableModel model = (DefaultTableModel) tblAllSubjects.getModel();

    for (Subject subject : DataStore.subjects) {
        Course course = DataStore.findProgramme(subject.getCourseCode());

        String programmeName = (course == null)
                ? "Unknown Programme"
                : course.getName();

        model.addRow(new Object[]{
            subject.getCode(),
            subject.getName(),
            subject.getCourseCode(),
            programmeName,
            subject.getLecturerUsername()
        });
    }
}
    
    private String getSelectedSubjectCode() {
    Object selected = cmbAuthoritySubject.getSelectedItem();

    if (selected == null) {
        return "";
    }

    return selected.toString().trim().toUpperCase();
}

private void registerSelectedSubject() {
    String studentNumber = txtAuthorityStudent.getText().trim();
    String subjectCode = getSelectedSubjectCode();

    if (studentNumber.isEmpty()) {
        msg("Please enter a student number or select a student from the table.");
        return;
    }

    if (subjectCode.isEmpty()) {
        msg("Please select a subject.");
        return;
    }

    Student student = DataStore.findStudent(studentNumber);

    if (student == null) {
        msg("Student not found.");
        return;
    }

    if (DataStore.findModule(subjectCode) == null) {
        msg("Subject not found.");
        return;
    }

    if (DataStore.findEnrolment(student.getStudentNumber(), subjectCode) != null) {
        msg("This student is already registered for " + subjectCode + ".");
        return;
    }

    String error = DataStore.register(student.getStudentNumber(), subjectCode);

    if (error != null) {
        msg(error);
        return;
    }

    msg("Student successfully registered for " + subjectCode + ".");
    refreshAll();
}
    private void deregisterSelectedSubject() {
    String studentNumber = txtAuthorityStudent.getText().trim();
    String subjectCode = getSelectedSubjectCode();

    if (studentNumber.isEmpty()) {
        msg("Please enter a student number or select a student from the table.");
        return;
    }

    if (subjectCode.isEmpty()) {
        msg("Please select a subject.");
        return;
    }

    Student student = DataStore.findStudent(studentNumber);

    if (student == null) {
        msg("Student not found.");
        return;
    }

    String actualStudentNumber = student.getStudentNumber();

    if (DataStore.findEnrolment(actualStudentNumber, subjectCode) == null) {
        msg("This student is not registered for " + subjectCode + ".");
        return;
    }

    if (DataStore.hasGrade(actualStudentNumber, subjectCode)) {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "This student already has a grade for " + subjectCode
                + ". Deregistering will also remove that grade. Continue?",
                "Confirm Deregistration",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
    } else {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to deregister this student from "
                + subjectCode + "?",
                "Confirm Deregistration",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
    }

    String error = DataStore.deregister(actualStudentNumber, subjectCode);

    if (error != null) {
        msg(error);
        return;
    }

    msg("Student successfully deregistered from " + subjectCode + ".");
    refreshAll();
}
    private void msg(String text) {
    JOptionPane.showMessageDialog(this, text);
}


    private void runAudit() {
        String sn = txtAuthorityStudent.getText().trim();
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
            Subject c = DataStore.findModule(e.getModuleCode());
            sb.append(e.getModuleCode()).append(" - ")
              .append(c == null ? "(unknown)" : c.getName())
              .append(": ").append(gradeText(e)).append("\n");
        }
        if (!any) sb.append("Not registered in any module.");
        JOptionPane.showMessageDialog(this, sb.toString(), "Audit",
                result.startsWith("Not in compliance") ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        refreshAll();
    }

    
    private void overrideStudent() {
        try { doOverride(); } catch (Exception ex) { showError("apply the override", ex); }
    }

    private void doOverride() {
        Object[] options = {"Edit details", "Change status", "Edit grade", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "What do you want to override?", "Override",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) editStudentDetails();
        else if (choice == 1) changeRegistration();
        else if (choice == 2) editGrade();
    }

   
    private void editGrade() {
        String sn = txtAuthorityStudent.getText().trim();
        String code = txtAuthorityGrade.getText().trim().toUpperCase();
        if (sn.isEmpty() || code.isEmpty()) { msg("Enter both a student number and a module code."); return; }
        if (DataStore.findEnrolment(sn, code) == null) { msg("This student is not registered for " + code + "."); return; }
        String input = JOptionPane.showInputDialog(this, "New grade (0 to 100) for " + sn + " in " + code + ":", "Edit grade", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        double grade;
        try { grade = Double.parseDouble(input.trim()); }
        catch (NumberFormatException ex) { msg("Enter a valid numerical grade."); return; }
        String err = DataStore.updateGrade(sn, code, grade);
        if (err != null) { msg(err); return; }
        msg("Grade saved. The audit for this student is pending again.");
        refreshAll();
    }

    private void showError(String action, Exception ex) {
        logger.log(java.util.logging.Level.SEVERE, action + " failed", ex);
        JOptionPane.showMessageDialog(this, "Could not " + action + ". Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void changeRegistration() {
        String sn = txtAuthorityStudent.getText().trim();
        String code = txtAuthorityGrade.getText().trim().toUpperCase();
        if (sn.isEmpty() || code.isEmpty()) {
            msg("Enter both a student number and a module code.");
            return;
        }
        Student s = DataStore.findStudent(sn);
        if (s == null) {
            msg("Student not found.");
            return;
        }
        if (DataStore.findModule(code) == null) {
            msg("Module not found.");
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
        String sn = txtAuthorityStudent.getText().trim();
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDashboardEnrolments = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtAuthorityStudent = new javax.swing.JTextField();
        btnAuthorityDeregister = new javax.swing.JButton();
        btnAuthorityRegister = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAuthorityGrade = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbAuthoritySubject = new javax.swing.JComboBox<>();
        btnAuthorityGrade = new javax.swing.JButton();
        pnlCards = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        scrStudentInfo = new javax.swing.JScrollPane();
        tblAllStudents = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scrEnrolment = new javax.swing.JScrollPane();
        tblAllEnrolments = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        scrProgrammes = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllSubjects = new javax.swing.JTable();

        jButton1.setText("jButton1");

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

        jPanel1.setBackground(new java.awt.Color(240, 244, 250));

        tblDashboardEnrolments.setBorder(javax.swing.BorderFactory.createTitledBorder("Master System Oversight Table"));
        tblDashboardEnrolments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student Number", "Name", "Module Code", "module", "Grades"
            }
        ));
        jScrollPane2.setViewportView(tblDashboardEnrolments);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtAuthorityStudent.addActionListener(this::txtAuthorityStudentActionPerformed);

        btnAuthorityDeregister.setText("Deregister");
        btnAuthorityDeregister.addActionListener(this::btnAuthorityDeregisterActionPerformed);

        btnAuthorityRegister.setText("Register");
        btnAuthorityRegister.addActionListener(this::btnAuthorityRegisterActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Authority Control Panel");

        jLabel2.setText("Student Number:");

        jLabel3.setText("Module Code:");

        txtAuthorityGrade.addActionListener(this::txtAuthorityGradeActionPerformed);

        jLabel5.setText("New Grade (%):");

        cmbAuthoritySubject.setModel(new javax.swing.DefaultComboBoxModel<>());
        cmbAuthoritySubject.addActionListener(this::cmbAuthoritySubjectActionPerformed);

        btnAuthorityGrade.setText("Edit Grade");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAuthorityRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAuthorityDeregister, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3)
                            .addComponent(txtAuthorityStudent)
                            .addComponent(jLabel2)
                            .addComponent(txtAuthorityGrade)
                            .addComponent(jLabel5)
                            .addComponent(cmbAuthoritySubject, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAuthorityGrade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addComponent(txtAuthorityStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbAuthoritySubject, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtAuthorityGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAuthorityRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAuthorityDeregister, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(btnAuthorityGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabs.addTab("Dashboard", jPanel1);

        tblAllStudents.setModel(new javax.swing.table.DefaultTableModel(
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
        scrStudentInfo.setViewportView(tblAllStudents);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrStudentInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrStudentInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Student Info", jPanel4);

        tblAllEnrolments.setModel(new javax.swing.table.DefaultTableModel(
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
        scrEnrolment.setViewportView(tblAllEnrolments);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrEnrolment, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrEnrolment, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Enrolments", jPanel3);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Course Code", "Course"
            }
        ));
        scrProgrammes.setViewportView(jTable1);

        tblAllSubjects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Code", "Module", "Lecturer", "Students Enrolled"
            }
        ));
        jScrollPane1.setViewportView(tblAllSubjects);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrProgrammes, javax.swing.GroupLayout.DEFAULT_SIZE, 887, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrProgrammes, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("Courses & Modules", jPanel5);

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
                .addContainerGap()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAuthorityStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorityStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorityStudentActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        doSearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAuthorityRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorityRegisterActionPerformed
        registerSelectedSubject();
    }//GEN-LAST:event_btnAuthorityRegisterActionPerformed

    private void txtAuthorityGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorityGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorityGradeActionPerformed

    private void btnAuthorityDeregisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorityDeregisterActionPerformed
        // TODO add your handling code here:
        deregisterSelectedSubject();
    }//GEN-LAST:event_btnAuthorityDeregisterActionPerformed

    private void cmbAuthoritySubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAuthoritySubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAuthoritySubjectActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAuthorityDeregister;
    private javax.swing.JButton btnAuthorityGrade;
    private javax.swing.JButton btnAuthorityRegister;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbAuthoritySubject;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JScrollPane scrEnrolment;
    private javax.swing.JScrollPane scrProgrammes;
    private javax.swing.JScrollPane scrStudentInfo;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblAllEnrolments;
    private javax.swing.JTable tblAllStudents;
    private javax.swing.JTable tblAllSubjects;
    private javax.swing.JTable tblDashboardEnrolments;
    private javax.swing.JTextField txtAuthorityGrade;
    private javax.swing.JTextField txtAuthorityStudent;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
