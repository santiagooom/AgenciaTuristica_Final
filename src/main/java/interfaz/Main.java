package interfaz;

import negocio.AgenciaTuristica;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Agencia Turística del Ecuador...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIUtils.configurarTema();
                    AgenciaTuristica agencia = new AgenciaTuristica();
                    VentanaPrincipal ventana = new VentanaPrincipal(agencia);
                    ventana.setVisible(true);
                } catch (Exception e) {
                    System.out.println("No se pudo iniciar la aplicación: " + e.getMessage());
                }
            }
        });
    }
}
