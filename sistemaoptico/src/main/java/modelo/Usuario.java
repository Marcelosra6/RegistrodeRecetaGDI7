/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa un usuario del sistema
 * Corresponde a la tabla 'usuario' de la base de datos
 */
public class Usuario {
    
    private int id;
    private String usuario;
    private String passwordHash;
    private String rol; // "admin" u "oftalmologo"
    private String dniOftalmologo; // Puede ser null
    private boolean activo;
    private LocalDateTime fechaCreacion;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor para login (solo usuario y password)
    public Usuario(String usuario, String passwordHash) {
        this.usuario = usuario;
        this.passwordHash = passwordHash;
    }
    
    // Constructor completo (sin id ni fecha_creacion - para insertar)
    public Usuario(String usuario, String passwordHash, String rol, 
                   String dniOftalmologo, boolean activo) {
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.dniOftalmologo = dniOftalmologo;
        this.activo = activo;
    }
    
    // Constructor completo con todos los campos (para consultas)
    public Usuario(int id, String usuario, String passwordHash, String rol,
                   String dniOftalmologo, boolean activo, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuario = usuario;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.dniOftalmologo = dniOftalmologo;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDniOftalmologo() {
        return dniOftalmologo;
    }

    public void setDniOftalmologo(String dniOftalmologo) {
        this.dniOftalmologo = dniOftalmologo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    // Método para verificar si es administrador
    public boolean esAdmin() {
        return "admin".equalsIgnoreCase(this.rol);
    }
    
    // Método para verificar si es oftalmólogo
    public boolean esOftalmologo() {
        return "oftalmologo".equalsIgnoreCase(this.rol);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}