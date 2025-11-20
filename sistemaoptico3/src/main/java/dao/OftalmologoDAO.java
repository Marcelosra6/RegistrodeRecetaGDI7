package dao;

import modelo.Oftalmologo;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OftalmologoDAO {

    public boolean insertarOftalmologo(Oftalmologo oft) {
        String sql = "INSERT INTO oftalmologo (DNI, nombre, apellido_paterno, apellido_materno) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, oft.getDni());
            ps.setString(2, oft.getNombre());
            ps.setString(3, oft.getApellidoPaterno());
            ps.setString(4, oft.getApellidoMaterno());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    public Oftalmologo obtenerPorDni(String dni) {
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM oftalmologo WHERE DNI = ?";

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
                oft.setDni(rs.getString("DNI"));
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

    public List<Oftalmologo> listarTodos() {
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM oftalmologo " +
                "ORDER BY apellido_paterno, apellido_materno, nombre";

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
                oft.setDni(rs.getString("DNI"));
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

    public List<Oftalmologo> buscar(String filtro) {
        String sql = "SELECT DNI, nombre, apellido_paterno, apellido_materno " +
                "FROM oftalmologo " +
                "WHERE DNI LIKE ? " +
                "   OR nombre LIKE ? " +
                "   OR apellido_paterno LIKE ? " +
                "   OR apellido_materno LIKE ? " +
                "ORDER BY apellido_paterno, apellido_materno, nombre";

        List<Oftalmologo> lista = new ArrayList<>();
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
                Oftalmologo oft = new Oftalmologo();
                oft.setDni(rs.getString("DNI"));
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

    public boolean actualizarOftalmologo(Oftalmologo oft) {
        String sql = "UPDATE oftalmologo SET nombre = ?, apellido_paterno = ?, apellido_materno = ? " +
                "WHERE DNI = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, oft.getNombre());
            ps.setString(2, oft.getApellidoPaterno());
            ps.setString(3, oft.getApellidoMaterno());
            ps.setString(4, oft.getDni());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

    public boolean eliminarOftalmologo(String dni) {
        String sql = "DELETE FROM oftalmologo WHERE DNI = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionDB.getConexion();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            // Puede fallar por FK desde receta o usuarios.dni_oftalmologo
            System.err.println("Error al eliminar oftalmólogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(null, ps, conn);
        }
    }

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
