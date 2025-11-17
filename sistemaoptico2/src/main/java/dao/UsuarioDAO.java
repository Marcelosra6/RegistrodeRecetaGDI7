/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Usuario;
import util.ConexionDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para gestionar operaciones de Usuario en la base de datos
 */
public class UsuarioDAO {
    
    /**
     * Valida las credenciales de un usuario (LOGIN)
     * @param usuario nombre de usuario
     * @param passwordHash contraseña (por ahora en texto plano, luego encriptaremos)
     * @return Usuario si las credenciales son correctas, null si no
     */
    public Usuario validarUsuario(String usuario, String passwordHash) {
        Usuario user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password_hash = ? AND activo = 1";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, passwordHash);
            
            // LOGS DE DEPURACIÓN
            System.out.println("\n===== DEBUG LOGIN =====");
            System.out.println("Usuario ingresado: '" + usuario + "'");
            System.out.println("Password ingresado: '" + passwordHash + "'");
            System.out.println("SQL: " + sql);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getString("dni_oftalmologo"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_creacion").toLocalDateTime()
                );
                System.out.println("✓ Login exitoso: " + usuario);
                System.out.println("Usuario encontrado en BD: " + user);
            } else {
                System.out.println("✗ No se encontró coincidencia en la BD");
                
                // Verificar si el usuario existe (sin validar password)
                String sqlCheck = "SELECT usuario, password_hash, activo FROM usuarios WHERE usuario = ?";
                PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
                psCheck.setString(1, usuario);
                ResultSet rsCheck = psCheck.executeQuery();
                
                if (rsCheck.next()) {
                    System.out.println("El usuario SÍ existe en la BD:");
                    System.out.println("  Usuario BD: '" + rsCheck.getString("usuario") + "'");
                    System.out.println("  Password BD: '" + rsCheck.getString("password_hash") + "'");
                    System.out.println("  Activo: " + rsCheck.getBoolean("activo"));
                } else {
                    System.out.println("El usuario NO existe en la BD");
                }
                
                rsCheck.close();
                psCheck.close();
            }
            System.out.println("=======================\n");
            
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        
        return user;
    }
    
    /**
     * Inserta un nuevo usuario en la base de datos
     * @param usuario Usuario a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO usuarios (usuario, password_hash, rol, dni_oftalmologo, activo) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getPasswordHash());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getDniOftalmologo());
            ps.setBoolean(5, usuario.isActivo());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Usuario insertado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, ps, conn);
        }
        
        return false;
    }
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM usuarios ORDER BY usuario";
        
        try {
            conn = ConexionDB.getConexion();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Usuario user = new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getString("dni_oftalmologo"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_creacion").toLocalDateTime()
                );
                usuarios.add(user);
            }
            
            System.out.println("✓ Se listaron " + usuarios.size() + " usuarios");
            
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, stmt, conn);
        }
        
        return usuarios;
    }
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param usuario nombre de usuario
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorUsuario(String usuario) {
        Usuario user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM usuarios WHERE usuario = ?";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getString("dni_oftalmologo"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_creacion").toLocalDateTime()
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(rs, ps, conn);
        }
        
        return user;
    }
    
    /**
     * Actualiza un usuario existente
     * @param usuario Usuario con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUsuario(Usuario usuario) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "UPDATE usuarios SET usuario = ?, password_hash = ?, rol = ?, dni_oftalmologo = ?, activo = ? WHERE id = ?";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getPasswordHash());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getDniOftalmologo());
            ps.setBoolean(5, usuario.isActivo());
            ps.setInt(6, usuario.getId());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Usuario actualizado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, ps, conn);
        }
        
        return false;
    }
    
    /**
     * Desactiva un usuario (no lo borra físicamente)
     * @param id ID del usuario a desactivar
     * @return true si se desactivó correctamente
     */
    public boolean desactivarUsuario(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Usuario desactivado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al desactivar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, ps, conn);
        }
        
        return false;
    }
    
    /**
     * Elimina un usuario físicamente de la base de datos
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarUsuario(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("✓ Usuario eliminado correctamente");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(null, ps, conn);
        }
        
        return false;
    }
    
    /**
     * Cierra los recursos de base de datos de forma segura
     */
    private void cerrarRecursos(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}