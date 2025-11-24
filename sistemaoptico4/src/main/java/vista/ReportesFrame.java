package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana para generar reportes y estadísticas
 */
public class ReportesFrame extends JDialog {
    
    private String tipoReporte;
    private JComboBox<String> cmbTipoReporte;
    private JComboBox<String> cmbPeriodo;
    private JTextArea txtResultados;
    private JButton btnGenerar;
    private JButton btnExportar;
    
    public ReportesFrame(JFrame parent, String tipo) {
        super(parent, "Módulo de Reportes y Estadísticas (Módulo en desarrollo)", true);
        this.tipoReporte = tipo;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(900, 650);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245));
        
        // Panel superior - Opciones
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new GridBagLayout());
        panelOpciones.setBackground(Color.WHITE);
        panelOpciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                "Configuración del Reporte (Módulo en desarrollo)",
                0, 0, new Font("Segoe UI", Font.BOLD, 14),
                new Color(155, 89, 182)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo de Reporte
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTipo = new JLabel("Tipo de Reporte:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelOpciones.add(lblTipo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbTipoReporte = new JComboBox<>(new String[]{
            "Recetas por Período",
            "Pacientes Atendidos",
            "Oftalmólogos Activos",
            "Tipos de Lentes Más Usados",
            "Enfermedades Comunes",
            "Estadísticas Generales"
        });
        cmbTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelOpciones.add(cmbTipoReporte, gbc);
        
        // Período
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblPeriodo = new JLabel("Período:");
        lblPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelOpciones.add(lblPeriodo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbPeriodo = new JComboBox<>(new String[]{
            "Hoy",
            "Última Semana",
            "Último Mes",
            "Últimos 3 Meses",
            "Este Año",
            "Todo el Tiempo"
        });
        cmbPeriodo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelOpciones.add(cmbPeriodo, gbc);
        
        // Botones superiores
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelBotonesSuperiores = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesSuperiores.setOpaque(false);
        
        btnGenerar = new JButton(" Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerar.setBackground(new Color(46, 204, 113));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.addActionListener(e -> generarReporte());
        
        btnExportar = new JButton(" Exportar PDF");
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExportar.setBackground(new Color(231, 76, 60));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.addActionListener(e -> exportarPDF());
        
        panelBotonesSuperiores.add(btnGenerar);
        panelBotonesSuperiores.add(btnExportar);
        panelOpciones.add(panelBotonesSuperiores, gbc);
        
        // Panel Resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Resultados del Reporte (Módulo en desarrollo)"
        ));
        
        txtResultados = new JTextArea();
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtResultados.setEditable(false);
        txtResultados.setText(obtenerMensajeBienvenida());
        
        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);
        
        // Panel inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        
        JButton btnCerrar = new JButton(" Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCerrar);
        
        JPanel contenedor = new JPanel(new BorderLayout(15, 15));
        contenedor.setBackground(new Color(245, 245, 245));
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        contenedor.add(panelOpciones, BorderLayout.NORTH);
        contenedor.add(panelResultados, BorderLayout.CENTER);
        
        add(contenedor, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private String obtenerMensajeBienvenida() {
        return "═══════════════════════════════════════════════════════════\n" +
               "       SISTEMA DE REPORTES Y ESTADÍSTICAS (Módulo en desarrollo)\n" +
               "═══════════════════════════════════════════════════════════\n\n" +
               "Este módulo aún está en desarrollo.";
    }
    
    private void generarReporte() {
        String tipo = (String) cmbTipoReporte.getSelectedItem();
        String periodo = (String) cmbPeriodo.getSelectedItem();
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("═══════════════════════════════════════════════════════════\n");
        reporte.append("  REPORTE: ").append(tipo).append(" (Módulo en desarrollo)\n");
        reporte.append("  PERÍODO: ").append(periodo).append("\n");
        reporte.append("  FECHA: ").append(java.time.LocalDate.now()).append("\n");
        reporte.append("═══════════════════════════════════════════════════════════\n\n");
        
        reporte.append("Este módulo está en desarrollo. No se han implementado consultas reales aún.\n\n");
        
        reporte.append("Próximamente incluirá:\n\n");
        reporte.append(" • Consultas reales a la base de datos\n");
        reporte.append(" • Gráficos estadísticos\n");
        reporte.append(" • Exportación avanzada a PDF/Excel\n");
        reporte.append(" • Comparativas históricas\n\n");
        
        reporte.append("═══════════════════════════════════════════════════════════\n");
        reporte.append("Fin del Reporte\n");
        
        txtResultados.setText(reporte.toString());
        txtResultados.setCaretPosition(0);
    }
    
    private void exportarPDF() {
        JOptionPane.showMessageDialog(this,
            "La exportación a PDF aún está en desarrollo.",
            "Exportación en desarrollo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
