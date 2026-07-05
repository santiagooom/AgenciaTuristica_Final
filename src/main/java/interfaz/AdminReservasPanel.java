package interfaz;

import modelo.CalendarioTuristico;
import modelo.Reserva;
import negocio.AgenciaTuristica;
import negocio.NegocioException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminReservasPanel extends JPanel {
    private AgenciaTuristica agencia;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private ArrayList<Reserva> reservasMostradas;
    private JComboBox<String> comboEstado;

    public AdminReservasPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        this.reservasMostradas = new ArrayList<Reserva>();
        construirInterfaz();
        refrescarTabla();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        String[] columnas = {"ID", "Turista", "Paquete", "Salida",
                "Contratación", "Precio", "Estado"};
        modeloTabla = UIUtils.crearModelo(columnas);
        tabla = UIUtils.crearTabla(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        comboEstado = new JComboBox<String>(new String[]{"RESERVADA", "REALIZADA", "CANCELADA"});
        JButton botonCambiar = new JButton("Cambiar estado");
        JButton botonActualizar = new JButton("Actualizar");
        controles.add(comboEstado);
        controles.add(botonCambiar);
        controles.add(botonActualizar);
        add(controles, BorderLayout.SOUTH);

        botonCambiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int fila = tabla.getSelectedRow();
                    if (fila < 0) {
                        throw new NegocioException("Seleccione una reserva.");
                    }
                    int filaModelo = tabla.convertRowIndexToModel(fila);
                    Reserva reserva = reservasMostradas.get(filaModelo);
                    String estado = (String) LecturasFormulario.leerCombo(comboEstado, "el estado");
                    agencia.cambiarEstadoReserva(reserva, estado);
                    UIUtils.mostrarInformacion(AdminReservasPanel.this,
                            "Estado actualizado correctamente.");
                    refrescarTabla();
                } catch (NegocioException ex) {
                    UIUtils.mostrarError(AdminReservasPanel.this, ex.getMessage());
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminReservasPanel.this,
                            "No se pudo cambiar el estado de la reserva.");
                }
            }
        });

        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarTabla();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminReservasPanel.this,
                            "No se pudieron actualizar las reservas.");
                }
            }
        });
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        reservasMostradas = agencia.getReservas();
        for (Reserva reserva : reservasMostradas) {
            modeloTabla.addRow(new Object[]{
                    reserva.getId(),
                    reserva.getTurista().getNombre(),
                    reserva.getSalida().getPaquete().getNombre(),
                    CalendarioTuristico.formatearFecha(reserva.getSalida().getFechaSalida()),
                    CalendarioTuristico.formatearFecha(reserva.getFechaContratacion()),
                    String.format("$%.2f", reserva.getSalida().getPrecioFinal()),
                    reserva.getEstado()
            });
        }
    }
}
