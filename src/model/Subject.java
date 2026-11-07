package model;

/** Represents a subject/module that belongs to an academic Course. */
public class Subject {

    private final String code;
    private final String name;
    private final String courseCode;
    private final String lecturerUsername;

    public Subject(String code, String name, String courseCode,
                  String lecturerUsername) {
        this.code = code;
        this.name = name;
        this.courseCode = courseCode;
        this.lecturerUsername = lecturerUsername;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /** Returns the code of the parent academic programme/course. */
    public String getCourseCode() {
        return courseCode;
    }

    public String getLecturerUsername() {
        return lecturerUsername;
    }
}
