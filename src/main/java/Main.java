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
        System.out.println("    (s) Find Stats About a Specific Pokemon");
        System.out.println("    (q) Quit");
        return sc.nextLine().charAt(0);
    }

    public static String getPokemonPrompt(Scanner sc) {
        String pokemon = "";
        System.out.println("Which Pokemon would you like information on (type the name)?");
        pokemon = sc.nextLine();
        return pokemon;
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

    public static void pokemonStats(Scanner sc) {
        System.out.println("What is the name of the pokemon?");
        String name = sc.nextLine();
        System.out.println("What is the level of the pokemon?");
        int level = sc.nextInt();
        sc.nextLine();
        System.out.println("What is the IV of the pokemon?");
        int iv = sc.nextInt();
        sc.nextLine();
        System.out.println("What is the EV of the pokemon?");
        int ev = sc.nextInt();
        sc.nextLine();
        System.out.println("What is the nature of the pokemon?");
        String nature = sc.nextLine();

        Pokemon poke = new Pokemon(name, level, iv, ev, nature);
        poke.printPokemon();
    }

    public static void main (String[] args) {
        Scanner sc = new Scanner (System.in);
        System.out.println("Welcome to Edward's Pokedex!");
        char prompt = initialPrompt(sc);
        while (prompt != 'q') {
            if (prompt == 'w')
                webScrapeTable(sc);
            else if (prompt == 'g') {
                String pokemon = getPokemonPrompt(sc);
                GeneralTable.printGeneralInfo(pokemon);
            }
            else if (prompt == 's')
                pokemonStats(sc);

            System.out.println();
            prompt = initialPrompt(sc);
        }
        System.out.println("Thanks for using this program!");
    }
}
