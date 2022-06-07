import java.util.Scanner;

public class Input {

    public static String inputValidPokemonName(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!GeneralTable.validPokemonName(input)) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return input;
    }

    public static int inputValidPokemonLevel(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!input.matches("\\d+") || Integer.parseInt(input) < 1 || Integer.parseInt(input) > 100) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }

    public static int inputValidPokemonV(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!input.matches("\\d+") || Integer.parseInt(input) < 0 || Integer.parseInt(input) > 31) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return Integer.parseInt(input);
    }

    public static String inputValidPokemonNature(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!NatureTable.validPokemonNature(input)) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return input.toLowerCase();
    }

    public static String inputValidPokemonMove(Scanner sc) {
        String input = "";
        System.out.print("                                 ");
        input = sc.nextLine();
        while (!MoveTable.validPokemonMove(input) && !input.equals("q")) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return input;
    }

    public static String[] inputValidPokemonMoves(Scanner sc) {
        String []moves;
        String []temp = new String[4];
        int movesNum = 4;
        for (int i = 0; i < 4; i++) {
            temp[i] = inputValidPokemonMove(sc);
            if (temp[i].equals("q")) {
                movesNum = i;
                break;
            }
        }
        moves = new String[movesNum];
        for (int i = 0; i < movesNum; i++) {
            moves[i] = temp[i];
        }
        return moves;
    }

    public static String inputValidType(Scanner sc) {
        String input = "";
        input = sc.nextLine();
        while (!TypeTable.validPokemonType(input)) {
            System.out.print("Invalid Input! Please try again. ");
            input = sc.nextLine();
        }
        return input;
    }
}
