/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Course {
    private String code, name, lecturerUsername;

    public Course(String code, String name, String lecturerUsername) {
        this.code = code;
        this.name = name;
        this.lecturerUsername = lecturerUsername;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getLecturerUsername() { return lecturerUsername; }
}
