package org.example.statements;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Polaznik> popis = new ArrayList<>();
        popis.add(new Polaznik("Maja", "Majić"));
        popis.add(new Polaznik("Ivica", "Ivić"));
        popis.add(new Polaznik("Dodo", "Dodek"));
        popis.add(new Polaznik("Josip", "Josić"));
        popis.add(new Polaznik("Luka", "Lukić"));


        DataSource dataSource = createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Spojeni na bazu!");

            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO polaznik (ime, prezime) VALUES (?, ?)");
            for (Polaznik p : popis) {
                stmt.setString(1, p.getIme());
                stmt.setString(2, p.getPrezime());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Greška pri spajanju na bazu");
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