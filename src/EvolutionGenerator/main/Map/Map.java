package Map;

import MapElements.Animal;
import MapElements.Grass;
import MapElements.Position;

import Observer.Observer;
import Observer.Observable;
import Observer.PiceOfInformation;

import java.util.*;

public class Map implements Observer, Observable {
    private HashMap<Position, TreeSet<Animal>> animalsSet;
    private HashMap<Position, Grass> grassesSet;
    private int mapWidth;
    private int mapHeight;
    private int jungleWidth;
    private int jungleHeight;
    private Position jungleOffset;
    private int mapId;
    ArrayList<Observer> observers;

    public Map(int mapWidth,int mapHeight,int jungleWidth,int jungleHeight,int mapId){
        this.animalsSet = new HashMap<>();
        this.grassesSet = new HashMap<>();
        this.setMapWidth(mapWidth);
        this.setMapHeight(mapHeight);
        this.setJungleWidth(jungleWidth);
        this.setJungleHeight(jungleHeight);
        this.setMapId(mapId);
        setJungleOffset(new Position((mapWidth-jungleWidth)/2,(mapHeight-jungleHeight)/2));
        this.observers = new ArrayList<>();
    }

    public boolean isInJungle(int x, int y){
        return ((jungleOffset.getX()<=x)&&((jungleOffset.getX()+ getJungleWidth() -1)>=x)&&(jungleOffset.getY()<=y)&&((jungleOffset.getY()+ getJungleHeight() -1)>=y));
    }

    public void addAnimalToAnimalsSet(Animal animal){
        addAnimalTo(this.getAnimalsSet(),animal);
    }

    public void addAnimalTo(HashMap<Position, TreeSet<Animal>> animalsSet,Animal animal){
        if(!animalsSet.containsKey(animal.getPosition()))
            animalsSet.put(new Position(animal.getPosition()),new TreeSet<>());
        animalsSet.get(animal.getPosition()).add(animal);
    }

    public Animal removeAnimalFromAnimalsSet(Animal animal){
        return removeAnimalFrom(this.getAnimalsSet(),animal);
    }

    public Animal removeAnimalFrom(HashMap<Position, TreeSet<Animal>> animalsSet,Animal animal){
        animalsSet.get(animal.getPosition()).remove(animal);
        if(animalsSet.get(animal.getPosition()).isEmpty())
            animalsSet.remove(animal.getPosition());
        return animal;
    }

    public Grass removeGrassFromGrassSet(Position position){
        return getGrassesSet().remove(position);
    }

    private boolean isGrassOn(Position position){
        return this.getGrassesSet().containsKey(position);
    }

    public void growGrassesOnMap(){
        for(int i=0;i<2;i++){
            Position newGrassPosition;
            do{
                newGrassPosition = Position.generateRandomPositionInRangeWithExcludedScope(getMapWidth(), getMapHeight(), getJungleWidth(), getJungleHeight(), getJungleOffset());
            }while(!isGrassOn(newGrassPosition));
            getGrassesSet().put(new Position(newGrassPosition),new Grass(newGrassPosition, getMapId()));
            do{
                newGrassPosition=Position.generateRandomPositionInRange(getJungleOffset().getX(), getJungleOffset().getY(), getJungleOffset().getX()+ getJungleWidth() -1, getJungleOffset().getY()+ getJungleHeight() -1);
            }while(!isGrassOn(newGrassPosition));
            getGrassesSet().put(new Position(newGrassPosition),new Grass(newGrassPosition, getMapId()));
        }
    }

    public ArrayList<Animal> collectDeadAnimals(){
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        for (Position position : getAnimalsSet().keySet()) {
            for (Animal animal : getAnimalsSet().get(position)) {
                if (animal.isDead()) {                        //possible problem with deleting set, before end of iterating
                    deadAnimals.add(animal);
                    removeAnimalFromAnimalsSet(animal);
                }

            }
        }
        return deadAnimals;
    }

    public void rotateAndMoveEachAnimal(){
        HashMap<Position, TreeSet<Animal>> refreshedAnimalsSet = new HashMap<>();
        for (Position position : getAnimalsSet().keySet()) {
            for (Animal animal : getAnimalsSet().get(position)) {
                //removeAnimalFromAnimalsSet(animal);
                animal.moveYourself(getMapHeight(), getMapWidth());
                addAnimalTo(refreshedAnimalsSet, animal);
            }
        }
        setAnimalsSet(refreshedAnimalsSet);
    }

    public boolean hasMoreItemsThan(HashMap<?,?> hashMap){
        return getAnimalsSet().size()>hashMap.size();
    }

    public void feedAnimals(){
        if(this.hasMoreItemsThan(getGrassesSet()))               // hasMoreItemsThan() is only to make almost whole sentence from java code ;)
            feedAnimalsIteratingOverGrassesSet();
        else
            feedAnimalsIteratingOverAnimalsSet();
    }

    public void feedAnimalsIteratingOverGrassesSet(){
        Iterator<Position> grassesPositionIterator = getGrassesSet().keySet().iterator();
        feedAnimalsUsing(grassesPositionIterator, getGrassesSet());
    }

    public void feedAnimalsIteratingOverAnimalsSet(){
        Iterator<Position> animalPositionIterator = getAnimalsSet().keySet().iterator();
        feedAnimalsUsing(animalPositionIterator, getAnimalsSet());
    }

    public void feedAnimalsUsing(Iterator<Position> positionIterator,HashMap<Position,?> positionHashMap){
        while(positionIterator.hasNext()){
            Position checkPosition = positionIterator.next();
            if(positionHashMap.containsKey(checkPosition)){
                getAnimalsSet().get(checkPosition).first().eat();
                removeGrassFromGrassSet(checkPosition);
            }
        }
    }

    public void reproduceAnimals(){
        Iterator<Position> positionIterator = getAnimalsSet().keySet().iterator();
        while(positionIterator.hasNext()) {
            Position animalsPlace = positionIterator.next();
            if(getAnimalsSet().get(animalsPlace).size()>1){
                Animal newBornAnimal = getAnimalsSet().get(animalsPlace).first().copulateWith(((Animal) getAnimalsSet().get(animalsPlace).toArray()[1]));
                addAnimalToAnimalsSet(newBornAnimal);
            }
        }
    }


    public HashMap<Position, TreeSet<Animal>> getAnimalsSet() {
        return animalsSet;
    }

    public void setAnimalsSet(HashMap<Position, TreeSet<Animal>> animalsSet) {
        this.animalsSet = animalsSet;
    }

    public HashMap<Position, Grass> getGrassesSet() {
        return grassesSet;
    }

    public void setGrassesSet(HashMap<Position, Grass> grassesSet) {
        this.grassesSet = grassesSet;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public void setJungleWidth(int jungleWidth) {
        this.jungleWidth = jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public void setJungleHeight(int jungleHeight) {
        this.jungleHeight = jungleHeight;
    }

    public Position getJungleOffset() {
        return jungleOffset;
    }

    public void setJungleOffset(Position jungleOffset) {
        this.jungleOffset = jungleOffset;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }


    @Override
    public void update(PiceOfInformation piceOfInformation) {
        inform(piceOfInformation);
    }

    @Override
    public void inform(PiceOfInformation piceOfInformation) {
        for(Observer observer : observers)
            observer.update(piceOfInformation);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }
}
