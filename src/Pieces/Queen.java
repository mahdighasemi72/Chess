package Pieces;

import java.awt.*;

public class Queen extends Piece{
    public Queen(String name, Point position) {
        super(name, position);
    }
    public boolean isCorrectPath(Point start, Point destination){
        if (destination.x == start.x){
            return true;
        } else if (destination.y == start.y) {
            return true;
        } else if (Math.abs(destination.x - start.x) == Math.abs(destination.y - start.y)){
            return true;
        }
        return false;
    }
}
