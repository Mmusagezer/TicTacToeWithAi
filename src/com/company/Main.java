package com.company; // It needs an edit, not something hard,but time-taking. It would have been easier and look better if I have used
// 2D arrays instead of strings.

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String command = "goOn";
        while (!command.equals("exit")) {
            command = menu();
        }
    }
    public static void printMap(String state) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                char nextChar = state.charAt(i * 3 + j);
                char symbols = nextChar == '_' ? ' ' : nextChar;
                System.out.print(symbols + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }
    public static boolean isOccupied(int x, int y, String state) {
        int xCoordinate = x - 1;
        int yCoordinate = y - 1;
        int location = xCoordinate * 3 +yCoordinate;
        return state.charAt(location) != '_';
    }
    public static int[] takeCoordinates(String state) {
        int x;
        int y;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter the coordinates: ");
            if (scanner.hasNextInt()) {
                x = scanner.nextInt();
            } else {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            if (scanner.hasNextInt()) {

                y = scanner.nextInt();
            } else {
                scanner.nextLine();
                System.out.println("You should enter numbers!");
                continue;
            }
            if (x > 3 || y > 3 || x < 1 || y < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }else if (isOccupied(x, y, state)) {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            break;
        }
        return new int[] {x-1, y-1};

    }
    public static String updateState(int[] coordinates, String state) {

        //System.out.println("Enter the coordinates: ");
        int index = 3 * coordinates[0] + coordinates[1];
        int numberOfX = 0;
        int numberOfY = 0;
        for(int i = 0; i < state.length(); i++) {
            if (state.charAt(i) == 'X') {
                numberOfX++;
            } else if (state.charAt(i) == 'O') {
                numberOfY++;
            }
        }

        String newChar = numberOfX > numberOfY ? "O" : "X";
        return state.substring(0, index) + newChar + state.substring(index + 1);
    }
    public static char showResult (String state){

        for(int i = 0; i < 3; i++) {

            String subStr = state.substring(i * 3, i * 3 + 3);
            if (subStr.equals("XXX")) {
                //System.out.println("X wins");
                return 'X';
            } else if (subStr.equals("OOO")) {
                //System.out.println("O wins");
                return 'O';
            }
        }
        for(int i = 0; i < 3; i++) {
            if (state.charAt(i) == 'X' && state.charAt(i+3) == 'X' && state.charAt(i+6) == 'X') {
                //System.out.println("X wins");
                return 'X';
            } else if (state.charAt(i) == 'O' && state.charAt(i+3) == 'O' && state.charAt(i+6) == 'O') {
                //System.out.println("O wins");
                return 'O';
            }
        }

        if (state.charAt(0) ==  'X' && state.charAt(4) ==  'X' && state.charAt(8) ==  'X' ||
                state.charAt(2) ==  'X' && state.charAt(4) ==  'X' && state.charAt(6) ==  'X') {
            //System.out.println("X wins");
            return 'X';
        } else if (state.charAt(0) ==  'O' && state.charAt(4) ==  'O' && state.charAt(8) ==  'O' ||
                state.charAt(2) ==  'O' && state.charAt(4) ==  'O' && state.charAt(6) ==  'O') {
            //System.out.println("O wins");
            return 'O';
        }

        for(int i = 0; i < 9; i++) {
            if (state.charAt(i) == '_') {
                //System.out.println("Game not finished");
                return 'N';
            }
        }
        //System.out.println("Draw");
        return 'D';
    }
    @Deprecated
    public static char givenTemplatePlay() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the cells: ");
        String givenState = scanner.nextLine();
        printMap(givenState);
        int[] givenCoordinates = takeCoordinates(givenState);
        String currentState = updateState(givenCoordinates, givenState);
        printMap(currentState);
        return showResult(currentState);
    }
    public static int[] takePcCoordinates(String state){
        int x;
        int y;
        Random random = new Random();
        while(true) {
            x = random.nextInt(3);
            y = random.nextInt(3);

            if (isOccupied(x+1, y+1, state)) {
                //System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            break;
        }
        return new int[] {x, y};

    }
    public static String playUser(String state) {

        int [] coordinates;
        coordinates = takeCoordinates(state);
        state = updateState(coordinates, state);
        return state;
    }
    public static String playEasy(String state) {

        int[] pcCoordinates = takePcCoordinates(state);
        state = updateState(pcCoordinates, state);
        System.out.println("Making move level \"easy\"");
        return state;

    }
    public static String playMedium(String state, char oOrX) {
        int[] pcCoordinates = new int[]{-1, -1};
        if (oOrX == 'O') {
            pcCoordinates =  playMediumO(state);
            if (Arrays.equals(pcCoordinates, new int[]{-1, -1})) {
                pcCoordinates = playMediumX(state);
            }
        } else if (oOrX == 'X') {                           // this part is to check which one medium is playing so that it evaluate if it can win first,
            pcCoordinates =  playMediumX(state);            //  otherwise, evaluates if opponent can win.
            if (Arrays.equals(pcCoordinates, new int[]{-1, -1})) {
                pcCoordinates = playMediumO(state);
            }
        }


        if (Arrays.equals(pcCoordinates, new int[]{-1, -1})) {
            pcCoordinates = takePcCoordinates(state);
        }
        state = updateState(pcCoordinates, state);
        System.out.println("Making move level \"medium\"");
        return state;


    }
    public static int[] playMediumO(String state){
        int[] pcCoordinates = {-1, -1};
        for (int i = 0; i < 3; i++) {
            if (state.charAt(i) == 'O' && state.charAt(i + 3) == 'O' || state.charAt(i + 6) == 'O' && state.charAt(i + 3) == 'O' || state.charAt(i) == 'O' && state.charAt(i + 6) == 'O') {
                if (state.charAt(i) == '_') {
                    pcCoordinates = new int[]{0, i};
                    break;
                } else if (state.charAt(i + 3) == '_') {
                    pcCoordinates = new int[]{1, i};
                    break;
                } else if (state.charAt(i + 6) == '_') {
                    pcCoordinates = new int[]{2, i};
                    break;
                }
            } else if (state.charAt(i * 3) == 'O' && state.charAt(i * 3 + 1) == 'O' || state.charAt(i * 3 + 2) == 'O' && state.charAt(i * 3 + 1) == 'O' || state.charAt(i * 3) == 'O' && state.charAt(i * 3 + 2) == 'O') {

                if (state.charAt(i * 3) == '_') {
                    pcCoordinates = new int[]{i, 0};
                    break;
                } else if (state.charAt(i * 3 + 1) == '_') {
                    pcCoordinates = new int[]{i, 1};
                    break;
                } else if (state.charAt(i * 3 + 2) == '_') {
                    pcCoordinates = new int[]{i, 2};
                    break;
                }
            }

        }
        if (state.charAt(0) == 'O' && state.charAt(4) == 'O' || state.charAt(0) == 'O' && state.charAt(8) == 'O' || state.charAt(8) == 'O' && state.charAt(4) == 'O') {
            if (state.charAt(0) == '_') {
                pcCoordinates = new int[]{0, 0};
            } else if (state.charAt(4) == '_') {
                pcCoordinates = new int[]{1, 1};
            } else if (state.charAt(8) == '_') {
                pcCoordinates = new int[]{2, 2};
            }
        } else if (state.charAt(2) == 'O' && state.charAt(4) == 'O' || state.charAt(2) == 'O' && state.charAt(6) == 'O' || state.charAt(6) == 'O' && state.charAt(4) == 'O') {
            if (state.charAt(2) == '_') {
                pcCoordinates = new int[]{0, 2};
            } else if (state.charAt(4) == '_') {
                pcCoordinates = new int[]{1, 1};
            } else if (state.charAt(6) == '_') {
                pcCoordinates = new int[]{2, 0};
            }

        }
        return pcCoordinates;
    }
    public static int[] playMediumX(String state) {
        int[] pcCoordinates = {-1, -1};
        for (int i = 0; i < 3; i++) {
            if (state.charAt(i) == 'X' && state.charAt(i + 3) == 'X' || state.charAt(i + 6) == 'X' && state.charAt(i + 3) == 'X' || state.charAt(i) == 'X' && state.charAt(i + 6) == 'X') {
                if (state.charAt(i) == '_') {
                    pcCoordinates = new int[]{0, i};
                    break;
                } else if (state.charAt(i + 3) == '_') {
                    pcCoordinates = new int[]{1, i};
                    break;
                } else if (state.charAt(i + 6) == '_') {
                    pcCoordinates = new int[]{2, i};
                    break;
                }
            } else if (state.charAt(i * 3) == 'X' && state.charAt(i * 3 + 1) == 'X' || state.charAt(i * 3 + 2) == 'X' && state.charAt(i * 3 + 1) == 'X' || state.charAt(i * 3) == 'X' && state.charAt(i * 3 + 2) == 'X') {

                if (state.charAt(i * 3) == '_') {
                    pcCoordinates = new int[]{i, 0};
                    break;
                } else if (state.charAt(i * 3 + 1) == '_') {
                    pcCoordinates = new int[]{i, 1};
                    break;
                } else if (state.charAt(i * 3 + 2) == '_') {
                    pcCoordinates = new int[]{i, 2};
                    break;
                }
            }

        }
        if (state.charAt(0) == 'X' && state.charAt(4) == 'X' || state.charAt(0) == 'X' && state.charAt(8) == 'X' || state.charAt(8) == 'X' && state.charAt(4) == 'X') {
            if (state.charAt(0) == '_') {
                pcCoordinates = new int[]{0, 0};
            } else if (state.charAt(4) == '_') {
                pcCoordinates = new int[]{1, 1};
            } else if (state.charAt(8) == '_') {
                pcCoordinates = new int[]{2, 2};
            }
        } else if (state.charAt(2) == 'X' && state.charAt(4) == 'X' || state.charAt(2) == 'X' && state.charAt(6) == 'X' || state.charAt(6) == 'X' && state.charAt(4) == 'X') {
            if (state.charAt(2) == '_') {
                pcCoordinates = new int[]{0, 2};
            } else if (state.charAt(4) == '_') {
                pcCoordinates = new int[]{1, 1};
            } else if (state.charAt(6) == '_') {
                pcCoordinates = new int[]{2, 0};
            }
        }
        return pcCoordinates;
    }
    public static int[] playAi(String state, int depth, boolean maximize) {
        int chosenIndex = -1;
        if (depth == 0 || showResult(state) != 'N') {
            switch (showResult(state)) {
                case 'X':
                    return new int[]{2, chosenIndex};
                case 'D':
                    return new int[]{1, chosenIndex};
                case 'O':
                    return new int[]{0, chosenIndex};
            }
        }
        int value;

        if (maximize) {
            value = -1;
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == '_' && value != 2) {
                    String state1 = state.substring(0, i) + "X" + state.substring(i + 1);
                    if (value < Math.max(value, playAi(state1, depth -1, false)[0])) {
                        chosenIndex = i;
                    }
                    value = Math.max(value, playAi(state1, depth -1, false)[0]);
                }
            }
        } else {
            value = 3;
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == '_' && value != 0) {
                    String state1 = state.substring(0, i) + "O" + state.substring(i + 1);
                    if (value > Math.max(value, playAi(state1, depth -1, true)[0])) {
                        chosenIndex = i;
                    }
                    value = Math.min(value, playAi(state1, depth -1, true)[0]);
                }
            }
        }
        return new int[]{value, chosenIndex};
        //return 0;
    }
    public static int[] playAiO(String state, int depth, boolean maximize) {
        int chosenIndex = 0;
        if (depth == 0 || showResult(state) != 'N') {
            switch (showResult(state)) {
                case 'O':
                    return new int[]{2, chosenIndex};
                case 'D':
                    return new int[]{1, chosenIndex};
                case 'X':
                    return new int[]{0, chosenIndex};
            }
        }
        int value;

        if (maximize) {
            value = -1;
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == '_') {
                    String state1 = state.substring(0, i) + "O" + state.substring(i + 1);
                    if (value < Math.max(value, playAiO(state1, depth -1, false)[0])) {
                        chosenIndex = i;
                    }
                    value = Math.max(value, playAiO(state1, depth -1, false)[0]);
                }
            }
        } else {
            value = 3;
            for (int i = 0; i < 9; i++) {
                if (state.charAt(i) == '_') {
                    String state1 = state.substring(0, i) + "X" + state.substring(i + 1);
                    if (value > Math.max(value, playAiO(state1, depth -1, true)[0])) {
                        chosenIndex = i;
                    }
                    value = Math.min(value, playAiO(state1, depth -1, true)[0]);
                }
            }
        }
        return new int[]{value, chosenIndex};
        //return 0;
    }
    public static String playHard(String state) {
        int numberOfX = state.length() - state.replace("X", "").length();
        int numberOfO = state.length() - state.replace("O", "").length();
        int numberOf_ = state.length() - state.replace("_", "").length();
        int[] valueAndIndex;
        int[] pcCoordinates;
        if (numberOfX == numberOfO) {
            valueAndIndex = playAi(state, numberOf_, true);
            int i = valueAndIndex[1];
            pcCoordinates = new int[] {i/3, i%3};
            state = updateState(pcCoordinates, state);
            System.out.println("Making move level \"hard\"");
            return state;

        } else {
            valueAndIndex = playAiO(state, numberOf_, true);
            int i = valueAndIndex[1];
            pcCoordinates = new int[] {i/3, i%3};
            state = updateState(pcCoordinates, state);
            System.out.println("Making move level \"hard\"");
            return state;
        }
    }
    public static char showCondition(String state) {
        char theCondition = showResult(state);
        printMap(state);
        return theCondition;
    }
    public static void chooseWinner(char ch) {
        switch (ch) {
            case 'X' -> System.out.println("X wins");
            case 'O' -> System.out.println("O wins");
            case 'D' -> System.out.println("Draw");
            default -> System.out.println("There must be something wrong with deciding condition!");
        }
    }
    public static String menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input command: ");
        String command = scanner.nextLine();
        if (command.equals("exit")) {

            return "exit";
        }
        String state = "_________";
        printMap(state);
        char condition = 'N';
        switch (command) {
            case "start user user" -> {
                while (condition == 'N') {
                    state = playUser(state);
                    condition = showCondition(state);
                }
                chooseWinner(condition);
            }
            case "start user easy" -> {
                while (condition == 'N') {
                    state = playUser(state);
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playEasy(state);
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start easy easy" -> {
                while (condition == 'N') {
                    state = playEasy(state);
                    condition = showCondition(state);
                }
                chooseWinner(condition);
            }
            case "start easy user" -> {
                while (condition == 'N') {
                    state = playEasy(state);
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playUser(state);
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start user medium" -> {
                while (condition == 'N') {
                    state = playUser(state);
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playMedium(state, 'O');
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start medium user" -> {
                while (condition == 'N') {
                    state = playMedium(state, 'X');
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playUser(state);
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start hard user" -> {
                while (condition == 'N') {
                    state = playHard(state);
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playUser(state);
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start user hard" -> {
                while (condition == 'N') {
                    state = playUser(state);
                    condition = showCondition(state);
                    if (condition == 'N') {
                        state = playHard(state);
                        condition = showCondition(state);
                    }
                }
                chooseWinner(condition);
            }
            case "start hard hard" -> {
                while (condition == 'N') {
                    state = playHard(state);
                    condition = showCondition(state);
                }
                chooseWinner(condition);
            }
            default -> {
                System.out.println("Bad parameters!");
                return "bad parameters";
            }
        }
        return "goOn";
    }
}


