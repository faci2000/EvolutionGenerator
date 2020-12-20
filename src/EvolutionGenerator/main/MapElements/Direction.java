package MapElements;

public enum Direction {
    N,NE,E,SE,S,SW,W,NW;

    public static Direction changeDirection(Direction mainDirection, Direction turn ){
        return Direction.values()[(mainDirection.ordinal()+turn.ordinal())%8];
    }


    @Override
    public String toString() {
        return switch (this) {
            case N -> "N";
            case NE -> "NE";
            case E -> "E";
            case SE -> "SE";
            case S -> "S";
            case SW -> "SW";
            case W -> "W";
            case NW -> "NW";
        };
    }
}
