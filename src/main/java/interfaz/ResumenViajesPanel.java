package interfaz;

import modelo.CalendarioTuristico;
import modelo.Reserva;
import modelo.Usuario;
import negocio.AgenciaTuristica;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class ResumenViajesPanel extends JPanel {
    private AgenciaTuristica agencia;
    private Usuario usuario;

    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloProximos;
    private DefaultTableModel modeloRealizados;

    public ResumenViajesPanel(AgenciaTuristica agencia, Usuario usuario) {
        this.agencia = agencia;
        this.usuario = usuario;
        construirInterfaz();
        refrescarTablas();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));

        String[] columnas = {
                "Reserva", "Turista", "Paquete", "Salida", "Regreso",
                "Lugar de salida", "Lugar de regreso", "Hotel", "Guía", "Estado"
        };

        modeloReservas = UIUtils.crearModelo(columnas);
        modeloProximos = UIUtils.crearModelo(columnas);
        modeloRealizados = UIUtils.crearModelo(columnas);

        JTable tablaReservas = UIUtils.crearTabla(modeloReservas);
        JTable tablaProximos = UIUtils.crearTabla(modeloProximos);
        JTable tablaRealizados = UIUtils.crearTabla(modeloRealizados);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Reservas registradas", new JScrollPane(tablaReservas));
        pestanas.addTab("Próximos viajes", new JScrollPane(tablaProximos));
        pestanas.addTab("Viajes realizados", new JScrollPane(tablaRealizados));
        add(pestanas, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonActualizar = new JButton("Actualizar información");
        botones.add(botonActualizar);
        add(botones, BorderLayout.SOUTH);

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTablas();
                } catch (Exception ex) {
                    UIUtils.mostrarError(ResumenViajesPanel.this,
                            "No se pudo actualizar la información de viajes.");
                }
            }
        });
    }

    private void refrescarTablas() {
        modeloReservas.setRowCount(0);
        modeloProximos.setRowCount(0);
        modeloRealizados.setRowCount(0);

        ArrayList<Reserva> reservas = agencia.getReservasParaUsuario(usuario);
        LocalDate hoy = LocalDate.now();

        for (Reserva reserva : reservas) {
            Object[] fila = crearFila(reserva);
            modeloReservas.addRow(fila);

            if (reserva.getEstado().equals("RESERVADA")
                    && !reserva.getSalida().getFechaSalida().isBefore(hoy)) {
                modeloProximos.addRow(crearFila(reserva));
            }

            if (reserva.getEstado().equals("REALIZADA")) {
                modeloRealizados.addRow(crearFila(reserva));
            }
        }
    }

    private Object[] crearFila(Reserva reserva) {
        return new Object[]{
                reserva.getId(),
                reserva.getTurista().getNombre(),
                reserva.getSalida().getPaquete().getNombre(),
                CalendarioTuristico.formatearFecha(reserva.getSalida().getFechaSalida()),
                CalendarioTuristico.formatearFecha(reserva.getSalida().getFechaRetorno()),
                reserva.getSalida().getPaquete().getLugarSalida(),
                reserva.getSalida().getPaquete().getLugarRegreso(),
                reserva.getSalida().getPaquete().getHotel().getNombre(),
                reserva.getSalida().getGuia().getNombre(),
                reserva.getEstado()
        };
    }
}
