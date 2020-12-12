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

    @Test
    public void generateRandomPositionInRangeTest(){
        for(int i=0;i<100;i++){
            assertTrue(Position.generateRandomPositionInRange(6,6).getX()<6);
            assertTrue(Position.generateRandomPositionInRange(6,6).getY()<6);
            assertTrue(Position.generateRandomPositionInRange(6,6).getX()>=0);
            assertTrue(Position.generateRandomPositionInRange(6,6).getY()>=0);
            //System.out.println(Position.generateRandomPositionInRange(6,6));
        }
        for(int i=0;i<100;i++){
            assertTrue(Position.generateRandomPositionInRange(6,6,1,2).getX()<6);
            assertTrue(Position.generateRandomPositionInRange(6,6,1,2).getY()<6);
            assertTrue(Position.generateRandomPositionInRange(6,6,1,2).getX()>=1);
            assertTrue(Position.generateRandomPositionInRange(6,6,1,2).getY()>=2);
            //System.out.println(Position.generateRandomPositionInRange(6,6,1,2));
        }
    }

    @Test
    public void generateRandomPositionInRangeWithExcludedScopeTest(){
        int xMaxRange=10;
        int yMaxRange=10;
        int xExcludedMax=4;
        int yExcludedMax=5;
        Position offset=new Position(3,3);
        for(int i=0;i<100;i++){
            Position testPosition = Position.generateRandomPositionInRangeWithExcludedScope(xMaxRange,yMaxRange,xExcludedMax,yExcludedMax,offset);
            //System.out.println(testPosition);
            assertTrue((testPosition.getX()<xMaxRange&&testPosition.getX()>(offset.getX()-1+xExcludedMax))||
                    (testPosition.getX()>=0&&testPosition.getX()<offset.getX())||
                    ((testPosition.getX()<=(offset.getX()-1+xExcludedMax)&&testPosition.getX()>=offset.getX())&&
                            ((testPosition.getY()<yMaxRange&&testPosition.getY()>(offset.getY()-1+yExcludedMax))||(testPosition.getY()>=0&&testPosition.getY()< offset.getY()))));

        }

    }
}
