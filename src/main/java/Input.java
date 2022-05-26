import java.util.Scanner;

public class Input {

    public static String inputValidPokemonName(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!GeneralTable.validPokemonName(input)) {
            System.out.println("Invalid Input! Please try again.");
            input = sc.nextLine();
        }
        return input;
    }

    public static int inputValidPokemonLevel(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > 100) {
            System.out.println("Invalid Input! Please try again.");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }

    public static int inputValidPokemonV(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!input.matches("\\d+") || Integer.parseInt(input) < 0 || Integer.parseInt(input) > 31) {
            System.out.println("Invalid Input! Please try again.");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }

    public static String inputValidPokemonNature(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!NatureTable.validPokemonNature(input)) {
            System.out.println("Invalid Input! Please try again.");
            input = sc.nextLine();
        }
        return input;
    }

    public static String inputValidPokemonMove(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!MoveTable.validPokemonMove(input)) {
            System.out.println("Invalid Input! Please try again.");
            input = sc.nextLine();
        }
        return input;
    }
}
