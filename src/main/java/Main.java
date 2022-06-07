/*
 * Edward Lee
 * May 21, 2022
 */

import java.util.Scanner;

public class Main {

    public static char initialPrompt(Scanner sc) {
        String input = "";
        System.out.println("What  would you like to do?");
        System.out.println("If this is your first time, you should probably web scrape all tables first.");
        System.out.println("    (w) Web Scrape Latest Tables");
        System.out.println("    (g) Show General Information About a Pokemon");
        System.out.println("    (s) Show Information About a Specific Pokemon");
        System.out.println("    (p) Print A List of Pokemon");
        System.out.println("    (q) Quit");
        while (true) {
            input = sc.nextLine().toLowerCase();
            if (input.length() == 1) {
                if (input.charAt(0) == 'w' || input.charAt(0) == 'g' ||
                        input.charAt(0) == 's' || input.charAt(0) == 'p' ||
                        input.charAt(0) == 'q') {
                    break;
                }
            }
            System.out.println("Invalid input! Please try again.");
        }
        return input.charAt(0);
    }

    public static String getPokemonPrompt(Scanner sc) {
        String pokemon = "";
        System.out.print("Which Pokemon would you like information on (type the name)? ");
        pokemon = Input.inputValidPokemonName(sc);
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
        System.out.print("What is the name of the pokemon? ");
        String name = Input.inputValidPokemonName(sc);
        System.out.print("What is the level of the pokemon? ");
        int level = Input.inputValidPokemonLevel(sc);
        System.out.print("What is the IV of the pokemon? ");
        int iv = Input.inputValidPokemonV(sc);
        System.out.print("What is the EV of the pokemon? ");
        int ev = Input.inputValidPokemonV(sc);
        System.out.print("What is the nature of the pokemon? ");
        String nature = Input.inputValidPokemonNature(sc);
        System.out.println("What are the 4 moves that the pokemon knows.");
        System.out.println("If the pokemon knows less than 4 moves, type 'q' after the last move.");
        String []moves = new String[4];
        for (int i = 0; i < 4; i++)
            moves[i] = Input.inputValidPokemonMove(sc);

        Pokemon poke = new Pokemon(name, level, iv, ev, nature, moves);
        poke.printPokemon();
    }

    public static void pokemonList(Scanner sc) {
        String input;
        System.out.println("What type of list do you want?");
        System.out.println("    (a) All pokemon");
        System.out.println("    (t) By Type");
        while (true) {
            input = sc.nextLine().toLowerCase();
            if (input.length() == 1) {
                if (input.charAt(0) == 'a' || input.charAt(0) == 't') {
                    break;
                }
            }
            System.out.println("Invalid input! Please try again.");
        }
        String type = "";
        if (input.charAt(0) == 'a')
            type = "all";
        else if (input.charAt(0) == 't') {
            System.out.print("What is the type of pokemon you want? ");
            type = Input.inputValidType(sc);
        }
        GeneralTable.printPokemonByType(type);
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
            else if (prompt == 'p')
                pokemonList(sc);

            System.out.println();
            prompt = initialPrompt(sc);
        }
        System.out.println("Thanks for using this program!");
    }
}
