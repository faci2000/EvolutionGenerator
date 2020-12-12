package Map;

import MapElements.Animal;
import MapElements.Grass;
import MapElements.Position;

import java.util.*;

public class Map {
    HashMap<Position, SortedSet<Animal>> animalsSet;
    HashMap<Position, Grass> grassesSet;
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
        addAnimalTo(this.animalsSet,animal);
    }

    public void addAnimalTo(HashMap<Position, SortedSet<Animal>> animalsSet,Animal animal){
        if(!animalsSet.containsKey(animal.getPosition()))
            animalsSet.put(new Position(animal.getPosition()),new TreeSet<Animal>());
        animalsSet.get(animal.getPosition()).add(animal);
    }

    public Animal removeAnimalFromAnimalsSet(Animal animal){
        return removeAnimalFrom(this.animalsSet,animal);
    }

    public Animal removeAnimalFrom(HashMap<Position, SortedSet<Animal>> animalsSet,Animal animal){
        animalsSet.get(animal.getPosition()).remove(animal);
        if(animalsSet.get(animal.getPosition()).isEmpty())
            animalsSet.remove(animal.getPosition());
        return animal;
    }

    public Grass removeGrassFromGrassSet(Position position){
        return grassesSet.remove(position);
    }

    private boolean isGrassOn(Position position){
        return this.grassesSet.containsKey(position);
    }

    public void growGrassesOnMap(){
        for(int i=0;i<2;i++){
            Position newGrassPosition;
            do{
                newGrassPosition = Position.generateRandomPositionInRangeWithExcludedScope(mapWidth,mapHeight,jungleWidth,jungleHeight,jungleOffset);
            }while(!isGrassOn(newGrassPosition));
            grassesSet.put(new Position(newGrassPosition),new Grass(newGrassPosition,mapId));
            do{
                newGrassPosition=Position.generateRandomPositionInRange(jungleOffset.getX(),jungleOffset.getY(),jungleOffset.getX()+jungleWidth-1,jungleOffset.getY()+jungleHeight-1);
            }while(!isGrassOn(newGrassPosition));
            grassesSet.put(new Position(newGrassPosition),new Grass(newGrassPosition,mapId));
        }
    }

    public ArrayList<Animal> collectDeadAnimals(){
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        Iterator<Position> positionIterator = animalsSet.keySet().iterator();
        while(positionIterator.hasNext()){
            Iterator<Animal> animalIterator = animalsSet.get(positionIterator.next()).iterator();
            while (animalIterator.hasNext()){
                Animal animal = animalIterator.next();
                if(animal.isDead()){                        //possible problem with deleting set, before end of iterating
                    deadAnimals.add(animal);
                    removeAnimalFromAnimalsSet(animal);
                }

            }
        }
        return deadAnimals;
    }

    public void rotateAndMoveEachAnimal(){
        HashMap<Position, SortedSet<Animal>> refreshedAnimalsSet = new HashMap<>();
        Iterator<Position> positionIterator = animalsSet.keySet().iterator();
        while(positionIterator.hasNext()) {
            Iterator<Animal> animalIterator = animalsSet.get(positionIterator.next()).iterator();
            while (animalIterator.hasNext()) {
                Animal animal = animalIterator.next();
                removeAnimalFromAnimalsSet(animal);
                animal.moveYourself(mapHeight,mapWidth);
                addAnimalTo(refreshedAnimalsSet,animal);
            }
        }
        animalsSet=refreshedAnimalsSet;
    }

    public boolean hasMoreItemsThan(HashMap<?,?> hashMap){
        return animalsSet.size()>hashMap.size();
    }

    public void feedAnimals(){
        if(this.hasMoreItemsThan(grassesSet))               // hasMoreItemsThan() is only to make almost whole sentence from instructions ;)
            feedAnimalsIteratingOverGrassesSet();
        else
            feedAnimalsIteratingOverAnimalsSet();
    }

    public void feedAnimalsIteratingOverGrassesSet(){

    }

    public void feedAnimalsIteratingOverAnimalsSet(){

    }


}
