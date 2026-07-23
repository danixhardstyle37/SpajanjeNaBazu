package org.example.statements;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class PozivanjeProcedure {

    public static void main(String[] args) {

        String imeKupca = dohvatiImeKupca(3);
        System.out.println("Ime: " + imeKupca);

    }

    public static String dohvatiImeKupca(int id) {

        try (Connection connection = createDataSource().getConnection()) {
            System.out.println("Spojeni na bazu!");

            CallableStatement cs = connection.prepareCall("{CALL DohvatiImeKupca(?,?)}");
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.NVARCHAR);
            cs.executeUpdate();

            return cs.getString(2);


        } catch (SQLException e) {
            System.err.println("Greška pri spajanju na bazu");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
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
