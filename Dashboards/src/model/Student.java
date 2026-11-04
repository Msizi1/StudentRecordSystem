/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Student {
    private String studentNumber, firstName, lastName, email, gender, dob;

    public Student(String studentNumber, String firstName, String lastName,
                   String email, String gender, String dob) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
    }

    public String getStudentNumber() { return studentNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getDob() { return dob; }
}