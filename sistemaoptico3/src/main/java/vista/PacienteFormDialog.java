package vista;

import modelo.Paciente;

import javax.swing.*;
import java.awt.*;

public class PacienteFormDialog extends JDialog {

    private JTextField txtDni;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;

    private boolean guardado = false;
    private Paciente paciente;

    public PacienteFormDialog(Frame owner, String titulo, Paciente paciente) {
        super(owner, titulo, true);
        this.paciente = (paciente != null) ? paciente : new Paciente();

        initComponents();
        cargarDatos();
        setLocationRelativeTo(owner);
        pack();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 5, 5));

        panelCampos.add(new JLabel("DNI:"));
        txtDni = new JTextField(10);
        panelCampos.add(txtDni);

        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField(20);
        panelCampos.add(txtNombre);

        panelCampos.add(new JLabel("Apellido Paterno:"));
        txtApellidoPaterno = new JTextField(20);
        panelCampos.add(txtApellidoPaterno);

        panelCampos.add(new JLabel("Apellido Materno:"));
        txtApellidoMaterno = new JTextField(20);
        panelCampos.add(txtApellidoMaterno);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> onGuardar());
        btnCancelar.addActionListener(e -> onCancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (paciente.getDni() != null) {
            // Modo edición
            txtDni.setText(paciente.getDni());
            txtDni.setEnabled(false); // El DNI no se cambia
            txtNombre.setText(paciente.getNombre());
            txtApellidoPaterno.setText(paciente.getApellidoPaterno());
            txtApellidoMaterno.setText(paciente.getApellidoMaterno());
        }
    }

    private void onGuardar() {
        String dni = txtDni.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apePat = txtApellidoPaterno.getText().trim();
        String apeMat = txtApellidoMaterno.getText().trim();

        if (dni.isEmpty() || nombre.isEmpty() || apePat.isEmpty() || apeMat.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dni.length() != 8) {
            JOptionPane.showMessageDialog(this,
                    "El DNI debe tener 8 caracteres.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        paciente.setDni(dni);
        paciente.setNombre(nombre);
        paciente.setApellidoPaterno(apePat);
        paciente.setApellidoMaterno(apeMat);

        guardado = true;
        dispose();
    }

    private void onCancelar() {
        guardado = false;
        dispose();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}
