package MapTestes;

import Map.Map;
import MapElements.Animal;
import MapElements.Gene;
import MapElements.Grass;
import MapElements.Position;
import Simulation.GlobalVariables;
import org.junit.jupiter.api.Test;
import java.lang.reflect.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MapTests {

    Map testMap = new Map(1,new ArrayList<>());
    @Test
    public void spawnPrecursorsTest(){
        testMap.spawnPrecursors();
        assertEquals(GlobalVariables.INITIAL_NUMBER_OF_ANIMALS,testMap.getAnimalsSet().size());
    }


    @Test
    public void feedAnimalsTest(){
        testMap.addAnimalToAnimalsSet(new Animal(new Position(0,1),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(0,1),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),20,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(1,1),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(1,1),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));

        testMap.getGrassesSet().put(new Position(0,1),new Grass(new Position(0,1),1));
        testMap.getGrassesSet().put(new Position(1,1),new Grass(new Position(1,1),1));
        Method method;
        try {
            method = testMap.getClass().getMethod("feedAnimals");
            assertEquals(11,testMap.getAnimalsSet().get(new Position(0,1)).peek().getEnergy());
            assertEquals(11,testMap.getAnimalsSet().get(new Position(1,1)).peek().getEnergy());
            assertEquals(10,testMap.getAnimalsSet().get(new Position(1,1)).peek().getEnergy());
        } catch (SecurityException e) {
            System.out.println(e);
        }
        catch (NoSuchMethodException e) {
            System.out.println(e);
        }
    }

    @Test
    public void rotateAndMoveEachAnimalTest(){
        testMap.addAnimalToAnimalsSet(new Animal(new Position(0,0),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(10,10),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));

        Method method;
        try {
            method = testMap.getClass().getMethod("rotateAndMoveEachAnimal");
            assertFalse(testMap.getAnimalsSet().containsKey(new Position(10,10)));
            assertFalse(testMap.getAnimalsSet().containsKey(new Position(0,0)));

        } catch (SecurityException e) {
            System.out.println(e);
        }
        catch (NoSuchMethodException e) {
            System.out.println(e);
        }
    }

    @Test
    public void collectDeadAnimalsTest(){
        testMap.addAnimalToAnimalsSet(new Animal(new Position(0,0),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),0,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(10,10),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));

        Method method;
        try {
            method = testMap.getClass().getMethod("collectDeadAnimals");
            assertTrue(testMap.getAnimalsSet().containsKey(new Position(10,10)));
            assertFalse(testMap.getAnimalsSet().containsKey(new Position(0,0)));

        } catch (SecurityException e) {
            System.out.println(e);
        }
        catch (NoSuchMethodException e) {
            System.out.println(e);
        }
    }

    @Test
    public void reproduceAnimalsTest(){
        testMap.addAnimalToAnimalsSet(new Animal(new Position(10,10),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),0,null,null));
        testMap.addAnimalToAnimalsSet(new Animal(new Position(10,10),testMap.getMapId(),new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null));

        Method method;
        try {
            method = testMap.getClass().getMethod("reproduceAnimals");
            assertFalse(10!=testMap.getAnimalsSet().get(new Position(10,10)).peek().getEnergy());

        } catch (SecurityException e) {
            System.out.println(e);
        }
        catch (NoSuchMethodException e) {
            System.out.println(e);
        }
    }

}
