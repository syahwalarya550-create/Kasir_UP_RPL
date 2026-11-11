package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public final class Koneksi {

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "kasir_up_rpl";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static final String URL =
            "jdbc:mariadb://" + HOST + ":" + PORT
            + "/" + DATABASE + "?sslMode=disable";

    private Koneksi() {
    }

    public static Connection getKoneksi() throws SQLException {

        try {
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver MariaDB tidak ditemukan. "
                    + "Periksa Libraries NetBeans.",
                    e
            );
        }

        try {
            return DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

        } catch (SQLException e) {
            throw new SQLException(
                    "Gagal menghubungkan database.\n"
                    + "Database: " + DATABASE + "\n"
                    + "Host: " + HOST + "\n"
                    + "Port: " + PORT + "\n"
                    + "Pesan: " + e.getMessage(),
                    e
            );
        }
    }

    public static boolean cekKoneksi() {

        try (Connection connection = getKoneksi()) {
            return connection != null
                    && !connection.isClosed();

        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean cekDatabase() {

        try (Connection connection = getKoneksi()) {
            return DATABASE.equals(
                    connection.getCatalog()
            );

        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean cekTabel(String namaTabel) {

        if (namaTabel == null
                || namaTabel.trim().isEmpty()) {
            return false;
        }

        try (Connection connection = getKoneksi()) {

            DatabaseMetaData metadata =
                    connection.getMetaData();

            try (ResultSet result = metadata.getTables(
                    DATABASE,
                    null,
                    namaTabel,
                    new String[]{"TABLE"})) {

                return result.next();
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public static String informasiKoneksi() {

        try (Connection connection = getKoneksi()) {

            DatabaseMetaData metadata =
                    connection.getMetaData();

            return "Koneksi database berhasil!\n\n"
                    + "Database: " + DATABASE + "\n"
                    + "DBMS: "
                    + metadata.getDatabaseProductName() + "\n"
                    + "Versi DBMS: "
                    + metadata.getDatabaseProductVersion() + "\n"
                    + "Driver: "
                    + metadata.getDriverName() + "\n"
                    + "Versi Driver: "
                    + metadata.getDriverVersion();

        } catch (SQLException e) {

            return "Koneksi database gagal!\n"
                    + e.getMessage();
        }
    }

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("       KASIR UP RPL");
        System.out.println("   PENGUJIAN DATABASE");
        System.out.println("================================");

        if (cekKoneksi()) {

            System.out.println("Status: TERHUBUNG");
            System.out.println();

            System.out.println(informasiKoneksi());
            System.out.println();

            System.out.println(
                    "Database: "
                    + (cekDatabase() ? "BERHASIL" : "GAGAL")
            );

            System.out.println(
                    "Tabel layanan: "
                    + (cekTabel("layanan")
                    ? "DITEMUKAN" : "TIDAK DITEMUKAN")
            );

            System.out.println(
                    "Tabel pesanan: "
                    + (cekTabel("pesanan")
                    ? "DITEMUKAN" : "TIDAK DITEMUKAN")
            );

            System.out.println(
                    "Tabel detail_pesanan: "
                    + (cekTabel("detail_pesanan")
                    ? "DITEMUKAN" : "TIDAK DITEMUKAN")
            );

        } else {

            System.out.println("Status: TIDAK TERHUBUNG");
            System.out.println();
            System.out.println(informasiKoneksi());
        }

        System.out.println("================================");
    }
}