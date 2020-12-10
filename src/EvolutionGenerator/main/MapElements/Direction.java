package MapElements;

public enum Direction {
    N,NE,E,SE,S,SW,W,NW;

    public static Direction changeDirection(Direction mainDirection, Direction turn ){
        return Direction.values()[(mainDirection.ordinal()+turn.ordinal())%8];
    }


    @Override
    public String toString() {
        switch(this){
            case N:
                return "N";
            case NE:
                return "NE";
            case E:
                return "E";
            case SE:
                return "SE";
            case S:
                return "S";
            case SW:
                return "SW";
            case W:
                return "W";
            case NW:
                return "NW";
            default:
                throw new IllegalArgumentException();
        }
    }
}
