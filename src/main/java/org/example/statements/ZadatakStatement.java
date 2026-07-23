package org.example.statements;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ZadatakStatement {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int izbor;

        do {
            System.out.println("1 - Unesi 5 drzava");
            System.out.println("2 - Obrisi drzave");
            System.out.println("3 - Izlaz");
            System.out.print("Odabir: ");

            izbor = sc.nextInt();

            switch (izbor) {

                case 1:
                    unesiDrzave();
                    break;

                case 2:
                    prikaziDrzave();

                    System.out.print("Unesite ID od kojeg zelite obrisati drzave: ");
                    int id = sc.nextInt();

                    obrisiDrzave(id);
                    break;

                case 3:
                    System.out.println("Kraj programa.");
                    break;

                default:
                    System.out.println("Pogresan odabir");
            }

        } while (izbor != 3);
    }

    public static void unesiDrzave() {

        ArrayList<Drzava> drzave = new ArrayList<>();

        drzave.add(new Drzava("Hrvatska"));
        drzave.add(new Drzava("Norveska"));
        drzave.add(new Drzava("Svedska"));
        drzave.add(new Drzava("Finska"));
        drzave.add(new Drzava("Japan"));

        try (Connection connection = createDataSource().getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Drzava (Naziv) VALUES (?)");

            for (Drzava d : drzave) {
                stmt.setString(1, d.getNaziv());
                stmt.executeUpdate();
            }

            System.out.println("Drzave su unesene,");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void prikaziDrzave() {

        try (Connection connection = createDataSource().getConnection()) {

            PreparedStatement stmt = connection.prepareStatement("SELECT IDDrzava, Naziv FROM Drzava");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("IDDrzava") + " - " + rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void obrisiDrzave(int id) {

        try (Connection connection = createDataSource().getConnection()) {

            CallableStatement cs = connection.prepareCall("{CALL ObrisiDrzave(?)}");

            cs.setInt(1, id);
            cs.executeUpdate();

            System.out.println("Drzave su obrisane");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DataSource createDataSource() {

        MysqlDataSource ds = new MysqlDataSource();

        ds.setServerName("localhost");
        ds.setPortNumber(3307);
        ds.setUser("root");
        ds.setDatabaseName("adventureworksobp");

        return ds;
    }

}
