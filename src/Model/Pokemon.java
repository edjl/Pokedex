/*
 * Edward Lee
 * December 21, 2023
 */

package src.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pokemon {

    String name;
    int level;
    Type []typing = new Type[2];
    int []STATS = {0, 0, 0, 0, 0, 0};
    double []ability = {1, 1, 1, 1, 1, 1};
    double []nature = {1, 1, 1, 1, 1, 1};
    int []stats = {0, 0, 0, 0, 0, 0};
    int []ev = {0, 0, 0, 0, 0, 0};
    int []iv = {0, 0, 0, 0, 0, 0};
    
    public Pokemon(String name, int level) {
        this.name = name;
        this.level = level;
        setAttr();
    }

    public void randValues() {
        stats[0] = (int)(Math.floor(0.01 * (2 * STATS[0] + iv[0] + Math.floor(0.25 * ev[0])) * level) + level + 10);
        for (int i = 1; i < stats.length; i++) {
            stats[i] = (int)(Math.floor(0.01 * (2 * STATS[i] + iv[i] + Math.floor(0.25 * ev[i])) * level) * nature[i]);
        }
    }

    // Set Type and STATS
    private void setAttr() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:databases/pokemon.db");
            String query = "SELECT * FROM pokemon WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                typing[0] = Type.valueOf(rs.getString("type1"));
                String type2 = rs.getString("type2");
                typing[1] = null;
                if (!rs.wasNull())
                    typing[1] = Type.valueOf(type2);
                    
                STATS[0] = rs.getInt("hp");
                STATS[1] = rs.getInt("attack");
                STATS[2] = rs.getInt("defense");
                STATS[3] = rs.getInt("spatk");
                STATS[4] = rs.getInt("spdef");
                STATS[5] = rs.getInt("speed");
            }
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // Set Nature
    public void setNature(String selected) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:databases/pokemon.db");
            String query = "SELECT * FROM nature WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selected);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                nature[0] = rs.getDouble("hp");
                nature[1] = rs.getDouble("attack");
                nature[2] = rs.getDouble("defense");
                nature[3] = rs.getDouble("spatk");
                nature[4] = rs.getDouble("spdef");
                nature[5] = rs.getDouble("speed");
            }
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void setName(String name) {
        this.name = name;
        setAttr();
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getName() {
        return name;
    }
    public int getLevel() {
        return level;
    }
    public int []getStats() {
        return stats;
    }
    public boolean isType(Type type) {
        return type == typing[0] || type == typing[0];
    }
    public double getTypeDamageMultiplier(Type type) {
        Connection connection = null;
        double damage = 1;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:databases/pokemon.db");
            String query = "SELECT * FROM types WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                damage *= rs.getFloat(typing[0].toString().toLowerCase());
                if (typing[1] != null)
                    damage *= rs.getFloat(typing[1].toString().toLowerCase());
            }
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                connection.close();
            }
            catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return damage;
    }
}
