package Pieces;

import java.awt.*;
import java.util.ArrayList;

public class Queen extends Piece{
    private ArrayList<Point> pointsToCheck;

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

    public ArrayList<Point> pointsToCheck (Point startPoint,Point destinationPoint){
        if (destinationPoint.x > startPoint.x & destinationPoint.y > startPoint.y){
            for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x+i, startPoint.y+i));
            }
        } else if (destinationPoint.x > startPoint.x & destinationPoint.y < startPoint.y) {
            for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x+i, startPoint.y-i));
            }
        } else if (destinationPoint.x < startPoint.x & destinationPoint.y > startPoint.y) {
            for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x-i, startPoint.y+i));
            }
        } else if (destinationPoint.x < startPoint.x & destinationPoint.y < startPoint.y) {
            for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++) {
                pointsToCheck.add(new Point(startPoint.x-i, startPoint.y-i));
            }
        } else if (destinationPoint.x - startPoint.x == 0){
            if (destinationPoint.y > startPoint.y){
                for (int i=1; i< Math.abs(destinationPoint.y-startPoint.y); i++){
                    pointsToCheck.add(new Point(startPoint.x, startPoint.y+i));
                }
            } else {
                for (int i=1; i< Math.abs(destinationPoint.y-startPoint.y); i++){
                    pointsToCheck.add(new Point(startPoint.x, startPoint.y-i));
                }
            }
        }else if (destinationPoint.y - startPoint.y == 0){
            if (destinationPoint.x > startPoint.x){
                for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++){
                    pointsToCheck.add(new Point(startPoint.x+i, startPoint.y));
                }
            } else {
                for (int i=1; i< Math.abs(destinationPoint.x-startPoint.x); i++){
                    pointsToCheck.add(new Point(startPoint.x-i, startPoint.y));
                }
            }
        }
        return pointsToCheck;
    }
}
