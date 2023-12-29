/*
 * Edward Lee
 * December 21, 2023
 */

package src.View;
import src.Simulation;
import src.Model.Move;
import src.Model.Pokemon;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.event.*;


public class SurvivabilityTestWindow extends Window {

    JComboBox<String> moveSelect;
    JComboBox<String> []natureSelect;
    JCheckBox burned, reflect, lightscreen;
    JCheckBox knowHP;
    JSpinner hpSelect;

    private JPanel createMainBottomPanel(boolean attacker) {
        JPanel bottomPanel = new CustomJPanel(new GridLayout(0, 1), 0, 0);

        JPanel naturePanel = new CustomJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel nature = new JLabel("Nature:");
        natureSelect[attacker ? 0 : 1] = new JComboBox<>(getNatureList());
        natureSelect[attacker ? 0 : 1].setSelectedIndex(0);
        naturePanel.add(nature);
        naturePanel.add(natureSelect[attacker ? 0 : 1]);
        bottomPanel.add(naturePanel);

        if (attacker) {
            JPanel attackPanel = new CustomJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            JLabel move = new JLabel("Attacking Move:");
            moveSelect = new JComboBox<>(getMovesList());
            moveSelect.setSelectedIndex(0);
            attackPanel.add(move);
            attackPanel.add(moveSelect);
            attackPanel.setPreferredSize(new Dimension(attackPanel.getPreferredSize().width, 27));
            bottomPanel.add(attackPanel);
        }
        else {
            JPanel hpPanel = new CustomJPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            knowHP = new JCheckBox("Know HP?");
            knowHP.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            knowHP.setSelected(true);
            knowHP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            JLabel hpLabel = new JLabel("HP:");
            hpLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            hpSelect = new JSpinner(new SpinnerNumberModel(20, 1, 1000, 1));
            hpPanel.add(knowHP);
            hpPanel.add(hpLabel);
            hpPanel.add(hpSelect);
            hpPanel.setPreferredSize(new Dimension(hpPanel.getPreferredSize().width, 27));
            bottomPanel.add(hpPanel);

            knowHP.addActionListener(e -> {
                if (knowHP.isSelected()) {
                    hpLabel.setText("HP Value:");
                    hpSelect.setModel(new SpinnerNumberModel(20, 1, 1000, 1));
                }
                else {
                    hpLabel.setText("HP %:");
                    hpSelect.setModel(new SpinnerNumberModel(100, 1, 100, 1));
                }
            });
        }

        return bottomPanel;
    }

    private JPanel createMainPanel(Pokemon pokemon, boolean attacker) {
        JLabel roleLabel = new JLabel((attacker) ? "Attacker:" : "Defender:");
        JComboBox<String> pokemonSelector = new JComboBox<>(getPokemonList());
        pokemonSelector.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        JLabel levelLabel = new JLabel("Level:");
        JSpinner levelSelect = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        levelSelect.setValue(pokemon.getLevel());

        JPanel topPanel = new CustomJPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        {
            topPanel.add(roleLabel);
            topPanel.add(pokemonSelector);
            topPanel.add(levelLabel);
            topPanel.add(levelSelect);
        }
        JPanel centerPanel = new CustomJPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JLabel imageLabel = new JLabel("");
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(imageLabel);
        centerPanel.add(Box.createVerticalGlue());

        JPanel bottomPanel = createMainBottomPanel(attacker);
        JPanel panel = new CustomJPanel(new BorderLayout());
        {
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);
        }

        pokemonSelector.addActionListener(e -> {
            String selectedValue = pokemonSelector.getSelectedItem().toString();
            pokemon.setName(selectedValue);
            imageLabel.setIcon(sizeIcon(pokemon.getName(), panel.getWidth() - 25, panel.getHeight() - topPanel.getHeight() - bottomPanel.getHeight() - 15));
        });
        levelSelect.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedValue = (int) levelSelect.getValue();
                pokemon.setLevel(selectedValue);
            }
        });
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bottomPanel.setPreferredSize(new Dimension(windowWidth/2, bottomPanel.getPreferredSize().height));
                int expectedHeight = panel.getHeight() - topPanel.getHeight() - bottomPanel.getHeight() - 15;
                imageLabel.setIcon(sizeIcon(pokemon.getName(), panel.getWidth() - 25, expectedHeight));
            }
        });

        return panel;
    }


    private JPanel createBottomPanel(Pokemon attacker, Pokemon defender) {

        JPanel panel = new CustomJPanel(new FlowLayout(FlowLayout.CENTER));

        JButton testButton = new JButton("Test");
        JLabel testResult = new JLabel("");
        panel.add(testButton);
        panel.add(testResult);

        testButton.addActionListener(e -> {
            int hp = (int) hpSelect.getValue();
            String selectedMove = (String) moveSelect.getSelectedItem();
            Move attackingMove = new Move(selectedMove);
            attacker.setNature((String)natureSelect[0].getSelectedItem());
            defender.setNature((String)natureSelect[1].getSelectedItem());
            double result = Simulation.simulateAttack(attacker, attackingMove, defender, hp, knowHP.isSelected());
            DecimalFormat df = new DecimalFormat("#.##");
            testResult.setText(defender.getName() + " has a " + df.format(100 * result) + "% chance of surviving!");
        });

        return panel;
    }


    public SurvivabilityTestWindow(CountDownLatch latch) {

        super("Pokédex Survivability Test", latch);
        setMinimumSize(new Dimension(970, 350));
        setBackground(Color.WHITE);

        Pokemon attacker, defender;
        attacker = new Pokemon("Bulbasaur", 5);
        defender = new Pokemon("Bulbasaur", 5);
        natureSelect = new JComboBox[2];

        // Creating panels for left and right sides
        JPanel leftPanel = createMainPanel(attacker, true);
        JPanel rightPanel = createMainPanel(defender, false);
        JPanel bottomPanel = createBottomPanel(attacker, defender);


        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowWidth = getWidth();
                windowHeight = getHeight();

                leftPanel.setPreferredSize(new Dimension(windowWidth/2 + 6, windowHeight - 50));
                rightPanel.setPreferredSize(new Dimension(windowWidth/2 + 6, windowHeight - 50));
                bottomPanel.setPreferredSize(new Dimension(windowWidth, 50));
            }
        });

        endOfConstructor();
    }


}
