/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase que representa una enfermedad asociada a una receta
 * Corresponde a la tabla 'enfermedad' de la base de datos
 */
public class Enfermedad {
    
    private int idReceta;
    private String enfermedad;
    
    // Constructor vacío
    public Enfermedad() {
    }
    
    // Constructor completo
    public Enfermedad(int idReceta, String enfermedad) {
        this.idReceta = idReceta;
        this.enfermedad = enfermedad;
    }

    // Getters y Setters
    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    @Override
    public String toString() {
        return "Enfermedad{" +
                "idReceta=" + idReceta +
                ", enfermedad='" + enfermedad + '\'' +
                '}';
    }
}