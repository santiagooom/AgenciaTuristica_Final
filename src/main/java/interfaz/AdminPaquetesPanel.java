package interfaz;

import modelo.Hotel;
import modelo.LugarTuristico;
import modelo.PaqueteTuristico;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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
import java.util.ArrayList;

public class AdminPaquetesPanel extends JPanel {
    private AgenciaTuristica agencia;

    private JTextField campoNombre;
    private JTextArea areaDescripcion;
    private JTextField campoDuracion;
    private JTextField campoPrecio;
    private JComboBox<String> comboRegion;
    private JComboBox<Hotel> comboHotel;
    private DefaultListModel<LugarTuristico> modeloLugares;
    private JList<LugarTuristico> listaLugares;
    private JComboBox<String> comboLugarSalida;
    private JComboBox<String> comboLugarRegreso;

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<PaqueteTuristico> paquetesMostrados;
    private PaqueteTuristico paqueteSeleccionado;

    public AdminPaquetesPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.paquetesMostrados = new ArrayList<PaqueteTuristico>();
        construirInterfaz();
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Crear o modificar paquete turístico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombre = new JTextField();
        areaDescripcion = new JTextArea(3, 28);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        campoDuracion = new JTextField();
        campoPrecio = new JTextField();
        comboRegion = new JComboBox<String>(
                new String[]{"Costa", "Sierra", "Oriente", "Galápagos"});
        comboHotel = new JComboBox<Hotel>();
        modeloLugares = new DefaultListModel<LugarTuristico>();
        listaLugares = new JList<LugarTuristico>(modeloLugares);
        listaLugares.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaLugares.setVisibleRowCount(4);
        comboLugarSalida = new JComboBox<String>(agencia.getPuntosSalidaQuito());
        comboLugarRegreso = new JComboBox<String>(agencia.getPuntosSalidaQuito());

        agregarCampo(formulario, gbc, "Región:", comboRegion, 0, 30);
        agregarCampo(formulario, gbc, "Nombre:", campoNombre, 1, 30);
        agregarCampo(formulario, gbc, "Descripción:", new JScrollPane(areaDescripcion), 2, 70);
        agregarCampo(formulario, gbc, "Duración en días:", campoDuracion, 3, 30);
        agregarCampo(formulario, gbc, "Precio base:", campoPrecio, 4, 30);
        agregarCampo(formulario, gbc, "Hotel base:", comboHotel, 5, 30);
        agregarCampo(formulario, gbc, "Lugares del recorrido:",
                new JScrollPane(listaLugares), 6, 85);
        agregarCampo(formulario, gbc, "Lugar de salida:", comboLugarSalida, 7, 30);
        agregarCampo(formulario, gbc, "Lugar de regreso:", comboLugarRegreso, 8, 30);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonNuevoLugar = new JButton("Crear lugar rápido");
        JButton botonNuevoHotel = new JButton("Crear hotel rápido");
        JButton botonActualizarCatalogos = new JButton("Actualizar catálogos");
        JButton botonNuevo = new JButton("Nuevo paquete");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonEliminar = new JButton("Eliminar / inhabilitar");
        JButton botonActivar = new JButton("Activar");
        botones.add(botonNuevoLugar);
        botones.add(botonNuevoHotel);
        botones.add(botonActualizarCatalogos);
        botones.add(botonNuevo);
        botones.add(botonGuardar);
        botones.add(botonEliminar);
        botones.add(botonActivar);

        String[] columnas = {"ID", "Nombre", "Región", "Lugares", "Hotel",
                "Salida", "Regreso", "Días", "Precio", "Estado"};
        modeloTabla = UIUtils.crearModelo(columnas);
        tabla = UIUtils.crearTabla(modeloTabla);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        comboRegion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarCatalogosRegion();
                    seleccionarPuntosPredeterminados();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudieron cargar los catálogos de la región.");
                }
            }
        });

        botonNuevoLugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearLugarRapido();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo crear el lugar.");
                }
            }
        });

        botonNuevoHotel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearHotelRapido();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo crear el hotel.");
                }
            }
        });

        botonActualizarCatalogos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTodo();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudieron actualizar los catálogos.");
                }
            }
        });

        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    limpiarFormulario();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo limpiar el formulario.");
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = LecturasFormulario.leerTexto(campoNombre, "nombre del paquete");
                    String descripcion = LecturasFormulario.leerArea(areaDescripcion, "descripción");
                    int duracion = LecturasFormulario.leerEntero(campoDuracion, "duración");
                    double precio = LecturasFormulario.leerDecimal(campoPrecio, "precio base");
                    Hotel hotel = (Hotel) LecturasFormulario.leerCombo(comboHotel, "el hotel");
                    ArrayList<Object> objetos = LecturasFormulario.leerLista(listaLugares, "lugares");
                    ArrayList<LugarTuristico> lugares = new ArrayList<LugarTuristico>();
                    for (Object objeto : objetos) {
                        lugares.add((LugarTuristico) objeto);
                    }
                    String lugarSalida = (String) LecturasFormulario.leerCombo(
                            comboLugarSalida, "el lugar de salida");
                    String lugarRegreso = (String) LecturasFormulario.leerCombo(
                            comboLugarRegreso, "el lugar de regreso");

                    if (paqueteSeleccionado == null) {
                        agencia.crearPaquete(nombre, descripcion, duracion, precio,
                                hotel, lugares, lugarSalida, lugarRegreso);
                        UIUtils.mostrarInformacion(AdminPaquetesPanel.this,
                                "Paquete creado correctamente.");
                    } else {
                        agencia.actualizarPaquete(paqueteSeleccionado, nombre, descripcion,
                                duracion, precio, hotel, lugares, lugarSalida, lugarRegreso);
                        UIUtils.mostrarInformacion(AdminPaquetesPanel.this,
                                "Paquete actualizado correctamente.");
                    }
                    limpiarFormulario();
                    refrescarTodo();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo guardar el paquete.");
                }
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (paqueteSeleccionado == null) {
                        throw new NegocioException("Seleccione un paquete.");
                    }
                    if (UIUtils.confirmar(AdminPaquetesPanel.this,
                            "¿Desea eliminar o inhabilitar el paquete?")) {
                        String resultado = agencia.eliminarPaquete(paqueteSeleccionado);
                        UIUtils.mostrarInformacion(AdminPaquetesPanel.this, resultado);
                        limpiarFormulario();
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo eliminar o inhabilitar el paquete.");
                }
            }
        });

        botonActivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (paqueteSeleccionado == null) {
                        throw new NegocioException("Seleccione un paquete.");
                    }
                    paqueteSeleccionado.setActivo(true);
                    UIUtils.mostrarInformacion(AdminPaquetesPanel.this,
                            "Paquete activado correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo activar el paquete.");
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    cargarSeleccion();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPaquetesPanel.this,
                            "No se pudo cargar el paquete seleccionado.");
                }
            }
        });
    }

    private void crearLugarRapido() throws NegocioException {
        String regionActual = (String) comboRegion.getSelectedItem();
        JTextField nombre = new JTextField();
        JTextField ubicacion = new JTextField();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        agregarCampo(panel, gbc, "Región:", new JLabel(regionActual), 0, 30);
        agregarCampo(panel, gbc, "Nombre del lugar:", nombre, 1, 30);
        agregarCampo(panel, gbc, "Ubicación:", ubicacion, 2, 30);

        int respuesta = JOptionPane.showConfirmDialog(this, panel,
                "Crear lugar para " + regionActual, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (respuesta == JOptionPane.OK_OPTION) {
            String nombreLugar = LecturasFormulario.leerTexto(nombre, "nombre del lugar");
            String ubicacionLugar = LecturasFormulario.leerTexto(ubicacion, "ubicación");
            LugarTuristico creado = agencia.crearLugar(nombreLugar, regionActual, ubicacionLugar);
            refrescarCatalogosRegion();
            seleccionarLugarEnLista(creado);
            UIUtils.mostrarInformacion(this, "Lugar creado correctamente.");
        }
    }

    private void crearHotelRapido() throws NegocioException {
        String regionActual = (String) comboRegion.getSelectedItem();
        ArrayList<LugarTuristico> lugaresRegion = agencia.getLugaresActivosPorRegion(regionActual);
        if (lugaresRegion.isEmpty()) {
            throw new NegocioException("Primero debe crear un lugar para la región " + regionActual + ".");
        }

        JTextField nombre = new JTextField();
        JComboBox<LugarTuristico> lugar = new JComboBox<LugarTuristico>();
        for (LugarTuristico item : lugaresRegion) {
            lugar.addItem(item);
        }
        JComboBox<Integer> categoria = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        agregarCampo(panel, gbc, "Nombre del hotel:", nombre, 0, 30);
        agregarCampo(panel, gbc, "Lugar:", lugar, 1, 30);
        agregarCampo(panel, gbc, "Categoría:", categoria, 2, 30);

        int respuesta = JOptionPane.showConfirmDialog(this, panel,
                "Crear hotel base", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (respuesta == JOptionPane.OK_OPTION) {
            String nombreHotel = LecturasFormulario.leerTexto(nombre, "nombre del hotel");
            LugarTuristico lugarHotel = (LugarTuristico) LecturasFormulario.leerCombo(
                    lugar, "el lugar");
            Integer categoriaHotel = (Integer) LecturasFormulario.leerCombo(
                    categoria, "la categoría");
            Hotel creado = agencia.crearHotel(nombreHotel, lugarHotel, categoriaHotel);
            refrescarCatalogosRegion();
            comboHotel.setSelectedItem(creado);
            seleccionarLugarEnLista(lugarHotel);
            UIUtils.mostrarInformacion(this,
                    "Hotel creado. Su lugar también quedó seleccionado para el paquete.");
        }
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta,
                              javax.swing.JComponent campo, int fila, int alto) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campo.setPreferredSize(new java.awt.Dimension(390, alto));
        panel.add(campo, gbc);
    }

    private void refrescarTodo() {
        refrescarCatalogosRegion();
        refrescarTabla();
    }

    private void refrescarCatalogosRegion() {
        String region = (String) comboRegion.getSelectedItem();
        if (region == null) {
            return;
        }

        Hotel hotelActual = (Hotel) comboHotel.getSelectedItem();
        comboHotel.removeAllItems();
        for (Hotel hotel : agencia.getHotelesActivosPorRegion(region)) {
            comboHotel.addItem(hotel);
        }
        if (hotelActual != null && hotelActual.getRegion().equalsIgnoreCase(region)) {
            comboHotel.setSelectedItem(hotelActual);
        }

        modeloLugares.clear();
        for (LugarTuristico lugar : agencia.getLugaresActivosPorRegion(region)) {
            modeloLugares.addElement(lugar);
        }
    }

    private void seleccionarPuntosPredeterminados() {
        String region = (String) comboRegion.getSelectedItem();
        if (region == null) {
            return;
        }
        if (region.equalsIgnoreCase("Galápagos")) {
            comboLugarSalida.setSelectedItem(AgenciaTuristica.AEROPUERTO_QUITO);
            comboLugarRegreso.setSelectedItem(AgenciaTuristica.AEROPUERTO_QUITO);
        } else if (region.equalsIgnoreCase("Sierra")) {
            comboLugarSalida.setSelectedItem(AgenciaTuristica.TERMINAL_CARCELEN);
            comboLugarRegreso.setSelectedItem(AgenciaTuristica.TERMINAL_CARCELEN);
        } else {
            comboLugarSalida.setSelectedItem(AgenciaTuristica.TERMINAL_QUITUMBE);
            comboLugarRegreso.setSelectedItem(AgenciaTuristica.TERMINAL_QUITUMBE);
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        paquetesMostrados = agencia.getPaquetes();
        for (PaqueteTuristico paquete : paquetesMostrados) {
            modeloTabla.addRow(new Object[]{
                    paquete.getId(), paquete.getNombre(), paquete.getRegion(),
                    paquete.getNombresLugares(), paquete.getHotel().getNombre(),
                    paquete.getLugarSalida(), paquete.getLugarRegreso(),
                    paquete.getDuracionDias(), String.format("$%.2f", paquete.getPrecioBase()),
                    paquete.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        paqueteSeleccionado = paquetesMostrados.get(filaModelo);

        comboRegion.setSelectedItem(paqueteSeleccionado.getRegion());
        refrescarCatalogosRegion();
        campoNombre.setText(paqueteSeleccionado.getNombre());
        areaDescripcion.setText(paqueteSeleccionado.getDescripcion());
        campoDuracion.setText(String.valueOf(paqueteSeleccionado.getDuracionDias()));
        campoPrecio.setText(String.valueOf(paqueteSeleccionado.getPrecioBase()));
        comboHotel.setSelectedItem(paqueteSeleccionado.getHotel());
        comboLugarSalida.setSelectedItem(paqueteSeleccionado.getLugarSalida());
        comboLugarRegreso.setSelectedItem(paqueteSeleccionado.getLugarRegreso());

        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < modeloLugares.size(); i++) {
            LugarTuristico lugar = modeloLugares.getElementAt(i);
            if (paqueteSeleccionado.getLugares().contains(lugar)) {
                indices.add(i);
            }
        }
        int[] seleccion = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            seleccion[i] = indices.get(i);
        }
        listaLugares.setSelectedIndices(seleccion);
    }

    private void seleccionarLugarEnLista(LugarTuristico lugarBuscado) {
        for (int i = 0; i < modeloLugares.size(); i++) {
            if (modeloLugares.getElementAt(i).getId() == lugarBuscado.getId()) {
                listaLugares.addSelectionInterval(i, i);
            }
        }
    }

    private void limpiarFormulario() {
        paqueteSeleccionado = null;
        campoNombre.setText("");
        areaDescripcion.setText("");
        campoDuracion.setText("");
        campoPrecio.setText("");
        listaLugares.clearSelection();
        tabla.clearSelection();
        comboRegion.setSelectedIndex(0);
        refrescarCatalogosRegion();
        seleccionarPuntosPredeterminados();
        if (comboHotel.getItemCount() > 0) {
            comboHotel.setSelectedIndex(0);
        }
    }
}
