package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana de inicio de sesión - Diseño Moderno y Animado
 */
public class LoginFrame extends JFrame {
    
    // Componentes de la interfaz
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JCheckBox chkMostrarPassword;
    
    // DAO para validar usuarios
    private UsuarioDAO usuarioDAO;
    
    // Colores del tema
    private final Color COLOR_PRIMARIO = new Color(74, 144, 226);
    private final Color COLOR_SECUNDARIO = new Color(67, 97, 238);
    private final Color COLOR_ACENTO = new Color(80, 227, 194);
    private final Color COLOR_FONDO = new Color(240, 242, 245);
    private final Color COLOR_EXITO = new Color(46, 213, 115);
    
    public LoginFrame() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema Optico - Inicio de Sesion");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true); // Sin bordes para forma personalizada
        
        // Forma redondeada de la ventana
        setShape(new RoundRectangle2D.Double(0, 0, 500, 650, 30, 30));
        
        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, COLOR_PRIMARIO,
                    0, getHeight(), COLOR_SECUNDARIO
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panelPrincipal.setLayout(null);
        
        // ===== SECCION SUPERIOR CON LOGO =====
        JPanel panelSuperior = new JPanel();
        panelSuperior.setOpaque(false);
        panelSuperior.setBounds(0, 40, 500, 150);
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        
        // Icono grande (circulo con texto)
        JPanel iconoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Circulo blanco
                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.fillOval(10, 10, 80, 80);
                
                // Letra O
                g2d.setColor(COLOR_SECUNDARIO);
                g2d.setFont(new Font("Arial", Font.BOLD, 50));
                g2d.drawString("O", 35, 68);
            }
        };
        iconoPanel.setOpaque(false);
        iconoPanel.setPreferredSize(new Dimension(100, 100));
        iconoPanel.setMaximumSize(new Dimension(100, 100));
        iconoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Titulo
        JLabel lblTitulo = new JLabel("Sistema Optico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitulo
        JLabel lblSubtitulo = new JLabel("Gestion de Recetas Medicas");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelSuperior.add(iconoPanel);
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(5));
        panelSuperior.add(lblSubtitulo);
        
        panelPrincipal.add(panelSuperior);
        
        // ===== PANEL DE LOGIN (TARJETA BLANCA) =====
        JPanel panelLogin = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Sombra
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                // Fondo blanco
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 25, 25);
            }
        };
        panelLogin.setLayout(null);
        panelLogin.setOpaque(false);
        panelLogin.setBounds(50, 220, 400, 370);
        
        // Label "Iniciar Sesion"
        JLabel lblIniciar = new JLabel("Iniciar Sesion");
        lblIniciar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblIniciar.setForeground(new Color(44, 62, 80));
        lblIniciar.setBounds(30, 25, 200, 35);
        panelLogin.add(lblIniciar);
        
        // Label Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(52, 73, 94));
        lblUsuario.setBounds(30, 80, 100, 25);
        panelLogin.add(lblUsuario);
        
        // Campo de texto Usuario estilizado
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtUsuario.setBounds(30, 110, 340, 45);
        txtUsuario.setBackground(new Color(248, 249, 250));
        
        // Efecto focus en campo usuario
        txtUsuario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 2, true),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        panelLogin.add(txtUsuario);
        
        // Label Contrasena
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(52, 73, 94));
        lblPassword.setBounds(30, 170, 120, 25);
        panelLogin.add(lblPassword);
        
        // Campo de contrasena estilizado
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtPassword.setBounds(30, 200, 340, 45);
        txtPassword.setBackground(new Color(248, 249, 250));
        
        // Efecto focus en campo contrasena
        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_PRIMARIO, 2, true),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        // Permitir login con Enter
        txtPassword.addActionListener(e -> iniciarSesion());
        panelLogin.add(txtPassword);
        
        // Checkbox mostrar contrasena
        chkMostrarPassword = new JCheckBox("Mostrar contraseña");
        chkMostrarPassword.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkMostrarPassword.setForeground(new Color(127, 140, 141));
        chkMostrarPassword.setOpaque(false);
        chkMostrarPassword.setBounds(30, 253, 150, 20);
        chkMostrarPassword.addActionListener(e -> {
            if (chkMostrarPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('*');
            }
        });
        panelLogin.add(chkMostrarPassword);
        
        // Boton Iniciar Sesion con gradiente
        btnLogin = new JButton("Iniciar Sesion") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    GradientPaint gp = new GradientPaint(0, 0, COLOR_SECUNDARIO.darker(), 0, getHeight(), COLOR_PRIMARIO.darker());
                    g2d.setPaint(gp);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, COLOR_SECUNDARIO.brighter(), 0, getHeight(), COLOR_PRIMARIO.brighter());
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, COLOR_SECUNDARIO, 0, getHeight(), COLOR_PRIMARIO);
                    g2d.setPaint(gp);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Texto
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setBounds(30, 285, 340, 50);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> iniciarSesion());
        panelLogin.add(btnLogin);
        
        panelPrincipal.add(panelLogin);
        
        // ===== BOTON SALIR FLOTANTE =====
        btnSalir = new JButton("X") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(231, 76, 60));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 118, 117));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 150));
                }
                
                g2d.fillOval(0, 0, getWidth(), getHeight());
                
                // Texto
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
            }
        };
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSalir.setBounds(445, 15, 40, 40);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> System.exit(0));
        panelPrincipal.add(btnSalir);
        
        // Footer
        JLabel lblFooter = new JLabel("(c) 2025 Sistema de Gestion Optica");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(255, 255, 255, 150));
        lblFooter.setBounds(0, 610, 500, 20);
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(lblFooter);
        
        add(panelPrincipal);
        
        // Hacer la ventana arrastrable
        hacerVentanaArrastrable(panelPrincipal);
    }
    
    /**
     * Hace que la ventana se pueda arrastrar
     */
    private void hacerVentanaArrastrable(JPanel panel) {
        final Point[] mouseDownCompCoords = {null};
        
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }
        });
        
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords[0].x, 
                           currCoords.y - mouseDownCompCoords[0].y);
            }
        });
    }
    
    /**
     * Metodo para validar el inicio de sesion (LOGICA ORIGINAL INTACTA)
     */
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        // Validar campos vacios
        if (usuario.isEmpty()) {
            mostrarError("Por favor ingrese su usuario");
            txtUsuario.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            mostrarError("Por favor ingrese su contrasena");
            txtPassword.requestFocus();
            return;
        }
        
        // Animacion de carga
        btnLogin.setText("Validando...");
        btnLogin.setEnabled(false);
        
        // Simular delay para efecto visual
        Timer timer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validar credenciales en la base de datos
                Usuario usuarioValidado = usuarioDAO.validarUsuario(usuario, password);
                
                if (usuarioValidado != null) {
                    // Login exitoso
                    mostrarExito("Bienvenido " + usuarioValidado.getUsuario() + "!\nRol: " + usuarioValidado.getRol());
                    
                    // Cerrar ventana de login
                    dispose();
                    
                    // Abrir ventana principal del sistema
                    MenuPrincipal menu = new MenuPrincipal(usuarioValidado);
                    menu.setVisible(true);
                    
                    System.out.println("Usuario autenticado: " + usuarioValidado);
                    
                } else {
                    // Login fallido
                    mostrarError("Usuario o contraseña incorrectos");
                    
                    // Limpiar campos
                    txtPassword.setText("");
                    txtUsuario.requestFocus();
                    
                    // Restaurar boton
                    btnLogin.setText("Iniciar Sesion");
                    btnLogin.setEnabled(true);
                    
                    // Efecto de shake
                    shakeWindow();
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Efecto de vibracion en la ventana
     */
    private void shakeWindow() {
        Point location = getLocation();
        int shakeCount = 0;
        final int maxShakes = 10;
        
        Timer shakeTimer = new Timer(50, null);
        shakeTimer.addActionListener(new ActionListener() {
            int count = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count < maxShakes) {
                    if (count % 2 == 0) {
                        setLocation(location.x + 10, location.y);
                    } else {
                        setLocation(location.x - 10, location.y);
                    }
                    count++;
                } else {
                    setLocation(location);
                    shakeTimer.stop();
                }
            }
        });
        shakeTimer.start();
    }
    
    /**
     * Muestra mensaje de error estilizado
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Atencion",
            JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Muestra mensaje de exito estilizado
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this,
            mensaje,
            "Exito",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Metodo main para probar el LoginFrame
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}