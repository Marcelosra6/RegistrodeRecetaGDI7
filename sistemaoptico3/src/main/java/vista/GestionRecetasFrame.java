package vista;

import dao.RecetaDAO;
import dao.RecetaDAO.RecetaListado;
import vista.VerRecetaDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;



public class GestionRecetasFrame extends JDialog {

    private JTextField txtBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private final RecetaDAO recetaDAO = new RecetaDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public GestionRecetasFrame(Frame owner) {
        super(owner, "Gestión de Recetas", true);
        initComponents();
        cargarRecetas(null);
        setSize(800, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSup = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSup.add(new JLabel("Buscar por paciente/DNI:"));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Refrescar");
        panelSup.add(txtBuscar);
        panelSup.add(btnBuscar);
        panelSup.add(btnRefrescar);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Paciente", "Oftalmólogo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnNueva = new JButton("Nueva Receta");
        JButton btnVer = new JButton("Ver / Imprimir");
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnNueva);
        panelBotones.add(btnVer);
        panelBotones.add(btnCerrar);


        btnBuscar.addActionListener(e -> cargarRecetas(txtBuscar.getText().trim()));
        btnRefrescar.addActionListener(e -> cargarRecetas(null));
        btnNueva.addActionListener(e -> nuevaReceta());
        btnVer.addActionListener(e -> verSeleccionada());
        btnCerrar.addActionListener(e -> dispose());

        add(panelSup, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarRecetas(String filtro) {
        List<RecetaListado> lista = recetaDAO.listarRecetas(filtro);
        modeloTabla.setRowCount(0);
        for (RecetaListado r : lista) {
            modeloTabla.addRow(new Object[]{
                    r.getIdReceta(),
                    sdf.format(r.getFechaExamen()),
                    r.getNombrePaciente(),
                    r.getNombreOftalmologo()
            });
        }
    }

    private void nuevaReceta() {
        NuevaRecetaDialog dialog = new NuevaRecetaDialog((Frame) getOwner());
        dialog.setVisible(true);
        // al cerrar, refrescamos
        cargarRecetas(txtBuscar.getText().trim());
    }
    private void verSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una receta de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        VerRecetaDialog dialog = new VerRecetaDialog((Frame) getOwner(), id);
        dialog.setVisible(true);
    }

}
