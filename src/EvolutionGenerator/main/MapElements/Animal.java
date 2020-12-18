package MapElements;

import Observer.Observable;
import Observer.Observer;
import Observer.PiceOfInformation;
import Simulation.GlobalVariables;

import java.util.ArrayList;

public class Animal extends MapItem implements Comparable, Observable {
    private Gene gene;
    private ArrayList<Animal> ancestors;
    private ArrayList<Animal> children;
    private int energy;
    Direction direction;
    private int id;
    ArrayList<Observer> observers;
    private int lifeSpan;
    public Animal(Position position, int mapID, Gene gene, int energy, Animal stronger, Animal weaker) {
        super(position, mapID);
        this.setGene(gene);
        this.setAncestors(new ArrayList<>());
        if(stronger!=null)
            getAncestors().add(stronger);
        if(weaker!=null)
            getAncestors().add(weaker);
        this.setChildren(new ArrayList<>());
        this.setEnergy(energy);
        this.setId(GlobalVariables.animalsAmount);
        GlobalVariables.animalsAmount+=1;


        if(stronger!=null)
            this.direction=stronger.direction;
        else
            this.direction=Direction.N;

        this.observers = new ArrayList<>();
//        observers.add(map);

        setLifeSpan(0);
    }

    private void changeDirection(){
        this.direction=Direction.changeDirection(this.direction, this.getGene().generateDirection());
    }

    public void moveYourself(int verticalMapMax, int horizontalMapMax){
        this.changeDirection();
        Position oldPosition =this.getPosition();
        this.setPosition(this.getPosition().makeMoveInDirection(this.direction));
        this.getPosition().checkAndCorrectPosition(horizontalMapMax,verticalMapMax);
        this.dailyEnergyDrain();
        this.setLifeSpan(this.getLifeSpan() + 1);
        inform(new PiceOfInformation(oldPosition,this.getPosition(),(MapItem) this));
    }

    public void eat(){
        this.setEnergy(this.getEnergy() + GlobalVariables.grassNutritionalValue);
    }

    private void decreaseEnergy(){
        this.setEnergy(this.getEnergy() * 3);
        this.setEnergy(this.getEnergy() / 4);
    }
    public void dailyEnergyDrain(){
        this.setEnergy(this.getEnergy() - GlobalVariables.animalsMoveEnergy);
    }

    public Animal copulateWith(Animal other){
        if(this.getEnergy() >=4 && other.getEnergy() >=4){
            Animal newAnimal = new Animal(this.getPosition(), this.getMapId(),Gene.generateNewGene(this.getGene(), other.getGene()), this.getEnergy() /4+ other.getEnergy() /4,this,other);
            this.decreaseEnergy();
            other.decreaseEnergy();
            return newAnimal;
        }
        return null;

    }

    public Boolean isDead(){
        return getEnergy() ==0;
    }

    @Override
    public int compareTo(Object o) {
        return this.getEnergy() - ((Animal) o).getEnergy();
    }

    @Override
    public boolean equals(Object other) {
        return this.getEnergy() == ((Animal) other).getEnergy();
    }

    @Override
    public String toString() {
        return "Animal "+ getId() +"\n"+position.toString()+"\n"+"Energy: "+ getEnergy() +"\n";
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

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public ArrayList<Animal> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Animal> children) {
        this.children = children;
    }

    public ArrayList<Animal> getAncestors() {
        return ancestors;
    }

    public void setAncestors(ArrayList<Animal> ancestors) {
        this.ancestors = ancestors;
    }
}
