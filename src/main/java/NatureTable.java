/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.sql.*;

public class NatureTable extends Table {
    static final String properName = "NATURE";
    static final String webURL = "https://bulbapedia.bulbagarden.net/wiki/Nature";
    static final String tSQL = "CREATE TABLE " + properName +
            "(Name VARCHAR(10) not null, " +
            "IncStat VARCHAR(20) not null, " +
            "DecStat VARCHAR(20) not null, " +
            "FavFlavor VARCHAR(10) not null, " +
            "DisFlavor VARCHAR(10) not null, " +
            "PRIMARY KEY (Name))";

    public static void updateTable() {
        Connection conn = null;
        Statement st = null;

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, properName, null);
            String sql;
            if (tables.next()) {
                sql = "DROP TABLE " + properName + ";";
                st.executeUpdate(sql);
            }

            sql = tSQL;
            st.executeUpdate(sql);
            final Document document = Jsoup.connect(webURL).get();

            for (Element row : document.select("table.roundy.sortable tr")) {
                String name = row.select("th:nth-of-type(1)").text();
                String inc = row.select("td:nth-of-type(3)").text();
                String dec = row.select("td:nth-of-type(4)").text();
                String fFlavor = row.select("td:nth-of-type(5)").text();
                String dFlavor = row.select("td:nth-of-type(6)").text();
                if (name.length() == 0 && !row.text().contains("Nature"))
                    break;

                if (!name.equals("#")) {
                    String query = "INSERT INTO " + properName + " (Name, IncStat, DecStat, FavFlavor, DisFlavor) VALUES " +
                            "(\'" + name + "\', \'" + inc + "\', \'" + dec + "\', \'" + fFlavor + "\', \'" + dFlavor + "\');";
                    st.executeUpdate(query);
                }
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static double getMultiplier (String nature, String stat) {
        Connection conn = null;
        Statement st = null;
        String incStat = "";
        String decStat = "";

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + nature + "\';";
            ResultSet rs = st.executeQuery(query);

            rs.next();
            incStat = rs.getString("IncStat");
            decStat = rs.getString("DecStat");
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (stat.equals(incStat))
            return 1.1;
        else if (stat.equals(decStat))
            return 0.9;
        return 1.0;
    }

    public static boolean validPokemonNature(String nature) {
        Connection conn = null;
        Statement st = null;

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + nature + "\';";
            ResultSet rs = st.executeQuery(query);
            boolean valid = rs.next();
            conn.close();
            return valid;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}