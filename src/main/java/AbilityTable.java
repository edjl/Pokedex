/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.sql.*;

public class AbilityTable implements Table {
    static final String properName = "ABILITY";
    static final String webURL = "https://pokemondb.net/ability";
    static final String tSQL = "CREATE TABLE " + properName +
            "(Name VARCHAR(20) not null, " +
            "Description VARCHAR(140) not null, " +
            "PRIMARY KEY (Name))";

    public static void updateTable() {
        Connection conn = null;
        Statement st = null;

        try {
            Class.forName(Driver);
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

            for (Element row : document.select("table.data-table.block-wide tr")) {
                String name = row.select("td:nth-of-type(1)").text();
                String des = row.select("td:nth-of-type(3)").text();
                name = Table.handleApostraphe(name);
                des = Table.handleApostraphe(des);

                if (!name.equals("Name") && !name.equals("")) {
                    String query = "INSERT INTO " + properName + " (Name, Description) VALUES " +
                            "(\'" + name + "\', \'" + des + "\');";
                    st.executeUpdate(query);
                }
            }
            if (st != null) {
                conn.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}