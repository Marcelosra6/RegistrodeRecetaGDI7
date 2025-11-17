package vista;

import dao.OftalmologoDAO;
import dao.PacienteDAO;
import dao.RecetaDAO;
import modelo.DetalleReceta;
import modelo.Oftalmologo;
import modelo.Paciente;
import modelo.Receta;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevaRecetaDialog extends JDialog {

    private JComboBox<Paciente> cbPaciente;
    private JComboBox<Oftalmologo> cbOftalmologo;
    //private JTextField txtTipoLente;
    private JComboBox<String> cbTipoLente;
    private JTextField txtVisionCorregida;
    private JTextArea txtObservaciones;

    private JTextField txtEsfOI;
    private JTextField txtCilOI;
    private JTextField txtEjeOI;
    private JTextField txtEsfOD;
    private JTextField txtCilOD;
    private JTextField txtEjeOD;
    private JTextField txtAdicional;
    private JTextField txtDistanciaIP;

    //private JTextField txtEnfermedades; // separadas por coma
    private JCheckBox chkMiopia;
    private JCheckBox chkHipermetropia;
    private JCheckBox chkAstigmatismo;
    private JCheckBox chkPresbicia;
    private JTextField txtOtrasEnfermedades;


    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final OftalmologoDAO oftDAO = new OftalmologoDAO();
    private final RecetaDAO recetaDAO = new RecetaDAO();

    public NuevaRecetaDialog(Frame owner) {
        super(owner, "Nueva Receta", true);
        initComponents();
        cargarCombos();
        setSize(800, 500);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior: paciente y oftalmólogo
        JPanel panelCabecera = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCabecera.setBorder(BorderFactory.createTitledBorder("Datos principales"));

        panelCabecera.add(new JLabel("Paciente:"));
        cbPaciente = new JComboBox<>();
        panelCabecera.add(cbPaciente);

        panelCabecera.add(new JLabel("Oftalmólogo:"));
        cbOftalmologo = new JComboBox<>();
        panelCabecera.add(cbOftalmologo);

        // Panel central: detalles de la medida
        JPanel panelDetalle = new JPanel(new GridLayout(6, 4, 5, 5));
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Medidas de la receta"));

        panelDetalle.add(new JLabel("Tipo de lente:"));
        cbTipoLente = new JComboBox<>(new String[]{
                "Monofocal",
                "Bifocal",
                "Progresivo",
                "Ocupacional"
        });
        cbTipoLente.setSelectedIndex(0); // Monofocal por defecto
        panelDetalle.add(cbTipoLente);


        panelDetalle.add(new JLabel("Visión corregida:"));
        txtVisionCorregida = new JTextField("VL/VP");
        panelDetalle.add(txtVisionCorregida);

        panelDetalle.add(new JLabel("Esf. Ojo Izq.:"));
        txtEsfOI = new JTextField("0.00");
        panelDetalle.add(txtEsfOI);
        panelDetalle.add(new JLabel("Esf. Ojo Der.:"));
        txtEsfOD = new JTextField("0.00");
        panelDetalle.add(txtEsfOD);

        panelDetalle.add(new JLabel("Cil. Ojo Izq.:"));
        txtCilOI = new JTextField("0.00");
        panelDetalle.add(txtCilOI);
        panelDetalle.add(new JLabel("Cil. Ojo Der.:"));
        txtCilOD = new JTextField("0.00");
        panelDetalle.add(txtCilOD);

        panelDetalle.add(new JLabel("Eje Ojo Izq.:"));
        txtEjeOI = new JTextField("0.00");
        panelDetalle.add(txtEjeOI);
        panelDetalle.add(new JLabel("Eje Ojo Der.:"));
        txtEjeOD = new JTextField("0.00");
        panelDetalle.add(txtEjeOD);

        panelDetalle.add(new JLabel("Adicional:"));
        txtAdicional = new JTextField("0.00");
        panelDetalle.add(txtAdicional);
        panelDetalle.add(new JLabel("Dist. interpupilar:"));
        txtDistanciaIP = new JTextField("60");
        panelDetalle.add(txtDistanciaIP);

        // Observaciones + enfermedades
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        JPanel panelObs = new JPanel(new BorderLayout());
        panelObs.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        panelObs.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);

        JPanel panelEnf = new JPanel();
        panelEnf.setLayout(new BoxLayout(panelEnf, BoxLayout.Y_AXIS));
        panelEnf.setBorder(BorderFactory.createTitledBorder("Enfermedades"));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkMiopia = new JCheckBox("Miopía");
        chkHipermetropia = new JCheckBox("Hipermetropía");
        chkAstigmatismo = new JCheckBox("Astigmatismo");
        chkPresbicia = new JCheckBox("Presbicia");
        fila1.add(chkMiopia);
        fila1.add(chkHipermetropia);
        fila1.add(chkAstigmatismo);
        fila1.add(chkPresbicia);

        JPanel fila2 = new JPanel(new BorderLayout(5, 5));
        fila2.add(new JLabel("Otras (separadas por coma):"), BorderLayout.WEST);
        txtOtrasEnfermedades = new JTextField();
        fila2.add(txtOtrasEnfermedades, BorderLayout.CENTER);

        panelEnf.add(fila1);
        panelEnf.add(fila2);


        panelInferior.add(panelObs, BorderLayout.CENTER);
        panelInferior.add(panelEnf, BorderLayout.SOUTH);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarReceta());
        btnCancelar.addActionListener(e -> dispose());

        // 🔹 Panel central que combina medidas + obs/enfermedades
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(panelDetalle, BorderLayout.CENTER);
        panelCentral.add(panelInferior, BorderLayout.SOUTH);

        // 🔹 Ahora sí armamos el dialog
        add(panelCabecera, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        cbPaciente.removeAllItems();
        for (Paciente p : pacienteDAO.listarTodos()) {
            cbPaciente.addItem(p);
        }

        cbOftalmologo.removeAllItems();
        for (Oftalmologo o : oftDAO.listarTodos()) {
            cbOftalmologo.addItem(o);
        }
    }

    private void guardarReceta() {
        Paciente paciente = (Paciente) cbPaciente.getSelectedItem();
        Oftalmologo oft = (Oftalmologo) cbOftalmologo.getSelectedItem();

        if (paciente == null || oft == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un paciente y un oftalmólogo.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Construir Receta
            Receta receta = new Receta();
            receta.setDniPaciente(paciente.getDni());
            receta.setDniOftalmologo(oft.getDni());
            receta.setFechaExamen(LocalDateTime.now()); // ahora

            // Construir DetalleReceta
            DetalleReceta d = new DetalleReceta();
            d.setTipoDeLente((String) cbTipoLente.getSelectedItem());
            d.setVisionCorregida(txtVisionCorregida.getText().trim());
            d.setObservaciones(txtObservaciones.getText().trim());

            d.setEsfOjoIzquierdo(new BigDecimal(txtEsfOI.getText().trim()));
            d.setCilindroOjoIzquierdo(new BigDecimal(txtCilOI.getText().trim()));
            d.setEjeOjoIzquierdo(new BigDecimal(txtEjeOI.getText().trim()));

            d.setEsfOjoDerecho(new BigDecimal(txtEsfOD.getText().trim()));
            d.setCilindroOjoDerecho(new BigDecimal(txtCilOD.getText().trim()));
            d.setEjeOjoDerecho(new BigDecimal(txtEjeOD.getText().trim()));

            d.setAdicional(new BigDecimal(txtAdicional.getText().trim()));
            d.setDistanciaInterpupilar(Integer.parseInt(txtDistanciaIP.getText().trim()));

            // Enfermedades
            List<String> enfermedades = new ArrayList<>();

            if (chkMiopia.isSelected())       enfermedades.add("Miopía");
            if (chkHipermetropia.isSelected()) enfermedades.add("Hipermetropía");
            if (chkAstigmatismo.isSelected()) enfermedades.add("Astigmatismo");
            if (chkPresbicia.isSelected())    enfermedades.add("Presbicia");

            String otras = txtOtrasEnfermedades.getText().trim();
            if (!otras.isEmpty()) {
                String[] partes = otras.split(",");
                for (String p : partes) {
                    String enf = p.trim();
                    if (!enf.isEmpty()) enfermedades.add(enf);
                }
            }


            int id = recetaDAO.crearRecetaConDetalle(receta, d, enfermedades);
            if (id > 0) {
                JOptionPane.showMessageDialog(this,
                        "Receta registrada con ID: " + id,
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la receta.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Error en el formato de números (esfera, cilindro, eje, adicional o distancia).\n" +
                            "Revisa los valores ingresados.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la receta: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
