package interfaz;

import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroTuristaPanel extends JPanel {
    private AgenciaTuristica agencia;
    private VentanaPrincipal ventana;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JPasswordField campoClave;
    private JTextField campoIntereses;
    private JComboBox<String> comboPregunta;
    private JTextField campoRespuesta;

    public RegistroTuristaPanel(AgenciaTuristica agencia, VentanaPrincipal ventana) {
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
                BorderFactory.createEmptyBorder(24, 32, 24, 32)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("REGISTRO DE TURISTA");
        titulo.setForeground(UIUtils.AZUL);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tarjeta.add(titulo, gbc);

        gbc.gridwidth = 1;
        agregarCampo(tarjeta, "Nombre completo", campoNombre = new JTextField(), 1, gbc);
        agregarCampo(tarjeta, "Correo", campoCorreo = new JTextField(), 2, gbc);
        agregarCampo(tarjeta, "Contraseña", campoClave = new JPasswordField(), 3, gbc);

        JLabel ayudaClave = new JLabel("Mínimo 8 caracteres, un número y un carácter especial.");
        ayudaClave.setForeground(UIUtils.ROJO);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        tarjeta.add(ayudaClave, gbc);

        agregarCampo(tarjeta, "Intereses por región", campoIntereses = new JTextField(), 5, gbc);

        JLabel ayuda = new JLabel("Costa, Sierra, Oriente o Galápagos. Separe varias regiones con coma.");
        ayuda.setForeground(UIUtils.ROJO);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        tarjeta.add(ayuda, gbc);

        // Pregunta de seguridad
        String[] preguntas = {
            "Nombre de la abuela materna",
            "Nombre de la Mascota",
            "Fecha de nacimiento",
            "Marca del Primer Auto",
            "Segundo nombre del Padre"
        };
        comboPregunta = new JComboBox<>(preguntas);
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        tarjeta.add(new JLabel("Pregunta de seguridad"), gbc);
        gbc.gridx = 1;
        UIUtils.configurarCampo(comboPregunta);
        comboPregunta.setPreferredSize(new java.awt.Dimension(290, 32));
        tarjeta.add(comboPregunta, gbc);

        agregarCampo(tarjeta, "Respuesta a la pregunta", campoRespuesta = new JTextField(), 8, gbc);

        JLabel ayudaSeguridad = new JLabel("Esta pregunta se usará para recuperar tu contraseña.");
        ayudaSeguridad.setForeground(UIUtils.ROJO);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        tarjeta.add(ayudaSeguridad, gbc);

        JButton botonGuardar = new JButton("Registrar");
        JButton botonVolver = new JButton("Volver");
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        tarjeta.add(botonGuardar, gbc);
        gbc.gridx = 1;
        tarjeta.add(botonVolver, gbc);

        add(tarjeta);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = LecturasFormulario.leerNombre(campoNombre);
                    String correo = LecturasFormulario.leerCorreo(campoCorreo);
                    String clave = LecturasFormulario.leerClave(campoClave);
                    String intereses = LecturasFormulario.leerTexto(campoIntereses, "intereses");
                    String pregunta = (String) comboPregunta.getSelectedItem();
                    String respuesta = LecturasFormulario.leerTexto(campoRespuesta, "respuesta de seguridad");
                    agencia.registrarTuristaConSeguridad(nombre, correo, clave, intereses, pregunta, respuesta);
                    UIUtils.mostrarInformacion(RegistroTuristaPanel.this,
                            "Registro correcto. Ahora puede iniciar sesión.");
                    ventana.mostrarLogin();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(RegistroTuristaPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(RegistroTuristaPanel.this,
                            "Ocurrió un error al registrar al turista.");
                }
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarLogin();
                } catch (Exception ex) {
                    UIUtils.mostrarError(RegistroTuristaPanel.this, "No se pudo volver al inicio.");
                }
            }
        });
    }

    private void agregarCampo(JPanel panel, String etiqueta, javax.swing.JComponent campo,
                              int fila, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        UIUtils.configurarCampo(campo);
        campo.setPreferredSize(new java.awt.Dimension(290, 32));
        panel.add(campo, gbc);
    }
}
