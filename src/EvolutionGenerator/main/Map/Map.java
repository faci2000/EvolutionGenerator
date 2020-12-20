package Map;

import MapElements.Animal;
import MapElements.Gene;
import MapElements.Grass;
import MapElements.Position;

import Observer.Observer;
import Observer.Observable;
import Observer.PiceOfInformation;
import Simulation.GlobalVariables;
import Visitor.VisitableElement;
import Visitor.Visitor;

import java.util.*;

public class Map implements Observer, Observable, VisitableElement {
    private HashMap<Position, PriorityQueue<Animal>> animalsSet;
    private HashMap<Position, Grass> grassesSet;
    private int mapWidth;
    private int mapHeight;
    private int jungleWidth;
    private int jungleHeight;
    private int grassesInJungle;
    private Position jungleOffset;
    private int mapId;
    private int allAnimalsAmount;
    ArrayList<Observer> observers;
    ArrayList<Animal> recentlyDiedAnimals;
    ArrayList<Animal> newBornAnimals;
    private int day;

    public Map(int mapId,ArrayList<Observer> observers){
        this.animalsSet = new HashMap<>();
        this.grassesSet = new HashMap<>();
        this.setMapWidth(GlobalVariables.mapWidth);
        this.setMapHeight(GlobalVariables.mapHeight);
        this.setJungleWidth((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapWidth));
        this.setJungleHeight((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapHeight));
        this.setMapId(mapId);
        setJungleOffset(new Position((mapWidth-jungleWidth)/2,(mapHeight-jungleHeight)/2));
        this.observers = observers;
        //spawnPrecursors();
        setAllAnimalsAmount(0);
        setDay(1);
    }

    public void spawnPrecursors(){
        newBornAnimals = new ArrayList<>();
        Animal animal;
        for(int i=0;i<GlobalVariables.initialNumberOfAnimals;i++){
            int counter=0;
            animal = new Animal(Position.generateRandomPositionInRange(this.getMapWidth()-1,this.getMapHeight()-1),0,Gene.generateRandomGene(),GlobalVariables.initialAnimalEnergy,null,null);
            do{
                counter++;
                animal.setPosition(Position.generateRandomPositionInRange(this.getMapWidth()-1,this.getMapHeight()-1));
            }while((!isFree(animal.getPosition()))&&counter<=GlobalVariables.initialNumberOfAnimals);

            newBornAnimals.add(animal);
            inform(new PiceOfInformation(null,animal.getPosition(),animal));
            this.addAnimalToAnimalsSet(animal);
            animal.addObserver(this);
        }
    }

    public static boolean isInJungle(int x, int y){
        return (((GlobalVariables.mapWidth-((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapWidth)))/2<=x)&&
                (((GlobalVariables.mapWidth-((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapWidth)))/2+ ((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapWidth)) -1)>=x)&&
                ((GlobalVariables.mapHeight-((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapHeight)))/2<=y)&&
                (((GlobalVariables.mapHeight-((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapHeight)))/2+ ((int) Math.round(GlobalVariables.jungleRatio*GlobalVariables.mapHeight)) -1)>=y));
    }

    public void addAnimalToAnimalsSet(Animal animal){
        allAnimalsAmount +=1;
        addAnimalTo(this.getAnimalsSet(),animal);
    }

    public void addAnimalTo(HashMap<Position, PriorityQueue<Animal>> animalsSet,Animal animal){
        if(!animalsSet.containsKey(animal.getPosition()))
            animalsSet.put(new Position(animal.getPosition()),new PriorityQueue<>());
        animalsSet.get(animal.getPosition()).add(animal);
    }

    public void removeAnimalFromAnimalsSet(Animal animal){
        removeAnimalFrom(this.getAnimalsSet(), animal);
    }

    public void removeAnimalFrom(HashMap<Position, PriorityQueue<Animal>> animalsSet, Animal animal){
        animalsSet.get(animal.getPosition()).remove(animal);
        if(animalsSet.get(animal.getPosition()).isEmpty())
            animalsSet.remove(animal.getPosition());
    }

    public void removeGrassFromGrassSet(Position position){
        getGrassesSet().remove(position);
    }

    private boolean isGrassOn(Position position){
        return this.getGrassesSet().containsKey(position);
    }

    private boolean isAnyAnimalOn(Position position){ return this.getAnimalsSet().containsKey(position);}

//    private boolean isInJungle(Position position){
//        return (position.getX()<mapWidth&&position.getX()>(jungleOffset.getX()-1+jungleWidth))||
//                (position.getX()>=0&&position.getX()<jungleOffset.getX())||
//                ((position.getX()<=(jungleOffset.getX()-1+jungleWidth)&&position.getX()>=jungleOffset.getX())&&
//                ((position.getY()<mapHeight&&position.getY()>(jungleOffset.getY()-1+jungleHeight))||(position.getY()>=0&&position.getY()< jungleOffset.getY())));
//    }

    private boolean isJungleFullOfGrass(){
        return grassesInJungle==jungleHeight*jungleWidth;
    }

    private boolean isFieldFullOfGrass(){
        return grassesSet.size()-grassesInJungle>=mapHeight*mapWidth-jungleHeight*jungleWidth;
    }

    private boolean isFree(Position position){
        return !(isGrassOn(position)||isAnyAnimalOn(position));
    }

//    public int getNumberOfAnimals(){
//        int numberOfAnimals=0;
//        for(Position position : animalsSet.keySet())
//            numberOfAnimals+=animalsSet.get(position).size();
//        return  numberOfAnimals;
//    }

    private void growGrassesOnMap(){
        for(int i=0;i<2;i++){

            Position newGrassPosition;
            //System.out.println(isFieldFullOfGrass());
            if(!isFieldFullOfGrass()){
                int counter=0;
                do{
                    counter++;
                    newGrassPosition = Position.generateRandomPositionInRangeWithExcludedScope(getMapWidth(), getMapHeight(), getJungleWidth(), getJungleHeight(), getJungleOffset());
                }while((!isFree(newGrassPosition))&&counter<=mapWidth*mapHeight);
                if((isFree(newGrassPosition))){
                    getGrassesSet().put(new Position(newGrassPosition),new Grass(newGrassPosition, getMapId()));
                    inform(new PiceOfInformation(null,newGrassPosition,grassesSet.get(newGrassPosition)));
                }
            }
            //System.out.println(isJungleFullOfGrass());
            if(!isJungleFullOfGrass()){
                int counter=0;
                do{
                    counter++;
                    newGrassPosition=Position.generateRandomPositionInRange(getJungleOffset().getX()+ getJungleWidth() -1, getJungleOffset().getY()+ getJungleHeight() -1,getJungleOffset().getX(), getJungleOffset().getY()) ;
                }while(((!isFree(newGrassPosition)))&&counter<=jungleWidth*jungleHeight);
                if((isFree(newGrassPosition))){
                    getGrassesSet().put(new Position(newGrassPosition),new Grass(newGrassPosition, getMapId()));
                    inform(new PiceOfInformation(null,newGrassPosition,grassesSet.get(newGrassPosition)));
                    grassesInJungle+=1;
                }
            }
        }
        setDay(getDay()+1);
    }

    private void collectDeadAnimals(){
        recentlyDiedAnimals = new ArrayList<>();
        for (Position position : getAnimalsSet().keySet()) {
            for (Animal animal : getAnimalsSet().get(position)) {
                if (animal.isDead()) {                        //possible problem with deleting set, before end of iterating
                    recentlyDiedAnimals.add(animal);
                    //removeAnimalFromAnimalsSet(animal);
                    inform(new PiceOfInformation(animal.getPosition(),null,animal));
                }

            }
        }
        for(Animal animal : recentlyDiedAnimals)
                removeAnimalFromAnimalsSet(animal);
    }

    private void rotateAndMoveEachAnimal(){
        HashMap<Position, PriorityQueue<Animal>> refreshedAnimalsSet = new HashMap<>();
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

    private void feedAnimals(){
        if(this.hasMoreItemsThan(getGrassesSet()))               // hasMoreItemsThan() is only to make almost whole sentence from java code ;)
            feedAnimalsIteratingOverGrassesSet();
        else
            feedAnimalsIteratingOverAnimalsSet();
    }

    private void feedAnimalsIteratingOverGrassesSet(){
        Iterator<Position> grassesPositionIterator = getGrassesSet().keySet().iterator();
        feedAnimalsUsing(grassesPositionIterator, getAnimalsSet());
    }

    private void feedAnimalsIteratingOverAnimalsSet(){
        Iterator<Position> animalPositionIterator = getAnimalsSet().keySet().iterator();
        feedAnimalsUsing(animalPositionIterator, getGrassesSet());
    }

    private void feedAnimalsUsing(Iterator<Position> positionIterator,HashMap<Position,?> positionHashMap){
        ArrayList<Position> eatenGrasses = new ArrayList<>();
        while(positionIterator.hasNext()){
            Position checkPosition = positionIterator.next();
            if(positionHashMap.containsKey(checkPosition)){
                getAnimalsSet().get(checkPosition).peek().eat();
                eatenGrasses.add(checkPosition);
                //removeGrassFromGrassSet(checkPosition);
                inform(new PiceOfInformation(checkPosition,null,grassesSet.get(checkPosition)));
                if(isInJungle(checkPosition.getX(),checkPosition.getY()))
                    grassesInJungle-=1;
            }
        }
        for(Position position : eatenGrasses)
            removeGrassFromGrassSet(position);
    }

    private void reproduceAnimals(){
        newBornAnimals = new ArrayList<>();
        for (Position animalsPlace : getAnimalsSet().keySet()) {
            if (getAnimalsSet().get(animalsPlace).size() > 1) {
                Animal newBornAnimal = getAnimalsSet().get(animalsPlace).peek().copulateWith(((Animal) getAnimalsSet().get(animalsPlace).toArray()[1]));
                //System.out.println(newBornAnimal);
                if (newBornAnimal != null) {
                    //addAnimalToAnimalsSet(newBornAnimal);
                    int counter = 0;
                    do {
                        counter++;
                        newBornAnimal.setPosition(Position.generateRandomPositionAroundOtherPosition(getMapWidth(), getMapHeight(), animalsPlace));
                    } while ((!isFree(newBornAnimal.getPosition())) && counter <= 10);
                    if ((!isFree(newBornAnimal.getPosition()))) {
                        newBornAnimal.setPosition(animalsPlace);
                    }
                    newBornAnimals.add(newBornAnimal);
                    getAnimalsSet().get(animalsPlace).peek().getChildren().add(newBornAnimal);
                    ((Animal) getAnimalsSet().get(animalsPlace).toArray()[1]).getChildren().add(newBornAnimal);
                    inform(new PiceOfInformation(null, newBornAnimal.getPosition(), newBornAnimal));
                    newBornAnimal.addObserver(this);

                }
            }
        }
        for (Animal animal : newBornAnimals)
            addAnimalToAnimalsSet(animal);
    }

    public void dayCycle(){
        this.collectDeadAnimals();
        this.rotateAndMoveEachAnimal();
        this.feedAnimals();
        this.reproduceAnimals();
        this.growGrassesOnMap();
    }

    public HashMap<Position, PriorityQueue<Animal>> getAnimalsSet() {
        return animalsSet;
    }

    public void setAnimalsSet(HashMap<Position, PriorityQueue<Animal>> animalsSet) {
        this.animalsSet = animalsSet;
    }

    public HashMap<Position, Grass> getGrassesSet() {
        return grassesSet;
    }

    public int getMapWidth() {
        return GlobalVariables.mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return GlobalVariables.mapHeight;
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

    @Override
    public void accept(Visitor visitor, Position key,boolean dead) {
        if(dead){
            for(Animal animal:recentlyDiedAnimals)
                if(animal.getPosition().equals(key)){
                    visitor.visitRecentlyDead(animal);
                    return;
                }
        }
        else {
            for(Animal animal:newBornAnimals)
                if(animal.getPosition().equals(key)){
                    visitor.visitNewBornAnimal(animal);
                    return;
                }
        }



    }

    public int getAllAnimalsAmount() {
        return allAnimalsAmount;
    }

    public void setAllAnimalsAmount(int allAnimalsAmount) {
        this.allAnimalsAmount = allAnimalsAmount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
