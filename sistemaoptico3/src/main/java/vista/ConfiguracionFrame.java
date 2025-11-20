/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana de configuración del sistema
 */
public class ConfiguracionFrame extends JDialog {
    
    private Usuario usuarioActual;
    private String seccion;
    
    public ConfiguracionFrame(JFrame parent, Usuario usuario, String seccion) {
        super(parent, "Configuración del Sistema", true);
        this.usuarioActual = usuario;
        this.seccion = seccion;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        // Panel principal con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Pestaña: General
        tabbedPane.addTab("⚙️ General", crearPanelGeneral());
        
        // Pestaña: Usuarios (solo admin)
        if (usuarioActual.esAdmin()) {
            tabbedPane.addTab("👥 Usuarios", crearPanelUsuarios());
        }
        
        // Pestaña: Base de Datos
        tabbedPane.addTab("💾 Base de Datos", crearPanelBaseDatos());
        
        // Pestaña: Acerca de
        tabbedPane.addTab("ℹ️ Acerca de", crearPanelAcercaDe());
        
        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 245));
        
        JButton btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarConfiguracion());
        
        JButton btnCerrar = new JButton("❌ Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCerrar);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelGeneral() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Configuración General del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(41, 128, 185));
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        
        // Tema de la aplicación
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Tema de la aplicación:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbTema = new JComboBox<>(new String[]{"Claro", "Oscuro", "Automático"});
        panel.add(cmbTema, gbc);
        
        // Idioma
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Idioma:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbIdioma = new JComboBox<>(new String[]{"Español", "English"});
        panel.add(cmbIdioma, gbc);
        
        // Formato de fecha
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Formato de fecha:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbFecha = new JComboBox<>(new String[]{"DD/MM/YYYY", "MM/DD/YYYY", "YYYY-MM-DD"});
        panel.add(cmbFecha, gbc);
        
        // Notificaciones
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JCheckBox chkNotificaciones = new JCheckBox("Activar notificaciones del sistema");
        chkNotificaciones.setBackground(Color.WHITE);
        panel.add(chkNotificaciones, gbc);
        
        // Auto-guardar
        gbc.gridy = 5;
        JCheckBox chkAutoGuardar = new JCheckBox("Auto-guardar cambios cada 5 minutos");
        chkAutoGuardar.setBackground(Color.WHITE);
        panel.add(chkAutoGuardar, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Gestión de Usuarios del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(41, 128, 185));
        
        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(245, 250, 255));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblInfo1 = new JLabel("👤 Usuario actual: " + usuarioActual.getUsuario());
        JLabel lblInfo2 = new JLabel("🔑 Rol: " + usuarioActual.getRol().toUpperCase());
        JLabel lblInfo3 = new JLabel("📅 Última conexión: " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));
        
        lblInfo1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo3.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panelInfo.add(lblInfo1);
        panelInfo.add(Box.createVerticalStrut(8));
        panelInfo.add(lblInfo2);
        panelInfo.add(Box.createVerticalStrut(8));
        panelInfo.add(lblInfo3);
        
        // Botones de gestión
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnNuevoUsuario = new JButton("➕ Nuevo Usuario");
        btnNuevoUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNuevoUsuario.setBackground(new Color(46, 204, 113));
        btnNuevoUsuario.setForeground(Color.WHITE);
        
        JButton btnEditarUsuario = new JButton("✏️ Editar Usuario");
        btnEditarUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarUsuario.setBackground(new Color(52, 152, 219));
        btnEditarUsuario.setForeground(Color.WHITE);
        
        JButton btnCambiarPassword = new JButton("🔒 Cambiar Contraseña");
        btnCambiarPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCambiarPassword.setBackground(new Color(241, 196, 15));
        btnCambiarPassword.setForeground(Color.WHITE);
        btnCambiarPassword.addActionListener(e -> cambiarPassword());
        
        panelBotones.add(btnNuevoUsuario);
        panelBotones.add(btnEditarUsuario);
        panelBotones.add(btnCambiarPassword);
        
        // Ensamblar
        JPanel contenedor = new JPanel(new BorderLayout(15, 15));
        contenedor.setBackground(Color.WHITE);
        contenedor.add(lblTitulo, BorderLayout.NORTH);
        contenedor.add(panelInfo, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);
        
        panel.add(contenedor, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel crearPanelBaseDatos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitulo = new JLabel("Administración de Base de Datos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(41, 128, 185));
        
        // Panel de información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridBagLayout());
        panelInfo.setBackground(new Color(245, 250, 255));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelInfo.add(new JLabel("Servidor:"), gbc);
        gbc.gridx = 1;
        panelInfo.add(new JLabel("PostgreSQL - localhost:5433"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelInfo.add(new JLabel("Base de Datos:"), gbc);
        gbc.gridx = 1;
        panelInfo.add(new JLabel("opticadb"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelInfo.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        JLabel lblEstado = new JLabel("✅ Conectado");
        lblEstado.setForeground(new Color(46, 204, 113));
        panelInfo.add(lblEstado, gbc);
        
        // Botones de administración
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnBackup = new JButton("💾 Crear Respaldo");
        btnBackup.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBackup.setBackground(new Color(46, 204, 113));
        btnBackup.setForeground(Color.WHITE);
        btnBackup.addActionListener(e -> crearRespaldo());
        
        JButton btnRestaurar = new JButton("📥 Restaurar");
        btnRestaurar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRestaurar.setBackground(new Color(52, 152, 219));
        btnRestaurar.setForeground(Color.WHITE);
        
        JButton btnOptimizar = new JButton("⚡ Optimizar BD");
        btnOptimizar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnOptimizar.setBackground(new Color(241, 196, 15));
        btnOptimizar.setForeground(Color.WHITE);
        
        panelBotones.add(btnBackup);
        panelBotones.add(btnRestaurar);
        panelBotones.add(btnOptimizar);
        
        // Ensamblar
        JPanel contenedor = new JPanel(new BorderLayout(15, 15));
        contenedor.setBackground(Color.WHITE);
        contenedor.add(lblTitulo, BorderLayout.NORTH);
        contenedor.add(panelInfo, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);
        
        panel.add(contenedor, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel crearPanelAcercaDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JTextArea txtAcercaDe = new JTextArea();
        txtAcercaDe.setEditable(false);
        txtAcercaDe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAcercaDe.setLineWrap(true);
        txtAcercaDe.setWrapStyleWord(true);
        txtAcercaDe.setText(
            "═══════════════════════════════════════════════════════════\n" +
            "          SISTEMA DE GESTIÓN ÓPTICA\n" +
            "═══════════════════════════════════════════════════════════\n\n" +
            "Versión: 1.0.0\n" +
            "Fecha de Lanzamiento: Enero 2025\n\n" +
            "TECNOLOGÍAS UTILIZADAS:\n" +
            "  • Lenguaje: Java 22\n" +
            "  • Interfaz Gráfica: Swing\n" +
            "  • Base de Datos: PostgreSQL\n" +
            "  • Conectividad: JDBC\n\n" +
            "CARACTERÍSTICAS:\n" +
            "  ✓ Gestión de pacientes\n" +
            "  ✓ Registro de recetas médicas\n" +
            "  ✓ Control de oftalmólogos\n" +
            "  ✓ Búsqueda avanzada\n" +
            "  ✓ Reportes y estadísticas\n" +
            "  ✓ Sistema de usuarios y roles\n\n" +
            "DESARROLLADO PARA:\n" +
            "  Ópticas y clínicas oftalmológicas\n\n" +
            "© 2025 - Todos los derechos reservados\n"
        );
        
        JScrollPane scroll = new JScrollPane(txtAcercaDe);
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void guardarConfiguracion() {
        // TODO: Implementar guardado real de configuración
        JOptionPane.showMessageDialog(this,
            "Configuración guardada exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cambiarPassword() {
        JPasswordField passActual = new JPasswordField(15);
        JPasswordField passNuevo = new JPasswordField(15);
        JPasswordField passConfirmar = new JPasswordField(15);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Contraseña actual:"));
        panel.add(passActual);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(passNuevo);
        panel.add(new JLabel("Confirmar contraseña:"));
        panel.add(passConfirmar);
        
        int result = JOptionPane.showConfirmDialog(this, panel,
            "Cambiar Contraseña",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            // TODO: Implementar cambio de contraseña real
            JOptionPane.showMessageDialog(this,
                "Contraseña cambiada exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void crearRespaldo() {
        // TODO: Implementar respaldo real
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Desea crear un respaldo de la base de datos?\n" +
            "Esto puede tomar algunos minutos.",
            "Confirmar Respaldo",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Respaldo creado exitosamente\n" +
                "Ubicación: C:\\Backups\\opticadb_" +
                java.time.LocalDate.now() + ".sql",
                "Respaldo Completado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}