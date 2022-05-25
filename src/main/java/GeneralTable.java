/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.*;

public class GeneralTable implements Table {
    static final String properName = "GENERAL";
    static final String webURL = "https://pokemondb.net/pokedex/all";
    static final String tSQL = "CREATE TABLE " + properName +
            "(Id INTEGER not null, " +
            "Name VARCHAR(35) not null, " +
            "Type1 VARCHAR(10) not null, " +
            "Type2 VARCHAR(10) not null, " +
            "Total INTEGER not null, " +
            "HP INTEGER not null, " +
            "Attack INTEGER not null, " +
            "Defense INTEGER not null, " +
            "SpAtk INTEGER not null, " +
            "SpDef INTEGER not null, " +
            "Speed INTEGER not null, " +
            "PRIMARY KEY (Id, Name))";

    public static String getName (String name, String subName) {
        name = Table.handleApostraphe(name);
        subName = Table.handleApostraphe(subName);
        if (subName.contains(name))
            return subName;
        else if (!subName.equals(""))
            return subName + " " + name;
        return name;
    }

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
                if (row.text().charAt(0) != '#') {
                    int number = Integer.parseInt(row.select(".infocard-cell-data").text());
                    String name = getName(row.select(".ent-name").text(), row.select(".text-muted").text());
                    String type1 = row.select(".cell-icon").text();
                    int statTotal = Integer.parseInt(row.select(".cell-total").text());
                    int statHP = Integer.parseInt(row.select("td.cell-num:nth-of-type(5)").text());
                    int statA = Integer.parseInt(row.select("td.cell-num:nth-of-type(6)").text());
                    int statD = Integer.parseInt(row.select("td.cell-num:nth-of-type(7)").text());
                    int statSpA = Integer.parseInt(row.select("td.cell-num:nth-of-type(8)").text());
                    int statSpD = Integer.parseInt(row.select("td.cell-num:nth-of-type(9)").text());
                    int statS = Integer.parseInt(row.select("td.cell-num:nth-of-type(10)").text());

                    String type2 = "";
                    if (type1.contains(" ")) {
                        int spaceIndex = type1.indexOf(' ');
                        type2 = type1.substring(spaceIndex+1);
                        type1 = type1.substring(0, spaceIndex);
                    }
                    String query = "INSERT INTO " + properName + " (Id, Name, Type1, Type2, Total, HP, Attack, Defense, SpAtk, SpDef, Speed) " +
                            "VALUES (" + number + ", \'" + name + "\', \'" + type1 + "\', \'" + type2 + "\', " + statTotal + ", " + statHP + ", " + statA +
                            ", " + statD + ", " + statSpA + ", " + statSpD + ", " + statS + ");";
                    st.executeUpdate(query);
                }
            }
            if (st != null) {
                conn.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
