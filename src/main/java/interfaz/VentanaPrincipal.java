package interfaz;

import modelo.Administrador;
import modelo.GuiaTuristico;
import modelo.Turista;
import modelo.Usuario;
import negocio.AgenciaTuristica;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class VentanaPrincipal extends JFrame {
    private AgenciaTuristica agencia;
    private JPanel contenido;

    public VentanaPrincipal(AgenciaTuristica agencia) {
        this.agencia = agencia;
        setTitle("Ecuador Travel - Agencia Turística");
        setSize(1180, 760);
        setMinimumSize(new Dimension(980, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(UIUtils.crearFranjaBandera(), BorderLayout.NORTH);
        contenido = new JPanel(new BorderLayout());
        raiz.add(contenido, BorderLayout.CENTER);
        setContentPane(raiz);
        mostrarLogin();
    }

    public void mostrarLogin() {
        try {
            mostrarPanel(new LoginPanel(agencia, this));
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo mostrar el inicio de sesión.");
        }
    }

    public void mostrarRegistro() {
        try {
            mostrarPanel(new RegistroTuristaPanel(agencia, this));
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo mostrar el registro.");
        }
    }

    public void mostrarRecuperarContraseña() {
        try {
            mostrarPanel(new RecuperarContrasenaPanel(agencia, this));
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo mostrar la recuperación de contraseña.");
        }
    }

    public void abrirSesion(Usuario usuario) {
        try {
            if (usuario instanceof Administrador) {
                mostrarPanel(new AdminPanel(agencia, (Administrador) usuario, this));
            } else if (usuario instanceof GuiaTuristico) {
                mostrarPanel(new GuiaPanel(agencia, (GuiaTuristico) usuario, this));
            } else if (usuario instanceof Turista) {
                mostrarPanel(new TuristaPanel(agencia, (Turista) usuario, this));
            } else {
                UIUtils.mostrarError(this, "Tipo de usuario no reconocido.");
                mostrarLogin();
            }
        } catch (Exception e) {
            UIUtils.mostrarError(this, "No se pudo abrir la sesión: " + e.getMessage());
        }
    }

    private void mostrarPanel(JPanel panel) {
        contenido.removeAll();
        UIUtils.aplicarEstilo(panel);
        contenido.add(panel, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }
}
