package MapElementsTests;

import MapElements.Animal;
import MapElements.Gene;
import MapElements.GlobalVariables;
import MapElements.Position;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    Animal testAnimal = new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null);

    @Test
    public void animalMoveYourselfTest(){
        for(int i=0;i<100;i++){
            Position oldPosition=testAnimal.getPosition();
            testAnimal.moveYourself(4,4);
            assertFalse(oldPosition.equals(testAnimal.getPosition()));
            assertTrue(testAnimal.getPosition().getX()<4&&testAnimal.getPosition().getY()<4&&testAnimal.getPosition().getX()>=0&&testAnimal.getPosition().getY()>=0);
            System.out.println(testAnimal.getPosition());
        }
    }


    @Test
    public void animalEatTest(){
        testAnimal.eat();
        assertTrue(0==testAnimal.compareTo(new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),20,null,null)));
    }

    @Test
    public void animalCopulateWithTest(){
        Animal other = new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null);
        assertEquals(new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),4,null,null),testAnimal.copulateWith(other));
        assertEquals(new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),2,null,null),testAnimal.copulateWith(other));
        assertEquals(new Animal(new Position(0,0),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),2,null,null),testAnimal.copulateWith(other));
        assertEquals(null,testAnimal.copulateWith(other));
    }



}
