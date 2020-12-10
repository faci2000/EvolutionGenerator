package MapElementsTests;
import MapElements.Direction;
import MapElements.Position;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PositionTest {

    @Test
    public void makeMoveInDirection(){
        assertEquals(new Position(0,1),new Position(0,0).makeMoveInDirection(Direction.N));
        assertEquals(new Position(1,1),new Position(0,0).makeMoveInDirection(Direction.NE));
        assertEquals(new Position(1,0),new Position(0,0).makeMoveInDirection(Direction.E));
        assertEquals(new Position(1,-1),new Position(0,0).makeMoveInDirection(Direction.SE));
        assertEquals(new Position(0,-1),new Position(0,0).makeMoveInDirection(Direction.S));
        assertEquals(new Position(-1,-1),new Position(0,0).makeMoveInDirection(Direction.SW));
        assertEquals(new Position(-1,0),new Position(0,0).makeMoveInDirection(Direction.W));
        assertEquals(new Position(-1,1),new Position(0,0).makeMoveInDirection(Direction.NW));
    }
}
