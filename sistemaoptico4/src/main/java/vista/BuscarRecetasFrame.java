package vista;

import dao.RecetaDAO;
import dao.RecetaDAO.RecetaListado;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BuscarRecetasFrame extends JDialog {

    private JTextField txtBuscarPaciente;
    private JTextField txtBuscarDNI;
    private JComboBox<String> cmbOftalmologo;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnVerDetalle;

    private final RecetaDAO recetaDAO = new RecetaDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // --- PALETA ---
    private final Color BG_WINDOW = new Color(248, 250, 252);
    private final Color PANEL_SUPERIOR = new Color(245, 247, 250);
    private final Color ACCENT = new Color(41, 128, 185);
    private final Color BTN_BG = new Color(52, 152, 219);
    private final Color BTN_TEXT = Color.WHITE;
    private final Color ROW_ALT = new Color(250, 252, 254);
    private final Color ROW_EVEN = Color.WHITE;

    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public BuscarRecetasFrame(JFrame parent) {
        super(parent, "Buscar Recetas", true);
        initComponents();
        setSize(980, 640);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_WINDOW);

        // --- Panel Superior con título y filtros ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(PANEL_SUPERIOR);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12),
                BorderFactory.createLineBorder(new Color(220, 225, 230))
        ));

        // Título
        JLabel titulo = new JLabel("Buscar Recetas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACCENT);

        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        leftTitle.setOpaque(false);
        leftTitle.add(titulo);
        panelSuperior.add(leftTitle, BorderLayout.NORTH);

        // Panel de filtros
        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DNI
        gbc.gridx = 0; gbc.gridy = 0;
        filtros.add(new JLabel("DNI Paciente:"), gbc);

        gbc.gridx = 1;
        txtBuscarDNI = new JTextField(15);
        txtBuscarDNI.setFont(FONT_NORMAL);
        txtBuscarDNI.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220)));
        filtros.add(txtBuscarDNI, gbc);

        // Nombre
        gbc.gridx = 2; gbc.gridy = 0;
        filtros.add(new JLabel("Nombre Paciente:"), gbc);

        gbc.gridx = 3;
        txtBuscarPaciente = new JTextField(20);
        txtBuscarPaciente.setFont(FONT_NORMAL);
        txtBuscarPaciente.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220)));
        filtros.add(txtBuscarPaciente, gbc);

        // Oftalmólogo
        gbc.gridx = 0; gbc.gridy = 1;
        filtros.add(new JLabel("Oftalmólogo:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        cmbOftalmologo = new JComboBox<>(new String[]{"Todos"});
        cmbOftalmologo.setFont(FONT_NORMAL);
        filtros.add(cmbOftalmologo, gbc);

        panelSuperior.add(filtros, BorderLayout.CENTER);

        // Panel de acciones
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        acciones.setOpaque(false);

        btnBuscar = crearBoton("Buscar", new Color(46, 204, 113));
        btnLimpiar = crearBoton("Limpiar", new Color(149, 165, 166));

        acciones.add(btnBuscar);
        acciones.add(btnLimpiar);

        panelSuperior.add(acciones, BorderLayout.SOUTH);

        // Listeners
        btnBuscar.addActionListener(e -> buscarRecetas());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // --- Tabla ---
        modeloTabla = new DefaultTableModel(
                new String[]{"ID Receta", "DNI Paciente", "Paciente", "Oftalmólogo", "Fecha Examen"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setFont(FONT_NORMAL);
        tablaResultados.setRowHeight(30);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResultados.setShowGrid(false);
        tablaResultados.setIntercellSpacing(new Dimension(0, 0));

        // Render alternado
        tablaResultados.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        // Encabezado estilizado
        JTableHeader header = tablaResultados.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );
                lbl.setOpaque(true);
                lbl.setFont(FONT_BOLD);
                lbl.setBackground(ACCENT);
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(tablaResultados);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scroll.getViewport().setBackground(ROW_EVEN);

        // --- Panel inferior ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelInferior.setBackground(BG_WINDOW);

        btnVerDetalle = crearBoton("Ver Detalle", ACCENT);
        JButton btnCerrar = crearBoton("Cerrar", new Color(149, 165, 166));

        btnCerrar.addActionListener(e -> dispose());
        btnVerDetalle.addActionListener(e -> verDetalle());

        panelInferior.add(btnVerDetalle);
        panelInferior.add(btnCerrar);

        // Add all
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    // --- Botón estilizado ---
    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = color;
                Color hover = base.darker();
                if (getModel().isPressed()) g2.setColor(hover);
                else if (getModel().isRollover()) g2.setColor(hover);
                else g2.setColor(base);

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(BTN_TEXT);
                g2.setFont(FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };

        b.setForeground(BTN_TEXT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(110, 36));
        return b;
    }

    // --- Renderer alternado ---
    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            if (isSelected) {
                c.setBackground(ACCENT);
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground((row % 2 == 1) ? ROW_ALT : ROW_EVEN);
                c.setForeground(new Color(33, 43, 54));
            }

            ((JComponent)c).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return c;
        }
    }

    // --- Lógica (sin tocar NADA) ---
    private void buscarRecetas() {
        String dni = txtBuscarDNI.getText().trim();
        String nombre = txtBuscarPaciente.getText().trim();

        String filtro = dni.isEmpty() ? nombre : dni;

        List<RecetaListado> recetas = recetaDAO.listarRecetas(
                filtro.isEmpty() ? null : filtro
        );

        modeloTabla.setRowCount(0);

        for (RecetaListado r : recetas) {
            modeloTabla.addRow(new Object[]{
                    r.getIdReceta(),
                    r.getDniPaciente(),
                    r.getNombrePaciente(),
                    r.getNombreOftalmologo(),
                    sdf.format(r.getFechaExamen())
            });
        }

        if (recetas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron recetas.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Se encontraron " + recetas.size() + " receta(s).",
                    "OK", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtBuscarDNI.setText("");
        txtBuscarPaciente.setText("");
        cmbOftalmologo.setSelectedIndex(0);
        modeloTabla.setRowCount(0);
    }

    private void verDetalle() {
        int fila = tablaResultados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una receta.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReceta = (int) modeloTabla.getValueAt(fila, 0);

        VerRecetaDialog dialog = new VerRecetaDialog(
                (Frame) getOwner(), idReceta
        );
        dialog.setVisible(true);
    }
}
