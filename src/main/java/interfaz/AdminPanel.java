package interfaz;

import modelo.Administrador;
import negocio.AgenciaTuristica;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JPanel {
    private AgenciaTuristica agencia;
    private Administrador administrador;
    private VentanaPrincipal ventana;

    public AdminPanel(AgenciaTuristica agencia, Administrador administrador,
                      VentanaPrincipal ventana) {
        this.agencia = agencia;
        this.administrador = administrador;
        this.ventana = ventana;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel encabezado = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Portal de administración - " + administrador.getNombre());
        UIUtils.prepararEncabezado(encabezado, titulo);
        JButton botonCerrar = new JButton("Cerrar sesión");
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(botonCerrar, BorderLayout.EAST);
        add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Usuarios", new AdminUsuariosPanel(agencia));
        pestanas.addTab("Lugares", new AdminLugaresPanel(agencia));
        pestanas.addTab("Hoteles", new AdminHotelesPanel(agencia));
        pestanas.addTab("Paquetes", new AdminPaquetesPanel(agencia));
        pestanas.addTab("Salidas", new AdminSalidasPanel(agencia));
        pestanas.addTab("Reservas", new AdminReservasPanel(agencia));
        pestanas.addTab("Resumen de viajes", new ResumenViajesPanel(agencia, administrador));
        pestanas.addTab("Reportes", new AdminReportesPanel(agencia));
        add(pestanas, BorderLayout.CENTER);

        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ventana.mostrarLogin();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminPanel.this, "No se pudo cerrar la sesión.");
                }
            }
        });
    }
}
