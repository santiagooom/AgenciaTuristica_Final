package interfaz;

import modelo.Administrador;
import modelo.CalendarioTuristico;
import modelo.GuiaTuristico;
import modelo.Turista;
import modelo.Usuario;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

public class AdminUsuariosPanel extends JPanel {
    private AgenciaTuristica agencia;
    private JComboBox<String> comboRol;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JPasswordField campoClave;
    private JTextField campoDatoRol;
    private JComboBox<String> comboPregunta;
    private JTextField campoRespuesta;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<Usuario> usuariosMostrados;
    private Usuario usuarioSeleccionado;

    public AdminUsuariosPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.usuariosMostrados = new ArrayList<Usuario>();
        construirInterfaz();
        refrescarTabla();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Crear o modificar usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboRol = new JComboBox<String>(new String[]{"TURISTA", "GUIA"});
        campoNombre = new JTextField();
        campoCorreo = new JTextField();
        campoClave = new JPasswordField();
        campoDatoRol = new JTextField();
        comboPregunta = new JComboBox<String>(new String[]{
                "Nombre de la abuela materna",
                "Nombre de la mascota",
                "Fecha de nacimiento",
                "Marca del primer auto",
                "Segundo nombre del padre"
        });
        campoRespuesta = new JTextField();

        agregarCampo(formulario, gbc, "Rol:", comboRol, 0);
        agregarCampo(formulario, gbc, "Nombre:", campoNombre, 1);
        agregarCampo(formulario, gbc, "Correo:", campoCorreo, 2);
        agregarCampo(formulario, gbc, "Contraseña (mín. 8, número y especial):", campoClave, 3);
        agregarCampo(formulario, gbc, "Intereses / región asignada:", campoDatoRol, 4);
        agregarCampo(formulario, gbc, "Pregunta de seguridad:", comboPregunta, 5);
        agregarCampo(formulario, gbc, "Respuesta de seguridad:", campoRespuesta, 6);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonNuevo = new JButton("Nuevo");
        JButton botonGuardar = new JButton("Guardar");
        JButton botonEliminar = new JButton("Eliminar / inhabilitar");
        JButton botonActivar = new JButton("Activar");
        botones.add(botonNuevo);
        botones.add(botonGuardar);
        botones.add(botonEliminar);
        botones.add(botonActivar);

        String[] columnas = {"ID", "Nombre", "Correo", "Rol", "Detalle", "Estado", "Registro"};
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
                    UIUtils.mostrarError(AdminUsuariosPanel.this, "No se pudo limpiar el formulario.");
                }
            }
        });

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String rol = (String) LecturasFormulario.leerCombo(comboRol, "el rol");
                    String nombre = LecturasFormulario.leerNombre(campoNombre);
                    String correo = LecturasFormulario.leerCorreo(campoCorreo);
                    String clave = LecturasFormulario.leerClave(campoClave);
                    String datoRol = LecturasFormulario.leerTexto(campoDatoRol,
                            rol.equals("TURISTA") ? "intereses" : "región asignada");

                    if (usuarioSeleccionado == null) {
                        String pregunta = (String) LecturasFormulario.leerCombo(
                                comboPregunta, "la pregunta de seguridad");
                        String respuesta = LecturasFormulario.leerTexto(
                                campoRespuesta, "respuesta de seguridad");
                        if (rol.equals("TURISTA")) {
                            agencia.registrarTuristaConSeguridad(nombre, correo, clave,
                                    datoRol, pregunta, respuesta);
                        } else {
                            GuiaTuristico guiaCreado = agencia.crearGuiaConSeguridad(
                                    nombre, correo, clave, datoRol, pregunta, respuesta);
                            UIUtils.mostrarInformacion(AdminUsuariosPanel.this,
                                    "Guía creado para la región " + guiaCreado.getRegionAsignada()
                                            + ". Fue asignado al trimestre que inicia el "
                                            + CalendarioTuristico.formatearFecha(guiaCreado.getFechaDisponibleDesde()) + ".");
                        }
                        if (rol.equals("TURISTA")) {
                            UIUtils.mostrarInformacion(AdminUsuariosPanel.this,
                                    "Turista creado correctamente.");
                        }
                    } else {
                        agencia.actualizarUsuario(usuarioSeleccionado, nombre, correo,
                                clave, datoRol);
                        UIUtils.mostrarInformacion(AdminUsuariosPanel.this,
                                "Usuario actualizado correctamente.");
                    }
                    limpiarFormulario();
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this,
                            "No se pudo guardar el usuario.");
                }
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (usuarioSeleccionado == null) {
                        throw new NegocioException("Seleccione un usuario.");
                    }
                    if (UIUtils.confirmar(AdminUsuariosPanel.this,
                            "¿Desea eliminar o inhabilitar al usuario seleccionado?")) {
                        String resultado = agencia.eliminarOInhabilitarUsuario(usuarioSeleccionado);
                        UIUtils.mostrarInformacion(AdminUsuariosPanel.this, resultado);
                        limpiarFormulario();
                        refrescarTabla();
                    }
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this,
                            "No se pudo eliminar o inhabilitar el usuario.");
                }
            }
        });

        botonActivar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (usuarioSeleccionado == null) {
                        throw new NegocioException("Seleccione un usuario.");
                    }
                    usuarioSeleccionado.setActivo(true);
                    UIUtils.mostrarInformacion(AdminUsuariosPanel.this,
                            "Usuario activado correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this, "No se pudo activar el usuario.");
                }
            }
        });

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    cargarSeleccion();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminUsuariosPanel.this,
                            "No se pudo cargar el usuario seleccionado.");
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
        campo.setPreferredSize(new java.awt.Dimension(250, 27));
        panel.add(campo, gbc);
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        usuariosMostrados = agencia.getUsuarios();
        for (Usuario usuario : usuariosMostrados) {
            modeloTabla.addRow(new Object[]{
                    usuario.getId(), usuario.getNombre(), usuario.getCorreo(),
                    usuario.getRol(), usuario.getDetalle(),
                    usuario.isActivo() ? "ACTIVO" : "INACTIVO",
                    CalendarioTuristico.formatearFecha(usuario.getFechaRegistro())
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        usuarioSeleccionado = usuariosMostrados.get(filaModelo);
        campoNombre.setText(usuarioSeleccionado.getNombre());
        campoCorreo.setText(usuarioSeleccionado.getCorreo());
        campoClave.setText(usuarioSeleccionado.getClave());

        if (usuarioSeleccionado instanceof Turista) {
            Turista turista = (Turista) usuarioSeleccionado;
            comboRol.setSelectedItem("TURISTA");
            campoDatoRol.setText(String.join(", ", turista.getIntereses()));
            comboRol.setEnabled(false);
            comboPregunta.setEnabled(false);
            campoRespuesta.setEnabled(false);
        } else if (usuarioSeleccionado instanceof GuiaTuristico) {
            GuiaTuristico guia = (GuiaTuristico) usuarioSeleccionado;
            comboRol.setSelectedItem("GUIA");
            campoDatoRol.setText(guia.getRegionAsignada());
            comboRol.setEnabled(false);
            comboPregunta.setEnabled(false);
            campoRespuesta.setEnabled(false);
        } else if (usuarioSeleccionado instanceof Administrador) {
            comboRol.setSelectedItem("GUIA");
            campoDatoRol.setText("");
            comboRol.setEnabled(false);
            comboPregunta.setEnabled(false);
            campoRespuesta.setEnabled(false);
        }
    }

    private void limpiarFormulario() {
        usuarioSeleccionado = null;
        comboRol.setEnabled(true);
        comboPregunta.setEnabled(true);
        campoRespuesta.setEnabled(true);
        comboRol.setSelectedIndex(0);
        comboPregunta.setSelectedIndex(0);
        campoNombre.setText("");
        campoCorreo.setText("");
        campoClave.setText("");
        campoDatoRol.setText("");
        campoRespuesta.setText("");
        tabla.clearSelection();
    }
}
