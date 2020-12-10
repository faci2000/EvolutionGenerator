package MapElements;

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
        switch (direction){
            case N:
                return new Position(this.getX(), this.getY() +1);
            case NE:
                return new Position(this.getX() +1, this.getY() +1);
            case E:
                return new Position(this.getX() +1, this.getY());
            case SE:
                return new Position(this.getX() +1, this.getY() -1);
            case S:
                return new Position(this.getX(), this.getY() -1);
            case SW:
                return new Position(this.getX() -1, this.getY() -1);
            case W:
                return new Position(this.getX() -1, this.getY());
            default:
                return new Position(this.getX() -1, this.getY() +1);

        }
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

    @Override
    public String toString() {
        return "("+ this.getX() +","+ this.getY() +")";
    }

    @Override
    public boolean equals(Object other) {
        return (this.getX() == ((Position) other).getX())&&(this.getY() == ((Position) other).getY());
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
