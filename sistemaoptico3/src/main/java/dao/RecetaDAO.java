package dao;

import modelo.DetalleReceta;
import modelo.Oftalmologo;
import modelo.Paciente;
import modelo.Receta;
import util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RecetaDAO {

    /**
     * Crea una receta, su detalle y sus enfermedades en una sola transacción.
     * Devuelve el id_receta generado o -1 si falla.
     */
    public int crearRecetaConDetalle(
            Receta receta,
            DetalleReceta detalle,
            List<String> enfermedades) throws SQLException {

        String sqlReceta = "INSERT INTO receta (DNI_paciente, DNI_oftalmologo, fecha_examen) " +
                "VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_receta (id_receta, tipo_de_lente, vision_corregida, " +
                "observaciones, esf_ojo_izquierdo, cilindro_ojo_izquierdo, eje_ojo_izquierdo, " +
                "esf_ojo_derecho, cilindro_ojo_derecho, eje_ojo_derecho, adicional, distancia_interpupilar) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        String sqlEnf = "INSERT INTO enfermedad (id_receta, enfermedad) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement psReceta = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psEnf = null;
        ResultSet rsKeys = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            // 1) Insertar receta
            psReceta = conn.prepareStatement(sqlReceta, Statement.RETURN_GENERATED_KEYS);
            psReceta.setString(1, receta.getDniPaciente());
            psReceta.setString(2, receta.getDniOftalmologo());
            psReceta.setTimestamp(3, Timestamp.valueOf(receta.getFechaExamen()));
            psReceta.executeUpdate();

            rsKeys = psReceta.getGeneratedKeys();
            if (!rsKeys.next()) {
                throw new SQLException("No se generó id_receta");
            }
            int idReceta = rsKeys.getInt(1);

            // 2) Insertar detalle
            psDetalle = conn.prepareStatement(sqlDetalle);
            psDetalle.setInt(1, idReceta);
            psDetalle.setString(2, detalle.getTipoDeLente());
            psDetalle.setString(3, detalle.getVisionCorregida());
            psDetalle.setString(4, detalle.getObservaciones());
            psDetalle.setBigDecimal(5, detalle.getEsfOjoIzquierdo());
            psDetalle.setBigDecimal(6, detalle.getCilindroOjoIzquierdo());
            psDetalle.setBigDecimal(7, detalle.getEjeOjoIzquierdo());
            psDetalle.setBigDecimal(8, detalle.getEsfOjoDerecho());
            psDetalle.setBigDecimal(9, detalle.getCilindroOjoDerecho());
            psDetalle.setBigDecimal(10, detalle.getEjeOjoDerecho());
            psDetalle.setBigDecimal(11, detalle.getAdicional());
            psDetalle.setInt(12, detalle.getDistanciaInterpupilar());
            psDetalle.executeUpdate();

            // 3) Insertar enfermedades (si hay)
            if (enfermedades != null && !enfermedades.isEmpty()) {
                psEnf = conn.prepareStatement(sqlEnf);
                for (String enf : enfermedades) {
                    if (enf == null || enf.trim().isEmpty()) continue;
                    psEnf.setInt(1, idReceta);
                    psEnf.setString(2, enf.trim());
                    psEnf.addBatch();
                }
                psEnf.executeBatch();
            }

            conn.commit();
            return idReceta;

        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) {}
            }
            throw ex;
        } finally {
            try { if (rsKeys != null) rsKeys.close(); } catch (SQLException ignored) {}
            try { if (psReceta != null) psReceta.close(); } catch (SQLException ignored) {}
            try { if (psDetalle != null) psDetalle.close(); } catch (SQLException ignored) {}
            try { if (psEnf != null) psEnf.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Listado de recetas con datos básicos de paciente y oftalmólogo.
     */
    public List<RecetaListado> listarRecetas(String filtroPacienteODni) {
        String sql = "SELECT r.id_receta, r.fecha_examen, " +
                "p.DNI AS dni_paciente, CONCAT(p.apellido_paterno,' ',p.apellido_materno,', ',p.nombre) AS paciente, " +
                "o.DNI AS dni_oft, CONCAT(o.apellido_paterno,' ',o.apellido_materno,', ',o.nombre) AS oftalmologo " +
                "FROM receta r " +
                "INNER JOIN paciente p ON r.DNI_paciente = p.DNI " +
                "INNER JOIN oftalmologo o ON r.DNI_oftalmologo = o.DNI ";

        String where = "";
        if (filtroPacienteODni != null && !filtroPacienteODni.trim().isEmpty()) {
            where = "WHERE p.DNI LIKE ? OR p.nombre LIKE ? OR p.apellido_paterno LIKE ? OR p.apellido_materno LIKE ? ";
        }
        String order = "ORDER BY r.fecha_examen DESC";

        List<RecetaListado> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql + where + order)) {

            if (!where.isEmpty()) {
                String like = "%" + filtroPacienteODni.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecetaListado r = new RecetaListado();
                    r.setIdReceta(rs.getInt("id_receta"));
                    r.setFechaExamen(new Date(rs.getTimestamp("fecha_examen").getTime()));
                    r.setDniPaciente(rs.getString("dni_paciente"));
                    r.setNombrePaciente(rs.getString("paciente"));
                    r.setDniOftalmologo(rs.getString("dni_oft"));
                    r.setNombreOftalmologo(rs.getString("oftalmologo"));
                    lista.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar recetas: " + e.getMessage());
        }
        return lista;
    }

    // Clase auxiliar para mostrar en la tabla
    public static class RecetaListado {
        private int idReceta;
        private Date fechaExamen;
        private String dniPaciente;
        private String nombrePaciente;
        private String dniOftalmologo;
        private String nombreOftalmologo;

        // getters y setters
        public int getIdReceta() { return idReceta; }
        public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

        public Date getFechaExamen() { return fechaExamen; }
        public void setFechaExamen(Date fechaExamen) { this.fechaExamen = fechaExamen; }

        public String getDniPaciente() { return dniPaciente; }
        public void setDniPaciente(String dniPaciente) { this.dniPaciente = dniPaciente; }

        public String getNombrePaciente() { return nombrePaciente; }
        public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

        public String getDniOftalmologo() { return dniOftalmologo; }
        public void setDniOftalmologo(String dniOftalmologo) { this.dniOftalmologo = dniOftalmologo; }

        public String getNombreOftalmologo() { return nombreOftalmologo; }
        public void setNombreOftalmologo(String nombreOftalmologo) { this.nombreOftalmologo = nombreOftalmologo; }
    }
    // Dentro de RecetaDAO
    public static class RecetaCompleta {
        private Receta receta;
        private DetalleReceta detalle;
        private Paciente paciente;
        private Oftalmologo oftalmologo;
        private List<String> enfermedades;

        public Receta getReceta() { return receta; }
        public void setReceta(Receta receta) { this.receta = receta; }

        public DetalleReceta getDetalle() { return detalle; }
        public void setDetalle(DetalleReceta detalle) { this.detalle = detalle; }

        public Paciente getPaciente() { return paciente; }
        public void setPaciente(Paciente paciente) { this.paciente = paciente; }

        public Oftalmologo getOftalmologo() { return oftalmologo; }
        public void setOftalmologo(Oftalmologo oftalmologo) { this.oftalmologo = oftalmologo; }

        public List<String> getEnfermedades() { return enfermedades; }
        public void setEnfermedades(List<String> enfermedades) { this.enfermedades = enfermedades; }
    }

    public RecetaCompleta obtenerRecetaCompleta(int idReceta) {
        String sqlDatos = "SELECT r.id_receta, r.fecha_examen, " +
                "p.DNI AS dni_p, p.nombre AS nom_p, p.apellido_paterno AS apep_p, p.apellido_materno AS apem_p, " +
                "o.DNI AS dni_o, o.nombre AS nom_o, o.apellido_paterno AS apep_o, o.apellido_materno AS apem_o, " +
                "d.tipo_de_lente, d.vision_corregida, d.observaciones, " +
                "d.esf_ojo_izquierdo, d.cilindro_ojo_izquierdo, d.eje_ojo_izquierdo, " +
                "d.esf_ojo_derecho, d.cilindro_ojo_derecho, d.eje_ojo_derecho, " +
                "d.adicional, d.distancia_interpupilar " +
                "FROM receta r " +
                "INNER JOIN paciente p ON r.DNI_paciente = p.DNI " +
                "INNER JOIN oftalmologo o ON r.DNI_oftalmologo = o.DNI " +
                "INNER JOIN detalle_receta d ON d.id_receta = r.id_receta " +
                "WHERE r.id_receta = ?";

        String sqlEnf = "SELECT enfermedad FROM enfermedad WHERE id_receta = ? ORDER BY enfermedad";

        RecetaCompleta rc = null;

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sqlDatos)) {

            ps.setInt(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rc = new RecetaCompleta();

                    // Receta
                    Receta r = new Receta();
                    r.setIdReceta(rs.getInt("id_receta"));
                    LocalDateTime fecha = rs.getTimestamp("fecha_examen").toLocalDateTime();
                    r.setFechaExamen(fecha);
                    r.setDniPaciente(rs.getString("dni_p"));
                    r.setDniOftalmologo(rs.getString("dni_o"));
                    rc.setReceta(r);

                    // Paciente
                    Paciente p = new Paciente();
                    p.setDni(rs.getString("dni_p"));
                    p.setNombre(rs.getString("nom_p"));
                    p.setApellidoPaterno(rs.getString("apep_p"));
                    p.setApellidoMaterno(rs.getString("apem_p"));
                    rc.setPaciente(p);

                    // Oftalmólogo
                    Oftalmologo o = new Oftalmologo();
                    o.setDni(rs.getString("dni_o"));
                    o.setNombre(rs.getString("nom_o"));
                    o.setApellidoPaterno(rs.getString("apep_o"));
                    o.setApellidoMaterno(rs.getString("apem_o"));
                    rc.setOftalmologo(o);

                    // Detalle
                    DetalleReceta d = new DetalleReceta();
                    d.setTipoDeLente(rs.getString("tipo_de_lente"));
                    d.setVisionCorregida(rs.getString("vision_corregida"));
                    d.setObservaciones(rs.getString("observaciones"));
                    d.setEsfOjoIzquierdo(rs.getBigDecimal("esf_ojo_izquierdo"));
                    d.setCilindroOjoIzquierdo(rs.getBigDecimal("cilindro_ojo_izquierdo"));
                    d.setEjeOjoIzquierdo(rs.getBigDecimal("eje_ojo_izquierdo"));
                    d.setEsfOjoDerecho(rs.getBigDecimal("esf_ojo_derecho"));
                    d.setCilindroOjoDerecho(rs.getBigDecimal("cilindro_ojo_derecho"));
                    d.setEjeOjoDerecho(rs.getBigDecimal("eje_ojo_derecho"));
                    d.setAdicional(rs.getBigDecimal("adicional"));
                    d.setDistanciaInterpupilar(rs.getInt("distancia_interpupilar"));
                    rc.setDetalle(d);
                }
            }

            if (rc != null) {
                // Enfermedades
                List<String> enfList = new ArrayList<>();
                try (PreparedStatement psEnf = conn.prepareStatement(sqlEnf)) {
                    psEnf.setInt(1, idReceta);
                    try (ResultSet rsEnf = psEnf.executeQuery()) {
                        while (rsEnf.next()) {
                            enfList.add(rsEnf.getString("enfermedad"));
                        }
                    }
                }
                rc.setEnfermedades(enfList);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener receta completa: " + e.getMessage());
        }

        return rc;
    }



}
