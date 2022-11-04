package Menu;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu {
    private static Scanner scanner = RegisterMenu.getScanner();
    private static Controller controller = RegisterMenu.getController();
    private static HashMap<Integer,String> chessPositions = new HashMap<Integer, String>();
    private static String selectedName;
    private static int selectedX;
    private static int selectedY;
    public static void play(){
        makeFirstChessPositions();
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.SELECT.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
                if (matcher.find()){
                    int y = Integer.parseInt(matcher.group(1));
                    int x = Integer.parseInt(matcher.group(2));
                    int position = (y*10) + x;
                    if (y > 8 | y== 0 | x > 8 | x == 0){
                        System.out.println("wrong coordination");
                    } else if (positionValue(position) == null) {
                        System.out.println("no piece on this spot");
                    } else if (!isYours(position)){
                        System.out.println("you can only select one of your pieces");
                    }else {
                        selectedName = positionValue(position);
                        selectedX = x;
                        selectedY = y;
                        System.out.println("selected");
                    }
                }
            } else if (ConsoleCommand.DESELECT.getStringMatcher(command).matches()) {
                if (selectedName.equals(null)){
                    System.out.println("no piece is selected");
                }else {
                    selectedName = null;
                    System.out.println("deselected");
                }
            } else if (ConsoleCommand.MOVE.getStringMatcher(command).matches()) {
                Matcher matcher = ConsoleCommand.MOVE.getStringMatcher(command);
                if (matcher.find()){
                    int destinationY = Integer.parseInt(matcher.group(1));
                    int destinationX = Integer.parseInt(matcher.group(2));
                    if (Controller.checkPath(selectedName,selectedX,selectedY,destinationX,destinationY)){
                        System.out.println("correct path");
                    }
                }
            }
        }
    }

    private static String positionValue(int position){
        String selectedValue = chessPositions.get(position);
        return selectedValue;
    }

    private static boolean isYours(int position){
        if (positionValue(position).equals("Pw")| positionValue(position).equals("Rw")|
                positionValue(position).equals("Nw")| positionValue(position).equals("Bw")|
                positionValue(position).equals("Kw")| positionValue(position).equals("Qw")){
            return true;
        }else {
            return false;
        }
    }



    private static void makeFirstChessPositions(){
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                chessPositions.put((i*10)+j, null);
            }
        }
        chessPositions.put(11,"Rw");
        chessPositions.put(12,"Nw");
        chessPositions.put(13,"Bw");
        chessPositions.put(14,"Qw");
        chessPositions.put(15,"Kw");
        chessPositions.put(16,"Bw");
        chessPositions.put(17,"Nw");
        chessPositions.put(18,"Rw");

        chessPositions.put(21,"Pw");
        chessPositions.put(22,"Pw");
        chessPositions.put(23,"Pw");
        chessPositions.put(24,"Pw");
        chessPositions.put(25,"Pw");
        chessPositions.put(26,"Pw");
        chessPositions.put(27,"Pw");
        chessPositions.put(28,"Pw");

        chessPositions.put(71,"Pb");
        chessPositions.put(72,"Pb");
        chessPositions.put(73,"Pb");
        chessPositions.put(74,"Pb");
        chessPositions.put(75,"Pb");
        chessPositions.put(76,"Pb");
        chessPositions.put(77,"Pb");
        chessPositions.put(78,"Pb");

        chessPositions.put(81,"Rb");
        chessPositions.put(82,"Nb");
        chessPositions.put(83,"Bb");
        chessPositions.put(84,"Qb");
        chessPositions.put(85,"Kb");
        chessPositions.put(86,"Bb");
        chessPositions.put(87,"Nb");
        chessPositions.put(88,"Rb");
    }

}
