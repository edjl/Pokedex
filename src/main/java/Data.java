
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.sql.*;

public class Data {
    static final String Driver = "com.mysql.jdbc.Driver";
    static final String user = "root";
    static final String pass = "Cindylee@2";
    static final String []tSQL = {"CREATE TABLE GENERAL" +
                "(Id INTEGER not null, " +
                "Name VARCHAR(20) not null, " +
                "Type VARCHAR(20) not null, " +
                "Total INTEGER not null, " +
                "HP INTEGER not null, " +
                "Attack INTEGER not null, " +
                "Defense INTEGER not null, " +
                "SpAtk INTEGER not null, " +
                "SpDef INTEGER not null, " +
                "Speed INTEGER not null, " +
                "PRIMARY KEY (Id, Name))",
            "CREATE TABLE MOVES" +
            "(Name VARCHAR(30) not null, " +
            "Type VARCHAR(10) not null, " +
            "Category Varchar(10) not null, " +
            "Damage INTEGER not null, " +
            "PP INTEGER not null, " +
            "Accuracy INTEGER not null, " +
            "PRIMARY KEY (Name, Category))",
            "",
            "CREATE TABLE NATURE" +
            "(Name VARCHAR(10) not null, " +
            "IncStat VARCHAR(10) not null, " +
            "DecStat VARCHAR(10) not null, " +
            "PRIMARY KEY (Name))",
            ""};
    static final String []webURL = {"https://pokemondb.net/pokedex/all",
            "https://bulbapedia.bulbagarden.net/wiki/List_of_moves",
            "https://bulbapedia.bulbagarden.net/wiki/Ability",
            "https://bulbapedia.bulbagarden.net/wiki/Nature",
            "https://bulbapedia.bulbagarden.net/wiki/Status_condition"};

    private static String properName(String tableName){
        if (tableName.equals("g"))
            return "GENERAL";
        if (tableName.equals("m"))
            return "MOVES";
        if (tableName.equals("a"))
            return "ABILITY";
        if (tableName.equals("n"))
            return "NATURE";
        return "STATUS CONDITION";
    }
    private static String sqlTableFormat(String tableName) {
        if (tableName.equals("g"))
            return tSQL[0];
        if (tableName.equals("m"))
            return tSQL[1];
        if (tableName.equals("a"))
            return tSQL[2];
        if (tableName.equals("n"))
            return tSQL[3];
        return tSQL[4];
    }
    private static String websiteURL(String tableName) {
        if (tableName.equals("g"))
            return webURL[0];
        if (tableName.equals("m"))
            return webURL[1];
        if (tableName.equals("a"))
            return webURL[2];
        if (tableName.equals("n"))
            return webURL[3];
        return webURL[4];
    }

    public static void updateTable(String tableName) {
        Connection conn = null;
        Statement st = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/POKEMON", user, pass);
            st = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, properName(tableName), null);
            String sql;
            if (tables.next()) {
                sql = "DROP TABLE " + properName(tableName) + ";";
                st.executeUpdate(sql);
            }

            sql = sqlTableFormat(tableName);
            st.executeUpdate(sql);
            final Document document = Jsoup.connect(websiteURL(tableName)).get();

            if (tableName.equals("g")) {
                for (Element row : document.select("table.data-table.block-wide tr")) {
                    if (row.text().charAt(0) != '#') {
                        int number = Integer.parseInt(row.select(".infocard-cell-data").text());
                        String name = row.select(".ent-name").text();
                        if (!row.select(".text-muted").text().equals("") && !row.select(".text-muted").text().equals("Male") && !row.select(".text-muted").text().equals("Female")) {
                            name = row.select(".text-muted").text();
                        }
                        name = name.replaceAll("\\s|'", "");
                        String type = row.select(".cell-icon").text().replaceAll("\\s", "");
                        int statTotal = Integer.parseInt(row.select(".cell-total").text());
                        int statHP = Integer.parseInt(row.select("td.cell-num:nth-of-type(5)").text());
                        int statA = Integer.parseInt(row.select("td.cell-num:nth-of-type(6)").text());
                        int statD = Integer.parseInt(row.select("td.cell-num:nth-of-type(7)").text());
                        int statSpA = Integer.parseInt(row.select("td.cell-num:nth-of-type(8)").text());
                        int statSpD = Integer.parseInt(row.select("td.cell-num:nth-of-type(9)").text());
                        int statS = Integer.parseInt(row.select("td.cell-num:nth-of-type(10)").text());

                        if (!row.select(".text-muted").text().equals("Female")) {
                            String query = "INSERT INTO GENERAL (Id, Name, Type, Total, HP, Attack, Defense, SpAtk, SpDef, Speed) " +
                                    "VALUES (" + number + ", \'" + name + "\', \'" + type + "\', " + statTotal + ", " + statHP + ", " + statA +
                                    ", " + statD + ", " + statSpA + ", " + statSpD + ", " + statS + ");";
                            st.executeUpdate(query);
                        }
                    }
                }
            } else if (tableName.equals("m")) {
                for (Element row : document.select("table.roundy.sortable tr")) {
                    if (row.text().equals("1 G-Max Wildfire Fire"))
                        break;
                    else if(!row.text().contains("Name")) {
                        String name = row.select("td:nth-of-type(2)").text();
                        name = name.replaceAll("\\s|'", "");
                        String type = row.select("td:nth-of-type(3)").text();
                        String category = row.select("td:nth-of-type(4)").text();
                        String dam = row.select("td:nth-of-type(6)").text().contains("—") ? "0" : row.select("td:nth-of-type(6)").text();
                        String pip = row.select("td:nth-of-type(5)").text().contains("—") ? "0" : row.select("td:nth-of-type(5)").text();
                        String acc = row.select("td:nth-of-type(7)").text().contains("—") ? "0%" : row.select("td:nth-of-type(7)").text();
                        if (dam.endsWith("*"))
                            dam = dam.substring(0, dam.length() - 2);
                        if (pip.endsWith("*"))
                            pip = pip.substring(0, pip.length() - 1);
                        if (acc.endsWith("*")) {
                            acc = acc.substring(0, acc.length() - 2);
                        } else {
                            acc = acc.substring(0, acc.length() - 1);
                        }
                        int damage = Integer.parseInt(dam);
                        int pp = Integer.parseInt(pip);
                        int accuracy = Integer.parseInt(acc);

                        String query = "INSERT INTO MOVES (Name, Type, Category, Damage, PP, Accuracy) " +
                                "VALUES (\'" + name + "\', \'" + type + "\', \'" + category + "\', \'" + damage + "\', \'" + pp + "\', \'" + accuracy + "\');";
                        st.executeUpdate(query);
                    }
                }
            }else if (tableName.equals("n")) {
                for (Element row : document.select("table.roundy.sortable tr")) {
                    String name = row.select("th:nth-of-type(1)").text();
                    String inc = row.select("td:nth-of-type(3)").text();
                    String dec = row.select("td:nth-of-type(4)").text();
                    inc = inc.replaceAll("\\s", "");
                    dec = dec.replaceAll("\\s", "");
                    if (name.length() == 0 && !row.text().contains("Nature"))
                        break;

                    String query = "INSERT INTO NATURE (Name, IncStat, DecStat) VALUES (\'" + name + "\', \'" + inc + "\', \'" + dec + "\');";
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

    public static String getGeneralInfo(String name, String col) {
        Connection conn = null;
        PreparedStatement st = null;
        String line = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/POKEMON", user, pass);

            String query = "SELECT " + col + " FROM GENERAL WHERE name=\'"+ name +"\';";
            st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                line = rs.getString(col);
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
        return line;
    }

    public static String getMoveInfo(String name, String col) {
        Connection conn = null;
        PreparedStatement st = null;
        String line = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/POKEMON", user, pass);

            String query = "SELECT " + col + " FROM MOVES WHERE name=\'"+ name +"\';";
            st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                line = rs.getString(col);
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
        return line;
    }
}
