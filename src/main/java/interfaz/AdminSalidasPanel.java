package interfaz;

import modelo.CalendarioTuristico;
import modelo.GuiaTuristico;
import modelo.PaqueteTuristico;
import modelo.SalidaProgramada;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminSalidasPanel extends JPanel {
    private AgenciaTuristica agencia;
    private JComboBox<PaqueteTuristico> comboPaquete;
    private JComboBox<String> comboTrimestre;
    private JComboBox<GuiaTuristico> comboGuia;
    private JTextField campoCupo;
    private JTextField campoPrecio;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<SalidaProgramada> salidasMostradas;
    private SalidaProgramada salidaSeleccionada;

    public AdminSalidasPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.salidasMostradas = new ArrayList<SalidaProgramada>();
        construirInterfaz();
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Salidas trimestrales de enero a diciembre de 2027"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboPaquete = new JComboBox<PaqueteTuristico>();
        comboTrimestre = new JComboBox<String>();
        comboGuia = new JComboBox<GuiaTuristico>();
        campoCupo = new JTextField();
        campoPrecio = new JTextField();

        for (LocalDate fecha : CalendarioTuristico.getFechasTrimestrales2027()) {
            comboTrimestre.addItem(CalendarioTuristico.formatearFecha(fecha));
        }

        agregarCampo(formulario, gbc, "Paquete:", comboPaquete, 0);
        agregarCampo(formulario, gbc, "Inicio del trimestre:", comboTrimestre, 1);
        agregarCampo(formulario, gbc, "Guía de la región:", comboGuia, 2);
        agregarCampo(formulario, gbc, "Cupo máximo:", campoCupo, 3);
        agregarCampo(formulario, gbc, "Precio final:", campoPrecio, 4);

        JLabel nota = new JLabel("El retorno se calcula automáticamente según la duración del paquete.");
        nota.setForeground(UIUtils.ROJO);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formulario.add(nota, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonNuevo = new JButton("Nuevo");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonEliminar = new JButton("Eliminar / inhabilitar");
        JButton botonActivar = new JButton("Activar");
        JButton botonActualizar = new JButton("Actualizar");
        botones.add(botonNuevo);
        botones.add(botonGuardar);
        botones.add(botonEliminar);
        botones.add(botonActivar);
        botones.add(botonActualizar);

        String[] columnas = {"ID", "Paquete", "Región", "Guía", "Salida", "Retorno",
                "Cupo", "Disponibles", "Precio", "Trimestre", "Estado"};
        modeloTabla = UIUtils.crearModelo(columnas);
        tabla = UIUtils.crearTabla(modeloTabla);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        comboPaquete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarGuiasCompatibles();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this,
                            "No se pudieron actualizar los guías de la región.");
                }
            }
        });

        comboTrimestre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarGuiasCompatibles();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this,
                            "No se pudieron actualizar los guías del trimestre.");
                }
            }
        });

        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    limpiarFormulario();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, "No se pudo limpiar el formulario.");
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PaqueteTuristico paquete = (PaqueteTuristico) LecturasFormulario.leerCombo(
                            comboPaquete, "el paquete");
                    int indiceTrimestre = comboTrimestre.getSelectedIndex();
                    if (indiceTrimestre < 0) {
                        throw new NegocioException("Debe seleccionar un trimestre.");
                    }
                    LocalDate fechaSalida = CalendarioTuristico.getFechaTrimestral(indiceTrimestre);
                    GuiaTuristico guia = (GuiaTuristico) LecturasFormulario.leerCombo(
                            comboGuia, "un guía disponible para la región");
                    int cupo = LecturasFormulario.leerEntero(campoCupo, "cupo máximo");
                    double precio = LecturasFormulario.leerDecimal(campoPrecio, "precio final");
                    LocalDate fechaRetorno = fechaSalida.plusDays(paquete.getDuracionDias() - 1);
                    String temporada = CalendarioTuristico.getNombreTrimestre(fechaSalida);

                    if (salidaSeleccionada == null) {
                        agencia.crearSalida(paquete, guia, fechaSalida, fechaRetorno,
                                cupo, precio, temporada);
                        UIUtils.mostrarInformacion(AdminSalidasPanel.this,
                                "Salida trimestral creada correctamente.");
                    } else {
                        agencia.actualizarSalida(salidaSeleccionada, paquete, guia,
                                fechaSalida, fechaRetorno, cupo, precio, temporada);
                        UIUtils.mostrarInformacion(AdminSalidasPanel.this,
                                "Salida trimestral actualizada correctamente.");
                    }
                    limpiarFormulario();
                    refrescarTodo();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, "No se pudo guardar la salida.");
                }
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (salidaSeleccionada == null) {
                        throw new NegocioException("Seleccione una salida.");
                    }
                    if (UIUtils.confirmar(AdminSalidasPanel.this,
                            "¿Desea eliminar o inhabilitar la salida?")) {
                        String resultado = agencia.eliminarSalida(salidaSeleccionada);
                        UIUtils.mostrarInformacion(AdminSalidasPanel.this, resultado);
                        limpiarFormulario();
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this,
                            "No se pudo eliminar o inhabilitar la salida.");
                }
            }
        });

        botonActivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (salidaSeleccionada == null) {
                        throw new NegocioException("Seleccione una salida.");
                    }
                    salidaSeleccionada.setActiva(true);
                    UIUtils.mostrarInformacion(AdminSalidasPanel.this,
                            "Salida activada correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this, "No se pudo activar la salida.");
                }
            }
        });

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTodo();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this,
                            "No se pudieron actualizar las listas.");
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    cargarSeleccion();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminSalidasPanel.this,
                            "No se pudo cargar la salida seleccionada.");
                }
            }
        });
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta,
                              javax.swing.JComponent campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        campo.setPreferredSize(new java.awt.Dimension(285, 30));
        panel.add(campo, gbc);
    }

    private void refrescarTodo() {
        refrescarPaquetes();
        refrescarGuiasCompatibles();
        refrescarTabla();
    }

    private void refrescarPaquetes() {
        Object paqueteActual = comboPaquete.getSelectedItem();
        comboPaquete.removeAllItems();
        for (PaqueteTuristico paquete : agencia.getPaquetes()) {
            if (paquete.isActivo()) {
                comboPaquete.addItem(paquete);
            }
        }
        if (paqueteActual != null) {
            comboPaquete.setSelectedItem(paqueteActual);
        }
    }

    private void refrescarGuiasCompatibles() {
        GuiaTuristico guiaActual = (GuiaTuristico) comboGuia.getSelectedItem();
        PaqueteTuristico paquete = (PaqueteTuristico) comboPaquete.getSelectedItem();
        LocalDate fecha = CalendarioTuristico.getFechaTrimestral(comboTrimestre.getSelectedIndex());
        comboGuia.removeAllItems();

        if (salidaSeleccionada != null
                && salidaSeleccionada.getPaquete() == paquete
                && salidaSeleccionada.getFechaSalida().equals(fecha)) {
            comboGuia.addItem(salidaSeleccionada.getGuia());
        }

        for (GuiaTuristico guia : agencia.getGuiasDisponibles(paquete, fecha)) {
            boolean repetido = false;
            for (int i = 0; i < comboGuia.getItemCount(); i++) {
                if (comboGuia.getItemAt(i).getId() == guia.getId()) {
                    repetido = true;
                }
            }
            if (!repetido) {
                comboGuia.addItem(guia);
            }
        }

        if (guiaActual != null) {
            comboGuia.setSelectedItem(guiaActual);
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        salidasMostradas = agencia.getSalidas();
        for (SalidaProgramada salida : salidasMostradas) {
            modeloTabla.addRow(new Object[]{
                    salida.getId(), salida.getPaquete().getNombre(), salida.getPaquete().getRegion(),
                    salida.getGuia().getNombre(),
                    CalendarioTuristico.formatearFecha(salida.getFechaSalida()),
                    CalendarioTuristico.formatearFecha(salida.getFechaRetorno()),
                    salida.getCupoMaximo(), salida.getCuposDisponibles(),
                    String.format("$%.2f", salida.getPrecioFinal()),
                    salida.getTemporada(), salida.isActiva() ? "ACTIVA" : "INACTIVA"
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        salidaSeleccionada = salidasMostradas.get(filaModelo);
        comboPaquete.setSelectedItem(salidaSeleccionada.getPaquete());
        comboTrimestre.setSelectedIndex(
                CalendarioTuristico.getIndiceTrimestre(salidaSeleccionada.getFechaSalida()));
        refrescarGuiasCompatibles();
        comboGuia.setSelectedItem(salidaSeleccionada.getGuia());
        campoCupo.setText(String.valueOf(salidaSeleccionada.getCupoMaximo()));
        campoPrecio.setText(String.valueOf(salidaSeleccionada.getPrecioFinal()));
    }

    private void limpiarFormulario() {
        salidaSeleccionada = null;
        if (comboPaquete.getItemCount() > 0) {
            comboPaquete.setSelectedIndex(0);
        }
        if (comboTrimestre.getItemCount() > 0) {
            comboTrimestre.setSelectedIndex(0);
        }
        campoCupo.setText("");
        campoPrecio.setText("");
        tabla.clearSelection();
        refrescarGuiasCompatibles();
    }
}
