package interfaz;

import modelo.Hotel;
import modelo.LugarTuristico;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import java.util.ArrayList;

public class AdminHotelesPanel extends JPanel {
    private AgenciaTuristica agencia;
    private JTextField campoNombre;
    private JComboBox<LugarTuristico> comboLugar;
    private JComboBox<Integer> comboCategoria;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<Hotel> hotelesMostrados;
    private Hotel hotelSeleccionado;

    public AdminHotelesPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.hotelesMostrados = new ArrayList<Hotel>();
        construirInterfaz();
        refrescarTodo();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder(
                "Crear o modificar hotel del catálogo base"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombre = new JTextField();
        comboLugar = new JComboBox<LugarTuristico>();
        comboCategoria = new JComboBox<Integer>(new Integer[]{1, 2, 3, 4, 5});

        agregarCampo(formulario, gbc, "Nombre:", campoNombre, 0);
        agregarCampo(formulario, gbc, "Lugar donde se encuentra:", comboLugar, 1);
        agregarCampo(formulario, gbc, "Categoría:", comboCategoria, 2);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonNuevoLugar = new JButton("Crear lugar rápido");
        JButton botonActualizar = new JButton("Actualizar catálogo");
        JButton botonNuevo = new JButton("Nuevo hotel");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonEliminar = new JButton("Eliminar / inhabilitar");
        JButton botonActivar = new JButton("Activar");
        botones.add(botonNuevoLugar);
        botones.add(botonActualizar);
        botones.add(botonNuevo);
        botones.add(botonGuardar);
        botones.add(botonEliminar);
        botones.add(botonActivar);

        String[] columnas = {"ID", "Nombre", "Lugar", "Región", "Categoría", "Estado"};
        modeloTabla = UIUtils.crearModelo(columnas);
        tabla = UIUtils.crearTabla(modeloTabla);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        botonNuevoLugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    crearLugarRapido();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo crear el lugar.");
                }
            }
        });

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTodo();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo actualizar el catálogo.");
                }
            }
        });

        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    limpiarFormulario();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo limpiar el formulario.");
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = LecturasFormulario.leerTexto(campoNombre, "nombre del hotel");
                    LugarTuristico lugar = (LugarTuristico) LecturasFormulario.leerCombo(
                            comboLugar, "el lugar del hotel");
                    Integer categoria = (Integer) LecturasFormulario.leerCombo(
                            comboCategoria, "la categoría");

                    if (hotelSeleccionado == null) {
                        agencia.crearHotel(nombre, lugar, categoria);
                        UIUtils.mostrarInformacion(AdminHotelesPanel.this,
                                "Hotel creado correctamente.");
                    } else {
                        agencia.actualizarHotel(hotelSeleccionado, nombre, lugar, categoria);
                        UIUtils.mostrarInformacion(AdminHotelesPanel.this,
                                "Hotel actualizado correctamente.");
                    }
                    limpiarFormulario();
                    refrescarTodo();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo guardar el hotel.");
                }
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (hotelSeleccionado == null) {
                        throw new NegocioException("Seleccione un hotel.");
                    }
                    if (UIUtils.confirmar(AdminHotelesPanel.this,
                            "¿Desea eliminar o inhabilitar el hotel?")) {
                        String resultado = agencia.eliminarHotel(hotelSeleccionado);
                        UIUtils.mostrarInformacion(AdminHotelesPanel.this, resultado);
                        limpiarFormulario();
                        refrescarTodo();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo eliminar o inhabilitar el hotel.");
                }
            }
        });

        botonActivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (hotelSeleccionado == null) {
                        throw new NegocioException("Seleccione un hotel.");
                    }
                    hotelSeleccionado.setActivo(true);
                    UIUtils.mostrarInformacion(AdminHotelesPanel.this,
                            "Hotel activado correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo activar el hotel.");
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    cargarSeleccion();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminHotelesPanel.this,
                            "No se pudo cargar el hotel seleccionado.");
                }
            }
        });
    }

    private void crearLugarRapido() throws NegocioException {
        JTextField nombre = new JTextField();
        JComboBox<String> region = new JComboBox<String>(
                new String[]{"Costa", "Sierra", "Oriente", "Galápagos"});
        JTextField ubicacion = new JTextField();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        agregarCampo(panel, gbc, "Nombre del lugar:", nombre, 0);
        agregarCampo(panel, gbc, "Región:", region, 1);
        agregarCampo(panel, gbc, "Ubicación:", ubicacion, 2);

        int respuesta = JOptionPane.showConfirmDialog(this, panel,
                "Crear lugar para el hotel", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (respuesta == JOptionPane.OK_OPTION) {
            String nombreLugar = LecturasFormulario.leerTexto(nombre, "nombre del lugar");
            String regionLugar = (String) LecturasFormulario.leerCombo(region, "la región");
            String ubicacionLugar = LecturasFormulario.leerTexto(ubicacion, "ubicación");
            LugarTuristico creado = agencia.crearLugar(nombreLugar, regionLugar, ubicacionLugar);
            refrescarLugares();
            comboLugar.setSelectedItem(creado);
            UIUtils.mostrarInformacion(this, "Lugar creado y seleccionado correctamente.");
        }
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta,
                              javax.swing.JComponent campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        campo.setPreferredSize(new java.awt.Dimension(290, 29));
        panel.add(campo, gbc);
    }

    private void refrescarTodo() {
        refrescarLugares();
        refrescarTabla();
    }

    private void refrescarLugares() {
        LugarTuristico seleccionado = (LugarTuristico) comboLugar.getSelectedItem();
        comboLugar.removeAllItems();
        for (LugarTuristico lugar : agencia.getLugares()) {
            if (lugar.isActivo()) {
                comboLugar.addItem(lugar);
            }
        }
        if (seleccionado != null) {
            comboLugar.setSelectedItem(seleccionado);
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        hotelesMostrados = agencia.getHoteles();
        for (Hotel hotel : hotelesMostrados) {
            modeloTabla.addRow(new Object[]{
                    hotel.getId(), hotel.getNombre(), hotel.getCiudad(), hotel.getRegion(),
                    hotel.getCategoria(), hotel.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        hotelSeleccionado = hotelesMostrados.get(filaModelo);
        campoNombre.setText(hotelSeleccionado.getNombre());
        comboLugar.setSelectedItem(hotelSeleccionado.getLugar());
        comboCategoria.setSelectedItem(hotelSeleccionado.getCategoria());
    }

    private void limpiarFormulario() {
        hotelSeleccionado = null;
        campoNombre.setText("");
        if (comboLugar.getItemCount() > 0) {
            comboLugar.setSelectedIndex(0);
        }
        comboCategoria.setSelectedIndex(0);
        tabla.clearSelection();
    }
}
