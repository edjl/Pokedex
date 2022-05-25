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
}
