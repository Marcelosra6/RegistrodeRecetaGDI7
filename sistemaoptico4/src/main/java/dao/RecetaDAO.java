package dao;

import modelo.DetalleReceta;
import modelo.Oftalmologo;
import modelo.Paciente;
import modelo.Receta;
import util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para gestionar operaciones de Receta
 * Actualizado para usar procedimientos almacenados y funciones de PostgreSQL
 */
public class RecetaDAO {

    /**
     * Crea una receta, su detalle y sus enfermedades en una sola transacción.
     * Devuelve el id_receta generado o -1 si falla.
     */
    public int crearRecetaConDetalle(
            Receta receta,
            DetalleReceta detalle,
            List<String> enfermedades) throws SQLException {

        Connection conn = null;
        CallableStatement csReceta = null;
        CallableStatement csDetalle = null;
        CallableStatement csEnf = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            // 1) Insertar receta y obtener ID generado
            System.out.println("🔹 Insertando receta...");
            System.out.println("   DNI Paciente: '" + receta.getDniPaciente() + "'");
            System.out.println("   DNI Oftalmólogo: '" + receta.getDniOftalmologo() + "'");
            
            csReceta = conn.prepareCall("CALL proced_ins_rec(?, ?, ?, ?)");
            csReceta.setString(1, receta.getDniPaciente().trim());
            csReceta.setString(2, receta.getDniOftalmologo().trim());
            csReceta.setTimestamp(3, Timestamp.valueOf(receta.getFechaExamen()));
            csReceta.setNull(4, Types.INTEGER);
            csReceta.registerOutParameter(4, Types.INTEGER);
            
            csReceta.execute();
            
            int idReceta = csReceta.getInt(4);
            if (idReceta <= 0) {
                throw new SQLException("No se generó id_receta");
            }

            System.out.println("✅ Receta insertada con ID: " + idReceta);

            // 2) Insertar detalle
            System.out.println("🔹 Insertando detalle de receta...");
            csDetalle = conn.prepareCall("CALL proced_ins_rec_det(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            csDetalle.setInt(1, idReceta);
            
            // Manejar posibles NULL en campos de texto
            if (detalle.getTipoDeLente() != null && !detalle.getTipoDeLente().trim().isEmpty()) {
                csDetalle.setString(2, detalle.getTipoDeLente().trim());
            } else {
                csDetalle.setNull(2, Types.VARCHAR);
            }
            
            if (detalle.getVisionCorregida() != null && !detalle.getVisionCorregida().trim().isEmpty()) {
                csDetalle.setString(3, detalle.getVisionCorregida().trim());
            } else {
                csDetalle.setNull(3, Types.VARCHAR);
            }
            
            if (detalle.getObservaciones() != null && !detalle.getObservaciones().trim().isEmpty()) {
                csDetalle.setString(4, detalle.getObservaciones().trim());
            } else {
                csDetalle.setNull(4, Types.VARCHAR);
            }
            
            csDetalle.setBigDecimal(5, detalle.getEsfOjoIzquierdo());
            csDetalle.setBigDecimal(6, detalle.getCilindroOjoIzquierdo());
            csDetalle.setBigDecimal(7, detalle.getEjeOjoIzquierdo());
            csDetalle.setBigDecimal(8, detalle.getEsfOjoDerecho());
            csDetalle.setBigDecimal(9, detalle.getCilindroOjoDerecho());
            csDetalle.setBigDecimal(10, detalle.getEjeOjoDerecho());
            csDetalle.setBigDecimal(11, detalle.getAdicional());

            if (detalle.getDistanciaInterpupilar() > 0) {
                csDetalle.setShort(12, (short) detalle.getDistanciaInterpupilar());
            } else {
                csDetalle.setNull(12, Types.SMALLINT);
            }

            csDetalle.execute();
            System.out.println("✅ Detalle de receta insertado");

            // 3) Insertar enfermedades
            if (enfermedades != null && !enfermedades.isEmpty()) {
                csEnf = conn.prepareCall("CALL proced_ins_rec_enf(?, ?)");
                for (String enf : enfermedades) {
                    if (enf == null || enf.trim().isEmpty()) continue;
                    csEnf.setInt(1, idReceta);
                    csEnf.setString(2, enf.trim());
                    csEnf.execute();
                    System.out.println("✅ Enfermedad insertada: " + enf.trim());
                }
            }

            conn.commit();
            System.out.println("✅ Transacción completada exitosamente");
            return idReceta;

        } catch (SQLException ex) {
            System.err.println("❌ Error SQL: " + ex.getMessage());
            System.err.println("   SQLState: " + ex.getSQLState());
            System.err.println("   ErrorCode: " + ex.getErrorCode());
            
            if (conn != null) {
                try { 
                    conn.rollback();
                    System.err.println("⚠️ Transacción revertida (rollback)");
                } catch (SQLException ignore) {}
            }
            throw ex;
        } finally {
            try { if (csReceta != null) csReceta.close(); } catch (SQLException ignored) {}
            try { if (csDetalle != null) csDetalle.close(); } catch (SQLException ignored) {}
            try { if (csEnf != null) csEnf.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Listado de recetas con datos básicos de paciente y oftalmólogo.
     * Usa la función almacenada proced_lis_rec_filtro
     */
    public List<RecetaListado> listarRecetas(String filtroPacienteODni) {
        String sql = "SELECT * FROM proced_lis_rec_filtro(?)";

        List<RecetaListado> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Si no hay filtro, enviar NULL
            if (filtroPacienteODni == null || filtroPacienteODni.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, filtroPacienteODni.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecetaListado r = new RecetaListado();
                    r.setIdReceta(rs.getInt("id_receta"));
                    r.setFechaExamen(new Date(rs.getTimestamp("fecha_examen").getTime()));
                    r.setDniPaciente(rs.getString("dni_paciente").trim());
                    r.setNombrePaciente(rs.getString("paciente"));
                    r.setDniOftalmologo(rs.getString("dni_oft").trim());
                    r.setNombreOftalmologo(rs.getString("oftalmologo"));
                    lista.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar recetas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene una receta completa con todos sus datos
     * Usa la función almacenada proced_sel_rec_completa
     */
    public RecetaCompleta obtenerRecetaCompleta(int idReceta) {
        String sqlDatos = "SELECT * FROM proced_sel_rec_completa(?)";
        String sqlEnf = "SELECT * FROM proced_lis_rec_enf(?)";

        RecetaCompleta rc = null;

        System.out.println("\n🔍 Buscando receta ID: " + idReceta);

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sqlDatos)) {

            ps.setInt(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✅ Receta encontrada, procesando datos...");
                    rc = new RecetaCompleta();

                    // Receta
                    Receta r = new Receta();
                    r.setIdReceta(rs.getInt("id_receta"));
                    LocalDateTime fecha = rs.getTimestamp("fecha_examen").toLocalDateTime();
                    r.setFechaExamen(fecha);
                    r.setDniPaciente(rs.getString("dni_p").trim());
                    r.setDniOftalmologo(rs.getString("dni_o").trim());
                    rc.setReceta(r);

                    // Paciente
                    Paciente p = new Paciente();
                    p.setDni(rs.getString("dni_p").trim());
                    p.setNombre(rs.getString("nom_p"));
                    p.setApellidoPaterno(rs.getString("apep_p"));
                    p.setApellidoMaterno(rs.getString("apem_p"));
                    rc.setPaciente(p);

                    // Oftalmólogo
                    Oftalmologo o = new Oftalmologo();
                    o.setDni(rs.getString("dni_o").trim());
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
                    
                    // Manejar NULL en distancia_interpupilar
                    int distancia = rs.getInt("distancia_interpupilar");
                    if (!rs.wasNull()) {
                        d.setDistanciaInterpupilar(distancia);
                    }
                    
                    rc.setDetalle(d);
                } else {
                    System.err.println("❌ No se encontró ninguna receta con ID: " + idReceta);
                }
            }

            if (rc != null) {
                System.out.println("🔍 Buscando enfermedades...");
                // Enfermedades usando función almacenada
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
                System.out.println("✅ Enfermedades cargadas: " + enfList.size());
            }

        } catch (SQLException e) {
            System.err.println("❌ Error SQL al obtener receta completa: " + e.getMessage());
            System.err.println("   SQLState: " + e.getSQLState());
            e.printStackTrace();
        }

        return rc;
    }

    // ========== CLASES AUXILIARES ==========

    /**
     * Clase auxiliar para mostrar en la tabla de listado
     */
    public static class RecetaListado {
        private int idReceta;
        private Date fechaExamen;
        private String dniPaciente;
        private String nombrePaciente;
        private String dniOftalmologo;
        private String nombreOftalmologo;

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

    /**
     * Clase auxiliar para obtener receta completa con todos los datos
     */
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
}