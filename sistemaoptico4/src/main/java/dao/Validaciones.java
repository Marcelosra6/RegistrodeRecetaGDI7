/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

public class Validaciones {
    
    public static String convertirYValidarDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return null;
        }
        try {
            long numero = Long.parseLong(dni.trim());
            if (numero <= 0) {
                return null; 
            }
            if (numero > 99999999) {
                return null; 
            }
            return String.format("%08d", numero);  
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
}
