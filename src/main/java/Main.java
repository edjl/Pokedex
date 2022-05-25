/*
 * Edward Lee
 * May 21, 2022
 */

import java.util.Scanner;

public class Main {

    public static char initialPrompt(Scanner sc) {
        System.out.println("What  would you like to do?");
        System.out.println("If this is your first time, you should probably web scrape all tables first.");
        System.out.println("    (w) Web Scrape Latest Tables");
        System.out.println("    (g) Find General Information About a Pokemon");
        System.out.println("    (q) Quit");
        return sc.nextLine().charAt(0);
    }

    public static void webScrapeTable(Scanner sc) {
        String tables = "";
        System.out.println("Which table(s) would you like to web scrape?");
        System.out.println("    (g) General Table (#, Pokemon, types, stats)");
        System.out.println("    (t) Type Table");
        System.out.println("    (m) Moves Table");
        System.out.println("    (a) Abilities Table");
        System.out.println("    (n) Natures Table");
        tables = sc.nextLine();
        if (tables.contains("g"))
            GeneralTable.updateTable();
        if (tables.contains("t"))
            TypeTable.updateTable();
        if (tables.contains("m"))
            MoveTable.updateTable();
        if (tables.contains("a"))
            AbilityTable.updateTable();
        if (tables.contains("n"))
            NatureTable.updateTable();

        System.out.println("Web scrape successful!");
    }

    public static String findPokemonGeneralInfo(Scanner sc) {
        String pokemon = "";
        System.out.println("Which Pokemon would you like information on (type the name)?");
        pokemon = sc.nextLine();
        String []info = GeneralTable.getGeneralInfoByName(pokemon);
        System.out.print("Pokemon:\t" + info[0] + "\t  " + info[1].toUpperCase() + "    \t" + info[2]);
        System.out.println((info[3].equals("") ? "" : "-" + info[3]));
        System.out.print("\tStats:\t" + info[4] + "\t  ");
        for (int i = 5; i < info.length; i++)
            System.out.print(info[i] + "  ");
        System.out.println();
        return pokemon;
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner (System.in);
        System.out.println("Welcome to Edward's Pokedex!");
        char prompt = initialPrompt(sc);
        while (prompt != 'q') {
            if (prompt == 'w')
                webScrapeTable(sc);
            else if (prompt == 'g')
                findPokemonGeneralInfo(sc);

            System.out.println();
            prompt = initialPrompt(sc);
        }
        System.out.println("Thanks for using this program!");
    }
}
