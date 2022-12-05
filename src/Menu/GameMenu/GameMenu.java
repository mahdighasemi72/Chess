package Menu.GameMenu;

import Menu.ConsoleCommand;
import Menu.Controller;
import Menu.GameMenu.GameMenuProcess.MoveProcess;
import Menu.PrintMassage;
import Pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;

public class GameMenu {
    private Controller controller = Controller.getInstance() ;
    private PrintMassage printMassage;
    private MoveProcess move;
    private String selectedName;
    private int selectedX;
    private int selectedY;
    private boolean isWhiteTurn = true;
    private int undo = 0;
    private HashMap<Integer,String> chessPositions2;
    private ArrayList<Piece> chessPlate;
    private Stack<String> destroyedRivalPieces ;
    private Stack<String> moves ;//(pieceName,position,destinationPosition,enemy)

    public GameMenu(PrintMassage printMassage) {
        this.printMassage = printMassage;
        chessPositions2 = new HashMap<Integer, String>();
        destroyedRivalPieces = new Stack<>();
        moves = new Stack<>();
        move = new MoveProcess(chessPositions2,destroyedRivalPieces,moves,printMassage);
        makeFirstChessPositions();
    }

    public int play(String command, String loginUsername, String secondUsername){
        int menuNum = 3;
        menuNum = executeCommand(command);
        return menuNum;
    }

    private int executeCommand(String command) {
        int menuNum = 3;
        if (ConsoleCommand.SELECT.getStringMatcher(command).matches()){
            processSelect(command);
        } else if (ConsoleCommand.DESELECT.getStringMatcher(command).matches()) {
            processDeselect();
        } else if (ConsoleCommand.MOVE.getStringMatcher(command).matches()) {
            precessMoveCommand(command);
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
        if (undo<=2){
            processAllowedUndo();
        } else
            System.out.println(printMassage.CANNOT_UNDO_ANYMORE);
    }

    private void processAllowedUndo() {
        if (!isWhiteTurn){
            String lastMove[] = moves.pop().split(",");
            String pieceName = lastMove[0];
            int position = Integer.parseInt(lastMove[1]);
            int destinationPosition = Integer.parseInt(lastMove[2]);
            String destinationValue = lastMove[3];
            chessPositions2.put(position,pieceName);
            chessPositions2.put(destinationPosition,destinationValue);
            undo++;
            System.out.println(printMassage.UNDO_COMPLETED);
        } else
            System.out.println(printMassage.MOVE_BEFORE_UNDO);
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

    private void precessMoveCommand(String command) {
        Matcher matcher = ConsoleCommand.MOVE.getStringMatcher(command);
        if (matcher.find()){
            int destinationY = Integer.parseInt(matcher.group(1));
            int destinationX = Integer.parseInt(matcher.group(2));
            int destinationPosition = (destinationY*10) + destinationX;
            if (!isWhiteTurn){
                System.out.println(printMassage.ALREADY_MOVED);
            } else if (destinationY > 8 | destinationY== 0 | destinationX > 8 | destinationX == 0) {
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (selectedName == null) {
                System.out.println(printMassage.NOT_SELECTED);
            } else if (isYours(destinationPosition)) {
                System.out.println(printMassage.CANNOT_MOVE);
            } else {
                checkPath(selectedName,selectedX,selectedY,destinationX,destinationY);
            }
        }
    }

    private void processDeselect() {
        if (selectedName == null){
            System.out.println(printMassage.NOT_SELECTED);
        }else {
            selectedName = null;
            System.out.println(printMassage.DESELECTED);
        }
    }

    private void processSelect(String command) {
        Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
        if (matcher.find()){
            int y = Integer.parseInt(matcher.group(1));
            int x = Integer.parseInt(matcher.group(2));
            int position = (y*10) + x;
            if (y > 8 | y== 0 | x > 8 | x == 0){
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (!isWhiteTurn) {
                System.out.println(printMassage.ALREADY_MOVED);
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
        String selectedValue = chessPositions2.get(position);
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
        int destinationPosition = (10*destinationY) + destinationX;
        if (pieceName.equals(printMassage.PW)){
            if (destinationX == x) {
                if (y == 2){
                    if (destinationY == 3 | destinationY == 4){
                        if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY)){
                            System.out.println(printMassage.CANNOT_MOVE);
                        } else {
                            move.move(x, y, destinationX, destinationY);
                            isWhiteTurn = false;
                            selectedName = "";
                        }
                    } else {
                        System.out.println(printMassage.CANNOT_MOVE);
                    }
                } else if (destinationY == y+1){
                    if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                        System.out.println(printMassage.CANNOT_MOVE);
                    else{
                        move.move(x, y, destinationX, destinationY);
                        isWhiteTurn = false;
                        selectedName = "";
                    }
                } else{
                    System.out.println(printMassage.CANNOT_MOVE);
                }
            } else if (destinationX == x + 1 | destinationX == x - 1) {
                if (!isYours(destinationPosition) & positionValue(destinationPosition) != null & destinationY == y+1){
                    move.move(x, y, destinationX, destinationY);
                    isWhiteTurn = false;
                    selectedName = "";
                }
            }else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.RW)){
            if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    move.move(x, y, destinationX, destinationY);
                    isWhiteTurn = false;
                    selectedName = "";
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.NW)) {
            if (Math.abs(destinationY - y) == 2 & Math.abs(destinationX - x) == 1) {
                move.move(x, y, destinationX, destinationY);
                isWhiteTurn = false;
                selectedName = "";
            } else if (Math.abs(destinationY - y) == 1 & Math.abs(destinationX - x) == 2) {
                move.move(x, y, destinationX, destinationY);
                isWhiteTurn = false;
                selectedName = "";
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.BW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    move.move(x, y, destinationX, destinationY);
                    isWhiteTurn = false;
                    selectedName = "";
                }
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.QW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(pieceName,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    move.move(x, y, destinationX, destinationY);
                    isWhiteTurn = false;
                    selectedName = "";
                }
            } else if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(pieceName, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    move.move(x, y, destinationX, destinationY);
                    isWhiteTurn = false;
                    selectedName = "";
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.KW)) {
            if (Math.abs(destinationX-x) < 2 & Math.abs(destinationY-y) < 2){
                move.move(x, y, destinationX, destinationY);
                isWhiteTurn = false;
                selectedName = "";
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        }
    }

    private void makeFirstChessPositions(){
        for (int i=1; i<9; i++){
            for (int j=1; j<9; j++){
                chessPositions2.put((i*10)+j, null);
            }
        }
        chessPositions2.put(11,printMassage.RW);
        chessPositions2.put(12,printMassage.NW);
        chessPositions2.put(13,printMassage.BW);
        chessPositions2.put(14,printMassage.QW);
        chessPositions2.put(15,printMassage.KW);
        chessPositions2.put(16,printMassage.BW);
        chessPositions2.put(17,printMassage.NW);
        chessPositions2.put(18,printMassage.RW);

        chessPositions2.put(21,printMassage.PW);
        chessPositions2.put(22,printMassage.PW);
        chessPositions2.put(23,printMassage.PW);
        chessPositions2.put(24,printMassage.PW);
        chessPositions2.put(25,printMassage.PW);
        chessPositions2.put(26,printMassage.PW);
        chessPositions2.put(27,printMassage.PW);
        chessPositions2.put(28,printMassage.PW);

        chessPositions2.put(71,printMassage.PB);
        chessPositions2.put(72,printMassage.PB);
        chessPositions2.put(73,printMassage.PB);
        chessPositions2.put(74,printMassage.PB);
        chessPositions2.put(75,printMassage.PB);
        chessPositions2.put(76,printMassage.PB);
        chessPositions2.put(77,printMassage.PB);
        chessPositions2.put(78,printMassage.PB);

        chessPositions2.put(81,printMassage.RB);
        chessPositions2.put(82,printMassage.NB);
        chessPositions2.put(83,printMassage.BB);
        chessPositions2.put(84,printMassage.QB);
        chessPositions2.put(85,printMassage.KB);
        chessPositions2.put(86,printMassage.BB);
        chessPositions2.put(87,printMassage.NB);
        chessPositions2.put(88,printMassage.RB);
    }
    private ArrayList<Piece> makeFirstChessPlate(){
        ArrayList<Piece> firstChessPlate = new ArrayList<>();
        for (int i=1; i<=8; i++){
            Point position = new Point(i,2);
            Soldier whiteSoldier = new Soldier("Pw", position);
            firstChessPlate.add(whiteSoldier);
        }
        Point whiteCastlePosition1 = new Point(1,1);
        Castle whiteCastle1 = new Castle("Rw", whiteCastlePosition1);
        firstChessPlate.add(whiteCastle1);
        Point whiteCastlePosition2 = new Point(8,1);
        Castle whiteCastle2 = new Castle("Rw", whiteCastlePosition2);
        firstChessPlate.add(whiteCastle2);
        Point whiteHorsePosition1 = new Point(2,1);
        Horse whiteHorse1 = new Horse("Nw", whiteHorsePosition1);
        firstChessPlate.add(whiteHorse1);
        Point whiteHorsePosition2 = new Point(7,1);
        Horse whiteHorse2 = new Horse("Nw", whiteHorsePosition2);
        firstChessPlate.add(whiteHorse2);
        Point whiteElephantPosition1 = new Point(3,1);
        Elephant whiteElephant1 = new Elephant("Bw", whiteElephantPosition1);
        firstChessPlate.add(whiteElephant1);
        Point whiteElephantPosition2 = new Point(6,1);
        Elephant whiteElephant2 = new Elephant("Bw", whiteElephantPosition2);
        firstChessPlate.add(whiteElephant2);
        Point whiteQueenPosition = new Point(4,1);
        Queen whiteQueen = new Queen("Qw",whiteQueenPosition);
        firstChessPlate.add(whiteQueen);
        Point whiteKingPosition = new Point(5,1);
        King whiteKing = new King("Kw", whiteKingPosition);
        firstChessPlate.add(whiteKing);

        for (int i=1; i<=8; i++){
            Point position = new Point(i,7);
            Soldier blackSoldier = new Soldier("Pb", position);
            firstChessPlate.add(blackSoldier);
        }
        Point blackCastlePosition1 = new Point(1,8);
        Castle blackCastle1 = new Castle("Rb", blackCastlePosition1);
        firstChessPlate.add(blackCastle1);
        Point blackCastlePosition2 = new Point(8,8);
        Castle blackCastle2 = new Castle("Rb", blackCastlePosition2);
        firstChessPlate.add(blackCastle2);
        Point blackHorsePosition1 = new Point(2,8);
        Horse blackHorse1 = new Horse("Nb", blackHorsePosition1);
        firstChessPlate.add(blackHorse1);
        Point blackHorsePosition2 = new Point(7,8);
        Horse blackHorse2 = new Horse("Nb", blackHorsePosition2);
        firstChessPlate.add(blackHorse2);
        Point blackElephantPosition1 = new Point(3,8);
        Elephant blackElephant1 = new Elephant("Bb", blackElephantPosition1);
        firstChessPlate.add(blackElephant1);
        Point blackElephantPosition2 = new Point(6,8);
        Elephant blackElephant2 = new Elephant("Bb", blackElephantPosition2);
        firstChessPlate.add(blackElephant2);
        Point blackQueenPosition = new Point(4,8);
        Queen blackQueen = new Queen("Qb",blackQueenPosition);
        firstChessPlate.add(blackQueen);
        Point blackKingPosition = new Point(5,8);
        King blackKing = new King("Kb", blackKingPosition);
        firstChessPlate.add(blackKing);

        return firstChessPlate;
    }
}



