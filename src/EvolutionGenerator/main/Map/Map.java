package Map;

import MapElements.Animal;
import MapElements.Grass;
import MapElements.Position;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class Map {
    HashMap<Position, SortedSet<Animal>> AnimalsSet;
    HashMap<Position, Grass> GrassesSet;
    int mapWidth;
    int mapHeight;
    int jungleWidth;
    int jungleHeight;
    Position jungleOffset;
    int mapId;

    public Map(int mapWidth,int mapHeight,int jungleWidth,int jungleHeight,int mapId){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
        this.mapId = mapId;
        jungleOffset=new Position((mapWidth-jungleWidth)/2,(mapHeight-jungleHeight)/2);
    }

    public void addAnimalToAnimalsSet(Animal animal){
        if(!AnimalsSet.containsKey(animal.getPosition()))
           AnimalsSet.put(new Position(animal.getPosition()),new TreeSet<Animal>());
        AnimalsSet.get(animal.getPosition()).add(animal);
    }

    public Animal removeAnimalFromAnimalsSet(Animal animal){
        AnimalsSet.get(animal.getPosition()).remove(animal);
        if(AnimalsSet.get(animal.getPosition()).isEmpty())
            AnimalsSet.remove(animal.getPosition());
        return animal;
    }



}
