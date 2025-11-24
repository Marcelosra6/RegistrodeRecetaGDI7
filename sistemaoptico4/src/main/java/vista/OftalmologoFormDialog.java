package vista;

import modelo.Oftalmologo;
import dao.Validaciones;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class OftalmologoFormDialog extends JDialog {

    private JTextField txtDni;
    private JTextField txtNombre;
    private JTextField txtApellidoPaterno;
    private JTextField txtApellidoMaterno;

    private boolean guardado = false;
    private Oftalmologo oftalmologo;

    // Paleta moderna
    private final Color BG = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color BORDER = new Color(220, 225, 233);

    public OftalmologoFormDialog(Frame owner, String titulo, Oftalmologo oftalmologo) {
        super(owner, titulo, true);
        this.oftalmologo = (oftalmologo != null) ? oftalmologo : new Oftalmologo();

        initComponents();
        cargarDatos();
        setLocationRelativeTo(owner);
        pack();
    }

    private void initComponents() {

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 8, 8));
        panelCampos.setBackground(CARD);
        panelCampos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Campos
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

        // Botones modernizados
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(BG);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(200, 230, 255));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(230, 230, 230));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);

        btnGuardar.addActionListener(e -> onGuardar());
        btnCancelar.addActionListener(e -> onCancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        if (oftalmologo.getDni() != null) {
            txtDni.setText(oftalmologo.getDni());
            txtDni.setEnabled(false);
            txtNombre.setText(oftalmologo.getNombre());
            txtApellidoPaterno.setText(oftalmologo.getApellidoPaterno());
            txtApellidoMaterno.setText(oftalmologo.getApellidoMaterno());
        }
    }

    private void onGuardar() {
        String dni = Validaciones.convertirYValidarDni(txtDni.getText());
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

        oftalmologo.setDni(dni);
        oftalmologo.setNombre(nombre);
        oftalmologo.setApellidoPaterno(apePat);
        oftalmologo.setApellidoMaterno(apeMat);

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

    public Oftalmologo getOftalmologo() {
        return oftalmologo;
    }
}
