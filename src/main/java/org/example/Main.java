package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DataSource ds = createDataSource();
        Scanner sc = new Scanner(System.in);

        try (Connection connection = ds.getConnection(); Statement stmt = connection.createStatement()) {
            System.out.println("Uspjesno ste spojeni na bazu podataka!");

            int odabir;

            do {
                System.out.println("\n----- UPRAVLJANJE DRZAVAMA -----");
                System.out.println("1. Nova drzava");
                System.out.println("2. Izmjena postojece drzave");
                System.out.println("3. Brisanje postojece drzave");
                System.out.println("4. Prikaz svih drzava");
                System.out.println("5. Kraj");
                System.out.print("Odabir: ");

                odabir = sc.nextInt();
                sc.nextLine();

                switch (odabir) {

                    case 1:
                        System.out.print("Unesi naziv nove drzave: ");
                        String nazivNoveDrzave = sc.nextLine();

                        String insertSql = "INSERT INTO Drzava (Naziv) " + "VALUES ('" + nazivNoveDrzave + "')";

                        int brojUbacenihRedaka = stmt.executeUpdate(insertSql);

                        if (brojUbacenihRedaka > 0) {
                            System.out.println("Drzava je uspjesno dodana.");
                        } else {
                            System.out.println("Drzava nije dodana.");
                        }

                        break;

                    case 2:
                        System.out.print("Unesi ID drzave koju zelite izmijeniti: ");
                        int idZaIzmjenu = sc.nextInt();
                        sc.nextLine();

                        if (idZaIzmjenu <= 3) {
                            System.out.println("Ne smijete mijenjati drzave " + "ciji je ID manji ili jednak 3.");
                            break;
                        }

                        System.out.print("Unesi novi naziv drzave: ");
                        String noviNaziv = sc.nextLine();

                        String updateSql = "UPDATE Drzava " +
                                        "SET Naziv = '" + noviNaziv + "' " +
                                        "WHERE IDDrzava = " + idZaIzmjenu;

                        int brojPromijenjenihRedaka = stmt.executeUpdate(updateSql);

                        if (brojPromijenjenihRedaka > 0) {
                            System.out.println("Drzava je uspjesno izmijenjena.");
                        } else {
                            System.out.println("Drzava s unesenim ID-em ne postoji.");
                        }

                        break;

                    case 3:
                        System.out.print("Unesi ID drzave koju zelite obrisati: ");
                        int idZaBrisanje = sc.nextInt();
                        sc.nextLine();

                        if (idZaBrisanje <= 3) {
                            System.out.println("Ne smijete brisati drzave " + "ciji je ID manji ili jednak 3.");
                            break;
                        }

                        String deleteSql = "DELETE FROM Drzava " + "WHERE IDDrzava = " + idZaBrisanje;

                        int brojObrisanihRedaka = stmt.executeUpdate(deleteSql);

                        if (brojObrisanihRedaka > 0) {
                            System.out.println("Drzava je uspjesno obrisana.");
                        } else {
                            System.out.println("Drzava s unesenim ID-em ne postoji.");
                        }

                        break;

                    case 4:
                        System.out.println("\nPopis drzava sortiranih po nazivu:");

                        String selectSql = "SELECT IDDrzava, Naziv " +
                                            "FROM Drzava " +
                                            "ORDER BY Naziv ASC";

                        try (ResultSet rs = stmt.executeQuery(selectSql)) {
                            while (rs.next()) {
                                System.out.printf("%d - %s%n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
                            }
                        }

                        break;

                    case 5:
                        System.out.println("Program zavrsen");
                        break;

                    default:
                        System.out.println("Pogresan odabir");
                }

            } while (odabir != 5);

        } catch (SQLException e) {
            System.out.println("Dogodila se greska pri radu s bazom podataka.");
            e.printStackTrace();
        }

        sc.close();
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