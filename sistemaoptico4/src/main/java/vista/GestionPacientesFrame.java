package vista;

import dao.PacienteDAO;
import modelo.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class GestionPacientesFrame extends JDialog {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    private final PacienteDAO pacienteDAO = new PacienteDAO();

    // Paleta y estilizado
    private final Color BG_WINDOW = new Color(248, 250, 252);
    private final Color PANEL_SUPERIOR = new Color(245, 247, 250);
    private final Color ACCENT = new Color(74, 144, 226);
    private final Color BTN_BG = new Color(52, 152, 219);
    private final Color BTN_TEXT = Color.WHITE;
    private final Color ROW_ALT = new Color(250, 252, 254);
    private final Color ROW_EVEN = Color.WHITE;
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public GestionPacientesFrame(Frame owner) {
        super(owner, "Gestión de Pacientes", true);
        initComponents();
        cargarTodos();
        setSize(820, 520);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG_WINDOW);

        // --- Panel superior: búsqueda ---
        JPanel panelSuperior = new JPanel(new BorderLayout(8, 8));
        panelSuperior.setBackground(PANEL_SUPERIOR);
        panelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12),
                BorderFactory.createLineBorder(new Color(220, 225, 230))
        ));

        // Left area (titulo) - ahora con FlowLayout para que no se corte
        JPanel leftTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        leftTitle.setOpaque(false);
        JLabel titulo = new JLabel("Gestión de Pacientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACCENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); // padding
        leftTitle.add(titulo);
        panelSuperior.add(leftTitle, BorderLayout.WEST);

        // Panel de búsqueda y acciones
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

        // --- Tabla ---
        modeloTabla = new DefaultTableModel(
                new Object[]{"DNI", "Nombre", "Apellido Paterno", "Apellido Materno"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPacientes = new JTable(modeloTabla);
        tablaPacientes.setFont(FONT_NORMAL);
        tablaPacientes.setRowHeight(30);
        tablaPacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPacientes.setShowGrid(false);
        tablaPacientes.setIntercellSpacing(new Dimension(0, 0));
        tablaPacientes.setForeground(new Color(33, 43, 54)); // texto bien visible
        tablaPacientes.setBackground(ROW_EVEN);

        // Encabezado: renderer robusto para garantizar legibilidad
        JTableHeader header = tablaPacientes.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setReorderingAllowed(false); // opcional: evita mover columnas
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
                // ponemos background fijo ACCENT y elegimos foreground con contraste
                lbl.setBackground(ACCENT);
                // calcular brillo para decidir color de texto (mejor contraste)
                int r = ACCENT.getRed(), g = ACCENT.getGreen(), b = ACCENT.getBlue();
                double brightness = (r * 0.299 + g * 0.587 + b * 0.114);
                lbl.setForeground(brightness > 186 ? Color.BLACK : Color.WHITE);
                return lbl;
            }
        });

        // Alternancia de filas
        tablaPacientes.setDefaultRenderer(Object.class, new AlternatingRowRenderer());

        JScrollPane scrollTabla = new JScrollPane(tablaPacientes);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scrollTabla.getViewport().setBackground(ROW_EVEN);

        // --- Panel inferior ---
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
        btnNuevo.addActionListener(e -> nuevoPaciente());
        btnEditar.addActionListener(e -> editarPacienteSeleccionado());
        btnEliminar.addActionListener(e -> eliminarPacienteSeleccionado());
        btnCerrar.addActionListener(e -> dispose());
        txtBuscar.addActionListener(e -> buscar());

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    }

    // --- Botones corregidos (hover y pressed con buen contraste) ---
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color base = color;
                // hover y pressed más sutiles
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

    // pequeño helper para mezclar colores (más controlado que .darker())
    private static Color blend(Color c1, Color c2, float ratio) {
        float r = 1 - ratio;
        int red = Math.min(255, (int) (c1.getRed() * r + c2.getRed() * ratio));
        int green = Math.min(255, (int) (c1.getGreen() * r + c2.getGreen() * ratio));
        int blue = Math.min(255, (int) (c1.getBlue() * r + c2.getBlue() * ratio));
        return new Color(red, green, blue);
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
                        "No se pudo registrar el paciente.\nVerifica si el DNI ya existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarPacienteSeleccionado() {
        int fila = tablaPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un paciente.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) modeloTabla.getValueAt(fila, 0);
        Paciente existente = pacienteDAO.obtenerPorDni(dni);
        if (existente == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el paciente.",
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
                    "Selecciona un paciente.",
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
                        "No se pudo eliminar el paciente.\nEs posible que tenga recetas asociadas.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ------------------ Render personalizado ------------------
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
                c.setForeground(new Color(33, 43, 54)); // texto visible SIEMPRE
            }

            if (c instanceof JComponent) {
                ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            }

            return c;
        }
    }
}
