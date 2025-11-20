/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana de inicio de sesión del sistema
 */
public class LoginFrame extends JFrame {
    
    // Componentes de la interfaz
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    
    // DAO para validar usuarios
    private UsuarioDAO usuarioDAO;
    
    public LoginFrame() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        setLocationRelativeTo(null); // Centrar ventana
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema Óptico - Inicio de Sesión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 240, 240));
        
        // Título
        lblTitulo = new JLabel("SISTEMA DE RECETAS MÉDICAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 204));
        lblTitulo.setBounds(50, 20, 300, 30);
        panel.add(lblTitulo);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Óptica");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setBounds(165, 50, 100, 20);
        panel.add(lblSubtitulo);
        
        // Label Usuario
        lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsuario.setBounds(50, 100, 80, 25);
        panel.add(lblUsuario);
        
        // Campo de texto Usuario
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsuario.setBounds(140, 100, 200, 30);
        panel.add(txtUsuario);
        
        // Label Password
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(50, 145, 90, 25);
        panel.add(lblPassword);
        
        // Campo de contraseña
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBounds(140, 145, 200, 30);
        panel.add(txtPassword);
        
        // Botón Iniciar Sesión
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(80, 200, 130, 35);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        panel.add(btnLogin);
        
        // Botón Salir
        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSalir.setBackground(new Color(200, 200, 200));
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(220, 200, 120, 35);
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(btnSalir);
        
        // Agregar panel a la ventana
        add(panel);
        
        // Permitir login con Enter
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
    }
    
    /**
     * Método para validar el inicio de sesión
     */
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        // Validar campos vacíos
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese su usuario", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
            txtUsuario.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese su contraseña", 
                "Campo vacío", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }
        
        // Validar credenciales en la base de datos
        Usuario usuarioValidado = usuarioDAO.validarUsuario(usuario, password);
        
        if (usuarioValidado != null) {
            // Login exitoso
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido " + usuarioValidado.getUsuario() + "!\n" +
                "Rol: " + usuarioValidado.getRol(), 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cerrar ventana de login
            this.dispose();
            
            // Abrir ventana principal del sistema
            MenuPrincipal menu = new MenuPrincipal(usuarioValidado);
            menu.setVisible(true);
            
            System.out.println("Usuario autenticado: " + usuarioValidado);
            
        } else {
            // Login fallido
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de autenticación", 
                JOptionPane.ERROR_MESSAGE);
            
            // Limpiar campos
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }
    
    /**
     * Método main para probar el LoginFrame
     */
    public static void main(String[] args) {
        // Establecer Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Mostrar ventana de login
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });
    }
}