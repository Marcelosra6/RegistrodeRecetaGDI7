/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.math.BigDecimal;

/**
 * Clase que representa el detalle de una receta médica
 * Corresponde a la tabla 'detalle_receta' de la base de datos
 */
public class DetalleReceta {
    
    private int id;
    private int idReceta;
    private String tipoDeLente;
    private String visionCorregida;
    private String observaciones;
    
    // Ojo Izquierdo
    private BigDecimal esfOjoIzquierdo;
    private BigDecimal cilindroOjoIzquierdo;
    private BigDecimal ejeOjoIzquierdo;
    
    // Ojo Derecho
    private BigDecimal esfOjoDerecho;
    private BigDecimal cilindroOjoDerecho;
    private BigDecimal ejeOjoDerecho;
    
    // Adicionales
    private BigDecimal adicional;
    private int distanciaInterpupilar;
    
    // Constructor vacío
    public DetalleReceta() {
    }
    
    // Constructor sin ID (para insertar)
    public DetalleReceta(int idReceta, String tipoDeLente, String visionCorregida, 
                        String observaciones, BigDecimal esfOjoIzquierdo, 
                        BigDecimal cilindroOjoIzquierdo, BigDecimal ejeOjoIzquierdo,
                        BigDecimal esfOjoDerecho, BigDecimal cilindroOjoDerecho, 
                        BigDecimal ejeOjoDerecho, BigDecimal adicional, 
                        int distanciaInterpupilar) {
        this.idReceta = idReceta;
        this.tipoDeLente = tipoDeLente;
        this.visionCorregida = visionCorregida;
        this.observaciones = observaciones;
        this.esfOjoIzquierdo = esfOjoIzquierdo;
        this.cilindroOjoIzquierdo = cilindroOjoIzquierdo;
        this.ejeOjoIzquierdo = ejeOjoIzquierdo;
        this.esfOjoDerecho = esfOjoDerecho;
        this.cilindroOjoDerecho = cilindroOjoDerecho;
        this.ejeOjoDerecho = ejeOjoDerecho;
        this.adicional = adicional;
        this.distanciaInterpupilar = distanciaInterpupilar;
    }
    
    // Constructor completo
    public DetalleReceta(int id, int idReceta, String tipoDeLente, String visionCorregida,
                        String observaciones, BigDecimal esfOjoIzquierdo,
                        BigDecimal cilindroOjoIzquierdo, BigDecimal ejeOjoIzquierdo,
                        BigDecimal esfOjoDerecho, BigDecimal cilindroOjoDerecho,
                        BigDecimal ejeOjoDerecho, BigDecimal adicional,
                        int distanciaInterpupilar) {
        this.id = id;
        this.idReceta = idReceta;
        this.tipoDeLente = tipoDeLente;
        this.visionCorregida = visionCorregida;
        this.observaciones = observaciones;
        this.esfOjoIzquierdo = esfOjoIzquierdo;
        this.cilindroOjoIzquierdo = cilindroOjoIzquierdo;
        this.ejeOjoIzquierdo = ejeOjoIzquierdo;
        this.esfOjoDerecho = esfOjoDerecho;
        this.cilindroOjoDerecho = cilindroOjoDerecho;
        this.ejeOjoDerecho = ejeOjoDerecho;
        this.adicional = adicional;
        this.distanciaInterpupilar = distanciaInterpupilar;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getTipoDeLente() {
        return tipoDeLente;
    }

    public void setTipoDeLente(String tipoDeLente) {
        this.tipoDeLente = tipoDeLente;
    }

    public String getVisionCorregida() {
        return visionCorregida;
    }

    public void setVisionCorregida(String visionCorregida) {
        this.visionCorregida = visionCorregida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getEsfOjoIzquierdo() {
        return esfOjoIzquierdo;
    }

    public void setEsfOjoIzquierdo(BigDecimal esfOjoIzquierdo) {
        this.esfOjoIzquierdo = esfOjoIzquierdo;
    }

    public BigDecimal getCilindroOjoIzquierdo() {
        return cilindroOjoIzquierdo;
    }

    public void setCilindroOjoIzquierdo(BigDecimal cilindroOjoIzquierdo) {
        this.cilindroOjoIzquierdo = cilindroOjoIzquierdo;
    }

    public BigDecimal getEjeOjoIzquierdo() {
        return ejeOjoIzquierdo;
    }

    public void setEjeOjoIzquierdo(BigDecimal ejeOjoIzquierdo) {
        this.ejeOjoIzquierdo = ejeOjoIzquierdo;
    }

    public BigDecimal getEsfOjoDerecho() {
        return esfOjoDerecho;
    }

    public void setEsfOjoDerecho(BigDecimal esfOjoDerecho) {
        this.esfOjoDerecho = esfOjoDerecho;
    }

    public BigDecimal getCilindroOjoDerecho() {
        return cilindroOjoDerecho;
    }

    public void setCilindroOjoDerecho(BigDecimal cilindroOjoDerecho) {
        this.cilindroOjoDerecho = cilindroOjoDerecho;
    }

    public BigDecimal getEjeOjoDerecho() {
        return ejeOjoDerecho;
    }

    public void setEjeOjoDerecho(BigDecimal ejeOjoDerecho) {
        this.ejeOjoDerecho = ejeOjoDerecho;
    }

    public BigDecimal getAdicional() {
        return adicional;
    }

    public void setAdicional(BigDecimal adicional) {
        this.adicional = adicional;
    }

    public int getDistanciaInterpupilar() {
        return distanciaInterpupilar;
    }

    public void setDistanciaInterpupilar(int distanciaInterpupilar) {
        this.distanciaInterpupilar = distanciaInterpupilar;
    }

    @Override
    public String toString() {
        return "DetalleReceta{" +
                "id=" + id +
                ", idReceta=" + idReceta +
                ", tipoDeLente='" + tipoDeLente + '\'' +
                ", visionCorregida='" + visionCorregida + '\'' +
                '}';
    }
}