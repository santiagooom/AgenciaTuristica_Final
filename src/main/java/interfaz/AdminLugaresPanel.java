package interfaz;

import modelo.LugarTuristico;
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
import java.util.ArrayList;

public class AdminLugaresPanel extends JPanel {
    private AgenciaTuristica agencia;
    private JTextField campoNombre;
    private JComboBox<String> comboRegion;
    private JTextField campoUbicacion;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<LugarTuristico> lugaresMostrados;
    private LugarTuristico lugarSeleccionado;

    public AdminLugaresPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.lugaresMostrados = new ArrayList<LugarTuristico>();
        construirInterfaz();
        refrescarTabla();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Crear o modificar lugar turístico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombre = new JTextField();
        comboRegion = new JComboBox<String>(new String[]{"Costa", "Sierra", "Oriente", "Galápagos"});
        campoUbicacion = new JTextField();

        agregarCampo(formulario, gbc, "Nombre:", campoNombre, 0);
        agregarCampo(formulario, gbc, "Región:", comboRegion, 1);
        agregarCampo(formulario, gbc, "Ubicación:", campoUbicacion, 2);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonNuevo = new JButton("Nuevo");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonEliminar = new JButton("Eliminar / inhabilitar");
        JButton botonActivar = new JButton("Activar");
        botones.add(botonNuevo);
        botones.add(botonGuardar);
        botones.add(botonEliminar);
        botones.add(botonActivar);

        String[] columnas = {"ID", "Nombre", "Región", "Ubicación", "Estado"};
        modeloTabla = UIUtils.crearModelo(columnas);
        tabla = UIUtils.crearTabla(modeloTabla);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        botonNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    limpiarFormulario();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, "No se pudo limpiar el formulario.");
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = LecturasFormulario.leerTexto(campoNombre, "nombre del lugar");
                    String region = (String) LecturasFormulario.leerCombo(comboRegion, "la región");
                    String ubicacion = LecturasFormulario.leerTexto(campoUbicacion, "ubicación");
                    if (lugarSeleccionado == null) {
                        agencia.crearLugar(nombre, region, ubicacion);
                        UIUtils.mostrarInformacion(AdminLugaresPanel.this,
                                "Lugar creado correctamente.");
                    } else {
                        agencia.actualizarLugar(lugarSeleccionado, nombre, region, ubicacion);
                        UIUtils.mostrarInformacion(AdminLugaresPanel.this,
                                "Lugar actualizado correctamente.");
                    }
                    limpiarFormulario();
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, "No se pudo guardar el lugar.");
                }
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (lugarSeleccionado == null) {
                        throw new NegocioException("Seleccione un lugar.");
                    }
                    if (UIUtils.confirmar(AdminLugaresPanel.this,
                            "¿Desea eliminar o inhabilitar el lugar?")) {
                        String resultado = agencia.eliminarLugar(lugarSeleccionado);
                        UIUtils.mostrarInformacion(AdminLugaresPanel.this, resultado);
                        limpiarFormulario();
                        refrescarTabla();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this,
                            "No se pudo eliminar o inhabilitar el lugar.");
                }
            }
        });

        botonActivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (lugarSeleccionado == null) {
                        throw new NegocioException("Seleccione un lugar.");
                    }
                    lugarSeleccionado.setActivo(true);
                    UIUtils.mostrarInformacion(AdminLugaresPanel.this,
                            "Lugar activado correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this, "No se pudo activar el lugar.");
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    cargarSeleccion();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminLugaresPanel.this,
                            "No se pudo cargar el lugar seleccionado.");
                }
            }
        });
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String etiqueta,
                              javax.swing.JComponent campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        campo.setPreferredSize(new java.awt.Dimension(260, 27));
        panel.add(campo, gbc);
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        lugaresMostrados = agencia.getLugares();
        for (LugarTuristico lugar : lugaresMostrados) {
            modeloTabla.addRow(new Object[]{
                    lugar.getId(), lugar.getNombre(), lugar.getRegion(),
                    lugar.getUbicacion(), lugar.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        lugarSeleccionado = lugaresMostrados.get(filaModelo);
        campoNombre.setText(lugarSeleccionado.getNombre());
        comboRegion.setSelectedItem(lugarSeleccionado.getRegion());
        campoUbicacion.setText(lugarSeleccionado.getUbicacion());
    }

    private void limpiarFormulario() {
        lugarSeleccionado = null;
        campoNombre.setText("");
        comboRegion.setSelectedIndex(0);
        campoUbicacion.setText("");
        tabla.clearSelection();
    }
}
