package vista;

import dao.RecetaDAO;
import dao.RecetaDAO.RecetaCompleta;
import modelo.DetalleReceta;
import modelo.Paciente;
import modelo.Oftalmologo;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.time.LocalDateTime;




public class VerRecetaDialog extends JDialog {

    private final RecetaDAO recetaDAO = new RecetaDAO();
    private RecetaCompleta datos;

    private JPanel panelContenido; // lo que se imprimirá

    public VerRecetaDialog(Frame owner, int idReceta) {
        super(owner, "Detalle de Receta #" + idReceta, true);
        cargarDatos(idReceta);
        initComponents();
        setSize(700, 500);
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
        setLayout(new BorderLayout(10, 10));

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Paciente p = datos.getPaciente();
        Oftalmologo o = datos.getOftalmologo();
        DetalleReceta d = datos.getDetalle();

        // Encabezado
        JPanel panelCabecera = new JPanel(new GridLayout(3, 1));
        panelCabecera.add(new JLabel("Óptica - Sistema de Recetas Médicas", SwingConstants.CENTER));
        panelCabecera.add(new JLabel("Receta N° " + datos.getReceta().getIdReceta(), SwingConstants.CENTER));
        panelCabecera.add(new JLabel("Fecha de examen: " +
                datos.getReceta().getFechaExamen().format(fmt), SwingConstants.CENTER));

        // Datos de paciente y doctor
        JPanel panelPacienteDoctor = new JPanel(new GridLayout(2, 1));
        panelPacienteDoctor.setBorder(BorderFactory.createTitledBorder("Datos"));

        panelPacienteDoctor.add(new JLabel("Paciente: " + p.getDni() + " - " +
                p.getApellidoPaterno() + " " + p.getApellidoMaterno() + ", " + p.getNombre()));

        panelPacienteDoctor.add(new JLabel("Oftalmólogo: " + o.getDni() + " - " +
                o.getApellidoPaterno() + " " + o.getApellidoMaterno() + ", " + o.getNombre()));

        // Medidas
        JPanel panelMedidas = new JPanel(new GridLayout(5, 4, 5, 5));
        panelMedidas.setBorder(BorderFactory.createTitledBorder("Medidas"));

        panelMedidas.add(new JLabel("Tipo de lente:"));
        panelMedidas.add(new JLabel(d.getTipoDeLente()));
        panelMedidas.add(new JLabel("Visión corregida:"));
        panelMedidas.add(new JLabel(d.getVisionCorregida()));

        panelMedidas.add(new JLabel("Esf. Ojo Izq.:"));
        panelMedidas.add(new JLabel(d.getEsfOjoIzquierdo().toPlainString()));
        panelMedidas.add(new JLabel("Esf. Ojo Der.:"));
        panelMedidas.add(new JLabel(d.getEsfOjoDerecho().toPlainString()));

        panelMedidas.add(new JLabel("Cil. Ojo Izq.:"));
        panelMedidas.add(new JLabel(d.getCilindroOjoIzquierdo().toPlainString()));
        panelMedidas.add(new JLabel("Cil. Ojo Der.:"));
        panelMedidas.add(new JLabel(d.getCilindroOjoDerecho().toPlainString()));

        panelMedidas.add(new JLabel("Eje Ojo Izq.:"));
        panelMedidas.add(new JLabel(d.getEjeOjoIzquierdo().toPlainString()));
        panelMedidas.add(new JLabel("Eje Ojo Der.:"));
        panelMedidas.add(new JLabel(d.getEjeOjoDerecho().toPlainString()));

        panelMedidas.add(new JLabel("Adicional:"));
        panelMedidas.add(new JLabel(d.getAdicional().toPlainString()));
        panelMedidas.add(new JLabel("Dist. interpupilar:"));
        panelMedidas.add(new JLabel(d.getDistanciaInterpupilar() + " mm"));

        // Observaciones
        JPanel panelObs = new JPanel(new BorderLayout());
        panelObs.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        JTextArea areaObs = new JTextArea(datos.getDetalle().getObservaciones());
        areaObs.setLineWrap(true);
        areaObs.setWrapStyleWord(true);
        areaObs.setEditable(false);
        panelObs.add(new JScrollPane(areaObs), BorderLayout.CENTER);

        // Enfermedades
        JPanel panelEnf = new JPanel(new BorderLayout());
        panelEnf.setBorder(BorderFactory.createTitledBorder("Enfermedades"));
        String enfText = datos.getEnfermedades() == null || datos.getEnfermedades().isEmpty()
                ? "Ninguna registrada"
                : datos.getEnfermedades().stream().collect(Collectors.joining(", "));
        panelEnf.add(new JLabel(enfText), BorderLayout.CENTER);

        panelContenido.add(panelCabecera);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelPacienteDoctor);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelMedidas);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelObs);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(panelEnf);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnImprimir = new JButton("Imprimir / PDF");
        JButton btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

        btnImprimir.addActionListener(e -> imprimir());
        btnCerrar.addActionListener(e -> dispose());

        add(new JScrollPane(panelContenido), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Receta " + datos.getReceta().getIdReceta());

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                panelContenido.printAll(g2);
                return PAGE_EXISTS;
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(VerRecetaDialog.this,
                        "Error al imprimir: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
