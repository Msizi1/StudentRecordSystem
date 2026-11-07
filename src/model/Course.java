package model;

/**
 * Represents an academic programme/course, for example
 * BTech Computer Engineering. Individual subjects belong in Module.
 */
public class Course {

    private final String code;
    private final String name;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * Compatibility method for older dashboard code.
     * Lecturer assignment now belongs to each Module, not the programme.
     */
    @Deprecated
    public String getLecturerUsername() {
        return "See module assignments";
    }
}
