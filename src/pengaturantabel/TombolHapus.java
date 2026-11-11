
package pengaturantabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.IntConsumer;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TombolHapus extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor {

    private final JButton tombol = new JButton("Hapus");

    private final IntConsumer aksiHapus;

    private int barisModel = -1;

    public TombolHapus(
            JTable tabel,
            IntConsumer aksiHapus) {

        this.aksiHapus = aksiHapus;

        tombol.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tombol.setForeground(Color.WHITE);
        tombol.setBackground(new Color(220, 38, 38));
        tombol.setPreferredSize(new Dimension(75, 28));
        tombol.setFocusPainted(false);
        tombol.setBorderPainted(false);
        tombol.setContentAreaFilled(true);
        tombol.setOpaque(true);
        tombol.setFocusable(false);
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tombol.addActionListener(e -> {
            int baris = barisModel;
            fireEditingStopped();

            if (baris >= 0) {
                barisModel = -1;
                this.aksiHapus.accept(baris);
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean selected,
            boolean focused,
            int row,
            int column) {

        tombol.setText("Hapus");
        tombol.setBackground(new Color(220, 38, 38));
        tombol.setForeground(Color.WHITE);

        return tombol;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean selected,
            int row,
            int column) {

        barisModel = table.convertRowIndexToModel(row);

        tombol.setText("Hapus");
        tombol.setBackground(new Color(185, 28, 28));
        tombol.setForeground(Color.WHITE);

        return tombol;
    }

    @Override
    public Object getCellEditorValue() {
        return "Hapus";
    }
}
