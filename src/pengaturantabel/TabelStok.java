
package pengaturantabel;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class TabelStok {

    public static void atur(JTable tabel) {

        tabel.setRowHeight(42);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setForeground(new Color(45, 45, 45));
        tabel.setBackground(Color.WHITE);
        tabel.setSelectionBackground(new Color(230, 220, 255));
        tabel.setSelectionForeground(Color.BLACK);
        tabel.setShowGrid(false);
        tabel.setIntercellSpacing(new Dimension(0, 0));
        tabel.setFillsViewportHeight(true);

        JTableHeader header = tabel.getTableHeader();
        header.setPreferredSize(new Dimension(0, 42));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(51, 0, 102));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer tengah =
                new DefaultTableCellRenderer();

        tengah.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabel.getColumnCount(); i++) {
            tabel.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(new RendererBaris());
        }

        tabel.setDefaultRenderer(Object.class, new RendererBaris());
    }

    static class RendererBaris extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable tabel, Object value, boolean selected,
                boolean focus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    tabel, value, selected, focus, row, column);

            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            if (selected) {
                c.setBackground(new Color(230, 220, 255));
            } else if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(245, 245, 250));
            }

            setForeground(new Color(45, 45, 45));

            return c;
        }
    }
}
