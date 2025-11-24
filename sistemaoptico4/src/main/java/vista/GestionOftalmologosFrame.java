package vista;

import dao.OftalmologoDAO;
import modelo.Oftalmologo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class GestionOftalmologosFrame extends JDialog {

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private final OftalmologoDAO oftDAO = new OftalmologoDAO();

    // Paleta de colores
    private final Color BG_WINDOW = new Color(248, 250, 252);
    private final Color PANEL_SUPERIOR = new Color(245, 247, 250);
    private final Color ACCENT = new Color(74, 144, 226);
    private final Color BTN_BG = new Color(52, 152, 219);
    private final Color BTN_TEXT = Color.WHITE;
    private final Color ROW_ALT = new Color(250, 252, 254);
    private final Color ROW_EVEN = Color.WHITE;
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public GestionOftalmologosFrame(Frame owner) {
        super(owner, "Gestión de Oftalmólogos", true);
        initComponents();
        cargarTodos();
        setSize(820, 520);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_WINDOW);

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));
        panelSuperior.setBackground(PANEL_SUPERIOR);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12),
                BorderFactory.createLineBorder(new Color(220, 225, 230))
        ));

        // Título
        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        leftTitle.setOpaque(false);
        JLabel titulo = new JLabel("Gestión de Oftalmólogos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACCENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        leftTitle.add(titulo);
        panelSuperior.add(leftTitle, BorderLayout.WEST);

        // Búsqueda y acciones
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);

        txtBuscar = new JTextField(26);
        txtBuscar.setFont(FONT_NORMAL);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        txtBuscar.setToolTipText("Buscar por DNI o nombre");

        JButton btnBuscar = crearBoton("Buscar", ACCENT);
        JButton btnRefrescar = crearBoton("Refrescar", new Color(92, 184, 92));
        JButton btnNuevo = crearBoton("Nuevo", BTN_BG);

        acciones.add(txtBuscar);
        acciones.add(btnBuscar);
        acciones.add(btnRefrescar);
        acciones.add(btnNuevo);

        panelSuperior.add(acciones, BorderLayout.EAST);

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
        tabla.setFont(FONT_NORMAL);
        tabla.setRowHeight(30);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setForeground(new Color(33, 43, 54));
        tabla.setBackground(ROW_EVEN);

        // Encabezado
        JTableHeader header = tabla.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            private final Font hdrFont = new Font("Segoe UI", Font.BOLD, 13);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                lbl.setOpaque(true);
                lbl.setFont(hdrFont);
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                lbl.setBackground(ACCENT);
                int r = ACCENT.getRed(), g = ACCENT.getGreen(), b = ACCENT.getBlue();
                double brightness = (r * 0.299 + g * 0.587 + b * 0.114);
                lbl.setForeground(brightness > 186 ? Color.BLACK : Color.WHITE);
                return lbl;
            }
        });

        tabla.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollTabla.getViewport().setBackground(ROW_EVEN);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(BG_WINDOW);

        JButton btnEditar = crearBoton("Editar", new Color(255, 193, 7));
        JButton btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        JButton btnCerrar = crearBoton("Cerrar", new Color(149, 165, 166));

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
        txtBuscar.addActionListener(e -> buscar());

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = color;
                Color hover = blend(color, Color.BLACK, 0.06f);
                Color pressed = blend(color, Color.BLACK, 0.12f);

                if (getModel().isPressed()) {
                    g2.setColor(pressed);
                } else if (getModel().isRollover()) {
                    g2.setColor(hover);
                } else {
                    g2.setColor(base);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(BTN_TEXT);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };

        boton.setFont(FONT_BOLD);
        boton.setForeground(BTN_TEXT);
        boton.setPreferredSize(new Dimension(110, 36));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private static Color blend(Color c1, Color c2, float ratio) {
        float r = 1 - ratio;
        int red = Math.min(255, (int) (c1.getRed() * r + c2.getRed() * ratio));
        int green = Math.min(255, (int) (c1.getGreen() * r + c2.getGreen() * ratio));
        int blue = Math.min(255, (int) (c1.getBlue() * r + c2.getBlue() * ratio));
        return new Color(red, green, blue);
    }

    private void cargarTodos() {
        try {
            List<Oftalmologo> lista = oftDAO.listarTodos();
            llenarTabla(lista);
            System.out.println("✅ Cargados " + lista.size() + " oftalmólogos");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar oftalmólogos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buscar() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTodos();
            return;
        }
        try {
            List<Oftalmologo> lista = oftDAO.buscar(filtro);
            llenarTabla(lista);
        } catch (Exception e) {
            System.err.println("❌ Error al buscar: " + e.getMessage());
            e.printStackTrace();
        }
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
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el oftalmólogo.\nRevisa la consola para ver el error.",
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
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el oftalmólogo.\nRevisa la consola.",
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
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el oftalmólogo.\nPuede tener recetas o usuarios asociados.\nRevisa la consola.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class AlternatingRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(ACCENT);
                c.setForeground(Color.WHITE);
            } else {
                c.setBackground((row % 2 == 0) ? ROW_EVEN : ROW_ALT);
                c.setForeground(new Color(33, 43, 54));
            }

            if (c instanceof JComponent) {
                ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }

            return c;
        }
    }
}