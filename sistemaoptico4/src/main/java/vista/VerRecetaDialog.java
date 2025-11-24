package vista;

import dao.RecetaDAO;
import dao.RecetaDAO.RecetaCompleta;
import modelo.DetalleReceta;
import modelo.Paciente;
import modelo.Oftalmologo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.format.DateTimeFormatter;

public class VerRecetaDialog extends JDialog {

    private final RecetaDAO recetaDAO = new RecetaDAO();
    private RecetaCompleta datos;
    private JPanel panelContenido;

    // Paleta moderna
    private final Color BG_PRINCIPAL = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color ACCENT = new Color(59, 130, 246);
    private final Color ACCENT_LIGHT = new Color(219, 234, 254);
    private final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private final Color TEXT_SECONDARY = new Color(100, 116, 139);
    private final Color BORDER = new Color(226, 232, 240);

    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font FONT_VALUE = new Font("Segoe UI", Font.BOLD, 13);

    public VerRecetaDialog(Frame owner, int idReceta) {
        super(owner, "Detalle de Receta Médica", true);
        cargarDatos(idReceta);
        if (datos != null) {
            initComponents();
        }
        setSize(850, 700);
        setLocationRelativeTo(owner);
    }

    private void cargarDatos(int idReceta) {
        datos = recetaDAO.obtenerRecetaCompleta(idReceta);
        if (datos == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la receta con ID " + idReceta,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_PRINCIPAL);

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(BG_PRINCIPAL);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Paciente p = datos.getPaciente();
        Oftalmologo o = datos.getOftalmologo();
        DetalleReceta d = datos.getDetalle();

        // ENCABEZADO
        JPanel headerPanel = crearCard();
        headerPanel.setLayout(new BorderLayout(15, 15));
        headerPanel.setBackground(ACCENT);

        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setOpaque(false);

        JLabel lblTitulo = new JLabel("RECETA MÉDICA OFTALMOLÓGICA");
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNumero = new JLabel("N° " + datos.getReceta().getIdReceta());
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblNumero.setForeground(Color.WHITE);
        lblNumero.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFecha = new JLabel("Fecha: " + datos.getReceta().getFechaExamen().format(fmt));
        lblFecha.setFont(FONT_LABEL);
        lblFecha.setForeground(new Color(219, 234, 254));
        lblFecha.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerContent.add(Box.createVerticalStrut(10));
        headerContent.add(lblTitulo);
        headerContent.add(Box.createVerticalStrut(5));
        headerContent.add(lblNumero);
        headerContent.add(Box.createVerticalStrut(5));
        headerContent.add(lblFecha);
        headerContent.add(Box.createVerticalStrut(10));

        headerPanel.add(headerContent, BorderLayout.CENTER);

        // DATOS PACIENTE & OFTALMÓLOGO
        JPanel datosPanel = crearCard();
        datosPanel.setLayout(new GridLayout(2, 1, 0, 15));

        datosPanel.add(crearFilaDato("Paciente",
                p.getNombre() + " " + p.getApellidoPaterno() + " " + p.getApellidoMaterno(),
                "DNI: " + p.getDni().trim()));

        datosPanel.add(crearFilaDato("Oftalmólogo",
                o.getNombre() + " " + o.getApellidoPaterno() + " " + o.getApellidoMaterno(),
                "CMP: " + o.getDni().trim()));

        // PRESCRIPCIÓN ÓPTICA
        JPanel prescripcionPanel = crearCard();
        prescripcionPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblPrescripcion = new JLabel("Prescripción Óptica");
        lblPrescripcion.setFont(FONT_SUBTITLE);
        lblPrescripcion.setForeground(TEXT_PRIMARY);
        prescripcionPanel.add(lblPrescripcion, BorderLayout.NORTH);

        JPanel tablaPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        tablaPanel.setOpaque(false);

        JPanel ojoDerechoPanel = crearPanelOjo("OJO DERECHO",
                d.getEsfOjoDerecho().toPlainString(),
                d.getCilindroOjoDerecho().toPlainString(),
                d.getEjeOjoDerecho().toPlainString());

        JPanel ojoIzquierdoPanel = crearPanelOjo("OJO IZQUIERDO",
                d.getEsfOjoIzquierdo().toPlainString(),
                d.getCilindroOjoIzquierdo().toPlainString(),
                d.getEjeOjoIzquierdo().toPlainString());

        tablaPanel.add(ojoDerechoPanel);
        tablaPanel.add(ojoIzquierdoPanel);

        prescripcionPanel.add(tablaPanel, BorderLayout.CENTER);

        // DATOS ADICIONALES
        JPanel adicionalPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        adicionalPanel.setOpaque(false);
        adicionalPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        adicionalPanel.add(crearInfoBox("Tipo de Lente", d.getTipoDeLente()));
        adicionalPanel.add(crearInfoBox("Adicional", d.getAdicional().toPlainString()));
        adicionalPanel.add(crearInfoBox("Distancia Interpupilar", d.getDistanciaInterpupilar() + " mm"));

        prescripcionPanel.add(adicionalPanel, BorderLayout.SOUTH);

        // OBSERVACIONES
        if (d.getObservaciones() != null && !d.getObservaciones().trim().isEmpty()) {
            JPanel obsPanel = crearCard();
            obsPanel.setLayout(new BorderLayout(10, 10));

            JLabel lblObs = new JLabel("Observaciones");
            lblObs.setFont(FONT_SUBTITLE);
            lblObs.setForeground(TEXT_PRIMARY);

            JTextArea areaObs = new JTextArea(d.getObservaciones());
            areaObs.setFont(FONT_LABEL);
            areaObs.setForeground(TEXT_SECONDARY);
            areaObs.setLineWrap(true);
            areaObs.setWrapStyleWord(true);
            areaObs.setEditable(false);
            areaObs.setOpaque(false);
            areaObs.setBorder(new EmptyBorder(5, 0, 0, 0));

            obsPanel.add(lblObs, BorderLayout.NORTH);
            obsPanel.add(areaObs, BorderLayout.CENTER);

            panelContenido.add(obsPanel);
            panelContenido.add(Box.createVerticalStrut(15));
        }

        // AGREGAR TODO
        panelContenido.add(headerPanel);
        panelContenido.add(Box.createVerticalStrut(20));
        panelContenido.add(datosPanel);
        panelContenido.add(Box.createVerticalStrut(15));
        panelContenido.add(prescripcionPanel);
        panelContenido.add(Box.createVerticalStrut(15));

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(BG_PRINCIPAL);
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JButton btnImprimir = crearBoton("Imprimir", ACCENT);
        JButton btnCerrar = crearBoton("Cerrar", new Color(107, 114, 128));

        btnImprimir.addActionListener(e -> imprimir());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

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

    private JPanel crearFilaDato(String titulo, String principal, String secundario) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_SUBTITLE);
        lblTitulo.setForeground(TEXT_PRIMARY);

        JLabel lblPrincipal = new JLabel(principal);
        lblPrincipal.setFont(FONT_VALUE);
        lblPrincipal.setForeground(TEXT_PRIMARY);

        JLabel lblSecundario = new JLabel(secundario);
        lblSecundario.setFont(FONT_LABEL);
        lblSecundario.setForeground(TEXT_SECONDARY);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblPrincipal);
        panel.add(Box.createVerticalStrut(3));
        panel.add(lblSecundario);

        return panel;
    }

    private JPanel crearPanelOjo(String titulo, String esf, String cil, String eje) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(ACCENT_LIGHT);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_SUBTITLE);
        lblTitulo.setForeground(ACCENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearMedida("ESF", esf));
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearMedida("CIL", cil));
        panel.add(Box.createVerticalStrut(8));
        panel.add(crearMedida("EJE", eje + "°"));

        return panel;
    }

    private JPanel crearMedida(String label, String valor) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FONT_LABEL);
        lblLabel.setForeground(TEXT_SECONDARY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValor.setForeground(TEXT_PRIMARY);

        panel.add(lblLabel, BorderLayout.WEST);
        panel.add(lblValor, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearInfoBox(String label, String valor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(FONT_LABEL);
        lblLabel.setForeground(TEXT_SECONDARY);
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(FONT_VALUE);
        lblValor.setForeground(TEXT_PRIMARY);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblValor);

        return panel;
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

        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

     private void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Receta " + datos.getReceta().getIdReceta());

        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;

            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Obtener dimensiones
            double pageWidth = pageFormat.getImageableWidth();
            double pageHeight = pageFormat.getImageableHeight();
            
            int panelWidth = panelContenido.getWidth();
            int panelHeight = panelContenido.getHeight();
            
            // Calcular escala para ajustar al ancho de la página
            double scaleX = pageWidth / panelWidth;
            double scaleY = pageHeight / panelHeight;
            double scale = Math.min(scaleX, scaleY);
            
            // Si el contenido es más alto que la página, ajustar solo al ancho
            if (scale < 1.0 && scaleY < scaleX) {
                scale = scaleX;
            }
            
            // Aplicar transformaciones
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2.scale(scale, scale);
            
            // Renderizar el panel
            panelContenido.print(g2);
            
            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al imprimir: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
