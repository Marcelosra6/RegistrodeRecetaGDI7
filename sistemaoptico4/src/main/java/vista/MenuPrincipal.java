package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del sistema despues del login - Diseno Moderno
 */
public class MenuPrincipal extends JFrame {
    
    private Usuario usuarioActual;
    
    // Componentes de la interfaz
    private JLabel lblBienvenida;
    private JLabel lblRol;
    
    // Colores del tema moderno
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);
    private final Color COLOR_SECUNDARIO = new Color(67, 97, 238);
    private final Color COLOR_EXITO = new Color(46, 204, 113);
    private final Color COLOR_ADVERTENCIA = new Color(241, 196, 15);
    private final Color COLOR_INFO = new Color(155, 89, 182);
    private final Color COLOR_GRIS = new Color(149, 165, 166);
    private final Color COLOR_FONDO = new Color(236, 240, 241);
    private final Color COLOR_TARJETA = Color.WHITE;
    private final Color COLOR_TARJETA_HOVER = new Color(240, 248, 255);
    
    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Configuracion de la ventana
        setTitle("Sistema de Gestion Optica - Dashboard");
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
    }
    
    /**
     * Crea el panel superior con diseno moderno
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARIO,
                    getWidth(), 0, COLOR_SECUNDARIO
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(1200, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Panel izquierdo con info de usuario
        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzq.setOpaque(false);
        
        JPanel circuloUsuario = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillOval(0, 0, 60, 60);
                
                g2d.setColor(COLOR_SECUNDARIO);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                String inicial = usuarioActual.getUsuario().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int x = (60 - fm.stringWidth(inicial)) / 2;
                int y = (60 + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(inicial, x, y);
            }
        };
        circuloUsuario.setPreferredSize(new Dimension(60, 60));
        circuloUsuario.setOpaque(false);
        
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        lblBienvenida = new JLabel("Bienvenido, " + usuarioActual.getUsuario() + "!");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBienvenida.setForeground(Color.WHITE);
        
        lblRol = new JLabel("Rol: " + usuarioActual.getRol().toUpperCase());
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRol.setForeground(new Color(236, 240, 241));
        
        panelInfo.add(lblBienvenida);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblRol);
        
        panelIzq.add(circuloUsuario);
        panelIzq.add(panelInfo);
        
        JPanel panelDer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDer.setOpaque(false);
        
        JLabel lblFecha = new JLabel(obtenerFechaActual());
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFecha.setForeground(Color.WHITE);
        panelDer.add(lblFecha);
        
        panel.add(panelIzq, BorderLayout.WEST);
        panel.add(panelDer, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Crea el panel central con tarjetas modernas
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
        panel.add(crearTarjetaModerna("Pacientes", "Gestionar pacientes", "P", COLOR_PRIMARIO), gbc);
        
        gbc.gridx = 1;
        panel.add(crearTarjetaModerna("Recetas", "Nueva receta medica", "R", COLOR_EXITO), gbc);
        
        gbc.gridx = 2;
        panel.add(crearTarjetaModerna("Oftalmologos", "Gestionar oftalmologos", "O", COLOR_SECUNDARIO), gbc);
        
        gbc.gridx = 3;
        panel.add(crearTarjetaModerna("Buscar Recetas", "Buscar por paciente", "B", COLOR_ADVERTENCIA), gbc);
    
        return panel;
    }
    
    /**
     * Tarjetas modernas
     */
    private JPanel crearTarjetaModerna(String titulo, String descripcion, String letra, Color colorTema) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(15, 15));
        tarjeta.setBackground(COLOR_TARJETA);
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(200, 200, 200, 100)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
            )
        ));
        
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JPanel circuloIcono = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(new Color(colorTema.getRed(), colorTema.getGreen(), colorTema.getBlue(), 30));
                g2d.fillOval(35, 5, 80, 80);
                
                g2d.setColor(colorTema);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 40));
                FontMetrics fm = g2d.getFontMetrics();
                int x = 35 + (80 - fm.stringWidth(letra)) / 2;
                int y = 5 + (80 + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(letra, x, y);
            }
        };
        circuloIcono.setPreferredSize(new Dimension(150, 90));
        circuloIcono.setOpaque(false);
        
        JPanel barraColor = new JPanel();
        barraColor.setBackground(colorTema);
        barraColor.setPreferredSize(new Dimension(0, 5));
        
        panelSuperior.add(circuloIcono, BorderLayout.CENTER);
        panelSuperior.add(barraColor, BorderLayout.SOUTH);
        
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
     * Panel inferior
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(1200, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        
        JLabel lblFooter = new JLabel("Sistema de Gestion Optica (c) 2025 | PostgreSQL");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(189, 195, 199));
        
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(149, 165, 166));
        
        panel.add(lblFooter, BorderLayout.WEST);
        panel.add(lblVersion, BorderLayout.EAST);
        
        return panel;
    }
    
    private void accionTarjeta(String titulo) {
        switch (titulo) {
            case "Pacientes":
                abrirGestionPacientes();
                break;
            case "Recetas":
                abrirGestionRecetas();
                break;
            case "Oftalmologos":
                abrirGestionOftalmologos();
                break;
            case "Buscar Recetas":
                abrirBuscarRecetas();
                break;

            default:
                JOptionPane.showMessageDialog(this,
                    "Modulo: " + titulo + "\n(En desarrollo)",
                    "Informacion",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
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
    
    private void abrirBuscarRecetas() {
        BuscarRecetasFrame frame = new BuscarRecetasFrame(this);
        frame.setVisible(true);
    }

    
    private String obtenerFechaActual() {
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formato = 
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ahora.format(formato);
    }
}
