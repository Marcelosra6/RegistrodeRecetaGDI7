/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        super(parent, "Módulo de Reportes y Estadísticas", true);
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
                "Configuración del Reporte",
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
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel panelBotonesSuperiores = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesSuperiores.setOpaque(false);
        
        btnGenerar = new JButton("📊 Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerar.setBackground(new Color(46, 204, 113));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.addActionListener(e -> generarReporte());
        
        btnExportar = new JButton("💾 Exportar PDF");
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExportar.setBackground(new Color(231, 76, 60));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.addActionListener(e -> exportarPDF());
        
        panelBotonesSuperiores.add(btnGenerar);
        panelBotonesSuperiores.add(btnExportar);
        panelOpciones.add(panelBotonesSuperiores, gbc);
        
        // Panel central - Resultados
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Resultados del Reporte"
        ));
        
        txtResultados = new JTextArea();
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtResultados.setEditable(false);
        txtResultados.setText(obtenerMensajeBienvenida());
        
        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);
        
        // Panel inferior - Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        
        JButton btnCerrar = new JButton("❌ Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCerrar);
        
        // Agregar paneles
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
               "          SISTEMA DE REPORTES Y ESTADÍSTICAS\n" +
               "═══════════════════════════════════════════════════════════\n\n" +
               "Seleccione el tipo de reporte y período deseado,\n" +
               "luego haga clic en 'Generar Reporte'.\n\n" +
               "Los reportes disponibles incluyen:\n" +
               "  • Análisis de recetas emitidas\n" +
               "  • Estadísticas de pacientes\n" +
               "  • Rendimiento de oftalmólogos\n" +
               "  • Tendencias en prescripciones\n" +
               "  • Y más...\n\n";
    }
    
    private void generarReporte() {
        String tipo = (String) cmbTipoReporte.getSelectedItem();
        String periodo = (String) cmbPeriodo.getSelectedItem();
        
        StringBuilder reporte = new StringBuilder();
        reporte.append("═══════════════════════════════════════════════════════════\n");
        reporte.append("  REPORTE: ").append(tipo).append("\n");
        reporte.append("  PERÍODO: ").append(periodo).append("\n");
        reporte.append("  FECHA: ").append(java.time.LocalDate.now()).append("\n");
        reporte.append("═══════════════════════════════════════════════════════════\n\n");
        
        // TODO: Implementar consultas reales a la base de datos
        
        if (tipo.equals("Estadísticas Generales")) {
            reporte.append("📊 RESUMEN GENERAL\n");
            reporte.append("─────────────────────────────────────────────────────────\n\n");
            reporte.append("Total de Pacientes:           127\n");
            reporte.append("Total de Recetas:             348\n");
            reporte.append("Oftalmólogos Activos:         8\n");
            reporte.append("Promedio Recetas/Día:         12.3\n\n");
            
            reporte.append("📋 TIPOS DE LENTES MÁS COMUNES\n");
            reporte.append("─────────────────────────────────────────────────────────\n\n");
            reporte.append("1. Monofocales:               45%\n");
            reporte.append("2. Bifocales:                 28%\n");
            reporte.append("3. Progresivos:               18%\n");
            reporte.append("4. Lectura:                   9%\n\n");
            
            reporte.append("🏥 ENFERMEDADES MÁS FRECUENTES\n");
            reporte.append("─────────────────────────────────────────────────────────\n\n");
            reporte.append("1. Miopía:                    38%\n");
            reporte.append("2. Hipermetropía:             25%\n");
            reporte.append("3. Astigmatismo:              22%\n");
            reporte.append("4. Presbicia:                 15%\n\n");
        } else {
            reporte.append("📝 DATOS DEL REPORTE\n");
            reporte.append("─────────────────────────────────────────────────────────\n\n");
            reporte.append("Este reporte está en desarrollo.\n");
            reporte.append("Próximamente incluirá:\n\n");
            reporte.append("  • Gráficos estadísticos\n");
            reporte.append("  • Análisis detallado\n");
            reporte.append("  • Comparativas históricas\n");
            reporte.append("  • Exportación a PDF/Excel\n\n");
        }
        
        reporte.append("═══════════════════════════════════════════════════════════\n");
        reporte.append("Fin del Reporte\n");
        
        txtResultados.setText(reporte.toString());
        txtResultados.setCaretPosition(0);
    }
    
    private void exportarPDF() {
        // TODO: Implementar exportación real a PDF
        JOptionPane.showMessageDialog(this,
            "Funcionalidad de exportación a PDF\n" +
            "será implementada en la siguiente versión.\n\n" +
            "Por ahora puede copiar el contenido del reporte.",
            "Exportar PDF",
            JOptionPane.INFORMATION_MESSAGE);
    }
}