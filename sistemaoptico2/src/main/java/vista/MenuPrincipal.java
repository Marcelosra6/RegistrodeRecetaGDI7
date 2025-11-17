/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.GestionRecetasFrame;


/**
 * Ventana principal del sistema después del login
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
    
    public MenuPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        initComponents();
        configurarPermisos();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema de Recetas Médicas - Menú Principal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        
        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 245, 245));
        
        // Panel superior (información del usuario)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(0, 102, 204));
        panelSuperior.setPreferredSize(new Dimension(900, 80));
        panelSuperior.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        lblBienvenida = new JLabel("Bienvenido: " + usuarioActual.getUsuario());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        lblBienvenida.setForeground(Color.WHITE);
        
        lblRol = new JLabel("| Rol: " + usuarioActual.getRol().toUpperCase());
        lblRol.setFont(new Font("Arial", Font.PLAIN, 16));
        lblRol.setForeground(Color.WHITE);
        
        panelSuperior.add(lblBienvenida);
        panelSuperior.add(lblRol);
        
        // Panel central con diseño de tarjetas
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 3, 20, 20));
        panelCentral.setBackground(new Color(245, 245, 245));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Crear tarjetas de acceso rápido
        panelCentral.add(crearTarjeta("Pacientes", "Gestionar pacientes", "👤"));
        panelCentral.add(crearTarjeta("Recetas", "Nueva receta médica", "📋"));
        panelCentral.add(crearTarjeta("Oftalmólogos", "Gestionar oftalmólogos", "👨‍⚕️"));
        panelCentral.add(crearTarjeta("Buscar Recetas", "Buscar por paciente", "🔍"));
        panelCentral.add(crearTarjeta("Reportes", "Ver estadísticas", "📊"));
        panelCentral.add(crearTarjeta("Configuración", "Opciones del sistema", "⚙️"));
        
        // Panel inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(230, 230, 230));
        panelInferior.setPreferredSize(new Dimension(900, 40));
        
        JLabel lblFooter = new JLabel("Sistema de Recetas Médicas - Óptica © 2025");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInferior.add(lblFooter);
        
        // Agregar paneles a la ventana
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Crear barra de menú
        crearMenuBar();
    }
    
    /**
     * Crea una tarjeta de acceso rápido
     */
    private JPanel crearTarjeta(String titulo, String descripcion, String emoji) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BorderLayout(10, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Emoji (ícono)
        JLabel lblEmoji = new JLabel(emoji, SwingConstants.CENTER);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        
        // Panel de texto
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(5));
        panelTexto.add(lblDesc);
        
        tarjeta.add(lblEmoji, BorderLayout.CENTER);
        tarjeta.add(panelTexto, BorderLayout.SOUTH);
        
        // Agregar evento click
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accionTarjeta(titulo);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(new Color(240, 248, 255));
                panelTexto.setBackground(new Color(240, 248, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tarjeta.setBackground(Color.WHITE);
                panelTexto.setBackground(Color.WHITE);
            }
        });
        
        return tarjeta;
    }
    
    /**
     * Crea la barra de menú
     */
    private void crearMenuBar() {
        menuBar = new JMenuBar();
        
        // Menú Pacientes
        menuPacientes = new JMenu("Pacientes");
        menuPacientes.setFont(new Font("Arial", Font.PLAIN, 14));
        JMenuItem itemNuevoPaciente = new JMenuItem("Nuevo Paciente");
        JMenuItem itemListarPacientes = new JMenuItem("Listar Pacientes");
        JMenuItem itemBuscarPaciente = new JMenuItem("Buscar Paciente");
        menuPacientes.add(itemNuevoPaciente);
        menuPacientes.add(itemListarPacientes);
        menuPacientes.add(itemBuscarPaciente);

        // Acciones de los ítems de Pacientes
        itemNuevoPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionPacientes(); // luego podrías abrir directamente el formulario "Nuevo"
            }
        });

        itemListarPacientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionPacientes();
            }
        });

        itemBuscarPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirGestionPacientes();
            }
        });


        // Menú Recetas
        menuRecetas = new JMenu("Recetas");
        menuRecetas.setFont(new Font("Arial", Font.PLAIN, 14));
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
        menuOftalmologos = new JMenu("Oftalmólogos");
        menuOftalmologos.setFont(new Font("Arial", Font.PLAIN, 14));
        JMenuItem itemNuevoOftalmologo = new JMenuItem("Nuevo Oftalmólogo");
        JMenuItem itemListarOftalmologos = new JMenuItem("Listar Oftalmólogos");
        menuOftalmologos.add(itemNuevoOftalmologo);
        menuOftalmologos.add(itemListarOftalmologos);

        itemNuevoOftalmologo.addActionListener(e -> abrirGestionOftalmologos());
        itemListarOftalmologos.addActionListener(e -> abrirGestionOftalmologos());


        // Menú Reportes
        menuReportes = new JMenu("Reportes");
        menuReportes.setFont(new Font("Arial", Font.PLAIN, 14));
        JMenuItem itemReportePacientes = new JMenuItem("Reporte de Pacientes");
        JMenuItem itemReporteRecetas = new JMenuItem("Reporte de Recetas");
        menuReportes.add(itemReportePacientes);
        menuReportes.add(itemReporteRecetas);
        
        // Menú Administración (solo para admin)
        menuAdministracion = new JMenu("Administración");
        menuAdministracion.setFont(new Font("Arial", Font.PLAIN, 14));
        JMenuItem itemUsuarios = new JMenuItem("Gestionar Usuarios");
        JMenuItem itemRespaldo = new JMenuItem("Respaldo de BD");
        menuAdministracion.add(itemUsuarios);
        menuAdministracion.add(itemRespaldo);
        
        // Menú Ayuda
        menuAyuda = new JMenu("Ayuda");
        menuAyuda.setFont(new Font("Arial", Font.PLAIN, 14));
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de");
        itemAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarAcercaDe();
            }
        });
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        itemCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
        menuAyuda.add(itemAcercaDe);
        menuAyuda.addSeparator();
        menuAyuda.add(itemCerrarSesion);
        
        // Agregar menús a la barra
        menuBar.add(menuPacientes);
        menuBar.add(menuRecetas);
        menuBar.add(menuOftalmologos);
        menuBar.add(menuReportes);
        menuBar.add(menuAdministracion);
        menuBar.add(menuAyuda);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Configura los permisos según el rol del usuario
     */
    private void configurarPermisos() {
        if (usuarioActual.esOftalmologo()) {
            // Los oftalmólogos no tienen acceso a administración
            menuAdministracion.setEnabled(false);
        }
    }
    
    /**
     * Acciones de las tarjetas
     */
    private void accionTarjeta(String titulo) {
        switch (titulo) {
            case "Pacientes":
                abrirGestionPacientes();
                break;
            // case "Oftalmólogos": abrirGestionOftalmologos(); break;
            case "Oftalmólogos":
                abrirGestionOftalmologos();
                break;
            case "Oftalmologos":
                abrirGestionOftalmologos();
                break;
            // más adelante:
            // case "Recetas": abrirGestionRecetas(); break;
            case "Recetas":
                abrirGestionRecetas();
                break;


            default:
                JOptionPane.showMessageDialog(this,
                        "Módulo: " + titulo + "\n(En desarrollo)",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Abre el módulo de gestión de pacientes
     */
    private void abrirGestionPacientes() {
        GestionPacientesFrame frame = new GestionPacientesFrame(this);
        frame.setVisible(true);
        // Más adelante:
        // GestionPacientesFrame frame = new GestionPacientesFrame(this);
        // frame.setLocationRelativeTo(this);
        // frame.setVisible(true);
    }

    private void abrirGestionOftalmologos() {
        GestionOftalmologosFrame frame = new GestionOftalmologosFrame(this);
        frame.setVisible(true);
    }

    private void abrirGestionRecetas() {
        GestionRecetasFrame frame = new GestionRecetasFrame(this);
        frame.setVisible(true);
    }


    /**
     * Muestra información del sistema
     */
    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this, 
            "Sistema de Recetas Médicas\n" +
            "Versión 1.0\n\n" +
            "Desarrollado para Óptica\n" +
            "© 2025", 
            "Acerca de", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Cierra sesión y vuelve al login
     */
    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        }
    }
}