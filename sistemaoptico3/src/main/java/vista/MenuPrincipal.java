package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del sistema después del login - Diseño Mejorado
 */
public class MenuPrincipal extends JFrame {
    
    private Usuario usuarioActual;
    
    // Componentes de la interfaz
    private JLabel lblBienvenida;
    private JLabel lblRol;
    private JMenuBar menuBar;
    
    // Menús
    private JMenu menuPacientes;
    private JMenu menuRecetas;
    private JMenu menuOftalmologos;
    private JMenu menuReportes;
    private JMenu menuAdministracion;
    private JMenu menuAyuda;
    
    // Colores del tema moderno
    private final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_ADVERTENCIA = new Color(241, 196, 15);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TARJETA_HOVER = new Color(240, 248, 255);
    
    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        configurarPermisos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema de Gestión Óptica - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        
        // Panel superior mejorado
        JPanel panelSuperior = crearPanelSuperior();
        
        // Panel central con tarjetas mejoradas
        JPanel panelCentral = crearPanelCentral();
        
        // Panel inferior mejorado
        JPanel panelInferior = crearPanelInferior();
        
        // Agregar paneles
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Crear barra de menú
        crearMenuBar();
    }
    
    /**
     * Crea el panel superior con diseño moderno
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(COLOR_PRIMARIO);
        panel.setPreferredSize(new Dimension(1200, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel izquierdo con información del usuario
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzq.setOpaque(false);
        
        // Icono de usuario
        JLabel iconoUsuario = new JLabel("👤");
        iconoUsuario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconoUsuario.setForeground(Color.WHITE);
        
        // Info del usuario
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        lblBienvenida = new JLabel("¡Bienvenido, " + usuarioActual.getUsuario() + "!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(Color.WHITE);
        
        lblRol = new JLabel("Rol: " + usuarioActual.getRol().toUpperCase());
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRol.setForeground(new Color(236, 240, 241));
        
        panelInfo.add(lblBienvenida);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblRol);
        
        panelIzq.add(iconoUsuario);
        panelIzq.add(panelInfo);
        
        // Panel derecho con fecha
        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);
        
        JLabel lblFecha = new JLabel("📅 " + obtenerFechaActual());
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFecha.setForeground(Color.WHITE);
        panelDer.add(lblFecha);
        
        panel.add(panelIzq, BorderLayout.WEST);
        panel.add(panelDer, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Crea el panel central con tarjetas mejoradas
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(crearTarjetaModerna("Pacientes", "Gestionar pacientes", "👥", COLOR_PRIMARIO), gbc);
        
        gbc.gridx = 1;
        panel.add(crearTarjetaModerna("Recetas", "Nueva receta médica", "📋", COLOR_EXITO), gbc);
        
        gbc.gridx = 2;
        panel.add(crearTarjetaModerna("Oftalmólogos", "Gestionar oftalmólogos", "👨‍⚕️", COLOR_SECUNDARIO), gbc);
        
        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(crearTarjetaModerna("Buscar Recetas", "Buscar por paciente", "🔍", COLOR_ADVERTENCIA), gbc);
        
        gbc.gridx = 1;
        panel.add(crearTarjetaModerna("Reportes", "Ver estadísticas", "📊", new Color(155, 89, 182)), gbc);
        
        gbc.gridx = 2;
        panel.add(crearTarjetaModerna("Configuración", "Opciones del sistema", "⚙️", new Color(149, 165, 166)), gbc);
        
        return panel;
    }
    
    /**
     * Crea una tarjeta moderna mejorada
     */
    private JPanel crearTarjetaModerna(String titulo, String descripcion, String emoji, Color colorTema) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(15, 15));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Borde con sombra simulada
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(200, 200, 200, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            )
        ));
        
        // Panel superior con emoji
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel lblEmoji = new JLabel(emoji, SwingConstants.CENTER);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        
        // Barra de color
        JPanel barraColor = new JPanel();
        barraColor.setBackground(colorTema);
        barraColor.setPreferredSize(new Dimension(0, 5));
        
        panelSuperior.add(lblEmoji, BorderLayout.CENTER);
        panelSuperior.add(barraColor, BorderLayout.SOUTH);
        
        // Panel de texto
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        panelTexto.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(127, 140, 141));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(8));
        panelTexto.add(lblDesc);
        
        tarjeta.add(panelSuperior, BorderLayout.NORTH);
        tarjeta.add(panelTexto, BorderLayout.CENTER);
        
        // Efectos hover
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accionTarjeta(titulo);
            }
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(COLOR_TARJETA_HOVER);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(200, 200, 200, 150)),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorTema, 2, true),
                        BorderFactory.createEmptyBorder(24, 24, 24, 24)
                    )
                ));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(COLOR_TARJETA);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(200, 200, 200, 100)),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                        BorderFactory.createEmptyBorder(25, 25, 25, 25)
                    )
                ));
            }
        });
        
        return tarjeta;
    }
    
    /**
     * Crea el panel inferior
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(1200, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        
        JLabel lblFooter = new JLabel("Sistema de Gestión Óptica © 2025 | PostgreSQL");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(189, 195, 199));
        
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(149, 165, 166));
        
        panel.add(lblFooter, BorderLayout.WEST);
        panel.add(lblVersion, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Crea la barra de menú mejorada
     */
    private void crearMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        
        // Menú Pacientes
        menuPacientes = new JMenu("👥 Pacientes");
        menuPacientes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemNuevoPaciente = new JMenuItem("Nuevo Paciente");
        JMenuItem itemListarPacientes = new JMenuItem("Listar Pacientes");
        JMenuItem itemBuscarPaciente = new JMenuItem("Buscar Paciente");
        menuPacientes.add(itemNuevoPaciente);
        menuPacientes.add(itemListarPacientes);
        menuPacientes.add(itemBuscarPaciente);
        
        itemNuevoPaciente.addActionListener(e -> abrirGestionPacientes());
        itemListarPacientes.addActionListener(e -> abrirGestionPacientes());
        itemBuscarPaciente.addActionListener(e -> abrirGestionPacientes());
        
        // Menú Recetas
        menuRecetas = new JMenu("📋 Recetas");
        menuRecetas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemNuevaReceta = new JMenuItem("Nueva Receta");
        JMenuItem itemListarRecetas = new JMenuItem("Listar Recetas");
        JMenuItem itemBuscarReceta = new JMenuItem("Buscar Receta");
        menuRecetas.add(itemNuevaReceta);
        menuRecetas.add(itemListarRecetas);
        menuRecetas.add(itemBuscarReceta);
        
        itemNuevaReceta.addActionListener(e -> abrirGestionRecetas());
        itemListarRecetas.addActionListener(e -> abrirGestionRecetas());
        itemBuscarReceta.addActionListener(e -> abrirGestionRecetas());
        
        // Menú Oftalmólogos
        menuOftalmologos = new JMenu("👨‍⚕️ Oftalmólogos");
        menuOftalmologos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemNuevoOftalmologo = new JMenuItem("Nuevo Oftalmólogo");
        JMenuItem itemListarOftalmologos = new JMenuItem("Listar Oftalmólogos");
        menuOftalmologos.add(itemNuevoOftalmologo);
        menuOftalmologos.add(itemListarOftalmologos);
        
        itemNuevoOftalmologo.addActionListener(e -> abrirGestionOftalmologos());
        itemListarOftalmologos.addActionListener(e -> abrirGestionOftalmologos());
        
        // Menú Reportes
        menuReportes = new JMenu("📊 Reportes");
        menuReportes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemReportePacientes = new JMenuItem("Reporte de Pacientes");
        JMenuItem itemReporteRecetas = new JMenuItem("Reporte de Recetas");
        menuReportes.add(itemReportePacientes);
        menuReportes.add(itemReporteRecetas);
        
        // Menú Administración
        menuAdministracion = new JMenu("🔐 Administración");
        menuAdministracion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        JMenuItem itemRespaldo = new JMenuItem("Respaldo de BD");
        menuAdministracion.add(itemUsuarios);
        menuAdministracion.add(itemRespaldo);
        
        // Menú Ayuda
        menuAyuda = new JMenu("❓ Ayuda");
        menuAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        itemAcercaDe.addActionListener(e -> mostrarAcercaDe());
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> cerrarSesion());
        menuAyuda.add(itemAcercaDe);
        menuAyuda.addSeparator();
        menuAyuda.add(itemCerrarSesion);
        
        // Agregar menús a la barra
        menuBar.add(menuPacientes);
        menuBar.add(menuRecetas);
        menuBar.add(menuOftalmologos);
        menuBar.add(menuReportes);
        menuBar.add(menuAdministracion);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    private void configurarPermisos() {
        if (usuarioActual.esOftalmologo()) {
            menuAdministracion.setEnabled(false);
        }
    }
    
    // --- LÓGICA DE ACCIÓN DE TARJETAS MODIFICADA ---
    private void accionTarjeta(String titulo) {
        switch (titulo) {
            case "Pacientes":
                abrirGestionPacientes();
                break;
            case "Recetas":
                abrirGestionRecetas();
                break;
            case "Oftalmólogos":
                abrirGestionOftalmologos();
                break;
            case "Buscar Recetas": // <--- Caso agregado
                abrirBuscarRecetas();
                break;
            case "Reportes": // <--- Caso agregado
                abrirReportes();
                break;
            case "Configuración": // <--- Caso agregado
                abrirConfiguracion();
                break;
            default:
                JOptionPane.showMessageDialog(this,
                        "Módulo: " + titulo + "\n(En desarrollo)",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // --- MÉTODOS DE APERTURA EXISTENTES ---
    private void abrirGestionPacientes() {
        GestionPacientesFrame frame = new GestionPacientesFrame(this);
        frame.setVisible(true);
    }
    
    private void abrirGestionOftalmologos() {
        GestionOftalmologosFrame frame = new GestionOftalmologosFrame(this);
        frame.setVisible(true);
    }
    
    private void abrirGestionRecetas() {
        GestionRecetasFrame frame = new GestionRecetasFrame(this);
        frame.setVisible(true);
    }
    
    // --- MÉTODOS DE APERTURA AGREGADOS (Solución) ---
    private void abrirBuscarRecetas() {
        // Asumiendo que BuscarRecetasFrame es un JDialog o JFrame
        BuscarRecetasFrame frame = new BuscarRecetasFrame(this); 
        frame.setVisible(true);
    }
    
    private void abrirReportes() {
        // ReportesFrame requiere el 'parent' y un String de 'tipo'
        ReportesFrame frame = new ReportesFrame(this, "General"); 
        frame.setVisible(true);
    }
    
    private void abrirConfiguracion() {
        // ConfiguracionFrame requiere el 'parent', el 'usuario' y un String de 'seccion'
        ConfiguracionFrame frame = new ConfiguracionFrame(this, usuarioActual, "General"); 
        frame.setVisible(true);
    }
    // ------------------------------------------------
    
    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this,
            "═══════════════════════════════════\n" +
            "   Sistema de Gestión Óptica\n" +
            "═══════════════════════════════════\n\n" +
            "Versión: 1.0.0\n" +
            "Base de Datos: PostgreSQL\n" +
            "Desarrollado en: Java + Swing\n\n" +
            "© 2025 - Todos los derechos reservados\n",
            "Acerca del Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar Cierre de Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            // Nota: Se asume que la clase LoginFrame existe en el paquete vista.
            LoginFrame login = new LoginFrame(); 
            login.setVisible(true);
        }
    }
    
    private String obtenerFechaActual() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formato = 
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ahora.format(formato);
    }
}