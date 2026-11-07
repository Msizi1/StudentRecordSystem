package model;

/** A student's registration for one module. */
public class Enrolment {

    private final String studentNumber;
    private final String moduleCode;
    private double grade; // -1 means not graded yet

    public Enrolment(String studentNumber, String moduleCode) {
        this.studentNumber = studentNumber;
        this.moduleCode = moduleCode;
        this.grade = -1;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    /** Compatibility alias for older forms; this value is a module code. */
    @Deprecated
    public String getCourseCode() {
        return moduleCode;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
