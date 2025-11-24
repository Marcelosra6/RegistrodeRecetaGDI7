
package dao;

import modelo.Oftalmologo;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Oftalmólogo
 * Actualizado para usar procedimientos almacenados y funciones de PostgreSQL
 */
public class OftalmologoDAO {

    /**
     * Inserta un nuevo oftalmólogo usando el procedimiento almacenado
     */
    public boolean insertarOftalmologo(Oftalmologo oft) {
        String sql = "CALL proced_ins_oft(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, oft.getDni());
            cs.setString(2, oft.getNombre());
            cs.setString(3, oft.getApellidoPaterno());
            cs.setString(4, oft.getApellidoMaterno());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }

    /**
     * Obtiene un oftalmólogo por DNI usando la función almacenada
     */
    public Oftalmologo obtenerPorDni(String dni) {
        String sql = "SELECT * FROM proced_sel_oft_dni(?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Oftalmologo oft = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();

            if (rs.next()) {
                oft = new Oftalmologo();
                oft.setDni(rs.getString("dni").trim());
                oft.setNombre(rs.getString("nombre"));
                oft.setApellidoPaterno(rs.getString("apellido_paterno"));
                oft.setApellidoMaterno(rs.getString("apellido_materno"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener oftalmólogo por DNI: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return oft;
    }

    /**
     * Lista todos los oftalmólogos usando la función almacenada
     */
    public List<Oftalmologo> listarTodos() {
        String sql = "SELECT * FROM proced_lis_oft_todo()";

        List<Oftalmologo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Oftalmologo oft = new Oftalmologo();
                oft.setDni(rs.getString("dni").trim());
                oft.setNombre(rs.getString("nombre"));
                oft.setApellidoPaterno(rs.getString("apellido_paterno"));
                oft.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(oft);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar oftalmólogos: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return lista;
    }

    /**
     * Busca oftalmólogos por DNI o nombre usando la función almacenada
     */
    public List<Oftalmologo> buscar(String filtro) {
        String sql = "SELECT * FROM proced_lis_oft_filtro(?)";

        List<Oftalmologo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, filtro);
            rs = ps.executeQuery();

            while (rs.next()) {
                Oftalmologo oft = new Oftalmologo();
                oft.setDni(rs.getString("dni").trim());
                oft.setNombre(rs.getString("nombre"));
                oft.setApellidoPaterno(rs.getString("apellido_paterno"));
                oft.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(oft);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar oftalmólogos: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return lista;
    }

    /**
     * Actualiza los datos de un oftalmólogo usando el procedimiento almacenado
     */
    public boolean actualizarOftalmologo(Oftalmologo oft) {
        String sql = "CALL proced_upd_oft(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, oft.getDni());
            cs.setString(2, oft.getNombre());
            cs.setString(3, oft.getApellidoPaterno());
            cs.setString(4, oft.getApellidoMaterno());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }

    /**
     * Elimina un oftalmólogo por DNI usando el procedimiento almacenado
     * Puede fallar por FK desde receta o usuarios.dni_oftalmologo
     */
    public boolean eliminarOftalmologo(String dni) {
        String sql = "CALL proced_eli_oft(?)";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, dni);

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }

    /**
     * Cierra recursos de forma segura
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