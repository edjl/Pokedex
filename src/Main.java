/*
 * Edward Lee
 * December 21, 2023
 */

package src;
import src.View.SurvivabilityTestWindow;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;


public class Main {

    public static void updateTables() {
        try {
            String pythonCommand = "python3";
            String pythonScript = "src/Webscraper/PokemonDBWebscraper.py";
            String []tablesToScrape = {"Move", "Nature", "Pokemon", "Type"};
            int tables = tablesToScrape.length;

            ProcessBuilder []pbs = new ProcessBuilder[tables];
            Process []processes = new Process[tables];
            pbs[0] = new ProcessBuilder(pythonCommand, pythonScript, "Tables");
            processes[0] = pbs[0].start();
            processes[0].waitFor();

            for (int i = 0; i < tables; i++) {
                pbs[i] = new ProcessBuilder(pythonCommand, pythonScript, tablesToScrape[i]);
                processes[i] = pbs[i].start();
            }

            for (int i = 0; i < tables; i++) {
                int exitCode = processes[i].waitFor();
                if (exitCode == 0) {
                    System.out.println("    " + tablesToScrape[i] + " table webscraped successful!");
                }
                else {
                    System.out.println("    " + tablesToScrape[i] + " table webscraped failed!");
                }
            }
        }
        catch (IOException | InterruptedException e) {
        }
    }

    public static void updatePokemonImages() {
        try {
            String pythonCommand = "python3";
            String pythonScript = "src/Webscraper/PokemonImageWebscraper.py";

            ProcessBuilder pb = new ProcessBuilder(pythonCommand, pythonScript);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("    Pokémon images webscraped successful!");
            }
            else {
                System.out.println("    Pokémon images webscraped failed!");
            }
        }
        catch (IOException | InterruptedException e) {
        }
    }

    public static void survivabilityTest(CountDownLatch latch) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            new SurvivabilityTestWindow(latch);
        });
        latch.await();
    }

    public static void main(String []args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Pokédex System!\n");
        
        String userInput;
        do {
            System.out.println("What would you like to do?");
            System.out.println("    0: Exit");
            System.out.println("    11: Update Tables");
            System.out.println("    12: Update Pokemon Images");
            System.out.println("    21: Pokémon Survivability Test");
            userInput = scanner.nextLine();
            if (userInput.equals("11")) {
                updateTables();
            }
            else if (userInput.equals("12")) {
                updatePokemonImages();
            }
            else if (userInput.equals("21")) {
                CountDownLatch latch = new CountDownLatch(1);
                try {
                    survivabilityTest(latch);
                } catch (InterruptedException e) {}
            }
            System.out.println();
        }
        while (!userInput.equals("0"));

        System.out.println("Thank you for using the Pokédex System!");
        scanner.close();
    }
}