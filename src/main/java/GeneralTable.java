/*
 * Edward Lee
 * May 23, 2022
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.sql.*;
import java.util.ArrayList;

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
            "PRIMARY KEY (Name))";

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
                            "VALUES (" + number + ", \'" + name + "\', \'" + type1 + "\', \'" + type2 + "\', " + statTotal + ", " + statHP + ", " +
                            statA + ", " + statD + ", " + statSpA + ", " + statSpD + ", " + statS + ");";
                    st.executeUpdate(query);
                }
            }
            conn.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String []getGeneralInfoByName(String pokemon) {
        Connection conn = null;
        Statement st = null;
        String []pok = new String[11];

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Name = \'" + pokemon + "\';";
            ResultSet rs = st.executeQuery(query);

            rs.next();
            pok[0] = "" + rs.getInt("Id");
            pok[1] = "" + rs.getString("Name");
            pok[2] = "" + rs.getString("Type1");
            pok[3] = "" + rs.getString("Type2");
            pok[4] = "" + rs.getInt("Total");
            pok[5] = "" + rs.getInt("HP");
            pok[6] = "" + rs.getInt("Attack");
            pok[7] = "" + rs.getInt("Defense");
            pok[8] = "" + rs.getInt("SpAtk");
            pok[9] = "" + rs.getInt("SpDef");
            pok[10] = "" + rs.getInt("Speed");
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return pok;
    }

    public static boolean validPokemonName(String name) {
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

    public static void printGeneralInfo(String pokemon) {
        String []info = GeneralTable.getGeneralInfoByName(pokemon);
        System.out.print("Pokemon:\t" + info[0] + "\t  " + info[1].toUpperCase() + "    \t" + info[2]);
        System.out.println((info[3].equals("") ? "" : "-" + info[3]));
        System.out.print("\tStats:\t" + info[4] + "\t  ");
        for (int i = 5; i < info.length; i++)
            System.out.print(info[i] + "  ");
        System.out.println();
    }

    public static int []sortByIdNum(ArrayList<String[]> pokemonList) {
        int []order = new int[pokemonList.size()];
        ArrayList<ArrayList<Integer>> digit = new ArrayList<ArrayList<Integer>>();
        int count = 0;

        for (int i = 0; i < order.length; i++)
            order[i] = i;
        for (int i = 0; i < 10; i++) {
            ArrayList<Integer> list = new ArrayList<Integer>();
            digit.add(list);
        }

        for (int i = 0; i < 4; i++) {
            count = 0;
            for (int j = 0; j < order.length; j++) {
                String idNum = pokemonList.get(order[j])[0];
                int placement = 0;
                if (idNum.length() > i)
                    placement = (int)(idNum.charAt(idNum.length()-i-1)) - 48;
                digit.get(placement).add(order[j]);
            }
            for (int j = 0; j < digit.size(); j++) {
                for (int k = 0; k < digit.get(j).size(); k++) {
                    order[count++] = digit.get(j).get(k);
                }
                digit.get(j).clear();
            }
        }

        for(int i = order.length - 1; i > 0; i--) {
            if ((pokemonList.get(order[i-1])[2]).contains(pokemonList.get(order[i])[2])) {
                order[i] += order[i-1];
                order[i-1] = order[i] - order[i-1];
                order[i] -= order[i-1];
            }
        }

        return order;
    }

    public static void printPokemonByType(String type) {
        Connection conn = null;
        Statement st = null;
        ArrayList<String[]> pokemonList = new ArrayList<String[]>();

        try {
            conn = DriverManager.getConnection(database, user, pass);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);

            String query = "SELECT * FROM " + properName + " WHERE Type1 = \'" + type + "\' OR Type2 = \'" + type + "\';";
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String []pokemon = new String[5];
                pokemon[0] = rs.getString("Id");
                pokemon[1] = "" + rs.getString("Name").length();
                pokemon[2] = rs.getString("Name");
                pokemon[3] = rs.getString("Type1");
                pokemon[4] = rs.getString("Type2");
                pokemonList.add(pokemon);
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int []order = sortByIdNum(pokemonList);

        System.out.println("List of " + type + " Pokemon:");
        for (int i = 0; i < order.length; i++) {
            String []pokemon = pokemonList.get(order[i]);
            System.out.print("\t" + pokemon[0] + "\t" + pokemon[2]);
            for (int j = Integer.parseInt(pokemon[1]); j <= 35; j++)
                System.out.print(" ");
            System.out.print(pokemon[3]);
            for (int j = pokemon[3].length(); j <= 10; j++)
                System.out.print(" ");
            System.out.println(pokemon[4]);
        }
    }
}
