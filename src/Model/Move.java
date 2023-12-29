/*
 * Edward Lee
 * December 21, 2023
 */

package src.Model;
import java.sql.*;

public class Move {
    
    String name;
    Type type;
    MoveCategory category;
    int power, accuracy;

    public Move(String name) {
        this.name = name;
        updateUsingName();
    }

    private void updateUsingName() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:databases/pokemon.db");
            String query = "SELECT * FROM moves WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                type = Type.valueOf(rs.getString("type"));
                category = MoveCategory.valueOf(rs.getString("category"));
                power = rs.getInt("power");
                if (rs.wasNull())
                    power = 0;
                accuracy = rs.getInt("accuracy");
                if (rs.wasNull())
                    accuracy = 0;
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
        updateUsingName();
    }
    public Type getType() {
        return type;
    }
    public MoveCategory getCategory() {
        return category;
    }
    public int getPower() {
        return power;
    }
    public int getAccuracy() {
        return accuracy;
    }
}
