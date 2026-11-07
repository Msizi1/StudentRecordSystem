package gui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java 
 */

import data.DataStore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import model.*;


/**
 */
public class LecturerDashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerDashboard.class.getName());

    private String username;

    private List<Subject> mySubjects;

    private JLabel lblStudentsCount;
    private JLabel lblCoursesCount;
    private JLabel lblEnrolCount;
    private JLabel lblUnregCount;

    private DefaultTableModel rosterModel;
    private DefaultTableModel enrolModel;
    private DefaultTableModel infoModel;
    private DefaultTableModel mySubjectsModel;
  
    
    public LecturerDashboard(String username) {

    this.username = username;

    initComponents();

    mySubjects = DataStore.modulesForLecturer(username);

    setupTables();

    buildCards();

    loadMySubjectCodes();

    wireExtras();

    setMinimumSize(new java.awt.Dimension(1000, 650));

    pack();

    setLocationRelativeTo(null);

    lblUser.setText(username);

    lblWelcome.setText("Welcome back, " + username);

    refreshAll();
}
 

    public static void main(String args[]) {

    java.awt.EventQueue.invokeLater(() -> {

        new LoginForm().setVisible(true);});
}

   private DefaultTableModel readOnly(String... columns) {

    return new DefaultTableModel(columns, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
} 
    
private void setupTables() {

    // 1. Dashboard: Student Roster
    rosterModel = readOnly(
        "Student Number",
        "Full Name",
        "Subject Code",
        "Subject",
        "Current Grade");

    tblLecturerRoster.setModel(rosterModel);

    tblLecturerRoster.setSelectionMode(
        ListSelectionModel.SINGLE_SELECTION
    );

    // 2. My Students
    infoModel = readOnly(
        "Student Number",
        "Full Name",
        "Email"
    );

    tblMyStudents.setModel(infoModel);

    // 3. My Enrolments
    enrolModel = readOnly(
        "Student Number",
        "Student Name",
        "Subject Code",
        "Subject",
        "Grade"
    );

    tblMtEnrolments.setModel(enrolModel);

    // 4. My Subjects
    mySubjectsModel = readOnly(
        "Subject Code",
        "Subject Name",
        "Students Enrolled"
    );

    tblMySubjects.setModel(mySubjectsModel);
}



    private void wireExtras() {

    // Search when Enter is pressed
    txtSearch.addActionListener(e -> loadRoster());

    // Select a student from the roster
    tblLecturerRoster.addMouseListener(
        new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent evt) {

                rosterRowClicked();
            }
        }
    );
}

   
    private void buildCards() {
        pnlLecturerCards.setLayout(new java.awt.GridLayout(1, 4, 12, 0));
        pnlLecturerCards.setOpaque(false);
        pnlLecturerCards.add(makeCard("Assigned Students", lblStudentsCount = new JLabel("0")));
        pnlLecturerCards.add(makeCard("My Modules", lblCoursesCount = new JLabel("0")));
        pnlLecturerCards.add(makeCard("Registered", lblEnrolCount = new JLabel("0")));
        pnlLecturerCards.add(makeCard("Unregistered", lblUnregCount = new JLabel("0")));
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
        JLabel icon = new JLabel(new DocIcon());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(t, BorderLayout.CENTER);
        top.add(icon, BorderLayout.EAST);

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

  
    private boolean isMine(String subjectCode) {

    for (Subject subject : mySubjects) {

        if (subject.getCode().equalsIgnoreCase(subjectCode)) {
            return true;
        }
    }

    return false;
}
 
    private void loadMySubjectCodes() {

    cmbGradeSubject.removeAllItems();

    for (Subject subject : mySubjects) {

        cmbGradeSubject.addItem(subject.getCode());
    }

    cmbGradeSubject.setSelectedIndex(-1);
}
    
    private String gradeText(Enrolment e) {

    if (!DataStore.hasGrade(
            e.getStudentNumber(),
            e.getModuleCode())) {

        return "Not graded";
    }

    return String.format("%.1f%%", e.getGrade());
}

    private String fullName(String studentNumber) {

    Student s = DataStore.findStudent(studentNumber);

    return s == null
            ? "(unknown)"
            : s.getFirstName() + " " + s.getLastName();
}

    private void refreshAll() {

    loadCards();

    loadRoster();

    loadTabs();
}

    private void loadCards() {

    Set<String> assignedStudents = new HashSet<>();

    int registered = 0;

    // Count enrolments in this lecturer's subjects
    for (Enrolment e : DataStore.enrolments) {

        if (isMine(e.getModuleCode())) {

            registered++;

            assignedStudents.add(e.getStudentNumber());
        }
    }

    // Count students not enrolled in any of this lecturer's subjects
    int unregistered = 0;

    for (Student s : DataStore.students) {

        if (!assignedStudents.contains(s.getStudentNumber())) {
            unregistered++;
        }
    }

    lblStudentsCount.setText(
        String.valueOf(assignedStudents.size())
    );

    lblCoursesCount.setText(
        String.valueOf(mySubjects.size())
    );

    lblEnrolCount.setText(
        String.valueOf(registered)
    );

    lblUnregCount.setText(
        String.valueOf(unregistered)
    );
}
    
   
    
private void loadRoster() {

    rosterModel.setRowCount(0);

    String search = txtSearch.getText().trim().toLowerCase();

    for (Enrolment e : DataStore.enrolments) {

        String subjectCode = e.getModuleCode();

        // Only show this lecturer's subjects
        if (!isMine(subjectCode)) {
            continue;
        }

        Student student =
                DataStore.findStudent(e.getStudentNumber());

        Subject subject =
                DataStore.findModule(subjectCode);

        if (student == null || subject == null) {
            continue;
        }

        String name = student.getFirstName()
                + " " + student.getLastName();

        // Search by student number, name or subject
        String searchable = (
                e.getStudentNumber() + " "
                + name + " "
                + subjectCode + " "
                + subject.getName()
        ).toLowerCase();

        if (!search.isEmpty()
                && !searchable.contains(search)) {
            continue;
        }

        rosterModel.addRow(new Object[]{

            student.getStudentNumber(),

            name,

            subject.getCode(),

            subject.getName(),

            gradeText(e)
        });
    }

    if (!search.isEmpty()
            && rosterModel.getRowCount() == 0) {

        JOptionPane.showMessageDialog(
            this,
            "No matching students or subjects found."
        );
    }
}

   

    
private void loadTabs() {

    enrolModel.setRowCount(0);

    infoModel.setRowCount(0);

    mySubjectsModel.setRowCount(0);

    Set<String> seenStudents = new HashSet<>();

    // Load lecturer's student enrolments
    for (Enrolment e : DataStore.enrolments) {

        String subjectCode = e.getModuleCode();

        if (!isMine(subjectCode)) {
            continue;
        }

        Student student =
                DataStore.findStudent(e.getStudentNumber());

        Subject subject =
                DataStore.findModule(subjectCode);

        if (student == null || subject == null) {
            continue;
        }

        String fullName = student.getFirstName()
                + " " + student.getLastName();

        // My Enrolments
        enrolModel.addRow(new Object[]{

            student.getStudentNumber(),

            fullName,

            subject.getCode(),

            subject.getName(),

            gradeText(e)
        });

        // My Students (no duplicates)
        if (seenStudents.add(student.getStudentNumber())) {

            infoModel.addRow(new Object[]{

                student.getStudentNumber(),

                fullName,

                student.getEmail()
            });
        }
    }

    // My Subjects
    for (Subject subject : mySubjects) {

        mySubjectsModel.addRow(new Object[]{

            subject.getCode(),

            subject.getName(),

            DataStore.countEnrolled(subject.getCode())
        });
    }
}


    
    private void rosterRowClicked() {

    int selectedRow = tblLecturerRoster.getSelectedRow();

    if (selectedRow < 0) {
        return;
    }

    int row = tblLecturerRoster.convertRowIndexToModel(
        selectedRow
    );

    String studentNumber =
            rosterModel.getValueAt(row, 0).toString();

    String subjectCode =
            rosterModel.getValueAt(row, 2).toString();

    String currentGrade =
            rosterModel.getValueAt(row, 4).toString();

    txtGradeStudent.setText(studentNumber);

    cmbGradeSubject.setSelectedItem(subjectCode);

    txtGrade.setText(
        currentGrade.equals("Not graded")
            ? ""
            : currentGrade.replace("%", "")
    );
}

   



    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {

    try {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            new LoginForm().setVisible(true);

            dispose();
        }

    } catch (Exception ex) {

        logger.log(
                java.util.logging.Level.SEVERE,
                "Logout failed",
                ex
        );

        JOptionPane.showMessageDialog(
                this,
                "Unable to return to the login screen."
        );
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

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        pnlGradeControl = new javax.swing.JPanel();
        txtGradeStudent = new javax.swing.JTextField();
        btnClearGrade = new javax.swing.JButton();
        btnSaveGrade = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtGrade = new javax.swing.JTextField();
        cmbGradeSubject = new javax.swing.JComboBox<>();
        pnlLecturerCards = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        scrLecturerRoster = new javax.swing.JScrollPane();
        tblLecturerRoster = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        scrMyStudents = new javax.swing.JScrollPane();
        tblMyStudents = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scrMyEnrolments = new javax.swing.JScrollPane();
        tblMtEnrolments = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        scrMySubjects = new javax.swing.JScrollPane();
        tblMySubjects = new javax.swing.JTable();

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
                .addGap(26, 26, 26)
                .addComponent(lblTitle)
                .addGap(38, 38, 38)
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

        pnlGradeControl.setBackground(new java.awt.Color(255, 255, 255));

        txtGradeStudent.addActionListener(this::txtGradeStudentActionPerformed);

        btnClearGrade.setText("Clear");
        btnClearGrade.addActionListener(this::btnClearGradeActionPerformed);

        btnSaveGrade.setText("Save");
        btnSaveGrade.addActionListener(this::btnSaveGradeActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Grade Control Panel");

        jLabel2.setText("Student Number:");

        jLabel3.setText("Module Code:");

        jLabel5.setText("New Grade (%)");

        txtGrade.addActionListener(this::txtGradeActionPerformed);

        cmbGradeSubject.setModel(new javax.swing.DefaultComboBoxModel<>());

        javax.swing.GroupLayout pnlGradeControlLayout = new javax.swing.GroupLayout(pnlGradeControl);
        pnlGradeControl.setLayout(pnlGradeControlLayout);
        pnlGradeControlLayout.setHorizontalGroup(
            pnlGradeControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGradeControlLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlGradeControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2)
                    .addGroup(pnlGradeControlLayout.createSequentialGroup()
                        .addComponent(btnSaveGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClearGrade))
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(cmbGradeSubject, 0, 393, Short.MAX_VALUE)
                    .addComponent(txtGradeStudent)
                    .addComponent(txtGrade))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        pnlGradeControlLayout.setVerticalGroup(
            pnlGradeControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGradeControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGradeStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbGradeSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlGradeControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClearGrade, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(btnSaveGrade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlLecturerCardsLayout = new javax.swing.GroupLayout(pnlLecturerCards);
        pnlLecturerCards.setLayout(pnlLecturerCardsLayout);
        pnlLecturerCardsLayout.setHorizontalGroup(
            pnlLecturerCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 853, Short.MAX_VALUE)
        );
        pnlLecturerCardsLayout.setVerticalGroup(
            pnlLecturerCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 85, Short.MAX_VALUE)
        );

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setText("Welcome back, Lecture");

        tblLecturerRoster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student Number", "Name", "Module", "Current Grade"
            }
        ));
        scrLecturerRoster.setViewportView(tblLecturerRoster);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Student Roster");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(scrLecturerRoster, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlGradeControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlLecturerCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlLecturerCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlGradeControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrLecturerRoster, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        tabs.addTab("Dashboard", jPanel1);

        tblMyStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Student Number", "Name", "Email"
            }
        ));
        scrMyStudents.setViewportView(tblMyStudents);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMyStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMyStudents, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("My Students", jPanel4);

        tblMtEnrolments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Student Number", "Name", "Module", "Module Code", "Grade"
            }
        ));
        scrMyEnrolments.setViewportView(tblMtEnrolments);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMyEnrolments, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMyEnrolments, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        tabs.addTab("My Enrolments", jPanel3);

        tblMySubjects.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Module Code", "Module", "Students Enrolled"
            }
        ));
        scrMySubjects.setViewportView(tblMySubjects);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMySubjects, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrMySubjects, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("My Subjects", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabs)))
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

    private void txtGradeStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGradeStudentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGradeStudentActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        loadRoster();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnClearGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearGradeActionPerformed


   
    txtGradeStudent.setText("");

    txtGrade.setText("");

    cmbGradeSubject.setSelectedIndex(-1);

    }//GEN-LAST:event_btnClearGradeActionPerformed

    private void btnSaveGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveGradeActionPerformed
        // TODO add your handling code here:
          try {

        String studentNumber =
                txtGradeStudent.getText().trim();

        String subjectCode =
                (String) cmbGradeSubject.getSelectedItem();

        String gradeText =
                txtGrade.getText().trim();

        if (studentNumber.isEmpty()
                || subjectCode == null
                || gradeText.isEmpty()) {

            throw new IllegalArgumentException(
                    "Complete all grade fields."
            );
        }

        if (!isMine(subjectCode)) {

            throw new IllegalArgumentException(
                    "You cannot edit grades for this subject."
            );
        }

        Student student =
                DataStore.findStudent(studentNumber);

        if (student == null) {

            throw new IllegalArgumentException(
                    "Student not found."
            );
        }

        Enrolment enrolment =
                DataStore.findEnrolment(
                    studentNumber,
                    subjectCode
                );

        if (enrolment == null) {

            throw new IllegalArgumentException(
                    "Student is not enrolled in this subject."
            );
        }

        double grade = Double.parseDouble(gradeText);

        String error = DataStore.updateGrade(
                studentNumber,
                subjectCode,
                grade
        );

        if (error != null) {

            throw new IllegalArgumentException(error);
        }

        JOptionPane.showMessageDialog(
                this,
                "Grade successfully saved."
        );

        // Clear form
        txtGradeStudent.setText("");

        txtGrade.setText("");

        cmbGradeSubject.setSelectedIndex(-1);

        // Refresh dashboard
        refreshAll();

    } catch (NumberFormatException ex) {

        JOptionPane.showMessageDialog(
                this,
                "Enter a valid numerical grade."
        );

    } catch (IllegalArgumentException ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Grade Error",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception ex) {

        logger.log(
                java.util.logging.Level.SEVERE,
                "Grade update failed",
                ex
        );

        JOptionPane.showMessageDialog(
                this,
                "An unexpected error occurred."
        );
    }
    }//GEN-LAST:event_btnSaveGradeActionPerformed

    private void txtGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGradeActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearGrade;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSaveGrade;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbGradeSubject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlGradeControl;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlLecturerCards;
    private javax.swing.JScrollPane scrLecturerRoster;
    private javax.swing.JScrollPane scrMyEnrolments;
    private javax.swing.JScrollPane scrMyStudents;
    private javax.swing.JScrollPane scrMySubjects;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblLecturerRoster;
    private javax.swing.JTable tblMtEnrolments;
    private javax.swing.JTable tblMyStudents;
    private javax.swing.JTable tblMySubjects;
    private javax.swing.JTextField txtGrade;
    private javax.swing.JTextField txtGradeStudent;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
