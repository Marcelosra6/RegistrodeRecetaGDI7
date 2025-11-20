package vista;

import dao.PacienteDAO;
import modelo.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionPacientesFrame extends JDialog {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    public GestionPacientesFrame(Frame owner) {
        super(owner, "Gestión de Pacientes", true);
        initComponents();
        cargarTodos();
        setSize(700, 400);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- Panel superior: búsqueda ---
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

        // --- Tabla ---
        modeloTabla = new DefaultTableModel(
                new Object[]{"DNI", "Nombre", "Apellido Paterno", "Apellido Materno"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // no editar directamente la tabla
            }
        };

        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPacientes);

        // --- Panel inferior: botones CRUD ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCerrar);

        // --- Listeners ---
        btnBuscar.addActionListener(e -> buscar());
        btnRefrescar.addActionListener(e -> cargarTodos());
        btnNuevo.addActionListener(e -> nuevoPaciente());
        btnEditar.addActionListener(e -> editarPacienteSeleccionado());
        btnEliminar.addActionListener(e -> eliminarPacienteSeleccionado());
        btnCerrar.addActionListener(e -> dispose());

        // --- Añadir a la ventana ---
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarTodos() {
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        llenarTabla(pacientes);
    }

    private void buscar() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTodos();
            return;
        }
        List<Paciente> pacientes = pacienteDAO.buscar(filtro);
        llenarTabla(pacientes);
    }

    private void llenarTabla(List<Paciente> pacientes) {
        modeloTabla.setRowCount(0);
        for (Paciente p : pacientes) {
            modeloTabla.addRow(new Object[]{
                    p.getDni(),
                    p.getNombre(),
                    p.getApellidoPaterno(),
                    p.getApellidoMaterno()
            });
        }
    }

    private void nuevoPaciente() {
        PacienteFormDialog dialog = new PacienteFormDialog(
                (Frame) getOwner(), "Nuevo Paciente", null);
        dialog.setVisible(true);

        if (dialog.isGuardado()) {
            boolean ok = pacienteDAO.insertarPaciente(dialog.getPaciente());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Paciente registrado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el paciente.\n" +
                                "Verifica si el DNI ya existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un paciente de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);
        Paciente existente = pacienteDAO.obtenerPorDni(dni);
        if (existente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el paciente seleccionado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        PacienteFormDialog dialog = new PacienteFormDialog(
                (Frame) getOwner(), "Editar Paciente", existente);
        dialog.setVisible(true);

        if (dialog.isGuardado()) {
            boolean ok = pacienteDAO.actualizarPaciente(dialog.getPaciente());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Paciente actualizado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el paciente.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un paciente de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar al paciente con DNI " + dni + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean ok = pacienteDAO.eliminarPaciente(dni);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Paciente eliminado correctamente.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el paciente.\n" +
                                "Es posible que tenga recetas asociadas.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
