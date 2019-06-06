package be.marche.duobac;

import java.sql.SQLException;

public class LaunchClass {

    public static void main(String[] arg) {

        CSVReader reader = new CSVReader();
        DbHelper dbHelper = new DbHelper();
        dbHelper.connect();

        //   for(int year = 2000; year < 2006; year++){
        int year = 20222;
        String csvFile = "/var/www/eid/data/relevés " + year + ".txt";
        reader.open(csvFile, dbHelper, year);
        //   }

        try {
            dbHelper.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
