
package pengaturantabel;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.function.IntConsumer;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class TombolAksi extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor {

    private final JPanel panel;
    private final JButton btnEdit;
    private final JButton btnHapus;

    private final IntConsumer aksiEdit;
    private final IntConsumer aksiHapus;

    private int barisModel = -1;

    public TombolAksi(
            JTable tabel,
            IntConsumer aksiEdit,
            IntConsumer aksiHapus) {

        this.aksiEdit = aksiEdit;
        this.aksiHapus = aksiHapus;

        panel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 5, 5)
        );

        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");

        aturTombol();

        panel.add(btnEdit);
        panel.add(btnHapus);

        btnEdit.addActionListener(e -> {
            int baris = barisModel;

            fireEditingStopped();

            if (baris >= 0) {
                this.aksiEdit.accept(baris);
            }
        });

        btnHapus.addActionListener(e -> {
            int baris = barisModel;

            fireEditingStopped();

            if (baris >= 0) {
                this.aksiHapus.accept(baris);
            }
        });
    }

  private void aturTombol() {
    btnEdit.setBackground(new Color(51, 102, 255));
    btnEdit.setForeground(Color.WHITE);
    btnEdit.setFocusPainted(false);
    btnEdit.setBorderPainted(false);
    btnEdit.setOpaque(true);

    btnHapus.setBackground(new Color(220, 53, 69));
    btnHapus.setForeground(Color.WHITE);
    btnHapus.setFocusPainted(false);
    btnHapus.setBorderPainted(false);
    btnHapus.setOpaque(true);

    // Ukuran tombol
    btnEdit.setPreferredSize(new Dimension(70, 30));
    btnHapus.setPreferredSize(new Dimension(80, 30));

    // Margin tulisan
    btnEdit.setMargin(new Insets(2, 5, 2, 5));
    btnHapus.setMargin(new Insets(2, 5, 2, 5));
}

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean selected,
            boolean focus,
            int row,
            int column) {

        panel.setBackground(
            selected
                ? table.getSelectionBackground()
                : Color.WHITE
        );

        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean selected,
            int row,
            int column) {

        barisModel = table.convertRowIndexToModel(row);

        panel.setBackground(Color.WHITE);

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
