package interfaz;

import modelo.Usuario;
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

public class LoginPanel extends JPanel {
    private AgenciaTuristica agencia;
    private VentanaPrincipal ventana;
    private JTextField campoCorreo;
    private JPasswordField campoClave;

    public LoginPanel(AgenciaTuristica agencia, VentanaPrincipal ventana) {
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

        JLabel titulo = new JLabel("ECUADOR TRAVEL");
        titulo.setForeground(UIUtils.AZUL);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tarjeta.add(titulo, gbc);

        JLabel subtitulo = new JLabel("Agencia turística del Ecuador");
        subtitulo.setForeground(UIUtils.ROJO);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 1;
        tarjeta.add(subtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        tarjeta.add(new JLabel("Correo"), gbc);
        campoCorreo = new JTextField();
        campoCorreo.setPreferredSize(new Dimension(280, 34));
        UIUtils.configurarCampo(campoCorreo);
        gbc.gridx = 1;
        tarjeta.add(campoCorreo, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        tarjeta.add(new JLabel("Contraseña"), gbc);
        campoClave = new JPasswordField();
        campoClave.setPreferredSize(new Dimension(280, 34));
        UIUtils.configurarCampo(campoClave);
        gbc.gridx = 1;
        tarjeta.add(campoClave, gbc);

        JButton botonIngresar = new JButton("Iniciar sesión");
        gbc.gridy = 4;
        gbc.gridx = 0;
        tarjeta.add(botonIngresar, gbc);

        JButton botonRegistro = new JButton("Crear cuenta de turista");
        gbc.gridx = 1;
        tarjeta.add(botonRegistro, gbc);

        JButton botonRecuperarContraseña = new JButton("Recuperar contraseña");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        tarjeta.add(botonRecuperarContraseña, gbc);

        add(tarjeta);

        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String correo = LecturasFormulario.leerCorreo(campoCorreo);
                    String clave = LecturasFormulario.leerClave(campoClave);
                    Usuario usuario = agencia.autenticar(correo, clave);
                    ventana.abrirSesion(usuario);
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(LoginPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(LoginPanel.this, "Ocurrió un error al iniciar sesión.");
                }
            }
        });

        botonRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarRegistro();
                } catch (Exception ex) {
                    UIUtils.mostrarError(LoginPanel.this, "No se pudo abrir el registro.");
                }
            }
        });

        botonRecuperarContraseña.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarRecuperarContraseña();
                } catch (Exception ex) {
                    UIUtils.mostrarError(LoginPanel.this, "No se pudo abrir la recuperación de contraseña.");
                }
            }
        });
    }
}
