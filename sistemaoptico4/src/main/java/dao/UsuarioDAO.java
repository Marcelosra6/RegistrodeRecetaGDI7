package dao;

import modelo.Usuario;
import util.ConexionDB;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones de Usuario
 * Actualizado para usar procedimientos almacenados y funciones de PostgreSQL
 */
public class UsuarioDAO {
    
    /**
     * Valida las credenciales de un usuario (LOGIN)
     * Usa la función almacenada proced_val_usu_login
     */
    public Usuario validarUsuario(String usuario, String passwordHash) {
        Usuario user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM proced_val_usu_login(?, ?)";
        
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
                    rs.getString("dni_oftalmologo") != null ? rs.getString("dni_oftalmologo").trim() : null,
                    rs.getBoolean("activo"),
                    rs.getTimestamp("fecha_creacion").toLocalDateTime()
                );
                System.out.println("✓ Login exitoso: " + usuario);
                System.out.println("Usuario encontrado en BD: " + user);
            } else {
                System.out.println("✗ No se encontró coincidencia en la BD");
                
                // Verificar si el usuario existe usando la función almacenada
                String sqlCheck = "SELECT * FROM proced_ver_usu_existe(?)";
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
     * Inserta un nuevo usuario usando el procedimiento almacenado
     */
    public boolean insertarUsuario(Usuario usuario) {
        Connection conn = null;
        CallableStatement cs = null;
        
        String sql = "CALL proced_ins_usu(?, ?, ?, ?, ?)";
        
        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, usuario.getUsuario());
            cs.setString(2, usuario.getPasswordHash());
            cs.setString(3, usuario.getRol());
            cs.setString(4, usuario.getDniOftalmologo());
            cs.setBoolean(5, usuario.isActivo());
            
            cs.execute();
            System.out.println("✓ Usuario insertado correctamente");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }
    
    /**
     * Obtiene todos los usuarios usando la función almacenada
     */
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM proced_lis_usu_todo()";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Usuario user = new Usuario(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getString("dni_oftalmologo") != null ? rs.getString("dni_oftalmologo").trim() : null,
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
            cerrarRecursos(rs, ps, conn);
        }
        
        return usuarios;
    }
    
    /**
     * Busca un usuario por su nombre de usuario
     * Usa la función almacenada proced_ver_usu_existe
     */
    public Usuario buscarPorUsuario(String usuario) {
        Usuario user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM proced_ver_usu_existe(?)";
        
        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Esta función solo devuelve usuario, password_hash y activo
                // Para obtener todos los datos, mejor usar proced_lis_usu_todo con filtro manual
                user = new Usuario();
                user.setUsuario(rs.getString("usuario"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setActivo(rs.getBoolean("activo"));
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
     * Actualiza un usuario usando el procedimiento almacenado
     */
    public boolean actualizarUsuario(Usuario usuario) {
        Connection conn = null;
        CallableStatement cs = null;
        
        String sql = "CALL proced_upd_usu(?, ?, ?, ?, ?, ?)";
        
        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setInt(1, usuario.getId());
            cs.setString(2, usuario.getUsuario());
            cs.setString(3, usuario.getPasswordHash());
            cs.setString(4, usuario.getRol());
            cs.setString(5, usuario.getDniOftalmologo());
            cs.setBoolean(6, usuario.isActivo());
            
            cs.execute();
            System.out.println("✓ Usuario actualizado correctamente");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }
    
    /**
     * Desactiva un usuario (baja lógica) usando el procedimiento almacenado
     */
    public boolean desactivarUsuario(int id) {
        Connection conn = null;
        CallableStatement cs = null;
        
        String sql = "CALL proced_baja_usu(?)";
        
        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            
            cs.execute();
            System.out.println("✓ Usuario desactivado correctamente");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al desactivar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }
    
    /**
     * Elimina un usuario físicamente usando el procedimiento almacenado
     */
    public boolean eliminarUsuario(int id) {
        Connection conn = null;
        CallableStatement cs = null;
        
        String sql = "CALL proced_eli_usu(?)";
        
        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            
            cs.execute();
            System.out.println("✓ Usuario eliminado correctamente");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
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