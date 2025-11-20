package dao;

import modelo.Paciente;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar operaciones CRUD de Paciente
 */
public class PacienteDAO {

    /**
     * Inserta un nuevo paciente
     */
    public boolean insertarPaciente(Paciente paciente) {
        String sql = "INSERT INTO paciente (DNI, nombre, apellido_paterno, apellido_materno) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, paciente.getDni());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getApellidoPaterno());
            ps.setString(4, paciente.getApellidoMaterno());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar paciente: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    /**
     * Obtiene un paciente por DNI
     */
    public Paciente obtenerPorDni(String dni) {
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM paciente WHERE DNI = ?";

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
                paciente.setDni(rs.getString("DNI"));
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
     * Lista todos los pacientes ordenados por apellidos y nombre
     */
    public List<Paciente> listarTodos() {
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM paciente " +
                "ORDER BY apellido_paterno, apellido_materno, nombre";

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
                paciente.setDni(rs.getString("DNI"));
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
     * Busca pacientes por DNI o por nombre/apellidos
     */
    public List<Paciente> buscar(String filtro) {
        // Buscamos por DNI o por cualquier parte del nombre completo
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM paciente " +
                "WHERE DNI LIKE ? " +
                "   OR nombre LIKE ? " +
                "   OR apellido_paterno LIKE ? " +
                "   OR apellido_materno LIKE ? " +
                "ORDER BY apellido_paterno, apellido_materno, nombre";

        List<Paciente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String like = "%" + filtro + "%";

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setDni(rs.getString("DNI"));
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
     * Actualiza los datos de un paciente (por DNI)
     */
    public boolean actualizarPaciente(Paciente paciente) {
        String sql = "UPDATE paciente SET nombre = ?, apellido_paterno = ?, apellido_materno = ? " +
                "WHERE DNI = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellidoPaterno());
            ps.setString(3, paciente.getApellidoMaterno());
            ps.setString(4, paciente.getDni());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    /**
     * Elimina un paciente por DNI
     * (Ten en cuenta que si luego hay FK desde receta, el DELETE puede fallar)
     */
    public boolean eliminarPaciente(String dni) {
        String sql = "DELETE FROM paciente WHERE DNI = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    /**
     * Cierra recursos de forma segura (igual patrón que en UsuarioDAO)
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
