/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos MySQL en XAMPP
 */
public class ConexionDB {
    
    // Configuración de conexión para XAMPP
    private static final String URL = "jdbc:mysql://localhost:3307/OpticaDB";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // XAMPP por defecto no tiene password
    
    // Driver de MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Parámetros adicionales para evitar warnings
    private static final String PARAMETROS = "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si hay error en la conexión
     */
    public static Connection getConexion() throws SQLException {
        Connection conexion = null;
        
        try {
            // Cargar el driver de MySQL
            Class.forName(DRIVER);
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL + PARAMETROS, USUARIO, PASSWORD);
            
            System.out.println("✓ Conexión exitosa a la base de datos OpticaDB");
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: No se encontró el driver de MySQL");
            System.err.println("Asegúrate de tener mysql-connector-java en Libraries");
            throw new SQLException("Driver no encontrado", e);
            
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos");
            System.err.println("Verifica que MySQL esté corriendo en XAMPP");
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
                System.out.println("✓ Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("✗ Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Método de prueba para verificar la conexión
     */
    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CONEXIÓN A MYSQL (XAMPP) ===\n");
        
        Connection conexion = null;
        
        try {
            conexion = getConexion();
            
            if (conexion != null && !conexion.isClosed()) {
                System.out.println("\n¡CONEXIÓN EXITOSA!");
                System.out.println("Base de datos: OpticaDB");
                System.out.println("Servidor: localhost:3307");
            }
            
        } catch (SQLException e) {
            System.err.println("\n¡ERROR DE CONEXIÓN!");
            System.err.println("\nVerifica:");
            System.err.println("1. XAMPP está abierto");
            System.err.println("2. MySQL está iniciado (botón verde)");
            System.err.println("3. La base de datos 'OpticaDB' existe");
            System.err.println("4. mysql-connector-java.jar está en Libraries");
            
        } finally {
            cerrarConexion(conexion);
        }
    }
}