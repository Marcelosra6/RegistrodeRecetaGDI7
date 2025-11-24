package vista;

import dao.OftalmologoDAO;
import dao.PacienteDAO;
import dao.RecetaDAO;
import modelo.DetalleReceta;
import modelo.Oftalmologo;
import modelo.Paciente;
import modelo.Receta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NuevaRecetaDialog extends JDialog {

    private JComboBox<Paciente> cbPaciente;
    private JComboBox<Oftalmologo> cbOftalmologo;
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

    private JCheckBox chkMiopia;
    private JCheckBox chkHipermetropia;
    private JCheckBox chkAstigmatismo;
    private JCheckBox chkPresbicia;
    private JTextField txtOtrasEnfermedades;

    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final OftalmologoDAO oftDAO = new OftalmologoDAO();
    private final RecetaDAO recetaDAO = new RecetaDAO();

    // Paleta moderna
    private final Color BG_PRINCIPAL = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color ACCENT = new Color(59, 130, 246);
    private final Color ACCENT_LIGHT = new Color(219, 234, 254);
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color BORDER = new Color(226, 232, 240);
    private final Color SUCCESS = new Color(16, 185, 129);
    private final Color DANGER = new Color(239, 68, 68);

    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_VALUE = new Font("Segoe UI", Font.PLAIN, 13);

    public NuevaRecetaDialog(Frame owner) {
        super(owner, "Nueva Receta Médica", true);
        initComponents();
        cargarCombos();
        setSize(950, 720);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_PRINCIPAL);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(BG_PRINCIPAL);
        contenedor.setBorder(new EmptyBorder(20, 20, 20, 20));

        // ========== HEADER ==========
        JPanel headerPanel = crearCard();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(ACCENT);

        JLabel lblTitulo = new JLabel("NUEVA RECETA MÉDICA");
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(lblTitulo, BorderLayout.CENTER);

        // ========== DATOS PRINCIPALES ==========
        JPanel datosPrincipalesCard = crearCard();
        datosPrincipalesCard.setLayout(new BorderLayout(10, 10));

        JLabel lblSeccion1 = new JLabel("Datos Principales");
        lblSeccion1.setFont(FONT_SUBTITLE);
        lblSeccion1.setForeground(TEXT_PRIMARY);

        JPanel gridDatos = new JPanel(new GridLayout(2, 2, 15, 15));
        gridDatos.setOpaque(false);

        cbPaciente = new JComboBox<>();
        cbOftalmologo = new JComboBox<>();

        estilizarComboBox(cbPaciente);
        estilizarComboBox(cbOftalmologo);

        gridDatos.add(crearCampo("Paciente:", cbPaciente));
        gridDatos.add(crearCampo("Oftalmólogo:", cbOftalmologo));

        datosPrincipalesCard.add(lblSeccion1, BorderLayout.NORTH);
        datosPrincipalesCard.add(gridDatos, BorderLayout.CENTER);

        // ========== INFORMACIÓN GENERAL ==========
        JPanel infoGeneralCard = crearCard();
        infoGeneralCard.setLayout(new BorderLayout(10, 10));

        JLabel lblSeccion2 = new JLabel("Información General");
        lblSeccion2.setFont(FONT_SUBTITLE);
        lblSeccion2.setForeground(TEXT_PRIMARY);

        JPanel gridInfo = new JPanel(new GridLayout(2, 2, 15, 15));
        gridInfo.setOpaque(false);

        cbTipoLente = new JComboBox<>(new String[]{"Monofocal", "Bifocal", "Progresivo", "Ocupacional"});
        txtVisionCorregida = new JTextField("VL/VP");
        txtAdicional = new JTextField("0.00");
        txtDistanciaIP = new JTextField("60");

        estilizarComboBox(cbTipoLente);
        estilizarTextField(txtVisionCorregida);
        estilizarTextField(txtAdicional);
        estilizarTextField(txtDistanciaIP);

        gridInfo.add(crearCampo("Tipo de lente:", cbTipoLente));
        gridInfo.add(crearCampo("Visión corregida:", txtVisionCorregida));
        gridInfo.add(crearCampo("Adicional:", txtAdicional));
        gridInfo.add(crearCampo("Dist. interpupilar (mm):", txtDistanciaIP));

        infoGeneralCard.add(lblSeccion2, BorderLayout.NORTH);
        infoGeneralCard.add(gridInfo, BorderLayout.CENTER);

        // ========== MEDICIONES OCULARES ==========
        JPanel medicionesCard = crearCard();
        medicionesCard.setLayout(new BorderLayout(10, 10));

        JLabel lblSeccion3 = new JLabel("Mediciones Oculares");
        lblSeccion3.setFont(FONT_SUBTITLE);
        lblSeccion3.setForeground(TEXT_PRIMARY);

        JPanel contenedorOjos = new JPanel(new GridLayout(1, 2, 20, 0));
        contenedorOjos.setOpaque(false);

        // Ojo Derecho
        JPanel ojoDerecho = crearPanelOjo("OJO DERECHO");
        txtEsfOD = new JTextField("0.00");
        txtCilOD = new JTextField("0.00");
        txtEjeOD = new JTextField("0");

        estilizarTextField(txtEsfOD);
        estilizarTextField(txtCilOD);
        estilizarTextField(txtEjeOD);

        ojoDerecho.add(crearCampo("Esfera (ESF):", txtEsfOD));
        ojoDerecho.add(Box.createVerticalStrut(10));
        ojoDerecho.add(crearCampo("Cilindro (CIL):", txtCilOD));
        ojoDerecho.add(Box.createVerticalStrut(10));
        ojoDerecho.add(crearCampo("Eje:", txtEjeOD));

        // Ojo Izquierdo
        JPanel ojoIzquierdo = crearPanelOjo("OJO IZQUIERDO");
        txtEsfOI = new JTextField("0.00");
        txtCilOI = new JTextField("0.00");
        txtEjeOI = new JTextField("0");

        estilizarTextField(txtEsfOI);
        estilizarTextField(txtCilOI);
        estilizarTextField(txtEjeOI);

        ojoIzquierdo.add(crearCampo("Esfera (ESF):", txtEsfOI));
        ojoIzquierdo.add(Box.createVerticalStrut(10));
        ojoIzquierdo.add(crearCampo("Cilindro (CIL):", txtCilOI));
        ojoIzquierdo.add(Box.createVerticalStrut(10));
        ojoIzquierdo.add(crearCampo("Eje:", txtEjeOI));

        contenedorOjos.add(ojoDerecho);
        contenedorOjos.add(ojoIzquierdo);

        medicionesCard.add(lblSeccion3, BorderLayout.NORTH);
        medicionesCard.add(contenedorOjos, BorderLayout.CENTER);

        // ========== ENFERMEDADES ==========
        JPanel enfermedadesCard = crearCard();
        enfermedadesCard.setLayout(new BorderLayout(10, 10));

        JLabel lblSeccion4 = new JLabel("Diagnósticos y Enfermedades");
        lblSeccion4.setFont(FONT_SUBTITLE);
        lblSeccion4.setForeground(TEXT_PRIMARY);

        JPanel checksPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        checksPanel.setOpaque(false);

        chkMiopia = new JCheckBox("Miopía");
        chkHipermetropia = new JCheckBox("Hipermetropía");
        chkAstigmatismo = new JCheckBox("Astigmatismo");
        chkPresbicia = new JCheckBox("Presbicia");

        estilizarCheckBox(chkMiopia);
        estilizarCheckBox(chkHipermetropia);
        estilizarCheckBox(chkAstigmatismo);
        estilizarCheckBox(chkPresbicia);

        checksPanel.add(chkMiopia);
        checksPanel.add(chkHipermetropia);
        checksPanel.add(chkAstigmatismo);
        checksPanel.add(chkPresbicia);

        txtOtrasEnfermedades = new JTextField();
        estilizarTextField(txtOtrasEnfermedades);

        JPanel otrasPanel = new JPanel(new BorderLayout(10, 5));
        otrasPanel.setOpaque(false);
        otrasPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblOtras = new JLabel("Otras (separadas por coma):");
        lblOtras.setFont(FONT_LABEL);
        lblOtras.setForeground(TEXT_SECONDARY);

        otrasPanel.add(lblOtras, BorderLayout.NORTH);
        otrasPanel.add(txtOtrasEnfermedades, BorderLayout.CENTER);

        JPanel enfContent = new JPanel();
        enfContent.setLayout(new BoxLayout(enfContent, BoxLayout.Y_AXIS));
        enfContent.setOpaque(false);
        enfContent.add(checksPanel);
        enfContent.add(otrasPanel);

        enfermedadesCard.add(lblSeccion4, BorderLayout.NORTH);
        enfermedadesCard.add(enfContent, BorderLayout.CENTER);

        // ========== OBSERVACIONES ==========
        JPanel observacionesCard = crearCard();
        observacionesCard.setLayout(new BorderLayout(10, 10));

        JLabel lblSeccion5 = new JLabel("Observaciones");
        lblSeccion5.setFont(FONT_SUBTITLE);
        lblSeccion5.setForeground(TEXT_PRIMARY);

        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFont(FONT_VALUE);
        txtObservaciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setBorder(null);

        observacionesCard.add(lblSeccion5, BorderLayout.NORTH);
        observacionesCard.add(scrollObs, BorderLayout.CENTER);

        // Agregar todo al contenedor
        contenedor.add(headerPanel);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(datosPrincipalesCard);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(infoGeneralCard);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(medicionesCard);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(enfermedadesCard);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(observacionesCard);

        JScrollPane scrollPane = new JScrollPane(contenedor);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ========== BOTONES ==========
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton btnGuardar = crearBoton("Guardar Receta", SUCCESS);
        JButton btnCancelar = crearBoton("Cancelar", new Color(107, 114, 128));

        btnGuardar.addActionListener(e -> guardarReceta());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    private JPanel crearPanelOjo(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(ACCENT_LIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_SUBTITLE);
        lblTitulo.setForeground(ACCENT);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    private JPanel crearCampo(String label, JComponent campo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FONT_LABEL);
        lblLabel.setForeground(TEXT_SECONDARY);
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);

        return panel;
    }

    private void estilizarTextField(JTextField field) {
        field.setFont(FONT_VALUE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(200, 36));
    }

    private void estilizarComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_VALUE);
        combo.setPreferredSize(new Dimension(200, 36));
    }

    private void estilizarCheckBox(JCheckBox check) {
        check.setFont(FONT_VALUE);
        check.setOpaque(false);
        check.setForeground(TEXT_PRIMARY);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };

        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
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
            Receta receta = new Receta();
            receta.setDniPaciente(paciente.getDni());
            receta.setDniOftalmologo(oft.getDni());
            receta.setFechaExamen(LocalDateTime.now());

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

            List<String> enfermedades = new ArrayList<>();
            if (chkMiopia.isSelected()) enfermedades.add("Miopía");
            if (chkHipermetropia.isSelected()) enfermedades.add("Hipermetropía");
            if (chkAstigmatismo.isSelected()) enfermedades.add("Astigmatismo");
            if (chkPresbicia.isSelected()) enfermedades.add("Presbicia");

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
                        "Receta registrada exitosamente con ID: " + id,
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
                    "Error en el formato de números.\nVerifique que los valores ingresados sean correctos.",
                    "Error de Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar en la base de datos:\n" + ex.getMessage(),
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}