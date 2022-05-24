/*
 * Edward Lee
 * May 23, 2022
 */

public interface Table {
    static final String Driver = "com.mysql.jdbc.Driver";
    static final String database = "jdbc:mysql://localhost/POKEMON";
    static final String user = "root";
    static final String pass = "";
    public static void updateTable() {}
    public static String handleApostraphe(String line) {
        int index = 0;
        while(line.substring(index).contains("'")) {
            index += line.substring(index).indexOf('\'');
            line = line.substring(0, index) + "'" + line.substring(index);
            index += 2;
        }
        return line;
    }
}
