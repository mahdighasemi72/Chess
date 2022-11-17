package Menu;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;

public class GameMenu {
    private Controller controller = Controller.getInstance() ;
    private PrintMassage printMassage = new PrintMassage();
    private HashMap<Integer,String> chessPositions = new HashMap<Integer, String>();
    private String selectedName;
    private int selectedX;
    private int selectedY;
    private boolean isWhiteTurn;
    private int undo;
    private Stack<String> destroyedRivalPieces = new Stack<>();
    private Stack<String> moves = new Stack<>();//(piecename,position,destinationposition,enemy)
    public int play(String command){
        int menuNum = 3;
        makeFirstChessPositions();
        undo = 0;
        isWhiteTurn = true;
        menuNum = executeCommand(command);
        return menuNum;
    }

    private int executeCommand(String command) {
        int menuNum = 3;
        if (ConsoleCommand.SELECT.getStringMatcher(command).matches()){

            selectProcess(command);
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
            System.out.printf(printMassage.NUMBER_OF_UNDO_MOVES, 2-undo);
        }
        return menuNum;
    }

    private void processUndo() {
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
                System.out.println(printMassage.UNDO_COMPLETED);
            } else
                System.out.println(printMassage.MOVE_BEFORE_UNDO);
        } else
            System.out.println(printMassage.CANNOT_UNDO_ANYMORE);
    }

    private void processShowTurn() {
        if (isWhiteTurn)
            System.out.println(printMassage.WHITE_TURN);
        else
            System.out.println(printMassage.BLACK_TURN);
    }

    private void processNextTurn() {
        if (isWhiteTurn)
            System.out.println(printMassage.MOVE_BEFORE_CHANGE_TURN);
        else {
            System.out.println(printMassage.TURN_COMPLETED);
            isWhiteTurn = true;
        }
    }

    private void movePrecess(String command) {
        Matcher matcher = ConsoleCommand.MOVE.getStringMatcher(command);
        if (matcher.find()){
            int destinationY = Integer.parseInt(matcher.group(1));
            int destinationX = Integer.parseInt(matcher.group(2));
            int destinationPosition = (destinationY*10) + destinationX;
            if (!isWhiteTurn){
                System.out.println(printMassage.ALREADY_MOVED);
            } else if (destinationY > 8 | destinationY== 0 | destinationX > 8 | destinationX == 0) {
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (selectedName.equals(null)) {
                System.out.println(printMassage.NOT_SELECTED);
            } else if (isYours(destinationPosition)) {
                System.out.println(printMassage.CANNOT_MOVE);
            } else {
                checkPath(selectedName,selectedX,selectedY,destinationX,destinationY);
            }
        }
    }

    private void deselectProcess() {
        if (selectedName.equals(null)){
            System.out.println(printMassage.NOT_SELECTED);
        }else {
            selectedName = null;
            System.out.println(printMassage.DESELECTED);
        }
    }

    private void selectProcess(String command) {
        Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
        if (matcher.find()){
            int y = Integer.parseInt(matcher.group(1));
            int x = Integer.parseInt(matcher.group(2));
            int position = (y*10) + x;
            if (y > 8 | y== 0 | x > 8 | x == 0){
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (positionValue(position) == null) {
                System.out.println(printMassage.NO_PIECE_ON_THIS_SPOT);
            } else if (!isYours(position)){
                System.out.println(printMassage.SELECT_YOUR_PIECE);
            }else {
                selectedName = positionValue(position);
                selectedX = x;
                selectedY = y;
                System.out.println(printMassage.SELECTED);
            }
        }
    }

    private String positionValue(int position){
        String selectedValue = chessPositions.get(position);
        return selectedValue;
    }
    private boolean isYours(int position){
        if (positionValue(position) == printMassage.PW | positionValue(position)== printMassage.RW |
                positionValue(position)== printMassage.NW | positionValue(position)== printMassage.BW |
                positionValue(position)== printMassage.KW | positionValue(position)== printMassage.QW){
            return true;
        }else {
            return false;
        }
    }
    private boolean isBarrierOnPath(String pieceName, int x, int y, int destinationX, int destinationY){
        int destinationPosition = (destinationY*10) + destinationX;
        if (pieceName.equals(printMassage.PW)) {
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
        } else if (pieceName.equals(printMassage.RW)) {
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
        } else if (pieceName.equals(printMassage.BW)) {
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
        }else if (pieceName.equals(printMassage.QW)) {
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

    public void checkPath(String pieceName, int x, int y, int destinationX, int destinationY){
        int position = (10*y) + x;
        int destinationPosition = (10*destinationY) + destinationX;
        if (pieceName.equals(printMassage.PW)){
            if (destinationX == x) {
                if (y == 2){
                    if (destinationY == 3 | destinationY == 4){
                        if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY)){
                            System.out.println(printMassage.CANNOT_MOVE);
                        } else {
                            move(pieceName, x, y, destinationX, destinationY);
                        }
                    } else {
                        System.out.println(printMassage.CANNOT_MOVE);
                    }
                } else if (destinationY == y+1){
                    if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                        System.out.println(printMassage.CANNOT_MOVE);
                    else{
                        move(pieceName, x, y, destinationX, destinationY);
                    }
                } else{
                    System.out.println(printMassage.CANNOT_MOVE);
                }
            } else if (destinationX == x + 1 | destinationX == x - 1) {
                if (!isYours(destinationPosition) & positionValue(destinationPosition) != null & destinationY == y+1){
                    move(pieceName, x, y, destinationX, destinationY);
                }
            }else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.RW)){
            if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.NW)) {
            if (Math.abs(destinationY - y) == 2 & Math.abs(destinationX - x) == 1) {
                move(pieceName, x, y, destinationX, destinationY);
            } else if (Math.abs(destinationY - y) == 1 & Math.abs(destinationX - x) == 2) {
                move(pieceName, x, y, destinationX, destinationY);
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.BW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.QW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    move(pieceName, x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.KW)) {
            if (Math.abs(destinationX-x) < 2 & Math.abs(destinationY-y) < 2){
                    move(pieceName, x, y, destinationX, destinationY);
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        }
    }
    private void move(String pieceName, int x, int y, int destinationX, int destinationY){
        int position = (10*y) + x;
        int destinationPosition = (10*destinationY) + destinationX;
        if (positionValue(destinationPosition)==null){
            chessPositions.put(destinationPosition, positionValue(position));
            chessPositions.put(position, null);
            isWhiteTurn = false;
            System.out.println(printMassage.MOVED);
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
            System.out.println(printMassage.RIVAL_PIECE_DESTROYED);
        }
    }
    private void makeFirstChessPositions(){
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                chessPositions.put((i*10)+j, null);
            }
        }
        chessPositions.put(11,printMassage.RW);
        chessPositions.put(12,printMassage.NW);
        chessPositions.put(13,printMassage.BW);
        chessPositions.put(14,printMassage.QW);
        chessPositions.put(15,printMassage.KW);
        chessPositions.put(16,printMassage.BW);
        chessPositions.put(17,printMassage.NW);
        chessPositions.put(18,printMassage.RW);

        chessPositions.put(21,printMassage.PW);
        chessPositions.put(22,printMassage.PW);
        chessPositions.put(23,printMassage.PW);
        chessPositions.put(24,printMassage.PW);
        chessPositions.put(25,printMassage.PW);
        chessPositions.put(26,printMassage.PW);
        chessPositions.put(27,printMassage.PW);
        chessPositions.put(28,printMassage.PW);

        chessPositions.put(71,printMassage.PB);
        chessPositions.put(72,printMassage.PB);
        chessPositions.put(73,printMassage.PB);
        chessPositions.put(74,printMassage.PB);
        chessPositions.put(75,printMassage.PB);
        chessPositions.put(76,printMassage.PB);
        chessPositions.put(77,printMassage.PB);
        chessPositions.put(78,printMassage.PB);

        chessPositions.put(81,printMassage.RB);
        chessPositions.put(82,printMassage.NB);
        chessPositions.put(83,printMassage.BB);
        chessPositions.put(84,printMassage.QB);
        chessPositions.put(85,printMassage.KB);
        chessPositions.put(86,printMassage.BB);
        chessPositions.put(87,printMassage.NB);
        chessPositions.put(88,printMassage.RB);
    }
}



