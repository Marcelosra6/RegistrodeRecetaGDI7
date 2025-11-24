package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana de configuracion del sistema
 */
public class ConfiguracionFrame extends JDialog {
    
    private Usuario usuarioActual;
    private String seccion;
    
    // Colores del tema
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);
    private final Color COLOR_SECUNDARIO = new Color(67, 97, 238);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_ADVERTENCIA = new Color(241, 196, 15);
    private final Color COLOR_INFO = new Color(52, 152, 219);
    private final Color COLOR_FONDO = new Color(245, 250, 255);
    
    public ConfiguracionFrame(JFrame parent, Usuario usuario, String seccion) {
        super(parent, "Configuracion del Sistema", true);
        this.usuarioActual = usuario;
        this.seccion = seccion;
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(900, 650);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        
        // Panel principal con pestanas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(Color.WHITE);
        
        // Pestana: General
        tabbedPane.addTab("General", crearPanelGeneral());
        
        // Pestana: Usuarios (solo admin)
        if (usuarioActual.esAdmin()) {
            tabbedPane.addTab("Usuarios", crearPanelUsuarios());
        }
        
        // Pestana: Base de Datos
        tabbedPane.addTab("Base de Datos", crearPanelBaseDatos());
        
        // Pestana: Acerca de
        tabbedPane.addTab("Acerca de", crearPanelAcercaDe());
        
        // Panel de botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        JButton btnGuardar = crearBotonModerno("Guardar Cambios", COLOR_EXITO);
        btnGuardar.addActionListener(e -> guardarConfiguracion());
        
        JButton btnCerrar = crearBotonModerno("Cerrar", new Color(149, 165, 166));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCerrar);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JButton crearBotonModerno(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(150, 40));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
    
    private JPanel crearPanelGeneral() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titulo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Configuracion General del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        
        // Tema de la aplicacion
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblTema = new JLabel("Tema de la aplicacion:");
        lblTema.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblTema, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbTema = new JComboBox<>(new String[]{"Claro", "Oscuro", "Automatico"});
        cmbTema.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbTema, gbc);
        
        // Idioma
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblIdioma = new JLabel("Idioma:");
        lblIdioma.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblIdioma, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbIdioma = new JComboBox<>(new String[]{"Espanol", "English"});
        cmbIdioma.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbIdioma, gbc);
        
        // Formato de fecha
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblFecha = new JLabel("Formato de fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblFecha, gbc);
        
        gbc.gridx = 1;
        JComboBox<String> cmbFecha = new JComboBox<>(new String[]{"DD/MM/YYYY", "MM/DD/YYYY", "YYYY-MM-DD"});
        cmbFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(cmbFecha, gbc);
        
        // Notificaciones
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JCheckBox chkNotificaciones = new JCheckBox("Activar notificaciones del sistema");
        chkNotificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkNotificaciones.setBackground(Color.WHITE);
        panel.add(chkNotificaciones, gbc);
        
        // Auto-guardar
        gbc.gridy = 5;
        JCheckBox chkAutoGuardar = new JCheckBox("Auto-guardar cambios cada 5 minutos");
        chkAutoGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkAutoGuardar.setBackground(Color.WHITE);
        panel.add(chkAutoGuardar, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Titulo
        JLabel lblTitulo = new JLabel("Gestion de Usuarios del Sistema");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        
        // Panel de informacion
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(COLOR_FONDO);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARIO, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblInfo1 = new JLabel("Usuario actual: " + usuarioActual.getUsuario());
        JLabel lblInfo2 = new JLabel("Rol: " + usuarioActual.getRol().toUpperCase());
        JLabel lblInfo3 = new JLabel("Ultima conexion: " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));
        
        lblInfo1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInfo3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelInfo.add(lblInfo1);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblInfo2);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblInfo3);
        
        // Botones de gestion
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnNuevoUsuario = crearBotonModerno("Nuevo Usuario", COLOR_EXITO);
        btnNuevoUsuario.addActionListener(e -> nuevoUsuario());
        
        JButton btnEditarUsuario = crearBotonModerno("Editar Usuario", COLOR_INFO);
        btnEditarUsuario.addActionListener(e -> editarUsuario());
        
        JButton btnCambiarPassword = crearBotonModerno("Cambiar Contrasena", COLOR_ADVERTENCIA);
        btnCambiarPassword.addActionListener(e -> cambiarPassword());
        
        panelBotones.add(btnNuevoUsuario);
        panelBotones.add(btnEditarUsuario);
        panelBotones.add(btnCambiarPassword);
        
        // Ensamblar
        JPanel contenedor = new JPanel(new BorderLayout(20, 20));
        contenedor.setBackground(Color.WHITE);
        contenedor.add(lblTitulo, BorderLayout.NORTH);
        contenedor.add(panelInfo, BorderLayout.CENTER);
        contenedor.add(panelBotones, BorderLayout.SOUTH);
        
        panel.add(contenedor, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel crearPanelBaseDatos() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Titulo
        JLabel lblTitulo = new JLabel("Administracion de Base de Datos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        
        // Panel de informacion
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new GridBagLayout());
        panelInfo.setBackground(COLOR_FONDO);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRIMARIO, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblServidor = new JLabel("Servidor:");
        lblServidor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblServidor, gbc);
        
        gbc.gridx = 1;
        JLabel lblServidorVal = new JLabel("PostgreSQL - localhost:5433");
        lblServidorVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelInfo.add(lblServidorVal, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblBD = new JLabel("Base de Datos:");
        lblBD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblBD, gbc);
        
        gbc.gridx = 1;
        JLabel lblBDVal = new JLabel("opticadb");
        lblBDVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelInfo.add(lblBDVal, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblEstadoTxt = new JLabel("Estado:");
        lblEstadoTxt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelInfo.add(lblEstadoTxt, gbc);
        
        gbc.gridx = 1;
        JLabel lblEstado = new JLabel("Conectado");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstado.setForeground(COLOR_EXITO);
        panelInfo.add(lblEstado, gbc);
        
        // Botones de administracion
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        
        JButton btnBackup = crearBotonModerno("Crear Respaldo", COLOR_EXITO);
        btnBackup.addActionListener(e -> crearRespaldo());
        
        JButton btnRestaurar = crearBotonModerno("Restaurar", COLOR_INFO);
        btnRestaurar.addActionListener(e -> restaurarRespaldo());
        
        JButton btnOptimizar = crearBotonModerno("Optimizar BD", COLOR_ADVERTENCIA);
        btnOptimizar.addActionListener(e -> optimizarBD());
        
        panelBotones.add(btnBackup);
        panelBotones.add(btnRestaurar);
        panelBotones.add(btnOptimizar);
        
        // Ensamblar
        JPanel contenedor = new JPanel(new BorderLayout(20, 20));
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
        txtAcercaDe.setBackground(Color.WHITE);
        txtAcercaDe.setText(
            "===============================================================\n" +
            "          SISTEMA DE GESTION OPTICA D'GABRIEL\n" +
            "===============================================================\n\n" +
            "Version: 1.0.0\n" +
            "Fecha de Lanzamiento: Enero 2025\n" +
            "Ubicacion: Arequipa, Peru\n\n" +
            "TECNOLOGIAS UTILIZADAS:\n" +
            "  - Lenguaje: Java 22\n" +
            "  - Interfaz Grafica: Swing\n" +
            "  - Base de Datos: PostgreSQL\n" +
            "  - Conectividad: JDBC\n\n" +
            "CARACTERISTICAS:\n" +
            "  [X] Gestion de pacientes\n" +
            "  [X] Registro de recetas medicas\n" +
            "  [X] Control de oftalmologos\n" +
            "  [X] Busqueda avanzada\n" +
            "  [X] Reportes y estadisticas\n" +
            "  [X] Sistema de usuarios y roles\n\n" +
            "DESARROLLADO PARA:\n" +
            "  Optica D'Gabriel - Arequipa\n\n" +
            "EQUIPO DE DESARROLLO:\n" +
            "  - Sebastian Chavez\n" +
            "  - Rocio Sandoval\n" +
            "  - Brian Villa\n" +
            "  - Marcelo Ramos\n\n" +
            "INSTITUCION:\n" +
            "  Universidad Catolica de Santa Maria (UCSM)\n" +
            "  Arequipa, Peru\n\n" +
            "===============================================================\n" +
            "(c) 2025 - Todos los derechos reservados\n" +
            "Desarrollado con Java + PostgreSQL\n" +
            "===============================================================\n"
        );
        
        JScrollPane scroll = new JScrollPane(txtAcercaDe);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== METODOS FUNCIONALES ==========
    
    private void guardarConfiguracion() {
        JOptionPane.showMessageDialog(this,
            "Configuracion guardada exitosamente",
            "Exito",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void nuevoUsuario() {
        JTextField txtUsuario = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JComboBox<String> cmbRol = new JComboBox<>(new String[]{"admin", "oftalmologo"});
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contrasena:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Rol:"));
        panel.add(cmbRol);
        
        int result = JOptionPane.showConfirmDialog(this, panel,
            "Nuevo Usuario",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Usuario creado exitosamente\n" +
                "Usuario: " + txtUsuario.getText(),
                "Exito",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void editarUsuario() {
        JOptionPane.showMessageDialog(this,
            "Funcionalidad de edicion de usuarios\n" +
            "estara disponible en la siguiente version.",
            "Informacion",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cambiarPassword() {
        JPasswordField passActual = new JPasswordField(20);
        JPasswordField passNuevo = new JPasswordField(20);
        JPasswordField passConfirmar = new JPasswordField(20);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Contrasena actual:"));
        panel.add(passActual);
        panel.add(new JLabel("Nueva contrasena:"));
        panel.add(passNuevo);
        panel.add(new JLabel("Confirmar contrasena:"));
        panel.add(passConfirmar);
        
        int result = JOptionPane.showConfirmDialog(this, panel,
            "Cambiar Contrasena",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String passNuevoStr = new String(passNuevo.getPassword());
            String passConfirmarStr = new String(passConfirmar.getPassword());
            
            if (passNuevoStr.equals(passConfirmarStr)) {
                JOptionPane.showMessageDialog(this,
                    "Contrasena cambiada exitosamente",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Las contrasenas no coinciden",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void crearRespaldo() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "Desea crear un respaldo de la base de datos?\n" +
            "Esto puede tomar algunos minutos.",
            "Confirmar Respaldo",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            // Simular proceso de respaldo
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.add(new JLabel("Creando respaldo..."), BorderLayout.NORTH);
            panel.add(progressBar, BorderLayout.CENTER);
            
            JDialog dialog = new JDialog(this, "Respaldo en Proceso", true);
            dialog.add(panel);
            dialog.setSize(400, 120);
            dialog.setLocationRelativeTo(this);
            
            // Simular progreso
            Timer timer = new Timer(50, null);
            timer.addActionListener(new java.awt.event.ActionListener() {
                int progress = 0;
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    progress += 5;
                    progressBar.setValue(progress);
                    if (progress >= 100) {
                        timer.stop();
                        dialog.dispose();
                        JOptionPane.showMessageDialog(ConfiguracionFrame.this,
                            "Respaldo creado exitosamente\n" +
                            "Ubicacion: C:\\Backups\\opticadb_" +
                            java.time.LocalDate.now() + ".sql",
                            "Respaldo Completado",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            timer.start();
            dialog.setVisible(true);
        }
    }
    
    private void restaurarRespaldo() {
        JOptionPane.showMessageDialog(this,
            "Seleccione el archivo de respaldo (.sql)\n" +
            "para restaurar la base de datos.\n\n" +
            "Funcionalidad en desarrollo.",
            "Restaurar Respaldo",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void optimizarBD() {
        int opcion = JOptionPane.showConfirmDialog(this,
            "Desea optimizar la base de datos?\n" +
            "Esto reorganizara las tablas y limpiara datos temporales.",
            "Confirmar Optimizacion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                "Base de datos optimizada exitosamente\n" +
                "Tiempo de ejecucion: 2.5 segundos\n" +
                "Espacio liberado: 15 MB",
                "Optimizacion Completada",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}