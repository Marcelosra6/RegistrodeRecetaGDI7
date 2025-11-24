// Versión mejorada visualmente del PacienteFormDialog
// (Sin alterar la lógica interna)

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
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Datos del Paciente",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));
        panelCampos.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DNI
        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("DNI:"), gbc);

        gbc.gridx = 1;
        txtDni = new JTextField(12);
        txtDni.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelCampos.add(txtDni, gbc);

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelCampos.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelCampos.add(txtNombre, gbc);

        // Apellido paterno
        gbc.gridx = 0; gbc.gridy = 2;
        panelCampos.add(new JLabel("Apellido Paterno:"), gbc);

        gbc.gridx = 1;
        txtApellidoPaterno = new JTextField(20);
        txtApellidoPaterno.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelCampos.add(txtApellidoPaterno, gbc);

        // Apellido materno
        gbc.gridx = 0; gbc.gridy = 3;
        panelCampos.add(new JLabel("Apellido Materno:"), gbc);

        gbc.gridx = 1;
        txtApellidoMaterno = new JTextField(20);
        txtApellidoMaterno.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelCampos.add(txtApellidoMaterno, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.addActionListener(e -> onGuardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.addActionListener(e -> onCancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (paciente.getDni() != null) {
            txtDni.setText(paciente.getDni());
            txtDni.setEnabled(false);
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
        if (dni == null) {
            JOptionPane.showMessageDialog(this,
                    "El DNI es inválido.\nDebe ser un número positivo de máximo 8 dígitos (sin letras).",
                    "Error en DNI",
                    JOptionPane.WARNING_MESSAGE);
            return; // Detenemos el guardado
        }
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
