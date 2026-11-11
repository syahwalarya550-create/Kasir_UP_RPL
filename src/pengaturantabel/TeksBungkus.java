
package pengaturantabel;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class TeksBungkus extends JTextArea
        implements TableCellRenderer {

    public TeksBungkus() {

        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(
                4, 5, 4, 5));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable tabel,
            Object nilai,
            boolean dipilih,
            boolean fokus,
            int baris,
            int kolom) {

        setText(nilai == null ? "" : nilai.toString());

        if (dipilih) {
            setBackground(tabel.getSelectionBackground());
            setForeground(tabel.getSelectionForeground());
        } else {
            setBackground(tabel.getBackground());
            setForeground(tabel.getForeground());
        }

        setFont(tabel.getFont());

        return this;
    }
}
