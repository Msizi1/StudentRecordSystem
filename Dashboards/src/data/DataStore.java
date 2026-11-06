/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java 
 */
package data;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class DataStore {
    public static final ArrayList<User> users = new ArrayList<>();
    public static final ArrayList<Student> students = new ArrayList<>();
    public static final ArrayList<Course> courses = new ArrayList<>();
    public static final ArrayList<Enrolment> enrolments = new ArrayList<>();

   
    private static final java.util.Set<String> graded = new java.util.HashSet<>();

   
    private static final java.util.Map<String, String> audits = new java.util.HashMap<>();


    static {
        users.add(new User("lecturer1", "lect123", "LECTURER"));
        users.add(new User("admin1", "admin123", "ADMIN"));
        users.add(new User("authority1", "auth123", "AUTHORITY"));

        students.add(new Student("S1001", "Thabo", "Mokoena", "thabo@cput.ac.za", "Male", "2005-03-14"));
        students.add(new Student("S1002", "Ayesha", "Davids", "ayesha@cput.ac.za", "Female", "2004-11-02"));
        students.add(new Student("S1003", "Msizi", "Ndlovu", "msizi@cput.ac.za", "Male", "2005-07-21"));

        courses.add(new Course("CE101", "Computer Engineering", "lecturer1"));
        courses.add(new Course("CA102", "Computer Architecture", "lecturer1"));
        courses.add(new Course("EM103", "Engineering Mathematics", "other"));

        enrolments.add(new Enrolment("S1001", "CE101"));
        enrolments.add(new Enrolment("S1002", "CE101"));
        enrolments.add(new Enrolment("S1003", "CA102"));
    }

   
    public static User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.checkPassword(password)) return u;
        }
        return null;
    }

   
    public static Student findStudent(String studentNumber) {
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(studentNumber)) return s;
        }
        return null;
    }

    public static List<Student> searchStudents(String keyword) {
        List<Student> result = new ArrayList<>();
        String k = keyword.toLowerCase();
        for (Student s : students) {
            if (s.getStudentNumber().toLowerCase().contains(k)
                    || s.getFirstName().toLowerCase().contains(k)
                    || s.getLastName().toLowerCase().contains(k)) {
                result.add(s);
            }
        }
        return result;
    }

    
    public static Course findCourse(String code) {
        for (Course c : courses) {
            if (c.getCode().equalsIgnoreCase(code)) return c;
        }
        return null;
    }

   
    public static List<Course> coursesForLecturer(String username) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c.getLecturerUsername().equals(username)) result.add(c);
        }
        return result;
    }

   
    public static int countEnrolled(String courseCode) {
        int count = 0;
        for (Enrolment e : enrolments) {
            if (e.getCourseCode().equals(courseCode)) count++;
        }
        return count;
    }

    public static Enrolment findEnrolment(String studentNumber, String courseCode) {
        for (Enrolment e : enrolments) {
            if (e.getStudentNumber().equals(studentNumber) && e.getCourseCode().equals(courseCode)) return e;
        }
        return null;
    }

    
    public static String register(String studentNumber, String courseCode) {
        if (findStudent(studentNumber) == null) return "Student not found.";
        if (findCourse(courseCode) == null) return "Course not found.";
        if (findEnrolment(studentNumber, courseCode) != null) return "Already registered.";
        enrolments.add(new Enrolment(studentNumber, courseCode));
        audits.remove(auditKey(studentNumber));
        return null;
    }

    public static String deregister(String studentNumber, String courseCode) {
        Enrolment e = findEnrolment(studentNumber, courseCode);
        if (e == null) return "Enrolment not found.";
        enrolments.remove(e);
        graded.remove(gradeKey(studentNumber, courseCode));   
        audits.remove(auditKey(studentNumber));
        return null;
    }


    public static String updateGrade(String studentNumber, String courseCode, double grade) {
        if (grade < 0 || grade > 100) return "Grade must be between 0 and 100.";
        Enrolment e = findEnrolment(studentNumber, courseCode);
        if (e == null) return "Enrolment not found.";
        e.setGrade(grade);
        graded.add(gradeKey(studentNumber, courseCode));
        audits.remove(auditKey(studentNumber));
        return null;
    }

    
    public static boolean hasGrade(String studentNumber, String courseCode) {
        return graded.contains(gradeKey(studentNumber, courseCode));
    }

 
    public static int countGraded(String courseCode) {
        int count = 0;
        for (Enrolment e : enrolments) {
            if (e.getCourseCode().equals(courseCode) && hasGrade(e.getStudentNumber(), courseCode)) count++;
        }
        return count;
    }

   
    public static String updateStudent(String studentNumber, String first, String last,
                                       String email, String gender, String dob) {
        first = first.trim(); last = last.trim(); email = email.trim();
        gender = gender.trim(); dob = dob.trim();
        if (first.isEmpty() || last.isEmpty()) return "First name and last name are required.";
        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) return "Enter a valid email address.";
        if (gender.isEmpty()) return "Gender is required.";
        try {
            java.time.LocalDate.parse(dob);
        } catch (java.time.format.DateTimeParseException ex) {
            return "Date of birth must look like 2005-03-14.";
        }
        for (int i = 0; i < students.size(); i++) {
            Student old = students.get(i);
            if (old.getStudentNumber().equalsIgnoreCase(studentNumber)) {
                students.set(i, new Student(old.getStudentNumber(), first, last, email, gender, dob));
                audits.remove(auditKey(old.getStudentNumber()));   
                return null;
            }
        }
        return "Student not found.";
    }

   
    public static String performAudit(String studentNumber) {
        Student s = findStudent(studentNumber);
        if (s == null) return null;
        int enrolled = 0, ungraded = 0;
        for (Enrolment e : enrolments) {
            if (e.getStudentNumber().equals(s.getStudentNumber())) {
                enrolled++;
                if (!hasGrade(e.getStudentNumber(), e.getCourseCode())) ungraded++;
            }
        }
        List<String> issues = new ArrayList<>();
        if (enrolled == 0) issues.add("not registered in any course");
        if (ungraded > 0) issues.add(ungraded + " grade(s) pending");
        if (s.getEmail() == null || !s.getEmail().contains("@")) issues.add("invalid email");
        String result = issues.isEmpty() ? "In compliance"
                : "Not in compliance: " + String.join("; ", issues);
        audits.put(auditKey(s.getStudentNumber()), result);
        return result;
    }

    
    public static String getAudit(String studentNumber) {
        String r = audits.get(auditKey(studentNumber));
        if (r == null) return null;
        return r.startsWith("Not in compliance") ? "Not in compliance" : "In compliance";
    }

    
    public static String getAuditDetails(String studentNumber) {
        return audits.get(auditKey(studentNumber));
    }

    public static int countFlagged() {
        int n = 0;
        for (Student s : students) {
            String r = audits.get(auditKey(s.getStudentNumber()));
            if (r != null && r.startsWith("Not in compliance")) n++;
        }
        return n;
    }

    private static String auditKey(String studentNumber) {
        return studentNumber.toUpperCase();
    }

    private static String gradeKey(String studentNumber, String courseCode) {
        return studentNumber.toUpperCase() + "|" + courseCode.toUpperCase();
    }
}
