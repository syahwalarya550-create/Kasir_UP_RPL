
package pengaturantabel;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TombolAksiRiwayat extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor {

    public interface Aksi {
        void edit(int baris);
        void hapus(int baris);
    }

    private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
    private final JButton tombolEdit = new JButton("Edit");
    private final JButton tombolHapus = new JButton("Hapus");

    private int baris;
    private final Aksi aksi;

    public TombolAksiRiwayat(Aksi aksi) {
        this.aksi = aksi;

        tombolEdit.setFocusable(false);
        tombolHapus.setFocusable(false);

        tombolEdit.addActionListener(e -> {
            fireEditingStopped();
            this.aksi.edit(baris);
        });

        tombolHapus.addActionListener(e -> {
            fireEditingStopped();
            this.aksi.hapus(baris);
        });

        panel.add(tombolEdit);
        panel.add(tombolHapus);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean selected,
            boolean focus, int row, int column) {

        panel.setBackground(selected
                ? table.getSelectionBackground()
                : table.getBackground());

        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean selected,
            int row, int column) {

        this.baris = row;
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
