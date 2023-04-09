package Menu.GameMenu;

import Menu.ConsoleCommand;
import Menu.Controller;
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
    private Piece selected;
    private boolean isWhiteTurn = true;
    private int undo = 0;
    private Plate plate;
    private ArrayList<Piece> chessPlate;
    private Stack<Piece> destroyedRivalPieces ;
    private Stack<String> moves ;//(pieceName,position,destinationPosition,enemy)
    private Elephant elephant;
    private Horse horse;
    private Castle castle;
    private Queen queen;
    private King king;
    private Soldier soldier;

    public GameMenu(PrintMassage printMassage) {
        this.printMassage = printMassage;
        destroyedRivalPieces = new Stack<>();
        moves = new Stack<>();
        plate = new Plate();
        chessPlate = plate.makeFirstChessPlate();
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
            int positionX = position % 10;
            int positionY = position / 10;
            Point startPoint = new Point(positionX,positionY);
            int destinationPosition = Integer.parseInt(lastMove[2]);
            int destinationPositionX = destinationPosition % 10;
            int destinationPositionY = destinationPosition / 10;
            Point destinationPoint = new Point(destinationPositionX,destinationPositionY);
            String destinationValue = lastMove[3];
            for (Piece piece : chessPlate) {
                if (piece.getPosition() == destinationPoint){
                    piece.setPosition(startPoint);
                }
            }
            if (destinationValue != ""){
                Piece lastDestroyedPiece = destroyedRivalPieces.pop();
                lastDestroyedPiece.setPosition(destinationPoint);
            }
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
                Point destination = new Point(destinationX,destinationY);
                checkPath(selected,destination);
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
    private Point pointOfXY(int x,int y){
        Point point = new Point(x,y);
        return point;
    }
    private boolean isBarrierOnPath(Piece piece, Point destination){
        String pieceName = piece.getName();
        Point start = piece.getPosition();
        ArrayList<Point> pointsToCheck;
        if (pieceName.equals(printMassage.PW)) {
            pointsToCheck = soldier.pointsToCheck(start,destination);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        } else if (pieceName.equals(printMassage.RW)) {
            pointsToCheck = castle.pointsToCheck(start,destination);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        } else if (pieceName.equals(printMassage.BW)) {
            pointsToCheck = elephant.pointsToCheck(start,destination);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        }else if (pieceName.equals(printMassage.QW)) {
            pointsToCheck = queen.pointsToCheck(start, destination);
            for (Point point : pointsToCheck) {
                if (selectedPiece(point.x, point.y) != null)
                    return true;
            }
        }
        return false;
    }

    public void checkPath(Piece piece, Point destination){
        String pieceName = piece.getName();
        Point start = piece.getPosition();
        int x = start.x;
        int y = start.y;
        int destinationX = destination.x;
        int destinationY = destination.y;
        if (pieceName.equals(printMassage.PW)){
            if (destinationX == x && soldier.isCorrectPath(start, destination) && !isBarrierOnPath(piece, destination))
                setMoveVarriables(piece, destination);
            else if (destinationX == x + 1 | destinationX == x - 1) {
                if (!isYours(destinationX,destinationY) & selectedPiece(destinationX,destinationY) != null & destinationY == y+1){
                    setMoveVarriables(piece, destination);
                }
            }else {
                System.out.println(printMassage.CANNOT_MOVE);
            }
        } else if (pieceName.equals(printMassage.RW)){
            processCastleMove(piece, destination);
        } else if (pieceName.equals(printMassage.NW)) {
            processHorseMove(piece, destination);
        } else if (pieceName.equals(printMassage.BW)) {
            processElephantMove(piece,destination);
        } else if (pieceName.equals(printMassage.QW)) {
            processQueenMove(piece, destination);
        } else if (pieceName.equals(printMassage.KW)) {
            processKingMove(piece, destination);
        }
    }

    private void processKingMove(Piece piece, Point destination) {
        Point start = piece.getPosition();
        if (king.isCorrectPath(start, destination) && !isBarrierOnPath(piece, destination)){
            setMoveVarriables(piece, destination);
        }else System.out.println(printMassage.CANNOT_MOVE);
    }

    private void processQueenMove(Piece piece, Point destination) {
        Point start = piece.getPosition();
        if (queen.isCorrectPath(start, destination) && !isBarrierOnPath(piece, destination)){
            setMoveVarriables(piece, destination);
        } else System.out.println(printMassage.CANNOT_MOVE);
    }

    private void processElephantMove(Piece piece, Point destination) {
        Point start = piece.getPosition();
        if (elephant.isCorrectPath(start, destination) && !isBarrierOnPath(piece, destination)){
            setMoveVarriables(piece, destination);
        }
        else System.out.println(printMassage.CANNOT_MOVE);
    }

    private void processHorseMove(Piece piece, Point destination) {
        Point start = piece.getPosition();
        if(horse.isCorrectPath(start, destination)){
            setMoveVarriables(piece, destination);
        }
        else System.out.println(printMassage.CANNOT_MOVE);
    }

    private void processCastleMove(Piece piece, Point destination) {
        Point start = piece.getPosition();
        if (castle.isCorrectPath(start, destination) && !isBarrierOnPath(piece, destination)){
            setMoveVarriables(piece, destination);
        }
        else System.out.println(printMassage.CANNOT_MOVE);
    }

    private void setMoveVarriables(Piece piece, Point destination) {
        String pieceName = piece.getName();
        int x = piece.getPosition().x;
        int y = piece.getPosition().y;
        int position = 10*y + x;
        int destinationX = destination.x;
        int destinationY = destination.y;
        int destinationPosition = 10*destinationY +destinationX;
        if (selectedPiece(destinationX,destinationY) == null){
            piece.setPosition(destination);
            System.out.println(printMassage.MOVED);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + "");
        }else {
            Piece rivalPiece = selectedPiece(destinationX,destinationY);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + rivalPiece.getName());
            destroyedRivalPieces.push(rivalPiece);
            rivalPiece.setPosition(new Point(0,0));
            piece.setPosition(destination);
            System.out.println(printMassage.RIVAL_PIECE_DESTROYED);
        }
        isWhiteTurn = false;
        selected = null;
    }
}



