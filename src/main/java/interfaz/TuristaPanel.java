package interfaz;

import modelo.CalendarioTuristico;
import modelo.Recomendacion;
import modelo.Reserva;
import modelo.SalidaProgramada;
import modelo.Turista;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TuristaPanel extends JPanel {
    private AgenciaTuristica agencia;
    private Turista turista;
    private VentanaPrincipal ventana;

    private DefaultTableModel modeloSalidas;
    private JTable tablaSalidas;
    private ArrayList<SalidaProgramada> salidasMostradas;

    private DefaultTableModel modeloReservas;
    private JTable tablaReservas;
    private ArrayList<Reserva> reservasMostradas;

    private JComboBox<Reserva> comboReservasRecomendacion;
    private JComboBox<Integer> comboCalificacionGuia;
    private JComboBox<Integer> comboCalificacionExperiencia;
    private JTextArea areaComentario;
    private JTextArea areaReporte;

    public TuristaPanel(AgenciaTuristica agencia, Turista turista, VentanaPrincipal ventana) {
        this.agencia = agencia;
        this.turista = turista;
        this.ventana = ventana;
        this.salidasMostradas = new ArrayList<SalidaProgramada>();
        this.reservasMostradas = new ArrayList<Reserva>();
        construirInterfaz();
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel encabezado = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Portal del turista - " + turista.getNombre());
        UIUtils.prepararEncabezado(encabezado, titulo);
        JButton botonCerrar = new JButton("Cerrar sesión");
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrar, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Salidas disponibles", crearPanelSalidas());
        pestanas.addTab("Resumen de mis viajes", new ResumenViajesPanel(agencia, turista));
        pestanas.addTab("Mis reservas", crearPanelReservas());
        pestanas.addTab("Dejar recomendación", crearPanelRecomendacion());
        pestanas.addTab("Mis reportes", crearPanelReportes());
        add(pestanas, BorderLayout.CENTER);

        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarLogin();
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudo cerrar la sesión.");
                }
            }
        });
    }

    private JPanel crearPanelSalidas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        String[] columnas = {"ID", "Recomendado", "Paquete", "Lugares", "Salida",
                "Retorno", "Lugar de salida", "Lugar de regreso", "Hotel", "Guía", "Precio", "Cupos"};
        modeloSalidas = UIUtils.crearModelo(columnas);
        tablaSalidas = UIUtils.crearTabla(modeloSalidas);
        panel.add(new JScrollPane(tablaSalidas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonReservar = new JButton("Reservar salida seleccionada");
        botones.add(botonActualizar);
        botones.add(botonReservar);
        panel.add(botones, BorderLayout.SOUTH);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarSalidas();
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudieron actualizar las salidas.");
                }
            }
        });

        botonReservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fila = tablaSalidas.getSelectedRow();
                    if (fila < 0) {
                        throw new NegocioException("Seleccione una salida disponible.");
                    }
                    int filaModelo = tablaSalidas.convertRowIndexToModel(fila);
                    SalidaProgramada salida = salidasMostradas.get(filaModelo);
                    if (UIUtils.confirmar(TuristaPanel.this,
                            "¿Desea reservar " + salida.getPaquete().getNombre()
                                    + " para el " + CalendarioTuristico.formatearFecha(salida.getFechaSalida()) + "?")) {
                        agencia.reservar(turista, salida);
                        UIUtils.mostrarInformacion(TuristaPanel.this,
                                "Reserva registrada correctamente.");
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(TuristaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudo realizar la reserva.");
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelReservas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        String[] columnas = {"ID", "Paquete", "Salida", "Retorno", "Lugar de salida",
                "Lugar de regreso", "Hotel", "Fecha de contratación", "Estado", "Precio"};
        modeloReservas = UIUtils.crearModelo(columnas);
        tablaReservas = UIUtils.crearTabla(modeloReservas);
        panel.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonActualizar = new JButton("Actualizar");
        JButton botonCancelar = new JButton("Cancelar reserva");
        JButton botonReagendar = new JButton("Cancelar y reagendar");
        botones.add(botonActualizar);
        botones.add(botonCancelar);
        botones.add(botonReagendar);
        panel.add(botones, BorderLayout.SOUTH);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarReservas();
                    refrescarRecomendaciones();
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudieron actualizar las reservas.");
                }
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fila = tablaReservas.getSelectedRow();
                    if (fila < 0) {
                        throw new NegocioException("Seleccione una reserva.");
                    }
                    int filaModelo = tablaReservas.convertRowIndexToModel(fila);
                    Reserva reserva = reservasMostradas.get(filaModelo);
                    if (UIUtils.confirmar(TuristaPanel.this, "¿Desea cancelar la reserva seleccionada?")) {
                        agencia.cancelarReserva(reserva, turista);
                        UIUtils.mostrarInformacion(TuristaPanel.this,
                                "La reserva fue cancelada correctamente.");
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(TuristaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudo cancelar la reserva.");
                }
            }
        });

        botonReagendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fila = tablaReservas.getSelectedRow();
                    if (fila < 0) {
                        throw new NegocioException("Seleccione una reserva.");
                    }
                    int filaModelo = tablaReservas.convertRowIndexToModel(fila);
                    Reserva reserva = reservasMostradas.get(filaModelo);
                    if (UIUtils.confirmar(TuristaPanel.this,
                            "Se cancelará la reserva actual y se creará una nueva para la siguiente fecha disponible. ¿Desea continuar?")) {
                        Reserva nuevaReserva = agencia.reagendarReserva(reserva, turista);
                        UIUtils.mostrarInformacion(TuristaPanel.this,
                                "Reserva reagendada correctamente para el "
                                        + CalendarioTuristico.formatearFecha(nuevaReserva.getSalida().getFechaSalida()) + ".");
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(TuristaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudo reagendar la reserva.");
                }
            }
        });
        return panel;
    }

    private JPanel crearPanelRecomendacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboReservasRecomendacion = new JComboBox<Reserva>();
        comboCalificacionGuia = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});
        comboCalificacionExperiencia = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});
        areaComentario = new JTextArea(6, 35);
        areaComentario.setLineWrap(true);
        areaComentario.setWrapStyleWord(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Viaje realizado:"), gbc);
        gbc.gridx = 1;
        panel.add(comboReservasRecomendacion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Calificación del guía:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCalificacionGuia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Calificación de la experiencia:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCalificacionExperiencia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Comentario:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(areaComentario), gbc);

        JButton botonGuardar = new JButton("Registrar recomendación");
        gbc.gridy = 4;
        gbc.gridx = 1;
        panel.add(botonGuardar, gbc);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Reserva reserva = (Reserva) LecturasFormulario.leerCombo(
                            comboReservasRecomendacion, "un viaje realizado");
                    Integer calificacionGuia = (Integer) LecturasFormulario.leerCombo(
                            comboCalificacionGuia, "la calificación del guía");
                    Integer calificacionExperiencia = (Integer) LecturasFormulario.leerCombo(
                            comboCalificacionExperiencia, "la calificación de la experiencia");
                    String comentario = LecturasFormulario.leerArea(areaComentario, "comentario");
                    Recomendacion recomendacion = agencia.registrarRecomendacion(turista,
                            reserva, calificacionGuia, calificacionExperiencia, comentario);
                    UIUtils.mostrarInformacion(TuristaPanel.this,
                            "Recomendación registrada el " + CalendarioTuristico.formatearFecha(recomendacion.getFechaComentario()) + ".");
                    areaComentario.setText("");
                    refrescarTodo();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(TuristaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(TuristaPanel.this,
                            "No se pudo registrar la recomendación.");
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
                    UIUtils.mostrarError(TuristaPanel.this, "No se pudo actualizar el reporte.");
                }
            }
        });
        return panel;
    }

    private void refrescarTodo() {
        try {
            refrescarSalidas();
            refrescarReservas();
            refrescarRecomendaciones();
            refrescarReporte();
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo actualizar la información del turista.");
        }
    }

    private void refrescarSalidas() {
        modeloSalidas.setRowCount(0);
        salidasMostradas = agencia.getSalidasDisponibles(turista);
        for (SalidaProgramada salida : salidasMostradas) {
            String recomendado = "";
            for (String region : salida.getPaquete().getRegiones().split(",")) {
                if (turista.tieneInteres(region.trim())) {
                    recomendado = "SÍ";
                }
            }
            modeloSalidas.addRow(new Object[]{
                    salida.getId(),
                    recomendado,
                    salida.getPaquete().getNombre(),
                    salida.getPaquete().getNombresLugares(),
                    CalendarioTuristico.formatearFecha(salida.getFechaSalida()),
                    CalendarioTuristico.formatearFecha(salida.getFechaRetorno()),
                    salida.getPaquete().getLugarSalida(),
                    salida.getPaquete().getLugarRegreso(),
                    salida.getPaquete().getHotel().getNombre(),
                    salida.getGuia().getNombre(),
                    String.format("$%.2f", salida.getPrecioFinal()),
                    salida.getCuposDisponibles()
            });
        }
    }

    private void refrescarReservas() {
        modeloReservas.setRowCount(0);
        reservasMostradas = agencia.getReservasTurista(turista);
        for (Reserva reserva : reservasMostradas) {
            modeloReservas.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getSalida().getPaquete().getNombre(),
                    CalendarioTuristico.formatearFecha(reserva.getSalida().getFechaSalida()),
                    CalendarioTuristico.formatearFecha(reserva.getSalida().getFechaRetorno()),
                    reserva.getSalida().getPaquete().getLugarSalida(),
                    reserva.getSalida().getPaquete().getLugarRegreso(),
                    reserva.getSalida().getPaquete().getHotel().getNombre(),
                    CalendarioTuristico.formatearFecha(reserva.getFechaContratacion()),
                    reserva.getEstado(),
                    String.format("$%.2f", reserva.getSalida().getPrecioFinal())
            });
        }
    }

    private void refrescarRecomendaciones() {
        comboReservasRecomendacion.removeAllItems();
        ArrayList<Reserva> disponibles = agencia.getReservasRealizadasSinRecomendacion(turista);
        for (Reserva reserva : disponibles) {
            comboReservasRecomendacion.addItem(reserva);
        }
    }

    private void refrescarReporte() {
        areaReporte.setText(agencia.reporteTurista(turista));
    }
}
