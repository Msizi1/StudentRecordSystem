/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java 
 */

import data.DataStore;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.*;

/**
 */
public class LectureDashbord extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LectureDashbord.class.getName());

    private String username;
    private List<Course> myCourses;
    private JLabel lblStudentsCount, lblCoursesCount, lblEnrolCount, lblUnregCount;
    private JTable tblInfo, tblMyCourses;
    private DefaultTableModel rosterModel, assessModel, enrolModel, infoModel, myCoursesModel;

    
    private String clickedStudent, clickedCourse;

  
    public LectureDashbord() {
        this("lecturer1");
    }

    public LectureDashbord(String username) {
        this.username = username;
        this.myCourses = DataStore.coursesForLecturer(username);
        initComponents();
        setupTables();
        buildCards();
        wireExtras();
        pack();                    
        setLocationRelativeTo(null);
        lblUser.setText("Lecturer: " + username);
        jLabel4.setText("Welcome back, " + username);
        refreshAll();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LectureDashbord().setVisible(true));
    }


    private DefaultTableModel readOnly(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private javax.swing.border.Border titled(String text) {
        return BorderFactory.createTitledBorder(null, text,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new java.awt.Font("Segoe UI", 0, 18));
    }

    private void setupTables() {
    
        rosterModel = readOnly("Student ID", "Full Name", "Course", "Current Grade");
        tblStudents.setModel(rosterModel);
        tblStudents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setBorder(titled("My Class Roster"));

       
        assessModel = readOnly("Course Code", "Assessment", "Max Score", "Class Average");
        tblCourses.setModel(assessModel);
        jScrollPane2.setBorder(titled("Assessment Breakdown"));

       
        enrolModel = readOnly("Student No", "Name", "Course Code", "Course", "Grade");
        tblEnrolments.setModel(enrolModel);

        
        infoModel = readOnly("Student No.", "Name", "Email", "Gender", "Date of Birth");
        tblInfo = new JTable(infoModel);
        jPanel4.setLayout(new BorderLayout());
        jPanel4.setBackground(new Color(240, 244, 250));
        jPanel4.add(new JScrollPane(tblInfo), BorderLayout.CENTER);

        
        myCoursesModel = readOnly("Code", "Course", "Registered");
        tblMyCourses = new JTable(myCoursesModel);
        jPanel5.setLayout(new BorderLayout());
        jPanel5.setBackground(new Color(240, 244, 250));
        jPanel5.add(new JScrollPane(tblMyCourses), BorderLayout.CENTER);
    }


    private void wireExtras() {
        btnLogout.addActionListener(this::btnLogoutActionPerformed);
        btnAddGrade.addActionListener(e -> saveGrade(false));   
        txtSearch.addActionListener(this::btnSearchActionPerformed);
        tblStudents.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) { rosterRowClicked(); }
        });
    }

   
    private void buildCards() {
        pnlCards.setLayout(new java.awt.GridLayout(1, 4, 12, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(makeCard("Assigned Students", lblStudentsCount = new JLabel("0")));
        pnlCards.add(makeCard("My Courses", lblCoursesCount = new JLabel("0")));
        pnlCards.add(makeCard("Registered", lblEnrolCount = new JLabel("0")));
        pnlCards.add(makeCard("Unregistered", lblUnregCount = new JLabel("0")));
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

  
    private boolean isMine(String courseCode) {
        for (Course c : myCourses) {
            if (c.getCode().equals(courseCode)) return true;
        }
        return false;
    }

 
    private String gradeText(Enrolment e) {
        if (!DataStore.hasGrade(e.getStudentNumber(), e.getCourseCode())) return "Not graded";
        return String.format("%.1f%%", e.getGrade());
    }

    private String fullName(String studentNumber) {
        Student s = DataStore.findStudent(studentNumber);
        return s == null ? "(unknown)" : s.getFirstName() + " " + s.getLastName();
    }

    private void refreshAll() {
        loadCards();
        loadRoster();
        loadAssessments();
        loadTabs();
    }

    private void loadCards() {
        Set<String> assigned = new HashSet<>();
        int registered = 0;
        for (Enrolment e : DataStore.enrolments) {
            if (isMine(e.getCourseCode())) {
                registered++;
                assigned.add(e.getStudentNumber());
            }
        }
        int unreg = 0;
        for (Student s : DataStore.students) {
            boolean found = false;
            for (Enrolment e : DataStore.enrolments) {
                if (e.getStudentNumber().equals(s.getStudentNumber())) { found = true; break; }
            }
            if (!found) unreg++;
        }
        lblStudentsCount.setText(String.valueOf(assigned.size()));
        lblCoursesCount.setText(String.valueOf(myCourses.size()));
        lblEnrolCount.setText(String.valueOf(registered));
        lblUnregCount.setText(String.valueOf(unreg));
    }

   
    private void loadRoster() {
        rosterModel.setRowCount(0);
        String q = txtSearch.getText().trim().toLowerCase();
        for (Enrolment e : DataStore.enrolments) {
            if (!isMine(e.getCourseCode())) continue;
            if (!q.isEmpty() && !e.getStudentNumber().toLowerCase().contains(q)) continue;
            Course c = DataStore.findCourse(e.getCourseCode());
            rosterModel.addRow(new Object[]{e.getStudentNumber(), fullName(e.getStudentNumber()),
                c.getName() + " " + c.getCode(), gradeText(e)});
        }
        StringBuilder codes = new StringBuilder();
        for (Course c : myCourses) {
            if (codes.length() > 0) codes.append(", ");
            codes.append(c.getCode());
        }
        String title = "My Class Roster (" + codes + ")";
        if (!q.isEmpty()) title += " - search: " + txtSearch.getText().trim();
        jScrollPane1.setBorder(titled(title));
        if (!q.isEmpty() && rosterModel.getRowCount() == 0) {
            msg("No student matching \"" + txtSearch.getText().trim() + "\" in your courses.");
        }
    }

   
    private void loadAssessments() {
        assessModel.setRowCount(0);
        for (Course c : myCourses) {
            double sum = 0;
            int n = 0;
            for (Enrolment e : DataStore.enrolments) {
                if (e.getCourseCode().equals(c.getCode())
                        && DataStore.hasGrade(e.getStudentNumber(), c.getCode())) {
                    sum += e.getGrade();
                    n++;
                }
            }
            assessModel.addRow(new Object[]{c.getCode(), "Overall Grade", 100,
                n == 0 ? "-" : String.format("%.2f", sum / n)});
        }
    }

    private void loadTabs() {
        enrolModel.setRowCount(0);
        infoModel.setRowCount(0);
        myCoursesModel.setRowCount(0);
        Set<String> seen = new HashSet<>();
        for (Enrolment e : DataStore.enrolments) {
            if (!isMine(e.getCourseCode())) continue;
            Course c = DataStore.findCourse(e.getCourseCode());
            enrolModel.addRow(new Object[]{e.getStudentNumber(), fullName(e.getStudentNumber()),
                e.getCourseCode(), c.getName(), gradeText(e)});
            Student s = DataStore.findStudent(e.getStudentNumber());
            if (s != null && seen.add(s.getStudentNumber())) {
                infoModel.addRow(new Object[]{s.getStudentNumber(),
                    s.getFirstName() + " " + s.getLastName(),
                    s.getEmail(), s.getGender(), s.getDob()});
            }
        }
        for (Course c : myCourses) {
            myCoursesModel.addRow(new Object[]{c.getCode(), c.getName(),
                DataStore.countEnrolled(c.getCode())});
        }
    }

    
    private void rosterRowClicked() {
        int r = tblStudents.getSelectedRow();
        if (r < 0) return;
        clickedStudent = rosterModel.getValueAt(r, 0).toString();
        String courseText = rosterModel.getValueAt(r, 2).toString();
        clickedCourse = courseText.substring(courseText.lastIndexOf(' ') + 1); 
        String g = rosterModel.getValueAt(r, 3).toString();
        txtStudentNum.setText(clickedStudent);
        txtNewGrade.setText(g.equals("Not graded") ? "" : g.replace("%", ""));
    }

   
    private void saveGrade(boolean update) {
        String sn = txtStudentNum.getText().trim();
        String gradeText = txtNewGrade.getText().trim().replace("%", "");
        if (sn.isEmpty() || gradeText.isEmpty()) {
            msg("Enter a student number and a grade.");
            return;
        }
        double grade;
        try {
            grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException ex) {
            msg("Grade must be a number, e.g. 75.");
            return;
        }
        Student s = DataStore.findStudent(sn);
        if (s == null) {
            msg("Student not found.");
            return;
        }
        sn = s.getStudentNumber();

       
        String code;
        if (clickedCourse != null && sn.equalsIgnoreCase(clickedStudent)) {
            code = clickedCourse;
        } else {
            List<String> matches = new ArrayList<>();
            for (Course c : myCourses) {
                if (DataStore.findEnrolment(sn, c.getCode()) != null) matches.add(c.getCode());
            }
            if (matches.isEmpty()) {
                msg("That student isn't enrolled in any of your courses.");
                return;
            }
            if (matches.size() > 1) {
                msg("That student is in several of your courses. Click their row in the roster for the course you want.");
                return;
            }
            code = matches.get(0);
        }
        if (!isMine(code)) {
            msg("You can only grade students in your own courses.");
            return;
        }

        boolean hasGrade = DataStore.hasGrade(sn, code);
        if (!update && hasGrade) {
            msg(sn + " already has a grade in " + code + ". Use Update Grade to change it.");
            return;
        }
        if (update && !hasGrade) {
            msg(sn + " has no grade in " + code + " yet. Use Add Grade first.");
            return;
        }

        String err = DataStore.updateGrade(sn, code, grade);
        if (err != null) {
            msg(err);
            return;
        }
        msg(update ? "Grade updated for " + sn + " in " + code + "."
                   : "Grade added for " + sn + " in " + code + ".");
        txtStudentNum.setText("");
        txtNewGrade.setText("");
        clickedStudent = null;
        clickedCourse = null;
        refreshAll();
    }

    private void msg(String text) {
        JOptionPane.showMessageDialog(this, text);
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtNewGrade = new javax.swing.JTextField();
        txtStudentNum = new javax.swing.JTextField();
        btnUpdateGrade = new javax.swing.JButton();
        btnAddGrade = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlCards = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        scrEnrolment = new javax.swing.JScrollPane();
        tblEnrolments = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();

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

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Assesment Breakdown", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course Code", "Assesment", "Max Score", "Class Average"
            }
        ));
        jScrollPane2.setViewportView(tblCourses);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtNewGrade.addActionListener(this::txtNewGradeActionPerformed);

        txtStudentNum.addActionListener(this::txtStudentNumActionPerformed);

        btnUpdateGrade.setText("Update Grade");
        btnUpdateGrade.addActionListener(this::btnUpdateGradeActionPerformed);

        btnAddGrade.setText("Add Grade");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Grade Control Panel");

        jLabel2.setText("Student Number");

        jLabel3.setText("New Grade(%)");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddGrade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNewGrade, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStudentNum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateGrade, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
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
                    .addComponent(txtStudentNum, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNewGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddGrade, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout pnlCardsLayout = new javax.swing.GroupLayout(pnlCards);
        pnlCards.setLayout(pnlCardsLayout);
        pnlCardsLayout.setHorizontalGroup(
            pnlCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 779, Short.MAX_VALUE)
        );
        pnlCardsLayout.setVerticalGroup(
            pnlCardsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 172, Short.MAX_VALUE)
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Welcome back, Lecture");

        tblStudents.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Student Roster", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N
        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student No.", "Full Name", "Course", "Current Grade"
            }
        ));
        jScrollPane1.setViewportView(tblStudents);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 834, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        tabs.addTab("Dashboard", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 553, Short.MAX_VALUE)
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
                .addContainerGap(196, Short.MAX_VALUE)
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
            .addGap(0, 553, Short.MAX_VALUE)
        );

        tabs.addTab("Courses", jPanel5);

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

    private void txtStudentNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStudentNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStudentNumActionPerformed

    private void txtNewGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewGradeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNewGradeActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        loadRoster();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnUpdateGradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateGradeActionPerformed
        saveGrade(true);
    }//GEN-LAST:event_btnUpdateGradeActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddGrade;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateGrade;
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
    private javax.swing.JTextField txtNewGrade;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStudentNum;
    // End of variables declaration//GEN-END:variables
}
