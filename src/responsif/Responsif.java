
package responsif;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Responsif {

    private final JFrame frame;
    private final Map<Container, DataContainer> data = new IdentityHashMap<>();

    private Responsif(JFrame frame) {
        this.frame = frame;

        SwingUtilities.invokeLater(() -> {
            ubahLayout(frame.getContentPane());
            simpanData(frame.getContentPane());
            pasangListener(frame.getContentPane());

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.revalidate();
            frame.repaint();
        });
    }

    public static Responsif aktifkan(JFrame frame) {
        return new Responsif(frame);
    }

    private void ubahLayout(Container parent) {
        if (parent.getLayout() != null
                && parent.getLayout().getClass().getName()
                .equals("org.netbeans.lib.awtextra.AbsoluteLayout")) {
            parent.setLayout(null);
        }

        for (Component c : parent.getComponents()) {
            if (c instanceof Container) {
                ubahLayout((Container) c);
            }
        }
    }

    private void simpanData(Container parent) {
        // Hanya container dengan null layout yang diskalakan.
        if (parent.getLayout() == null) {
            Dimension awal = parent.getSize();

            if (awal.width <= 0 || awal.height <= 0) {
                awal = parent.getPreferredSize();
            }

            if (awal.width > 0 && awal.height > 0) {
                List<DataKomponen> daftar = new ArrayList<>();

                for (Component c : parent.getComponents()) {
                    daftar.add(new DataKomponen(c, c.getBounds()));
                }

                data.put(parent, new DataContainer(awal, daftar));
            }
        }

        for (Component c : parent.getComponents()) {
            if (c instanceof Container) {
                simpanData((Container) c);
            }
        }
    }

    private void pasangListener(Container parent) {
        if (parent.getLayout() == null && data.containsKey(parent)) {
            parent.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    SwingUtilities.invokeLater(() -> sesuaikan(parent));
                }
            });
        }

        for (Component c : parent.getComponents()) {
            if (c instanceof Container) {
                pasangListener((Container) c);
            }
        }
    }

    private void sesuaikan(Container parent) {
        DataContainer simpanan = data.get(parent);
        if (simpanan == null) return;

        Dimension sekarang = parent.getSize();
        Dimension awal = simpanan.ukuranAwal;

        if (sekarang.width <= 0 || sekarang.height <= 0
                || awal.width <= 0 || awal.height <= 0) {
            return;
        }

        double sx = (double) sekarang.width / awal.width;
        double sy = (double) sekarang.height / awal.height;

        for (DataKomponen item : simpanan.daftar) {
            Component c = item.komponen;

            if (c.getParent() != parent) continue;

            Rectangle b = item.boundsAwal;

            c.setBounds(
                    (int) Math.round(b.x * sx),
                    (int) Math.round(b.y * sy),
                    Math.max(1, (int) Math.round(b.width * sx)),
                    Math.max(1, (int) Math.round(b.height * sy))
            );
        }

        parent.revalidate();
        parent.repaint();
    }

    private static class DataContainer {
        final Dimension ukuranAwal;
        final List<DataKomponen> daftar;

        DataContainer(Dimension ukuranAwal, List<DataKomponen> daftar) {
            this.ukuranAwal = new Dimension(ukuranAwal);
            this.daftar = daftar;
        }
    }

    private static class DataKomponen {
        final Component komponen;
        final Rectangle boundsAwal;

        DataKomponen(Component komponen, Rectangle bounds) {
            this.komponen = komponen;
            this.boundsAwal = new Rectangle(bounds);
        }
    }
}
