package data;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Course;
import model.Enrolment;
import model.Subject;
import model.Student;
import model.User;

/**
 * In-memory data store for the Student Record System.
 * Data resets to these sample records whenever the application starts.
 * Course = academic programme; Module = individual subject.
 */
public final class DataStore {

    private DataStore() { }

    public static final String PROGRAMME_CODE = "BTECH-CE";
    public static final String PROGRAMME_NAME = "BTech Computer Engineering";

    /** Academic programmes/courses. */
    public static final ArrayList<Course> courses = new ArrayList<>();

    /** Individual subjects/modules belonging to a programme. */
    public static final ArrayList<Subject> subjects = new ArrayList<>();

    public static final ArrayList<User> users = new ArrayList<>();
    public static final ArrayList<Student> students = new ArrayList<>();
    public static final ArrayList<Enrolment> enrolments = new ArrayList<>();

    private static final Map<String, String> audits = new HashMap<>();

    static {
        // Demonstration accounts. These are not production credentials.
        users.add(new User("lecturer1", "lect123", "LECTURER"));
        users.add(new User("lecturer2", "lect456", "LECTURER"));
        users.add(new User("admin1", "admin123", "ADMIN"));
        users.add(new User("authority1", "auth123", "AUTHORITY"));

        // The academic programme is the parent of the modules below.
        courses.add(new Course(PROGRAMME_CODE, PROGRAMME_NAME));

        // Module lecturer assignments are temporary demonstration assignments.
        // Replace lecturer1 with the correct lecturer usernames when confirmed.
        subjects.add(new Subject("SDN150S", "Software Design", PROGRAMME_CODE, "lecturer1"));
        subjects.add(new Subject("EMA156S", "Engineering Mathematics 1", PROGRAMME_CODE, "lecturer1"));
        subjects.add(new Subject("ELC153S", "Electronics", PROGRAMME_CODE, "lecturer1"));
        subjects.add(new Subject("EEP150S", "Electrical Engineering Principles", PROGRAMME_CODE, "lecturer2"));

        // 23 sample students. Student numbers are 9-digit strings.
        students.add(new Student("202600001", "Thabo", "Mokoena", "thabo.mokoena@example.com", "Male", "2005-03-14"));
        students.add(new Student("202600002", "Ayesha", "Davids", "ayesha.davids@example.com", "Female", "2004-11-02"));
        students.add(new Student("202600003", "Msizi", "Ndlovu", "msizi.ndlovu@example.com", "Male", "2005-07-21"));
        students.add(new Student("202600004", "Lerato", "Molefe", "lerato.molefe@example.com", "Female", "2004-02-18"));
        students.add(new Student("202600005", "Sipho", "Dlamini", "sipho.dlamini@example.com", "Male", "2005-09-09"));
        students.add(new Student("202600006", "Zanele", "Khumalo", "zanele.khumalo@example.com", "Female", "2003-12-04"));
        students.add(new Student("202600007", "Thando", "Mbeki", "thando.mbeki@example.com", "Male", "2004-06-27"));
        students.add(new Student("202600008", "Naledi", "Pillay", "naledi.pillay@example.com", "Female", "2005-01-16"));
        students.add(new Student("202600009", "Bongani", "Zulu", "bongani.zulu@example.com", "Male", "2004-08-30"));
        students.add(new Student("202600010", "Amahle", "Nkosi", "amahle.nkosi@example.com", "Female", "2005-05-11"));
        students.add(new Student("202600011", "Sibusiso", "Mthembu", "sibusiso.mthembu@example.com", "Male", "2003-10-23"));
        students.add(new Student("202600012", "Keisha", "Daniels", "keisha.daniels@example.com", "Female", "2004-04-07"));
        students.add(new Student("202600013", "Lungelo", "Mkhize", "lungelo.mkhize@example.com", "Male", "2005-11-19"));
        students.add(new Student("202600014", "Nokuthula", "Sithole", "nokuthula.sithole@example.com", "Female", "2004-07-02"));
        students.add(new Student("202600015", "Ayanda", "Cele", "ayanda.cele@example.com", "Male", "2005-02-25"));
        students.add(new Student("202600016", "Mia", "Jacobs", "mia.jacobs@example.com", "Female", "2003-09-14"));
        students.add(new Student("202600017", "Kabelo", "Mokoena", "kabelo.mokoena@example.com", "Male", "2004-12-12"));
        students.add(new Student("202600018", "Zinhle", "Gumede", "zinhle.gumede@example.com", "Female", "2005-06-08"));
        students.add(new Student("202600019", "Lwazi", "Shabalala", "lwazi.shabalala@example.com", "Male", "2004-03-29"));
        students.add(new Student("202600020", "Olwethu", "Mahlangu", "olwethu.mahlangu@example.com", "Female", "2005-08-17"));
        students.add(new Student("202600021", "Mandla", "Zwane", "mandla.zwane@example.com", "Male", "2003-11-06"));
        students.add(new Student("202600022", "Chloe", "Petersen", "chloe.petersen@example.com", "Female", "2004-01-31"));
        students.add(new Student("202600023", "Nathi", "Buthelezi", "nathi.buthelezi@example.com", "Male", "2005-10-05"));

        // Demonstration enrolments. A student can be registered for several modules.
        String[] sdn = {"202600001", "202600002", "202600003", "202600004", "202600005",
            "202600006", "202600007", "202600008", "202600009", "202600010"};
        String[] ema = {"202600001", "202600002", "202600003", "202600004", "202600005",
            "202600006", "202600007", "202600008"};
        String[] elc = {"202600001", "202600003", "202600005", "202600007", "202600009", "202600011"};
        String[] eep = {"202600002", "202600004", "202600006", "202600008", "202600010", "202600012"};
        addSampleEnrolments("SDN150S", sdn);
        addSampleEnrolments("EMA156S", ema);
        addSampleEnrolments("ELC153S", elc);
        addSampleEnrolments("EEP150S", eep);
    }

    private static void addSampleEnrolments(String moduleCode, String[] numbers) {
        for (String number : numbers) {
            enrolments.add(new Enrolment(number, moduleCode));
        }
    }

    public static User login(String username, String password) {
        if (username == null || password == null) return null;
        String supplied = username.trim();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(supplied)
                    && user.checkPassword(password)) return user;
        }
        return null;
    }

    public static Student findStudent(String studentNumber) {
        String key = clean(studentNumber);
        if (key.isEmpty()) return null;
        for (Student student : students) {
            if (student.getStudentNumber().equalsIgnoreCase(key)) return student;
        }
        return null;
    }

    public static List<Student> searchStudents(String keyword) {
        List<Student> result = new ArrayList<>();
        String search = clean(keyword).toLowerCase();
        for (Student student : students) {
            if (student.getStudentNumber().toLowerCase().contains(search)
                    || student.getFirstName().toLowerCase().contains(search)
                    || student.getLastName().toLowerCase().contains(search)
                    || student.getEmail().toLowerCase().contains(search)) {
                result.add(student);
            }
        }
        return result;
    }

    /** Find a programme/course by its programme code. */
    public static Course findProgramme(String programmeCode) {
        String key = clean(programmeCode);
        for (Course course : courses) {
            if (course.getCode().equalsIgnoreCase(key)) return course;
        }
        return null;
    }

    /** Find an individual module by its module code. */
    public static Subject findModule(String subjectCode) {
        String key = clean(subjectCode);
        for (Subject subject : subjects) {
            if (subject.getCode().equalsIgnoreCase(key)) return subject;
        }
        return null;
    }

    /** Return all modules belonging to one academic programme. */
    public static List<Subject> modulesForCourse(String programmeCode) {
        List<Subject> result = new ArrayList<>();
        String key = clean(programmeCode);
        for (Subject subject : subjects) {
            if (subject.getCourseCode().equalsIgnoreCase(key)) result.add(subject);
        }
        return result;
    }

    /** Return modules assigned to the specified lecturer. */
    public static List<Subject> modulesForLecturer(String username) {
        List<Subject> result = new ArrayList<>();
        String key = clean(username);
        for (Subject subject : subjects) {
            if (subject.getLecturerUsername().equalsIgnoreCase(key)) result.add(subject);
        }
        return result;
    }

    /**
     * Compatibility method for older dashboard code. New code should use
     * findProgramme() for programmes or findModule() for module codes.
     * If a module code is supplied, this returns its parent programme.
     */
    @Deprecated
    public static Course findCourse(String code) {
        Course course = findProgramme(code);
        if (course != null) return course;
        Subject subject = findModule(code);
        return subject == null ? null : findProgramme(subject.getCourseCode());
    }

    /** Compatibility method: returns distinct programmes taught by a lecturer. */
    @Deprecated
    public static List<Course> coursesForLecturer(String username) {
        Set<String> programmeCodes = new LinkedHashSet<>();
        for (Subject subject : modulesForLecturer(username)) {
            programmeCodes.add(subject.getCourseCode());
        }
        List<Course> result = new ArrayList<>();
        for (String code : programmeCodes) {
            Course course = findProgramme(code);
            if (course != null) result.add(course);
        }
        return result;
    }

    public static Enrolment findEnrolment(String studentNumber, String moduleCode) {
        String studentKey = clean(studentNumber);
        String moduleKey = clean(moduleCode);
        for (Enrolment enrolment : enrolments) {
            if (enrolment.getStudentNumber().equalsIgnoreCase(studentKey)
                    && enrolment.getModuleCode().equalsIgnoreCase(moduleKey)) return enrolment;
        }
        return null;
    }

    /** Counts registrations for a module code. A programme code returns the total across its modules. */
    public static int countEnrolled(String moduleOrProgrammeCode) {
        String key = clean(moduleOrProgrammeCode);
        Subject subject = findModule(key);
        if (subject != null) {
            int count = 0;
            for (Enrolment e : enrolments) {
                if (e.getModuleCode().equalsIgnoreCase(key)) count++;
            }
            return count;
        }
        int total = 0;
        for (Subject s : modulesForCourse(key)) total += countEnrolled(s.getCode());
        return total;
    }

    public static int countGraded(String moduleOrProgrammeCode) {
        String key = clean(moduleOrProgrammeCode);
        Subject subject = findModule(key);
        int count = 0;
        if (subject != null) {
            for (Enrolment e : enrolments) {
                if (e.getModuleCode().equalsIgnoreCase(key) && e.getGrade() >= 0) count++;
            }
            return count;
        }
        for (Subject s : modulesForCourse(key)) count += countGraded(s.getCode());
        return count;
    }

    public static String addStudent(String studentNumber, String firstName,
            String lastName, String email, String gender, String dob) {
        studentNumber = clean(studentNumber);
        firstName = clean(firstName);
        lastName = clean(lastName);
        email = clean(email);
        gender = clean(gender);
        dob = clean(dob);

        if (!studentNumber.matches("\\d{9}")) return "Student number must contain exactly 9 digits.";
        if (findStudent(studentNumber) != null) return "This student number already exists.";
        if (firstName.isEmpty() || lastName.isEmpty()) return "Enter the student's first and last name.";
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) return "Enter a valid email address.";
        if (gender.isEmpty()) return "Select the student's gender.";
        if (!isValidDate(dob)) return "Date of birth must use YYYY-MM-DD format.";

        students.add(new Student(studentNumber, firstName, lastName, email, gender, dob));
        return null;
    }

    /** Register a student for a module, not for the programme itself. */
    public static String register(String studentNumber, String moduleCode) {
        studentNumber = clean(studentNumber);
        moduleCode = clean(moduleCode).toUpperCase();

        if (!studentNumber.matches("\\d{9}")) return "Student number must contain exactly 9 digits.";
        if (findStudent(studentNumber) == null) return "Student not found.";
        if (findModule(moduleCode) == null) return "Module not found. Enter a valid module code.";
        if (findEnrolment(studentNumber, moduleCode) != null) return "Student is already registered for this module.";

        enrolments.add(new Enrolment(studentNumber, moduleCode));
        audits.remove(auditKey(studentNumber));
        return null;
    }

    public static String deregister(String studentNumber, String moduleCode) {
        studentNumber = clean(studentNumber);
        moduleCode = clean(moduleCode).toUpperCase();

        if (!studentNumber.matches("\\d{9}")) return "Student number must contain exactly 9 digits.";
        if (findModule(moduleCode) == null) return "Module not found.";
        Enrolment enrolment = findEnrolment(studentNumber, moduleCode);
        if (enrolment == null) return "Enrolment not found.";

        enrolments.remove(enrolment);
        audits.remove(auditKey(studentNumber));
        return null;
    }

    public static boolean hasGrade(String studentNumber, String moduleCode) {
        Enrolment enrolment = findEnrolment(studentNumber, moduleCode);
        return enrolment != null && enrolment.getGrade() >= 0;
    }

    public static String updateGrade(String studentNumber, String moduleCode, double grade) {
        if (!Double.isFinite(grade) || grade < 0 || grade > 100) {
            return "Grade must be a number between 0 and 100.";
        }
        Enrolment enrolment = findEnrolment(studentNumber, moduleCode);
        if (enrolment == null) return "The student is not registered for this module.";
        enrolment.setGrade(grade);
        audits.remove(auditKey(studentNumber));
        return null;
    }

    public static String updateStudent(String studentNumber, String firstName,
            String lastName, String email, String gender, String dob) {
        studentNumber = clean(studentNumber);
        firstName = clean(firstName);
        lastName = clean(lastName);
        email = clean(email);
        gender = clean(gender);
        dob = clean(dob);

        if (!studentNumber.matches("\\d{9}")) return "Student number must contain exactly 9 digits.";
        if (firstName.isEmpty() || lastName.isEmpty()) return "First name and last name are required.";
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) return "Enter a valid email address.";
        if (gender.isEmpty()) return "Select the student's gender.";
        if (!isValidDate(dob)) return "Date of birth must use YYYY-MM-DD format.";

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentNumber().equalsIgnoreCase(studentNumber)) {
                students.set(i, new Student(studentNumber, firstName, lastName, email, gender, dob));
                audits.remove(auditKey(studentNumber));
                return null;
            }
        }
        return "Student not found.";
    }

    // Audit methods retained for compatibility with the current Authority form.
    public static String performAudit(String studentNumber) {
        Student student = findStudent(studentNumber);
        if (student == null) return null;

        int enrolled = 0;
        int ungraded = 0;
        for (Enrolment e : enrolments) {
            if (e.getStudentNumber().equals(student.getStudentNumber())) {
                enrolled++;
                if (!hasGrade(e.getStudentNumber(), e.getModuleCode())) ungraded++;
            }
        }

        List<String> issues = new ArrayList<>();
        if (enrolled == 0) issues.add("not registered in any module");
        if (ungraded > 0) issues.add(ungraded + " grade(s) pending");
        if (student.getEmail() == null || !student.getEmail().contains("@")) issues.add("invalid email");

        String result = issues.isEmpty() ? "In compliance"
                : "Not in compliance: " + String.join("; ", issues);
        audits.put(auditKey(student.getStudentNumber()), result);
        return result;
    }

    public static String getAudit(String studentNumber) {
        String result = audits.get(auditKey(studentNumber));
        if (result == null) return null;
        return result.startsWith("Not in compliance") ? "Not in compliance" : "In compliance";
    }

    public static String getAuditDetails(String studentNumber) {
        return audits.get(auditKey(studentNumber));
    }

    public static int countFlagged() {
        int count = 0;
        for (Student student : students) {
            String result = audits.get(auditKey(student.getStudentNumber()));
            if (result != null && result.startsWith("Not in compliance")) count++;
        }
        return count;
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean isValidDate(String value) {
        try {
            LocalDate.parse(value);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private static String auditKey(String studentNumber) {
        return clean(studentNumber).toUpperCase();
    }
}
