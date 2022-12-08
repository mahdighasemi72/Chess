package Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class Elephant extends Piece{
    private ArrayList<Point> pointsToCheck;
    public Elephant(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath (Point start, Point destination){
        if (Math.abs(destination.x - start.x) == Math.abs(destination.y - start.y)){
            return true;
        }
        return false;
    }

    public ArrayList<Point> pointsToCheck(Point startPoint, Point destinationPoint){
        if (destinationPoint.x > startPoint.x & destinationPoint.y > startPoint.y) {
            for (int i = 1; i < Math.abs(destinationPoint.x - startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x+i,startPoint.y+i));
            }
        } else if (destinationPoint.x > startPoint.x & destinationPoint.y < startPoint.y) {
            for (int i = 1; i < Math.abs(destinationPoint.x - startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x+i,startPoint.y-i));
            }
        } else if (destinationPoint.x < startPoint.x & destinationPoint.y > startPoint.y) {
            for (int i = 1; i < Math.abs(destinationPoint.x - startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x-i,startPoint.y+i));
            }
        } else if (destinationPoint.x < startPoint.x & destinationPoint.y < startPoint.y) {
            for (int i = 1; i < Math.abs(destinationPoint.x - startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x-i,startPoint.y-i));
            }
        }
        return pointsToCheck;
    }

}
