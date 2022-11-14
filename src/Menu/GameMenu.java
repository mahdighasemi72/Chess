package Menu;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;

public class GameMenu {
    private static Scanner scanner = RegisterMenu.getScanner();
    private static Controller controller = RegisterMenu.getController();
    private static HashMap<Integer,String> chessPositions = new HashMap<Integer, String>();
    private static String selectedName;
    private static int selectedX;
    private static int selectedY;
    private static boolean isWhiteTurn;
    private static int undo;
    private static Stack<String> destroyedRivalPieces = new Stack<>();
    private static Stack<String> moves = new Stack<>();//(piecename,position,destinationposition,enemy)
    public static void play(){
        makeFirstChessPositions();
        undo = 0;
        isWhiteTurn = true;
        while (true){
            String command = scanner.nextLine().trim();
            executeCommand(command);
        }
    }

    private static void executeCommand(String command) {
        if (ConsoleCommand.SELECT.getStringMatcher(command).matches()){
            Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
            selectProcess(matcher);
        } else if (ConsoleCommand.DESELECT.getStringMatcher(command).matches()) {
            deselectProcess();
        } else if (ConsoleCommand.MOVE.getStringMatcher(command).matches()) {
            movePrecess(command);
        } else if (ConsoleCommand.NEXT_TURN.getStringMatcher(command).matches()) {
            processNextTurn();
        } else if (ConsoleCommand.SHOW_TURN.getStringMatcher(command).matches()) {
            processShowTurn();
        } else if (ConsoleCommand.UNDO.getStringMatcher(command).matches()) {
            processUndo();
        } else if (ConsoleCommand.UNDO_NUMBER.getStringMatcher(command).matches()) {
            System.out.printf(PrintMassage.NUMBER_OF_UNDO_MOVES, 2-undo);
        }
    }

    private static void processUndo() {
        if (undo<3){
            if (!isWhiteTurn){
                String lastMove[] = moves.pop().split(",");
                String pieceName = lastMove[0];
                int position = Integer.parseInt(lastMove[1]);
                int destinationPosition = Integer.parseInt(lastMove[2]);
                String destinationValue = lastMove[3];
                chessPositions.put(position,pieceName);
                chessPositions.put(destinationPosition,destinationValue);
                undo++;
                System.out.println(PrintMassage.UNDO_COMPLETED);
            } else
                System.out.println(PrintMassage.MOVE_BEFORE_UNDO);
        } else
            System.out.println(PrintMassage.CANNOT_UNDO_ANYMORE);
    }

    private static void processShowTurn() {
        if (isWhiteTurn)
            System.out.println(PrintMassage.WHITE_TURN);
        else
            System.out.println(PrintMassage.BLACK_TURN);
    }

    private static void processNextTurn() {
        if (isWhiteTurn)
            System.out.println(PrintMassage.MOVE_BEFORE_CHANGE_TURN);
        else {
            System.out.println(PrintMassage.TURN_COMPLETED);
            isWhiteTurn = true;
        }
    }

    private static void movePrecess(String command) {
        Matcher matcher = ConsoleCommand.MOVE.getStringMatcher(command);
        if (matcher.find()){
            int destinationY = Integer.parseInt(matcher.group(1));
            int destinationX = Integer.parseInt(matcher.group(2));
            int destinationPosition = (destinationY*10) + destinationX;
            if (!isWhiteTurn){
                System.out.println(PrintMassage.ALREADY_MOVED);
            } else if (destinationY > 8 | destinationY== 0 | destinationX > 8 | destinationX == 0) {
                System.out.println(PrintMassage.WRONG_COORDINATION);
            } else if (selectedName.equals(null)) {
                System.out.println(PrintMassage.NOT_SELECTED);
            } else if (isYours(destinationPosition)) {
                System.out.println(PrintMassage.CANNOT_MOVE);
            } else {
                checkPath(selectedName,selectedX,selectedY,destinationX,destinationY);
            }
        }
    }

    private static void deselectProcess() {
        if (selectedName.equals(null)){
            System.out.println(PrintMassage.NOT_SELECTED);
        }else {
            selectedName = null;
            System.out.println(PrintMassage.DESELECTED);
        }
    }

    private static void selectProcess(Matcher matcher) {
        if (matcher.find()){
            int y = Integer.parseInt(matcher.group(1));
            int x = Integer.parseInt(matcher.group(2));
            int position = (y*10) + x;
            if (y > 8 | y== 0 | x > 8 | x == 0){
                System.out.println(PrintMassage.WRONG_COORDINATION);
            } else if (positionValue(position) == null) {
                System.out.println(PrintMassage.NO_PIECE_ON_THIS_SPOT);
            } else if (!isYours(position)){
                System.out.println(PrintMassage.SELECT_YOUR_PIECE);
            }else {
                selectedName = positionValue(position);
                selectedX = x;
                selectedY = y;
                System.out.println(PrintMassage.SELECTED);
            }
        }
    }

    private static String positionValue(int position){
        String selectedValue = chessPositions.get(position);
        return selectedValue;
    }
    private static boolean isYours(int position){
        if (positionValue(position) == PrintMassage.PW | positionValue(position)== PrintMassage.RW |
                positionValue(position)== PrintMassage.NW | positionValue(position)== PrintMassage.BW |
                positionValue(position)== PrintMassage.KW | positionValue(position)== PrintMassage.QW){
            return true;
        }else {
            return false;
        }
    }
    private static boolean isBarrierOnPath(String pieceName, int x, int y, int destinationX, int destinationY){
        int destinationPosition = (destinationY*10) + destinationX;
        if (pieceName.equals(PrintMassage.PW)) {
            if (y == 2){
                if (destinationY == 3){
                    if (positionValue(destinationPosition)!= null){
                        return true;
                    }
                } else if (destinationY == 4){
                    if (positionValue(destinationPosition)!= null | positionValue(30+x)!= null){
                        return true;
                    }
                }
            } else {
                if (positionValue(destinationPosition)!= null){
                    return true;
                }
            }
        } else if (pieceName.equals(PrintMassage.RW)) {
            if (destinationX == x ){
                if (destinationY > y){
                    for (int i=1; i< Math.abs(destinationY-y); i++){
                        if(positionValue(10*(y+i) + x )!= null){
                            return true;
                        }
                    }
                } else {
                    for (int i=1; i< Math.abs(destinationY-y); i++){
                        if(positionValue(10*(y-i) + x )!= null){
                            return true;
                        }
                    }
                }
            }else if (destinationY == y){
                if (destinationX > x){
                    for (int i=1; i< Math.abs(destinationX-x); i++){
                        if(positionValue(10*(y) + (x+i))!= null){
                            return true;
                        }
                    }
                } else {
                    for (int i=1; i< Math.abs(destinationX-x); i++){
                        if(positionValue(10*(y) + (x-i))!= null){
                            return true;
                        }
                    }
                }
            }
        } else if (pieceName.equals(PrintMassage.BW)) {
            if (destinationX > x & destinationY > y) {
                for (int i = 1; i < Math.abs(destinationX - x); i++) {
                    if (positionValue((10 * (y + i)) + (x + i))!= null)
                        return true;
                }
            } else if (destinationX > x & destinationY < y) {
                for (int i = 1; i < Math.abs(destinationX - x); i++) {
                    if (positionValue((10 * (y - i)) + (x + i))!= null)
                        return true;
                }
            } else if (destinationX < x & destinationY > y) {
                for (int i = 1; i < Math.abs(destinationX - x); i++) {
                    if (positionValue((10 * (y + i)) + (x - i))!= null)
                        return true;
                }
            } else if (destinationX < x & destinationY < y) {
                for (int i = 1; i < Math.abs(destinationX - x); i++) {
                    if (positionValue((10 * (y - i)) + (x - i))!= null)
                        return true;
                }
            }
        }else if (pieceName.equals(PrintMassage.QW)) {
                if (destinationX > x & destinationY > y){
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (positionValue((10 * (y + i)) + (x + i))!= null)
                            return true;
                    }
                } else if (destinationX > x & destinationY < y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (positionValue((10 * (y - i)) + (x + i))!= null)
                            return true;
                    }
                } else if (destinationX < x & destinationY > y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (positionValue((10 * (y + i)) + (x - i))!= null)
                            return true;
                    }
                } else if (destinationX < x & destinationY < y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (positionValue((10 * (y - i)) + (x - i))!= null)
                            return true;
                    }
                } else if (destinationX - x == 0){
                    if (destinationY > y){
                        for (int i=1; i< Math.abs(destinationY-y); i++){
                            if(positionValue(10*(y+i) + x )!= null){
                                return true;
                            }
                        }
                    } else {
                        for (int i=1; i< Math.abs(destinationY-y); i++){
                            if(positionValue(10*(y-i) + x )!= null){
                                return true;
                            }
                        }
                    }
                }else if (destinationY - y == 0){
                    if (destinationX > x){
                        for (int i=1; i< Math.abs(destinationX-x); i++){
                            if(positionValue(10*(y) + (x+i))!= null){
                                return true;
                            }
                        }
                    } else {
                        for (int i=1; i< Math.abs(destinationX-x); i++){
                            if(positionValue(10*(y) + (x-i))!= null){
                                return true;
                            }
                        }
                    }
                }
            }
        return false;
    }

    public static void checkPath(String pieceName, int x, int y, int destinationX, int destinationY){
        int position = (10*y) + x;
        int destinationPosition = (10*destinationY) + destinationX;
        if (pieceName.equals(PrintMassage.PW)){
            if (destinationX == x) {
                if (y == 2){
                    if (destinationY == 3 | destinationY == 4){
                        if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY)){
                            System.out.println(PrintMassage.CANNOT_MOVE);
                        } else {
                            move(pieceName, x, y, destinationX, destinationY);
                        }
                    } else {
                        System.out.println(PrintMassage.CANNOT_MOVE);
                    }
                } else if (destinationY == y+1){
                    if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                        System.out.println(PrintMassage.CANNOT_MOVE);
                    else{
                        move(pieceName, x, y, destinationX, destinationY);
                    }
                } else{
                    System.out.println(PrintMassage.CANNOT_MOVE);
                }
            } else if (destinationX == x + 1 | destinationX == x - 1) {
                if (!isYours(destinationPosition) & positionValue(destinationPosition) != null & destinationY == y+1){
                    move(pieceName, x, y, destinationX, destinationY);
                }
            }else {
                System.out.println(PrintMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(PrintMassage.RW)){
            if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(PrintMassage.CANNOT_MOVE);
                else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(PrintMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(PrintMassage.NW)) {
            if (Math.abs(destinationY - y) == 2 & Math.abs(destinationX - x) == 1) {
                move(pieceName, x, y, destinationX, destinationY);
            } else if (Math.abs(destinationY - y) == 1 & Math.abs(destinationX - x) == 2) {
                move(pieceName, x, y, destinationX, destinationY);
            } else
                System.out.println(PrintMassage.CANNOT_MOVE);
        } else if (pieceName.equals(PrintMassage.BW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(PrintMassage.CANNOT_MOVE);
                }else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else
                System.out.println(PrintMassage.CANNOT_MOVE);
        } else if (pieceName.equals(PrintMassage.QW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(PrintMassage.CANNOT_MOVE);
                }else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(PrintMassage.CANNOT_MOVE);
                else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(PrintMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(PrintMassage.KW)) {
            if (Math.abs(destinationX-x) < 2 & Math.abs(destinationY-y) < 2){
                    move(pieceName, x, y, destinationX, destinationY);
            } else {
                System.out.println(PrintMassage.CANNOT_MOVE);
            }
        }
    }
    private static void move(String pieceName, int x, int y, int destinationX, int destinationY){
        int position = (10*y) + x;
        int destinationPosition = (10*destinationY) + destinationX;
        if (positionValue(destinationPosition)==null){
            chessPositions.put(destinationPosition, positionValue(position));
            chessPositions.put(position, null);
            isWhiteTurn = false;
            System.out.println(PrintMassage.MOVED);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + "");
        } else {
            String rivalPiece = positionValue(destinationPosition);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + rivalPiece);
            destroyedRivalPieces.push(rivalPiece);
            chessPositions.put(destinationPosition, positionValue(position));
            chessPositions.put(position, null);
            isWhiteTurn = false;
            System.out.println(PrintMassage.RIVAL_PIECE_DESTROYED);
        }
    }
    private static void makeFirstChessPositions(){
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                chessPositions.put((i*10)+j, null);
            }
        }
        chessPositions.put(11,PrintMassage.RW);
        chessPositions.put(12,PrintMassage.NW);
        chessPositions.put(13,PrintMassage.BW);
        chessPositions.put(14,PrintMassage.QW);
        chessPositions.put(15,PrintMassage.KW);
        chessPositions.put(16,PrintMassage.BW);
        chessPositions.put(17,PrintMassage.NW);
        chessPositions.put(18,PrintMassage.RW);

        chessPositions.put(21,PrintMassage.PW);
        chessPositions.put(22,PrintMassage.PW);
        chessPositions.put(23,PrintMassage.PW);
        chessPositions.put(24,PrintMassage.PW);
        chessPositions.put(25,PrintMassage.PW);
        chessPositions.put(26,PrintMassage.PW);
        chessPositions.put(27,PrintMassage.PW);
        chessPositions.put(28,PrintMassage.PW);

        chessPositions.put(71,PrintMassage.PB);
        chessPositions.put(72,PrintMassage.PB);
        chessPositions.put(73,PrintMassage.PB);
        chessPositions.put(74,PrintMassage.PB);
        chessPositions.put(75,PrintMassage.PB);
        chessPositions.put(76,PrintMassage.PB);
        chessPositions.put(77,PrintMassage.PB);
        chessPositions.put(78,PrintMassage.PB);

        chessPositions.put(81,PrintMassage.RB);
        chessPositions.put(82,PrintMassage.NB);
        chessPositions.put(83,PrintMassage.BB);
        chessPositions.put(84,PrintMassage.QB);
        chessPositions.put(85,PrintMassage.KB);
        chessPositions.put(86,PrintMassage.BB);
        chessPositions.put(87,PrintMassage.NB);
        chessPositions.put(88,PrintMassage.RB);
    }
}



