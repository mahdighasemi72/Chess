package Menu.GameMenu.GameMenuProcess;

import Menu.PrintMassage;

import java.util.HashMap;
import java.util.Stack;

public class MoveProcess {
    private PrintMassage printMassage;
    private HashMap<Integer,String> chessPositions ;
    private Stack<String> destroyedRivalPieces ;
    private Stack<String> moves ;

    public MoveProcess(HashMap<Integer, String> chessPositions, Stack<String> destroyedRivalPieces, Stack<String> moves, PrintMassage printMassage) {
        this.printMassage = printMassage;
        this.chessPositions = chessPositions;
        this.destroyedRivalPieces = destroyedRivalPieces;
        this.moves = moves;
    }

    public void move(int x, int y, int destinationX, int destinationY){
        int position = (10*y) + x;
        String pieceName = chessPositions.get(position);
        int destinationPosition = (10*destinationY) + destinationX;
        if (chessPositions.get(destinationPosition)==null){
            chessPositions.put(destinationPosition, pieceName);
            chessPositions.put(position, null);
            System.out.println(printMassage.MOVED);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + "");
        } else {
            String rivalPiece = chessPositions.get(destinationPosition);
            moves.push(pieceName + "," + String.valueOf(position) + "," + String.valueOf(destinationPosition) +
                    "," + rivalPiece);
            destroyedRivalPieces.push(rivalPiece);
            chessPositions.put(destinationPosition, pieceName);
            chessPositions.put(position, null);
            System.out.println(printMassage.RIVAL_PIECE_DESTROYED);
        }
    }
}
