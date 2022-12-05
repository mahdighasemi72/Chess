package Pieces;

import java.awt.*;

public class Queen extends Piece{
    private final String symbol = "Qw";
    public Queen(String name, Point position) {
        super(name, position);
    }
    public boolean isCorrectPath(Point start, Point destination){
        if (destination.x == start.x){
            return true;
        } else if (destination.y == start.y) {
            return true;
        } else if (Math.sqrt(destination.x - start.x) == Math.sqrt(destination.y - start.y)){
            return true;
        }
        return false;
    }
}
