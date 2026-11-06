/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Enrolment {
    private String studentNumber;
    private String courseCode;
    private double grade; // -1 means not graded yet

    public Enrolment(String studentNumber, String courseCode) {
        this.studentNumber = studentNumber;
        this.courseCode = courseCode;
        this.grade = -1;
    }

    public String getStudentNumber() { 
        return studentNumber; 
    }
    public String getCourseCode() { 
        return courseCode; 
    }
    public double getGrade() { 
        return grade; 
    }
    public void setGrade(double grade) { 
        this.grade = grade; 
    }
}