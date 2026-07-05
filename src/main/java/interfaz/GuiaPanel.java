package interfaz;

import modelo.CalendarioTuristico;
import modelo.GuiaTuristico;
import modelo.Recomendacion;
import modelo.Reserva;
import modelo.SalidaProgramada;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GuiaPanel extends JPanel {
    private AgenciaTuristica agencia;
    private GuiaTuristico guia;
    private VentanaPrincipal ventana;

    private DefaultTableModel modeloSalidas;
    private JTable tablaSalidas;
    private ArrayList<SalidaProgramada> salidasMostradas;

    private DefaultTableModel modeloInscritos;
    private JTable tablaInscritos;

    private DefaultTableModel modeloCalificaciones;
    private JTable tablaCalificaciones;

    private JTextArea areaReporte;

    public GuiaPanel(AgenciaTuristica agencia, GuiaTuristico guia, VentanaPrincipal ventana) {
        this.agencia = agencia;
        this.guia = guia;
        this.ventana = ventana;
        this.salidasMostradas = new ArrayList<SalidaProgramada>();
        construirInterfaz();
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel encabezado = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Portal del guía - " + guia.getNombre());
        UIUtils.prepararEncabezado(encabezado, titulo);
        JButton botonCerrar = new JButton("Cerrar sesión");
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrar, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Mis salidas e inscritos", crearPanelSalidas());
        pestanas.addTab("Resumen de viajes", new ResumenViajesPanel(agencia, guia));
        pestanas.addTab("Mis calificaciones", crearPanelCalificaciones());
        pestanas.addTab("Mis reportes", crearPanelReportes());
        add(pestanas, BorderLayout.CENTER);

        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarLogin();
                } catch (Exception ex) {
                    UIUtils.mostrarError(GuiaPanel.this, "No se pudo cerrar la sesión.");
                }
            }
        });
    }

    private JPanel crearPanelSalidas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        String[] columnasSalidas = {"ID", "Paquete", "Salida", "Retorno", "Lugar de salida",
                "Lugar de regreso", "Hotel", "Cupos", "Reservas", "Estado"};
        modeloSalidas = UIUtils.crearModelo(columnasSalidas);
        tablaSalidas = UIUtils.crearTabla(modeloSalidas);

        String[] columnasInscritos = {"Reserva", "Turista", "Correo", "Contratación", "Estado"};
        modeloInscritos = UIUtils.crearModelo(columnasInscritos);
        tablaInscritos = UIUtils.crearTabla(modeloInscritos);

        JPanel centro = new JPanel(new java.awt.GridLayout(2, 1, 5, 5));
        centro.add(new JScrollPane(tablaSalidas));
        centro.add(new JScrollPane(tablaInscritos));
        panel.add(centro, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonVerInscritos = new JButton("Ver inscritos de la salida");
        JButton botonActualizar = new JButton("Actualizar");
        botones.add(botonVerInscritos);
        botones.add(botonActualizar);
        panel.add(botones, BorderLayout.SOUTH);

        botonVerInscritos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fila = tablaSalidas.getSelectedRow();
                    if (fila < 0) {
                        throw new NegocioException("Seleccione una salida.");
                    }
                    int filaModelo = tablaSalidas.convertRowIndexToModel(fila);
                    SalidaProgramada salida = salidasMostradas.get(filaModelo);
                    refrescarInscritos(salida);
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(GuiaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(GuiaPanel.this, "No se pudieron mostrar los inscritos.");
                }
            }
        });

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTodo();
                } catch (Exception ex) {
                    UIUtils.mostrarError(GuiaPanel.this, "No se pudo actualizar la información.");
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelCalificaciones() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        String[] columnas = {"Fecha", "Turista", "Paquete", "Guía", "Experiencia", "Comentario"};
        modeloCalificaciones = UIUtils.crearModelo(columnas);
        tablaCalificaciones = UIUtils.crearTabla(modeloCalificaciones);
        panel.add(new JScrollPane(tablaCalificaciones), BorderLayout.CENTER);
        JButton botonActualizar = new JButton("Actualizar calificaciones");
        panel.add(botonActualizar, BorderLayout.SOUTH);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarCalificaciones();
                } catch (Exception ex) {
                    UIUtils.mostrarError(GuiaPanel.this, "No se pudieron actualizar las calificaciones.");
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 14));
        panel.add(new JScrollPane(areaReporte), BorderLayout.CENTER);
        JButton botonActualizar = new JButton("Actualizar reporte");
        panel.add(botonActualizar, BorderLayout.SOUTH);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarReporte();
                } catch (Exception ex) {
                    UIUtils.mostrarError(GuiaPanel.this, "No se pudo actualizar el reporte.");
                }
            }
        });
        return panel;
    }

    private void refrescarTodo() {
        try {
            refrescarSalidas();
            modeloInscritos.setRowCount(0);
            refrescarCalificaciones();
            refrescarReporte();
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo actualizar la información del guía.");
        }
    }

    private void refrescarSalidas() {
        modeloSalidas.setRowCount(0);
        salidasMostradas = agencia.getSalidasGuia(guia);
        for (SalidaProgramada salida : salidasMostradas) {
            int reservasActivas = 0;
            for (Reserva reserva : salida.getReservas()) {
                if (!reserva.getEstado().equals("CANCELADA")) {
                    reservasActivas++;
                }
            }
            modeloSalidas.addRow(new Object[]{
                    salida.getId(),
                    salida.getPaquete().getNombre(),
                    CalendarioTuristico.formatearFecha(salida.getFechaSalida()),
                    CalendarioTuristico.formatearFecha(salida.getFechaRetorno()),
                    salida.getPaquete().getLugarSalida(),
                    salida.getPaquete().getLugarRegreso(),
                    salida.getPaquete().getHotel().getNombre(),
                    salida.getCupoMaximo(),
                    reservasActivas,
                    salida.isActiva() ? "ACTIVA" : "INACTIVA"
            });
        }
    }

    private void refrescarInscritos(SalidaProgramada salida) {
        modeloInscritos.setRowCount(0);
        for (Reserva reserva : salida.getReservas()) {
            modeloInscritos.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getTurista().getNombre(),
                    reserva.getTurista().getCorreo(),
                    CalendarioTuristico.formatearFecha(reserva.getFechaContratacion()),
                    reserva.getEstado()
            });
        }
    }

    private void refrescarCalificaciones() {
        modeloCalificaciones.setRowCount(0);
        for (Recomendacion recomendacion : agencia.getRecomendaciones()) {
            if (recomendacion.getReserva().getSalida().getGuia().getId() == guia.getId()) {
                modeloCalificaciones.addRow(new Object[]{
                        CalendarioTuristico.formatearFecha(recomendacion.getFechaComentario()),
                        recomendacion.getTurista().getNombre(),
                        recomendacion.getReserva().getSalida().getPaquete().getNombre(),
                        recomendacion.getCalificacionGuia(),
                        recomendacion.getCalificacionExperiencia(),
                        recomendacion.getComentario()
                });
            }
        }
    }

    private void refrescarReporte() {
        areaReporte.setText(agencia.reporteGuia(guia));
    }
}
