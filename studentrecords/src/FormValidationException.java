/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MAZIZ
 */
import java.util.List;

public class FormValidationException extends Exception {
    private final List<String> errorList;

    public FormValidationException(List<String> errorList) {
        super("Form validation failed with " + errorList.size() + " errors.");
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}



    
    

 
    
    

