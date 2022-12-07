package Menu.GameMenu.GameMenuProcess;

import Pieces.*;

import java.awt.*;
import java.util.ArrayList;

public class Plate {
    public ArrayList<Piece> makeFirstChessPlate(){
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
