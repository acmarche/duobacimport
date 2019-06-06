package be.marche.duobac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class DbHelper {

    Connection con = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Unable to load MySQL Driver");
        }
    }

    void connect() {

        System.out.println("Connecting database...");
        String jdbcUrl = "jdbc:mysql://localhost/eid?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        try {
            con = DriverManager.getConnection(jdbcUrl, "**", "**");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    int duobacExist(String matricule, String puce) {

        String query = "SELECT * FROM duobac WHERE rdv_matricule = ? AND puc_no_puce = ?";
        PreparedStatement preparedStmt;

        try {
            preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setString(1, matricule);
            preparedStmt.setString(2, puce);
            ResultSet result = preparedStmt.executeQuery();

            if (result.next()) {
                return result.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
