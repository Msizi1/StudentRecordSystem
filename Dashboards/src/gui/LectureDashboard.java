/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;
import data.DataStore;
import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author user
 */
public class LectureDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LectureDashboard.class.getName());
       private final User user;
    private JLabel lblCoursesCount, lblStudentsCount, lblPendingCount;

    public LectureDashboard(User user) {
        this.user = user;
        initComponents();
        setLocationRelativeTo(null);
        buildCards();
        lblUser.setText("Lecturer: " + user.getUsername());
        refreshAll();
    }

    // Does this course belong to the logged-in lecturer?
    private boolean isMyCourse(String code) {
        Course c = DataStore.findCourse(code);
        return c != null && c.getLecturerUsername().equals(user.getUsername());
    }

    // Is this student enrolled in at least one of my courses?
    private boolean isMyStudent(String studentNumber) {
        for (Enrolment e : DataStore.enrolments) {
            if (e.getStudentNumber().equals(studentNumber) && isMyCourse(e.getCourseCode())) {
                return true;
            }
        }
        return false;
    }

    private void buildCards() {
        pnlCards.setLayout(new java.awt.GridLayout(1, 3, 12, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(makeCard("My courses", lblCoursesCount = new JLabel("0")));
        pnlCards.add(makeCard("My students", lblStudentsCount = new JLabel("0")));
        pnlCards.add(makeCard("Grades not captured", lblPendingCount = new JLabel("0")));
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

    private void refreshAll() {
        loadStudents(DataStore.students);
        loadCourses();
        loadGrades();
        int n = 0;
        for (Student s : DataStore.students) {
            if (isMyStudent(s.getStudentNumber())) n++;
        }
        lblStudentsCount.setText(String.valueOf(n));
    }

    // Only students in my courses, number and name only
    private void loadStudents(List<Student> list) {
        DefaultTableModel m = (DefaultTableModel) tblStudents.getModel();
        m.setRowCount(0);
        for (Student s : list) {
            if (isMyStudent(s.getStudentNumber())) {
                m.addRow(new Object[]{s.getStudentNumber(),
                    s.getFirstName() + " " + s.getLastName()});
            }
        }
    }

    // Only my courses, with registered counts
    private void loadCourses() {
        DefaultTableModel m = (DefaultTableModel) tblCourses.getModel();
        m.setRowCount(0);
        List<Course> mine = DataStore.coursesForLecturer(user.getUsername());
        for (Course c : mine) {
            m.addRow(new Object[]{c.getCode(), c.getName(),
                c.getLecturerUsername(), DataStore.countEnrolled(c.getCode())});
        }
        lblCoursesCount.setText(String.valueOf(mine.size()));
    }

    // Enrolments in my courses with their grades
    private void loadGrades() {
        DefaultTableModel m = (DefaultTableModel) tblGrades.getModel();
        m.setRowCount(0);
        int pending = 0;
        for (Enrolment e : DataStore.enrolments) {
            if (!isMyCourse(e.getCourseCode())) continue;
            Student s = DataStore.findStudent(e.getStudentNumber());
            String grade = e.getGrade() < 0 ? "Not graded" : String.valueOf(e.getGrade());
            if (e.getGrade() < 0) pending++;
            m.addRow(new Object[]{e.getStudentNumber(),
                s.getFirstName() + " " + s.getLastName(), e.getCourseCode(), grade});
        }
        lblPendingCount.setText(String.valueOf(pending));
    }

    // Wire: tblGrades > Events > Mouse > mouseClicked
    private void tblGradesMouseClicked(java.awt.event.MouseEvent evt) {
        int r = tblGrades.getSelectedRow();
        String g = tblGrades.getValueAt(r, 3).toString();
        txtGrade.setText(g.equals("Not graded") ? "" : g);
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
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudents = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCourses = new javax.swing.JTable();
        pnlCards = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        scrGrades = new javax.swing.JScrollPane();
        tblGrades = new javax.swing.JTable();

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
                .addGap(15, 15, 15)
                .addComponent(lblTitle)
                .addGap(49, 49, 49)
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 886, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        tabs.addTab("Grades", jPanel3);

        jPanel1.setBackground(new java.awt.Color(240, 244, 250));

        tblStudents.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Student No.", "Name"
            }
        ));
        jScrollPane1.setViewportView(tblStudents);

        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Course", "Registered"
            }
        ));
        jScrollPane2.setViewportView(tblCourses);

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
        jLabel4.setText("Welcome back, Lecturer");

        tblGrades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student No", "Name", "Course Code", "Grades"
            }
        ));
        scrGrades.setViewportView(tblGrades);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(scrGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)))))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlCards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
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
                    .addComponent(tabs))
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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

     public static void main(String args[]) {

    java.awt.EventQueue.invokeLater(() -> {

        new LectureDashboard.setVisible(true);

    }); 

}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JScrollPane scrGrades;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblCourses;
    private javax.swing.JTable tblGrades;
    private javax.swing.JTable tblStudents;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private static class setVisible {

        public setVisible(boolean b) {
        }
    }
}
