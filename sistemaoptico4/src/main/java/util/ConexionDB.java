/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos PostgreSQL
 */
public class ConexionDB {
    
    // Configuración de conexión para PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/opticadb";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "root"; // ⚠️ Tu password de PostgreSQL
    
    // Driver de PostgreSQL
    private static final String DRIVER = "org.postgresql.Driver";
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si hay error en la conexión
     */
    public static Connection getConexion() throws SQLException {
        Connection conexion = null;
        
        try {
            // Cargar el driver de PostgreSQL
            Class.forName(DRIVER);
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            
            System.out.println("Conexión exitosa a PostgreSQL - opticadb");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de PostgreSQL");
            System.err.println("Asegúrate de tener postgresql en Dependencies (pom.xml)");
            throw new SQLException("Driver no encontrado", e);
            
        } catch (SQLException e) {
            System.err.println("Error al conectar con PostgreSQL");
            System.err.println("Verifica que PostgreSQL esté corriendo en el puerto 5432");
            System.err.println("Detalles: " + e.getMessage());
            throw e;
        }
        
        return conexion;
    }
    
    /**
     * Cierra la conexión de forma segura
     * @param conexion Connection a cerrar
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Método de prueba para verificar la conexión
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXIÓN A POSTGRESQL ===\n");
        
        Connection conexion = null;
        
        try {
            conexion = getConexion();
            
            if (conexion != null && !conexion.isClosed()) {
                System.out.println("\n¡CONEXIÓN EXITOSA!");
                System.out.println("Base de datos: opticadb");
                System.out.println("Servidor: localhost:5432 (PostgreSQL)");
                System.out.println("Usuario: postgres");
            }
            
        } catch (SQLException e) {
            System.err.println("\n¡ERROR DE CONEXIÓN!");
            System.err.println("\nVerifica:");
            System.err.println("1. PostgreSQL está instalado e iniciado");
            System.err.println("2. El servicio está corriendo en el puerto 5432");
            System.err.println("3. La base de datos 'opticadb' existe");
            System.err.println("4. El usuario 'postgres' y password son correctos");
            System.err.println("5. El driver postgresql está en Dependencies");
            
        } finally {
            cerrarConexion(conexion);
        }
    }
}