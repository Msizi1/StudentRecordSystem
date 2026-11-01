/**
 * Represents a student in the CPUT Faculty of Engineering and the Built Environment.
 * Stores personal and academic records.
 */
package my.studentrecords;

// Constructor Initialized
public class Student {
    
    private String studentID;
    private String fullName;
    private String course;
    private String registrationStatus;
    
    public Student(String studentID, String fullName, String course, String registrationStatus){
        this.studentID = studentID;
        this.fullName = fullName;
        this.course = course;
        this.registrationStatus = registrationStatus;
    }
    
    // Used getter and setter to get student Information
    public String getStudentID(){
        return studentID;
    }
    
    public void setStudentID(String studentID){
        this.studentID = studentID;
    }
    
    public String getFullName(){
        return fullName;
    }
    
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    
    public String getCourse(){
        return course;
    }
    
    public void setCourse(String course){
        this.course = course;
    }
    
    public String getRegistrationStatus(){
        return registrationStatus;
    }
    
    public void setRegistrationStatus(String registrationStatus){
        this.registrationStatus = registrationStatus;
    }
    
}
