package Menu.GameMenu;

import Menu.ConsoleCommand;
import Menu.Controller;
import Menu.GameMenu.GameMenuProcess.MoveProcess;
import Menu.GameMenu.GameMenuProcess.Plate;
import Menu.PrintMassage;
import Pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;

public class GameMenu {
    private Controller controller = Controller.getInstance() ;
    private PrintMassage printMassage;
    private MoveProcess move;
    private Piece selected;
    private int selectedX;
    private int selectedY;
    private boolean isWhiteTurn = true;
    private int undo = 0;
    private Plate plate;
    private ArrayList<Piece> chessPlate;
    private Stack<String> destroyedRivalPieces ;
    private Stack<String> moves ;//(pieceName,position,destinationPosition,enemy)
    private Elephant elephant;
    private Castle castle;

    public GameMenu(PrintMassage printMassage) {
        this.printMassage = printMassage;
        destroyedRivalPieces = new Stack<>();
        moves = new Stack<>();
        chessPlate = plate.makeFirstChessPlate();
        move = new MoveProcess(chessPlate,destroyedRivalPieces,moves,printMassage);
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
            if (!isWhiteTurn){
                System.out.println(printMassage.ALREADY_MOVED);
            } else if (destinationY > 8 | destinationY== 0 | destinationX > 8 | destinationX == 0) {
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (selected == null) {
                System.out.println(printMassage.NOT_SELECTED);
            } else if (isYours(destinationX,destinationY)) {
                System.out.println(printMassage.CANNOT_MOVE);
            } else {
                checkPath(selected,selectedX,selectedY,destinationX,destinationY);
            }
        }
    }

    private void processDeselect() {
        if (selected == null){
            System.out.println(printMassage.NOT_SELECTED);
        }else {
            selected = null;
            System.out.println(printMassage.DESELECTED);
        }
    }

    private Piece selectedPiece(int x, int y){
        Piece selectedPiece;
        for (Piece piece : chessPlate) {
            if (piece.getPosition().x == x && piece.getPosition().y == y){
                selectedPiece = piece;
                return selectedPiece;
            }
        }
        return null;
    }

    private void processSelect(String command) {
        Matcher matcher = ConsoleCommand.SELECT.getStringMatcher(command);
        if (matcher.find()){
            int y = Integer.parseInt(matcher.group(1));
            int x = Integer.parseInt(matcher.group(2));
            if (y > 8 | y== 0 | x > 8 | x == 0){
                System.out.println(printMassage.WRONG_COORDINATION);
            } else if (!isWhiteTurn) {
                System.out.println(printMassage.ALREADY_MOVED);
            } else if (selectedPiece(x,y) == null) {
                System.out.println(printMassage.NO_PIECE_ON_THIS_SPOT);
            } else if (!isYours(x, y)){
                System.out.println(printMassage.SELECT_YOUR_PIECE);
            }else {
                selected = selectedPiece(x,y);
                selectedX = x;
                selectedY = y;
                System.out.println(printMassage.SELECTED);
            }
        }
    }

    private boolean isYours(int x, int y){
        Piece selectedPiece = selectedPiece(x,y);
        String selectedPieceName = selectedPiece.getName();
        if (selectedPieceName == printMassage.PW || selectedPieceName == printMassage.RW ||
                selectedPieceName == printMassage.NW ||selectedPieceName == printMassage.BW ||
                selectedPieceName == printMassage.QW ||selectedPieceName == printMassage.KW)
        {
            return true;
        }
        return false;
    }
    private boolean isBarrierOnPath(Piece piece, int x, int y, int destinationX, int destinationY){
        String pieceName = piece.getName();
        Point startPoint = new Point(x,y);
        Point destinationPoint = new Point(destinationX,destinationY);
        ArrayList<Point> pointsToCheck;
        if (pieceName.equals(printMassage.PW)) {
            if (y == 2){
                if (destinationY == 3){
                    if (selectedPiece(destinationX,destinationY)!= null){
                        return true;
                    }
                } else if (destinationY == 4){
                    if (selectedPiece(destinationX,destinationY)!= null | selectedPiece(x,3)!= null){
                        return true;
                    }
                }
            } else {
                if (selectedPiece(destinationX,destinationY)!= null){
                    return true;
                }
            }
        } else if (pieceName.equals(printMassage.RW)) {
            pointsToCheck = castle.pointsToCheck(startPoint,destinationPoint);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        } else if (pieceName.equals(printMassage.BW)) {
            pointsToCheck = elephant.pointsToCheck(startPoint,destinationPoint);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        }else if (pieceName.equals(printMassage.QW)) {
                if (destinationX > x & destinationY > y){
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (selectedPiece(x+i,y+i)!= null)
                            return true;
                    }
                } else if (destinationX > x & destinationY < y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (selectedPiece(x+i,y-i)!= null)
                            return true;
                    }
                } else if (destinationX < x & destinationY > y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (selectedPiece(x-i,y+i)!= null)
                            return true;
                    }
                } else if (destinationX < x & destinationY < y) {
                    for (int i=1; i< Math.abs(destinationX-x); i++) {
                        if (selectedPiece(x-i,y-i)!= null)
                            return true;
                    }
                } else if (destinationX - x == 0){
                    if (destinationY > y){
                        for (int i=1; i< Math.abs(destinationY-y); i++){
                            if(selectedPiece(x,y+i)!= null){
                                return true;
                            }
                        }
                    } else {
                        for (int i=1; i< Math.abs(destinationY-y); i++){
                            if(selectedPiece(x,y-i)!= null){
                                return true;
                            }
                        }
                    }
                }else if (destinationY - y == 0){
                    if (destinationX > x){
                        for (int i=1; i< Math.abs(destinationX-x); i++){
                            if(selectedPiece(x+i, y)!= null){
                                return true;
                            }
                        }
                    } else {
                        for (int i=1; i< Math.abs(destinationX-x); i++){
                            if(selectedPiece(x-i, y)!= null){
                                return true;
                            }
                        }
                    }
                }
            }
        return false;
    }

    public void checkPath(Piece piece, int x, int y, int destinationX, int destinationY){
        String pieceName = piece.getName();
        if (pieceName.equals(printMassage.PW)){
            if (destinationX == x) {
                if (y == 2){
                    if (destinationY == 3 | destinationY == 4){
                        if (isBarrierOnPath(piece, x, y, destinationX, destinationY)){
                            System.out.println(printMassage.CANNOT_MOVE);
                        } else {
                            setMoveVarriables(x, y, destinationX, destinationY);
                        }
                    } else {
                        System.out.println(printMassage.CANNOT_MOVE);
                    }
                } else if (destinationY == y+1){
                    if (isBarrierOnPath(piece, x, y, destinationX, destinationY))
                        System.out.println(printMassage.CANNOT_MOVE);
                    else{
                        setMoveVarriables(x, y, destinationX, destinationY);
                    }
                } else{
                    System.out.println(printMassage.CANNOT_MOVE);
                }
            } else if (destinationX == x + 1 | destinationX == x - 1) {
                if (!isYours(destinationX,destinationY) & selectedPiece(destinationX,destinationY) != null & destinationY == y+1){
                    setMoveVarriables(x, y, destinationX, destinationY);
                }
            }else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.RW)){
            if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(piece, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    setMoveVarriables(x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.NW)) {
            if (Math.abs(destinationY - y) == 2 & Math.abs(destinationX - x) == 1) {
                setMoveVarriables(x, y, destinationX, destinationY);
            } else if (Math.abs(destinationY - y) == 1 & Math.abs(destinationX - x) == 2) {
                setMoveVarriables(x, y, destinationX, destinationY);
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.BW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(piece,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    setMoveVarriables(x, y, destinationX, destinationY);
                }
            } else
                System.out.println(printMassage.CANNOT_MOVE);
        } else if (pieceName.equals(printMassage.QW)) {
            if (Math.abs(destinationY-y) == Math.abs(destinationX-x)){
                if (isBarrierOnPath(piece,x,y,destinationX,destinationY)){
                    System.out.println(printMassage.CANNOT_MOVE);
                }else {
                    setMoveVarriables(x, y, destinationX, destinationY);
                }
            } else if (destinationX == x | destinationY == y){
                if (isBarrierOnPath(piece, x, y, destinationX, destinationY))
                    System.out.println(printMassage.CANNOT_MOVE);
                else {
                    setMoveVarriables(x, y, destinationX, destinationY);
                }
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.KW)) {
            if (Math.abs(destinationX-x) < 2 & Math.abs(destinationY-y) < 2){
                setMoveVarriables(x, y, destinationX, destinationY);
            } else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        }
    }

    private void setMoveVarriables(int x, int y, int destinationX, int destinationY) {
        move.move(x, y, destinationX, destinationY);
        isWhiteTurn = false;
        selected = null;
    }
}



