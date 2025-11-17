package vista;

import dao.OftalmologoDAO;
import modelo.Oftalmologo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionOftalmologosFrame extends JDialog {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    private final OftalmologoDAO oftDAO = new OftalmologoDAO();

    public GestionOftalmologosFrame(Frame owner) {
        super(owner, "Gestión de Oftalmólogos", true);
        initComponents();
        cargarTodos();
        setSize(700, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRefrescar = new JButton("Refrescar");
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnRefrescar);
        panelSuperior.add(panelBuscar, BorderLayout.WEST);

        // Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"DNI", "Nombre", "Apellido Paterno", "Apellido Materno"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        // Listeners
        btnBuscar.addActionListener(e -> buscar());
        btnRefrescar.addActionListener(e -> cargarTodos());
        btnNuevo.addActionListener(e -> nuevo());
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCerrar.addActionListener(e -> dispose());

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTodos() {
        List<Oftalmologo> lista = oftDAO.listarTodos();
        llenarTabla(lista);
    }

    private void buscar() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTodos();
            return;
        }
        List<Oftalmologo> lista = oftDAO.buscar(filtro);
        llenarTabla(lista);
    }

    private void llenarTabla(List<Oftalmologo> lista) {
        modeloTabla.setRowCount(0);
        for (Oftalmologo o : lista) {
            modeloTabla.addRow(new Object[]{
                    o.getDni(),
                    o.getNombre(),
                    o.getApellidoPaterno(),
                    o.getApellidoMaterno()
            });
        }
    }

    private void nuevo() {
        OftalmologoFormDialog dialog = new OftalmologoFormDialog(
                (Frame) getOwner(), "Nuevo Oftalmólogo", null);
        dialog.setVisible(true);

        if (dialog.isGuardado()) {
            boolean ok = oftDAO.insertarOftalmologo(dialog.getOftalmologo());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Oftalmólogo registrado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el oftalmólogo.\n" +
                                "Verifica si el DNI ya existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un oftalmólogo de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);
        Oftalmologo existente = oftDAO.obtenerPorDni(dni);
        if (existente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el oftalmólogo seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        OftalmologoFormDialog dialog = new OftalmologoFormDialog(
                (Frame) getOwner(), "Editar Oftalmólogo", existente);
        dialog.setVisible(true);

        if (dialog.isGuardado()) {
            boolean ok = oftDAO.actualizarOftalmologo(dialog.getOftalmologo());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Oftalmólogo actualizado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el oftalmólogo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un oftalmólogo de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al oftalmólogo con DNI " + dni + "?\n" +
                        "Si tiene recetas o usuarios asociados, la operación puede fallar.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = oftDAO.eliminarOftalmologo(dni);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Oftalmólogo eliminado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el oftalmólogo.\n" +
                                "Verifica si tiene recetas o usuarios asociados.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
