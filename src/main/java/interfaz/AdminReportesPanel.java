package interfaz;

import negocio.AgenciaTuristica;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminReportesPanel extends JPanel {
    private AgenciaTuristica agencia;
    private JTextArea areaReporte;

    public AdminReportesPanel(AgenciaTuristica agencia) {
        this.agencia = agencia;
        construirInterfaz();
        refrescarReporte();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(areaReporte), BorderLayout.CENTER);

        JButton botonActualizar = new JButton("Actualizar reporte general");
        add(botonActualizar, BorderLayout.SOUTH);
        botonActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refrescarReporte();
                } catch (Exception ex) {
                    UIUtils.mostrarError(AdminReportesPanel.this,
                            "No se pudo actualizar el reporte general.");
                }
            }
        });
    }

    private void refrescarReporte() {
        areaReporte.setText(agencia.reporteAdministrador());
    }
}
