/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa una receta médica
 * Corresponde a la tabla 'receta' de la base de datos
 */
public class Receta {
    
    private int idReceta;
    private String dniPaciente;
    private String dniOftalmologo;
    private LocalDateTime fechaExamen;
    
    // Objetos relacionados (para mostrar info completa)
    private Paciente paciente;
    private Oftalmologo oftalmologo;
    
    // Constructor vacío
    public Receta() {
    }
    
    // Constructor sin ID (para insertar)
    public Receta(String dniPaciente, String dniOftalmologo, LocalDateTime fechaExamen) {
        this.dniPaciente = dniPaciente;
        this.dniOftalmologo = dniOftalmologo;
        this.fechaExamen = fechaExamen;
    }
    
    // Constructor completo
    public Receta(int idReceta, String dniPaciente, String dniOftalmologo, LocalDateTime fechaExamen) {
        this.idReceta = idReceta;
        this.dniPaciente = dniPaciente;
        this.dniOftalmologo = dniOftalmologo;
        this.fechaExamen = fechaExamen;
    }

    // Getters y Setters
    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }

    public String getDniOftalmologo() {
        return dniOftalmologo;
    }

    public void setDniOftalmologo(String dniOftalmologo) {
        this.dniOftalmologo = dniOftalmologo;
    }

    public LocalDateTime getFechaExamen() {
        return fechaExamen;
    }

    public void setFechaExamen(LocalDateTime fechaExamen) {
        this.fechaExamen = fechaExamen;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Oftalmologo getOftalmologo() {
        return oftalmologo;
    }

    public void setOftalmologo(Oftalmologo oftalmologo) {
        this.oftalmologo = oftalmologo;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idReceta=" + idReceta +
                ", dniPaciente='" + dniPaciente + '\'' +
                ", dniOftalmologo='" + dniOftalmologo + '\'' +
                ", fechaExamen=" + fechaExamen +
                '}';
    }
}
