package Pieces;

import java.awt.*;
import java.util.ArrayList;

public class Soldier extends Piece {
    private ArrayList<Point> pointsToCheck;
    public Soldier(String name, Point position) {
        super(name, position);
    }

    public boolean isCorrectPath(Point start, Point destination){
        if (destination.x == start.x){
            switch (start.y){
                case 2 :
                    if (destination.y == 3 || destination.y == 4)
                        return true;
                    break;
                case 3-7 :
                    if (destination.y == start.y + 1)
                        return true;
                    break;
            }
        }
        return false;
    }

    public ArrayList<Point> pointsToCheck (Point startPoint,Point destinationPoint){
        pointsToCheck.add(new Point(destinationPoint));
        if (startPoint.y == 2 && destinationPoint.y == 4)
            pointsToCheck.add(new Point(startPoint.x,3));
        return pointsToCheck;
    }
}
