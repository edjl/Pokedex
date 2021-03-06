/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.*;

public class TypeTable extends Table {
    static final String properName = "TYPE";
    static final String webURL = "https://bulbapedia.bulbagarden.net/wiki/Type";
    static final String tSQL = "CREATE TABLE " + properName +
            "(Name VARCHAR(10) not null, " +
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
            int typeNum = 0;

            for (Element row : document.select("table.roundy tr")) {
                if (row.getElementsByTag("th").size() >= 18) {
                    for (Element col : row.getElementsByTag("th")) {
                        String type = col.getElementsByTag("a").attr("title");
                        String query = "ALTER TABLE TYPE " + "ADD " + type + " VARCHAR(2);";
                        st.executeUpdate(query);
                        typeNum++;
                    }
                }
                else if (row.select("td:nth-of-type(1)").text().contains("×")) {
                    String name = row.select("th:nth-of-type(1)").select("a:nth-of-type(1)").attr("title");
                    if (row.select("th:nth-of-type(1)").text().contains("Attacking type"))
                        name = row.select("th:nth-of-type(2)").select("a:nth-of-type(1)").attr("title");

                    String matchup = "";
                    for (int i = 1; i <= typeNum; i++) {
                        matchup += "\'" + row.select("td:nth-of-type(" + i + ")").text() + "\'";
                        if (i < typeNum)
                            matchup += ", ";
                    }
                    String query = "INSERT INTO " + properName + " VALUES " +
                            "(\'" + name + "\', " + matchup + ");";
                    st.executeUpdate(query);
                }
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean validPokemonType(String type) {
        Connection conn = null;
        Statement st = null;

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + type + "\';";
            ResultSet rs = st.executeQuery(query);
            boolean valid = rs.next();
            conn.close();
            return valid;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static double getMultiplier(String atkType, Pokemon pok) {
        Connection conn = null;
        Statement st = null;
        double multiplier = 1.0;

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + atkType + "\';";
            ResultSet rs = st.executeQuery(query);

            rs.next();
            String type1 = pok.getType();
            if (type1.contains(" ")) {
                String type2 = type1.substring(type1.indexOf(' ') + 1);
                type1 = type1.substring(0, type1.indexOf(' '));
                if (rs.getString(type2).equals("½×"))
                    multiplier *= 0.5;
                else if (rs.getString(type2).equals("2×"))
                    multiplier *= 2;
            }

            if (rs.getString(type1).equals("½×"))
                multiplier *= 0.5;
            else if (rs.getString(type1).equals("2×"))
                multiplier *= 2;
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return multiplier;
    }
}
