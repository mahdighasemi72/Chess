package Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu {
    private static Scanner scanner = new Scanner(System.in);
    private static Controller controller = RegisterMenu.getController();
    private static HashMap<Integer,String> chessPositions = new HashMap<Integer, String>(8,8);

    public static void play(){
        makeFirstChessPositions();
        while (true){
            String command = scanner.nextLine().trim();
            if (ConsoleCommand.SELECT.getStringMatcher(command).matches()){
                Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
                if (matcher.find()){
                    if (Integer.parseInt(matcher.group(1)) > 8 |
                            Integer.parseInt(matcher.group(1))== 0 |
                            Integer.parseInt(matcher.group(2)) > 8 |
                            Integer.parseInt(matcher.group(2))== 0){
                        System.out.println("wrong coordination");
                    }
                }
            }
        }
    }

    private static String select(int position){
        String positionValue = chessPositions.get(position);
        return positionValue;
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
