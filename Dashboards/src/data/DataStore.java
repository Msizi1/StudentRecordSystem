/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

    // Sample data, loaded once when the class is first used
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

    // ---- Login ----
    public static User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.checkPassword(password)) return u;
        }
        return null;
    }

    // ---- Students ----
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

    // ---- Courses ----
    public static Course findCourse(String code) {
        for (Course c : courses) {
            if (c.getCode().equalsIgnoreCase(code)) return c;
        }
        return null;
    }

    // Lecturers see only their own courses
    public static List<Course> coursesForLecturer(String username) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (c.getLecturerUsername().equals(username)) result.add(c);
        }
        return result;
    }

    // ---- Enrolments ----
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

    // Returns an error message, or null on success
    public static String register(String studentNumber, String courseCode) {
        if (findStudent(studentNumber) == null) return "Student not found.";
        if (findCourse(courseCode) == null) return "Course not found.";
        if (findEnrolment(studentNumber, courseCode) != null) return "Already registered.";
        enrolments.add(new Enrolment(studentNumber, courseCode));
        return null;
    }

    public static String deregister(String studentNumber, String courseCode) {
        Enrolment e = findEnrolment(studentNumber, courseCode);
        if (e == null) return "Enrolment not found.";
        enrolments.remove(e);
        return null;
    }

    // ---- Grades ----
    public static String updateGrade(String studentNumber, String courseCode, double grade) {
        if (grade < 0 || grade > 100) return "Grade must be between 0 and 100.";
        Enrolment e = findEnrolment(studentNumber, courseCode);
        if (e == null) return "Enrolment not found.";
        e.setGrade(grade);
        return null;
    }
}
