/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa un oftalmólogo
 * Corresponde a la tabla 'oftalmologo' de la base de datos
 */
public class Oftalmologo {
    
    private String dni;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    
    // Constructor vacío
    public Oftalmologo() {
    }
    
    // Constructor completo
    public Oftalmologo(String dni, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    /**
     * Obtiene el nombre completo del oftalmólogo
     */
    public String getNombreCompleto() {
        return "Dr(a). " + apellidoPaterno + " " + apellidoMaterno + ", " + nombre;
    }

    // En Oftalmologo.java
    @Override
    public String toString() {
        return dni + " - " + apellidoPaterno + " " + apellidoMaterno + ", " + nombre;
    }


    /*@Override
    public String toString() {
        return "Oftalmologo{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                '}';
    }*/
}