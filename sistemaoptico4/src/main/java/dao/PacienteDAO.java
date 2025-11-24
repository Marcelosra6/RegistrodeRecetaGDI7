package dao;

import modelo.Paciente;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Paciente
 * Actualizado para usar procedimientos almacenados y funciones de PostgreSQL
 */
public class PacienteDAO {

    /**
     * Inserta un nuevo paciente usando el procedimiento almacenado
     */
    public boolean insertarPaciente(Paciente paciente) {
        // ✅ CAMBIO: Quitar las llaves {}
        String sql = "CALL proced_ins_pas(?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null; // ✅ CAMBIO: Usar PreparedStatement en lugar de CallableStatement

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql); // ✅ CAMBIO: prepareStatement en lugar de prepareCall
            ps.setString(1, paciente.getDni());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellidoPaterno());
            ps.setString(4, paciente.getApellidoMaterno());

            ps.execute();
            System.out.println("✅ Paciente insertado correctamente");
            return true;

        } catch (SQLException e) {
            System.err.println("===== ERROR AL INSERTAR PACIENTE =====");
            System.err.println("DNI: " + paciente.getDni());
            System.err.println("Nombre: " + paciente.getNombre());
            System.err.println("Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            System.err.println("======================================");
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    /**
     * Obtiene un paciente por DNI usando la función almacenada
     */
    public Paciente obtenerPorDni(String dni) {
        String sql = "SELECT * FROM proced_sel_pas_dni(?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Paciente paciente = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new Paciente();
                paciente.setDni(rs.getString("dni").trim());
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellidoPaterno(rs.getString("apellido_paterno"));
                paciente.setApellidoMaterno(rs.getString("apellido_materno"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por DNI: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return paciente;
    }

    /**
     * Lista todos los pacientes usando la función almacenada
     */
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM proced_lis_pas_todo()";

        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setDni(rs.getString("dni").trim());
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellidoPaterno(rs.getString("apellido_paterno"));
                paciente.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(paciente);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return lista;
    }

    /**
     * Busca pacientes por DNI o por nombre/apellidos usando la función almacenada
     */
    public List<Paciente> buscar(String filtro) {
        String sql = "SELECT * FROM proced_lis_pas_filtro(?)";

        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, filtro);
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setDni(rs.getString("dni").trim());
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellidoPaterno(rs.getString("apellido_paterno"));
                paciente.setApellidoMaterno(rs.getString("apellido_materno"));
                lista.add(paciente);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pacientes: " + e.getMessage());
        } finally {
            cerrarRecursos(rs, ps, conn);
        }

        return lista;
    }

    /**
     * Actualiza los datos de un paciente usando el procedimiento almacenado
     */
    public boolean actualizarPaciente(Paciente paciente) {
        String sql = "CALL proced_upd_pas(?, ?, ?, ?)";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, paciente.getDni());
            cs.setString(2, paciente.getNombre());
            cs.setString(3, paciente.getApellidoPaterno());
            cs.setString(4, paciente.getApellidoMaterno());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, cs, conn);
        }
    }

    /**
     * Elimina un paciente por DNI usando el procedimiento almacenado
     */
    public boolean eliminarPaciente(String dni) {
        String sql = "CALL proced_eli_pas(?)";

        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = ConexionDB.getConexion();
            cs = conn.prepareCall(sql);
            cs.setString(1, dni);

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
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