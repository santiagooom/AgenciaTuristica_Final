package interfaz;

import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecuperarContrasenaPanel extends JPanel {
    private AgenciaTuristica agencia;
    private VentanaPrincipal ventana;
    private JTextField campoCorreo;
    private JTextField campoRespuesta;
    private JPasswordField campoNuevaClave;
    private JPasswordField campoConfirmacion;
    private JLabel labelPregunta;
    private String correoActual;
    private JPanel panelRestablecimiento;

    public RecuperarContrasenaPanel(AgenciaTuristica agencia,
                                     VentanaPrincipal ventana) {
        this.agencia = agencia;
        this.ventana = ventana;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new GridBagLayout());
        setBackground(UIUtils.FONDO);

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.BORDE),
                BorderFactory.createEmptyBorder(28, 34, 28, 34)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("RESTABLECER CONTRASEÑA");
        titulo.setForeground(UIUtils.AZUL);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tarjeta.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        tarjeta.add(new JLabel("Correo"), gbc);
        campoCorreo = new JTextField();
        campoCorreo.setPreferredSize(new Dimension(280, 34));
        UIUtils.configurarCampo(campoCorreo);
        gbc.gridx = 1;
        tarjeta.add(campoCorreo, gbc);

        panelRestablecimiento = new JPanel(new GridBagLayout());
        panelRestablecimiento.setOpaque(false);
        panelRestablecimiento.setVisible(false);

        GridBagConstraints detalle = new GridBagConstraints();
        detalle.insets = new Insets(8, 8, 8, 8);
        detalle.fill = GridBagConstraints.HORIZONTAL;

        labelPregunta = new JLabel("");
        labelPregunta.setForeground(UIUtils.ROJO);
        labelPregunta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detalle.gridx = 0;
        detalle.gridy = 0;
        detalle.gridwidth = 2;
        panelRestablecimiento.add(labelPregunta, detalle);

        detalle.gridwidth = 1;
        detalle.gridy = 1;
        detalle.gridx = 0;
        panelRestablecimiento.add(new JLabel("Respuesta"), detalle);
        campoRespuesta = new JTextField();
        campoRespuesta.setPreferredSize(new Dimension(280, 34));
        UIUtils.configurarCampo(campoRespuesta);
        detalle.gridx = 1;
        panelRestablecimiento.add(campoRespuesta, detalle);

        detalle.gridy = 2;
        detalle.gridx = 0;
        panelRestablecimiento.add(new JLabel("Nueva contraseña"), detalle);
        campoNuevaClave = new JPasswordField();
        UIUtils.configurarCampo(campoNuevaClave);
        detalle.gridx = 1;
        panelRestablecimiento.add(campoNuevaClave, detalle);

        JLabel ayudaClave = new JLabel("Mínimo 8 caracteres, un número y un carácter especial.");
        ayudaClave.setForeground(UIUtils.ROJO);
        detalle.gridy = 3;
        detalle.gridx = 0;
        detalle.gridwidth = 2;
        panelRestablecimiento.add(ayudaClave, detalle);
        detalle.gridwidth = 1;

        detalle.gridy = 4;
        detalle.gridx = 0;
        panelRestablecimiento.add(new JLabel("Confirmar contraseña"), detalle);
        campoConfirmacion = new JPasswordField();
        UIUtils.configurarCampo(campoConfirmacion);
        detalle.gridx = 1;
        panelRestablecimiento.add(campoConfirmacion, detalle);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        tarjeta.add(panelRestablecimiento, gbc);

        JButton botonBuscar = new JButton("Buscar pregunta");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        tarjeta.add(botonBuscar, gbc);

        JButton botonRestablecer = new JButton("Restablecer contraseña");
        gbc.gridx = 1;
        tarjeta.add(botonRestablecer, gbc);

        JButton botonVolver = new JButton("Volver al login");
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        tarjeta.add(botonVolver, gbc);

        add(tarjeta);

        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPregunta();
            }
        });

        botonRestablecer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restablecerContrasena();
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarLogin();
                } catch (Exception ex) {
                    UIUtils.mostrarError(RecuperarContrasenaPanel.this,
                            "No se pudo volver al login.");
                }
            }
        });
    }

    private void buscarPregunta() {
        try {
            String correo = LecturasFormulario.leerCorreo(campoCorreo);
            String pregunta = agencia.obtenerPreguntaSeguridad(correo);
            correoActual = correo;
            labelPregunta.setText(pregunta);
            campoRespuesta.setText("");
            campoNuevaClave.setText("");
            campoConfirmacion.setText("");
            panelRestablecimiento.setVisible(true);
            revalidate();
            repaint();
        } catch (NegocioException ex) {
            correoActual = null;
            panelRestablecimiento.setVisible(false);
            UIUtils.mostrarError(this, ex.getMessage());
        } catch (Exception ex) {
            correoActual = null;
            panelRestablecimiento.setVisible(false);
            UIUtils.mostrarError(this,
                    "Ocurrió un error al buscar la pregunta.");
        }
    }

    private void restablecerContrasena() {
        try {
            if (correoActual == null || correoActual.isEmpty()) {
                throw new NegocioException("Debe buscar un correo primero.");
            }
            String respuesta = LecturasFormulario.leerTexto(
                    campoRespuesta, "respuesta de seguridad");
            String nuevaClave = LecturasFormulario.leerClave(campoNuevaClave);
            String confirmacion = LecturasFormulario.leerClave(campoConfirmacion);
            if (!nuevaClave.equals(confirmacion)) {
                throw new NegocioException(
                        "La nueva contraseña y su confirmación no coinciden.");
            }
            agencia.restablecerContrasena(
                    correoActual, respuesta, nuevaClave);
            UIUtils.mostrarInformacion(this,
                    "Contraseña restablecida correctamente. Ahora puede iniciar sesión.");
            ventana.mostrarLogin();
        } catch (NegocioException ex) {
            UIUtils.mostrarError(this, ex.getMessage());
        } catch (Exception ex) {
            UIUtils.mostrarError(this,
                    "Ocurrió un error al restablecer la contraseña.");
        }
    }
}
