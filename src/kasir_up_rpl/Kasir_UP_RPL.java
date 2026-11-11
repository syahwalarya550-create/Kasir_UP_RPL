package kasir_up_rpl;

import admin.BerandaView;
import database.Koneksi;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class Kasir_UP_RPL {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                Koneksi.getKoneksi().close();

                new BerandaView().setVisible(true);

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}