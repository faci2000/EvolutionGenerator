package MapElements;

import java.util.Objects;
import java.util.Random;

public class Position {
    protected int x;
    protected int y;

    public Position(int x, int y){
        this.setX(x);
        this.setY(y);
    }

    public Position(Position other){
        this.x = other.x;
        this.y = other.y;
    }

    public Position makeMoveInDirection(Direction direction){
        return switch (direction) {
            case N -> new Position(this.getX(), this.getY() + 1);
            case NE -> new Position(this.getX() + 1, this.getY() + 1);
            case E -> new Position(this.getX() + 1, this.getY());
            case SE -> new Position(this.getX() + 1, this.getY() - 1);
            case S -> new Position(this.getX(), this.getY() - 1);
            case SW -> new Position(this.getX() - 1, this.getY() - 1);
            case W -> new Position(this.getX() - 1, this.getY());
            default -> new Position(this.getX() - 1, this.getY() + 1);
        };
    }
    private int checkRange(int number,int range){
        /*if(number>range)
            return (number-1)*-1;
        else if(number<range*-1)
            return (number+1)*-1;
        return number;*/
        return (number+range)%range;
    }

    public void checkAndCorrectPosition(int xMax, int yMax){
        this.setX(checkRange(this.getX(),xMax));
        this.setY(checkRange(this.getY(),yMax));
    }

    public static Position generateRandomPositionInRange(int xMax, int yMax){
        return Position.generateRandomPositionInRange(xMax,yMax,0,0);
    }

    public static Position generateRandomPositionInRange(int xMax, int yMax,int xMin, int yMin){
        return new Position(new Random().nextInt(xMax-xMin+1)+xMin,new Random().nextInt(yMax-yMin+1)+yMin);
    }

    public static Position generateRandomPositionInRangeWithExcludedScope(int xMaxRange, int yMaxRange,int xExcludedMax, int yExcludedMax,Position offset){
        int avbPlaces= xMaxRange*yMaxRange-xExcludedMax*yExcludedMax;
        int selectedPlace= new Random().nextInt(avbPlaces);
        selectedPlace += Math.min(xExcludedMax*yExcludedMax,((Math.max(0,selectedPlace+1-(offset.y*xMaxRange+ offset.x))==0)?0:Math.ceil((((float)Math.max(0,selectedPlace+1-(offset.y*xMaxRange+ offset.x)))/(xMaxRange-xExcludedMax))))*xExcludedMax);
        return new Position(selectedPlace%xMaxRange,selectedPlace/xMaxRange);
    }

    public static Position generateRandomPositionAroundOtherPosition(int xMaxRange, int yMaxRange,Position positionOffset){
        Position returnPosition = Position.generateRandomPositionInRangeWithExcludedScope(3,3,1,1,new Position(1,1));
        returnPosition.setX(returnPosition.getX()+positionOffset.getX()-1);
        returnPosition.setY(returnPosition.getY()+positionOffset.getY()-1);
        returnPosition.checkAndCorrectPosition(xMaxRange,yMaxRange);
        return  returnPosition;
    }

    @Override
    public String toString() {
        return "("+ this.getX() +","+ this.getY() +")";
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Position))
            return false;
        return (this.getX() == ((Position) other).getX())&&(this.getY() == ((Position) other).getY());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
