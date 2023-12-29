/*
 * Edward Lee
 * December 21, 2023
 */

package src.View;

import javax.swing.*;

import src.Model.MoveCategory;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Window extends JFrame {

    int windowWidth = 1000;
    int windowHeight = 600;

    public Window(String title, CountDownLatch latch) {
        super(title);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(windowWidth, windowHeight);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                latch.countDown();
            }
        });
    }
    public void endOfConstructor() {
        setLocationRelativeTo(null);
        setVisible(true);
    }


    protected ImageIcon sizePokemonIcon(String pokemonName, int availWidth, int availHeight) {
        ImageIcon originalIcon = new ImageIcon("images/pokemon/" + pokemonName + ".png");
        double imageRatio = ((double)(originalIcon.getIconWidth())) / originalIcon.getIconHeight();
        int width = -1, height = -1;
        if (availWidth <= imageRatio * availHeight) {
            width = availWidth;
            height = (int)(availWidth / imageRatio);
        }
        else {
            width = (int)(availHeight * imageRatio);
            height = availHeight;
        }
        Image originalImage = originalIcon.getImage();

        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        return scaledIcon;
    }

    protected String[] getPokemonList() {
        String db = "pokemon.db";
        String table = "pokemon";
        String col = "name";
        String order = "id ASC, total ASC";
        return getList(db, table, col, order);
    }
    protected String[] getMovesList() {
        String db = "pokemon.db";
        String table = "moves";
        String col = "name";
        String order = "name ASC";
        return getList(db, table, col, order);
    }
    protected String[] getNatureList() {
        String db = "pokemon.db";
        String table = "nature";
        String col = "name";
        String order = "name ASC";
        return getList(db, table, col, order);
    }

    protected String[] getList(String db, String table, String col, String order) {
        String []list = {};
        ArrayList<String> temp_list = new ArrayList<>();

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:databases/" + db);
            String query = "SELECT " + col + " FROM " + table + " ORDER BY " + order;
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                temp_list.add(rs.getString("name"));
            }
            list = temp_list.toArray(new String[0]);
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
        return list;
    }
}
