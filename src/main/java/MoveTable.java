/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.SocketOption;
import java.sql.*;

public class MoveTable implements Table {
    static final String properName = "MOVES";
    static final String webURL = "https://bulbapedia.bulbagarden.net/wiki/List_of_moves";
    static final String tSQL = "CREATE TABLE " + properName +
            "(Name VARCHAR(30) not null, " +
            "Type VARCHAR(10) not null, " +
            "Category Varchar(10) not null, " +
            "Damage Varchar(5) not null, " +
            "PP Varchar(5) not null, " +
            "Accuracy Varchar(5) not null, " +
            "PRIMARY KEY (Name, Category))";

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
                if (row.text().equals("1 G-Max Wildfire Fire"))
                    break;
                else if(!row.text().contains("Name")) {
                    String name = row.select("td:nth-of-type(2)").text();
                    name = Table.handleApostraphe(name);
                    String type = row.select("td:nth-of-type(3)").text();
                    String category = row.select("td:nth-of-type(4)").text();
                    String dam = row.select("td:nth-of-type(6)").text().contains("—") ? "0" : row.select("td:nth-of-type(6)").text();
                    String pp = row.select("td:nth-of-type(5)").text().contains("—") ? "0" : row.select("td:nth-of-type(5)").text();
                    String acc = row.select("td:nth-of-type(7)").text().contains("—") ? "0%" : row.select("td:nth-of-type(7)").text();

                    String query = "INSERT INTO " + properName + " (Name, Type, Category, Damage, PP, Accuracy) " +
                            "VALUES (\'" + name + "\', \'" + type + "\', \'" + category + "\', \'" + dam + "\', \'" + pp + "\', \'" + acc + "\');";
                    st.executeUpdate(query);
                }
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String []getMoveInfoByName(String move) {
        Connection conn = null;
        Statement st = null;
        String []mov = new String[6];

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + move + "\';";
            ResultSet rs = st.executeQuery(query);

            rs.next();
            mov[0] = "" + rs.getString("Name");
            mov[1] = "" + rs.getString("Type");
            mov[2] = "" + rs.getString("Category");
            mov[3] = "" + rs.getString("Damage");
            mov[4] = "" + rs.getString("PP");
            mov[5] = "" + rs.getString("Accuracy");
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mov;
    }

    public static boolean validPokemonMove(String name) {
        Connection conn = null;
        Statement st = null;

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + name + "\';";
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