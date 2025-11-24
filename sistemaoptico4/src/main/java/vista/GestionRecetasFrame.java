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

public class GestionRecetasFrame extends JDialog {
    
    private JTextField txtBuscar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private final RecetaDAO recetaDAO = new RecetaDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    // Paleta de colores
    private final Color BG_WINDOW = new Color(248, 250, 252);
    private final Color PANEL_SUPERIOR = new Color(245, 247, 250);
    private final Color ACCENT = new Color(74, 144, 226);
    private final Color BTN_BG = new Color(52, 152, 219);
    private final Color BTN_SUCCESS = new Color(46, 204, 113);
    private final Color BTN_WARNING = new Color(230, 126, 34);
    private final Color BTN_DANGER = new Color(231, 76, 60);
    private final Color BTN_TEXT = Color.WHITE;
    private final Color ROW_ALT = new Color(250, 252, 254);
    private final Color ROW_EVEN = Color.WHITE;
    private final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    
    public GestionRecetasFrame(Frame owner) {
        super(owner, "Gestión de Recetas", true);
        initComponents();
        cargarRecetas(null);
        setSize(1000, 550);
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
        JLabel titulo = new JLabel("Gestión de Recetas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(ACCENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
        leftTitle.add(titulo);
        panelSuperior.add(leftTitle, BorderLayout.WEST);
        
        // Búsqueda y acciones
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Buscar paciente/DNI:");
        lblBuscar.setFont(FONT_NORMAL);
        
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(FONT_NORMAL);
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        
        JButton btnBuscar = crearBoton("Buscar", ACCENT);
        JButton btnRefrescar = crearBoton("Refrescar", new Color(92, 184, 92));
        JButton btnNueva = crearBoton("Nueva Receta", BTN_SUCCESS);
        
        acciones.add(lblBuscar);
        acciones.add(txtBuscar);
        acciones.add(btnBuscar);
        acciones.add(btnRefrescar);
        acciones.add(btnNueva);
        
        panelSuperior.add(acciones, BorderLayout.EAST);
        
        // Tabla
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Paciente", "Oftalmólogo"}, 0
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
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        scroll.getViewport().setBackground(ROW_EVEN);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(BG_WINDOW);
        
        JButton btnVer = crearBoton("Ver Detalle", BTN_WARNING);
        JButton btnCerrar = crearBoton("Cerrar", new Color(149, 165, 166));
        
        panelBotones.add(btnVer);
        panelBotones.add(btnCerrar);
        
        // Listeners
        btnBuscar.addActionListener(e -> cargarRecetas(txtBuscar.getText().trim()));
        btnRefrescar.addActionListener(e -> cargarRecetas(null));
        btnNueva.addActionListener(e -> nuevaReceta());
        btnVer.addActionListener(e -> verSeleccionada());
        btnCerrar.addActionListener(e -> dispose());
        txtBuscar.addActionListener(e -> cargarRecetas(txtBuscar.getText().trim()));
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
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
        boton.setPreferredSize(new Dimension(texto.length() > 10 ? 130 : 110, 36));
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
    
    private void cargarRecetas(String filtro) {
        try {
            List<RecetaListado> lista = recetaDAO.listarRecetas(filtro);
            modeloTabla.setRowCount(0);
            for (RecetaListado r : lista) {
                modeloTabla.addRow(new Object[]{
                        r.getIdReceta(),
                        sdf.format(r.getFechaExamen()),
                        r.getNombrePaciente(),
                        r.getNombreOftalmologo()
                });
            }
            System.out.println("✅ Cargadas " + lista.size() + " recetas");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar recetas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void nuevaReceta() {
        NuevaRecetaDialog dialog = new NuevaRecetaDialog((Frame) getOwner());
        dialog.setVisible(true);
        cargarRecetas(txtBuscar.getText().trim());
    }
    
    private void verSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una receta de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        VerRecetaDialog dialog = new VerRecetaDialog((Frame) getOwner(), id);
        dialog.setVisible(true);
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