
package pengaturantabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public final class WarnaTabel {

    private WarnaTabel() {
    }

    public static void atur(
            JTable tabel,
            Color warnaHeader,
            Color warnaIsi,
            Color warnaHover,
            Color warnaDipilih) {

        tabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabel.setRowHeight(28);
        tabel.setForeground(warnaIsi);
        tabel.setBackground(warnaIsi);
        tabel.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);

        tabel.setShowGrid(true);
        tabel.setGridColor(new Color(170, 190, 220));
        tabel.setIntercellSpacing(
                new java.awt.Dimension(1, 1));

        JTableHeader header = tabel.getTableHeader();

        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(
                new java.awt.Dimension(0, 32));

        header.setReorderingAllowed(false);

        header.setDefaultRenderer(
                new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean selected,
                    boolean focused,
                    int row,
                    int column) {

                setText(value == null ? "" : value.toString());

                setBackground(warnaHeader);
                setForeground(warnaTeks(warnaHeader));

                setFont(new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14));

                setHorizontalAlignment(
                        javax.swing.SwingConstants.CENTER);

                setOpaque(true);

                return this;
            }
        });

        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean selected,
                    boolean focused,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(
                        table,
                        value,
                        selected,
                        focused,
                        row,
                        column);

                Color warnaLatar = warnaIsi;

                if (selected) {
                    warnaLatar = warnaDipilih;

                } else if (row == barisHover) {
                    warnaLatar = warnaHover;
                }

                setBackground(warnaLatar);
                setForeground(warnaTeks(warnaLatar));

                setFont(new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14));

                setOpaque(true);

                return this;
            }
        };

        for (int i = 0; i < tabel.getColumnCount(); i++) {

            tabel.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderer);
        }

        final int[] hover = {-1};

        tabel.addMouseMotionListener(
                new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int baris = tabel.rowAtPoint(e.getPoint());

                if (hover[0] != baris) {
                    hover[0] = baris;
                    barisHover = baris;
                    tabel.repaint();
                }
            }
        });

        tabel.addMouseListener(
                new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {

                hover[0] = -1;
                barisHover = -1;
                tabel.repaint();
            }
        });

        barisHover = -1;
    }

    private static int barisHover = -1;

    private static Color warnaTeks(Color warna) {

        double kecerahan =
                (0.299 * warna.getRed())
                + (0.587 * warna.getGreen())
                + (0.114 * warna.getBlue());

        return kecerahan >= 150
                ? Color.BLACK
                : Color.WHITE;
    }
}
