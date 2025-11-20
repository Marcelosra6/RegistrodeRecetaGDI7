/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Ventana para buscar recetas por diversos criterios
 */
public class BuscarRecetasFrame extends JDialog {
    
    private JTextField txtBuscarPaciente;
    private JTextField txtBuscarDNI;
    private JComboBox<String> cmbOftalmologo;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnVerDetalle;
    
    public BuscarRecetasFrame(JFrame parent) {
        super(parent, "Buscar Recetas", true);
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(1000, 650);
        setLayout(new BorderLayout(10, 10));
        
        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new GridBagLayout());
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Criterios de Búsqueda",
            0, 0, new Font("Segoe UI", Font.BOLD, 14),
            new Color(41, 128, 185)
        ));
        panelBusqueda.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // DNI Paciente
        gbc.gridx = 0; gbc.gridy = 0;
        panelBusqueda.add(new JLabel("DNI Paciente:"), gbc);
        
        gbc.gridx = 1;
        txtBuscarDNI = new JTextField(15);
        panelBusqueda.add(txtBuscarDNI, gbc);
        
        // Nombre Paciente
        gbc.gridx = 2; gbc.gridy = 0;
        panelBusqueda.add(new JLabel("Nombre Paciente:"), gbc);
        
        gbc.gridx = 3;
        txtBuscarPaciente = new JTextField(20);
        panelBusqueda.add(txtBuscarPaciente, gbc);
        
        // Oftalmólogo
        gbc.gridx = 0; gbc.gridy = 1;
        panelBusqueda.add(new JLabel("Oftalmólogo:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        cmbOftalmologo = new JComboBox<>(new String[]{"Todos", "Dr. García", "Dra. López"});
        panelBusqueda.add(cmbOftalmologo, gbc);
        
        // Botones
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(new Color(46, 204, 113));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.addActionListener(e -> buscarRecetas());
        panelBusqueda.add(btnBuscar, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        btnLimpiar = new JButton("🗑️ Limpiar");
        btnLimpiar.setBackground(new Color(149, 165, 166));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.addActionListener(e -> limpiarCampos());
        panelBusqueda.add(btnLimpiar, gbc);
        
        // Tabla de resultados
        String[] columnas = {"ID Receta", "DNI Paciente", "Paciente", "Oftalmólogo", "Fecha Examen", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResultados.setRowHeight(30);
        tablaResultados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaResultados.getTableHeader().setBackground(new Color(52, 152, 219));
        tablaResultados.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Resultados de Búsqueda"));
        
        // Panel de botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(Color.WHITE);
        
        btnVerDetalle = new JButton("📄 Ver Detalle");
        btnVerDetalle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVerDetalle.setBackground(new Color(41, 128, 185));
        btnVerDetalle.setForeground(Color.WHITE);
        btnVerDetalle.addActionListener(e -> verDetalle());
        
        JButton btnCerrar = new JButton("❌ Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnVerDetalle);
        panelBotones.add(btnCerrar);
        
        // Agregar componentes
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void buscarRecetas() {
        // TODO: Implementar búsqueda real en la base de datos
        modeloTabla.setRowCount(0);
        
        // Datos de ejemplo
        modeloTabla.addRow(new Object[]{"1", "12345678", "PÉREZ GARCÍA, JUAN", "Dr. García", "2025-01-15", "Completada"});
        modeloTabla.addRow(new Object[]{"2", "87654321", "LÓPEZ MARTÍNEZ, MARÍA", "Dra. López", "2025-01-16", "Completada"});
        
        JOptionPane.showMessageDialog(this,
            "Se encontraron " + modeloTabla.getRowCount() + " receta(s)",
            "Búsqueda Completada",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limpiarCampos() {
        txtBuscarDNI.setText("");
        txtBuscarPaciente.setText("");
        cmbOftalmologo.setSelectedIndex(0);
        modeloTabla.setRowCount(0);
    }
    
    private void verDetalle() {
        int filaSeleccionada = tablaResultados.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una receta",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Abrir ventana de detalle de receta
        String idReceta = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
        JOptionPane.showMessageDialog(this,
            "Mostrando detalle de receta #" + idReceta + "\n(Funcionalidad en desarrollo)",
            "Detalle de Receta",
            JOptionPane.INFORMATION_MESSAGE);
    }
}