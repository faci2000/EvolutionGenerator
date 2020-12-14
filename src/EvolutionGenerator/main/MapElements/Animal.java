package MapElements;

import Map.Map;
import Observer.Observable;
import Observer.Observer;
import Observer.PiceOfInformation;
import javafx.geometry.Pos;

import java.util.ArrayList;

public class Animal extends MapItem implements Comparable, Observable {
    Gene gene;
    ArrayList<Animal> ancestors;
    ArrayList<Animal> children;
    int energy;
    Direction direction;
    int id;
    ArrayList<Observer> observers;
    public Animal(Position position, int mapID, Gene gene, int energy, Animal stronger, Animal weaker) {
        super(position, mapID);
        this.gene=gene;
        this.ancestors = new ArrayList<>();
        if(stronger!=null)
            ancestors.add(stronger);
        if(weaker!=null)
            ancestors.add(weaker);
        this.children = new ArrayList<>();
        this.energy=energy;
        this.id=GlobalVariables.animalsAmount;
        GlobalVariables.animalsAmount+=1;


        if(stronger!=null)
            this.direction=stronger.direction;
        else
            this.direction=Direction.N;

        this.observers = new ArrayList<>();
//        observers.add(map);

    }

    private void changeDirection(){
        this.direction=Direction.changeDirection(this.direction,this.gene.generateDirection());
    }

    public void moveYourself(int verticalMapMax, int horizontalMapMax){
        this.changeDirection();
        Position oldPosition =this.getPosition();
        this.setPosition(this.getPosition().makeMoveInDirection(this.direction));
        this.getPosition().checkAndCorrectPosition(horizontalMapMax,verticalMapMax);
        this.dailyEnergyDrain();
        inform(new PiceOfInformation(oldPosition,this.getPosition()));
    }

    public void eat(){
        this.energy+=Grass.nutritionalValue;
    }

    private void decreaseEnergy(){
        this.energy*=3;
        this.energy/=4;
    }
    public void dailyEnergyDrain(){
        this.energy-=1;
    }

    public Animal copulateWith(Animal other){
        if(this.energy>=4 && other.energy>=4){
            Animal newAnimal = new Animal(this.getPosition(), this.getMapId(),Gene.generateNewGene(this.gene,other.gene),this.energy/4+other.energy/4,this,other);
            this.decreaseEnergy();
            other.decreaseEnergy();
            return newAnimal;
        }
        return null;

    }

    public Boolean isDead(){
        return energy==0;
    }

    @Override
    public int compareTo(Object o) {
        return this.energy-((Animal)o).energy;
    }

    @Override
    public boolean equals(Object other) {
        return this.energy== ((Animal) other).energy;
    }

    @Override
    public String toString() {
        return "Animal "+id+"\n"+position.toString()+"\n"+"Energy: "+energy+"\n";
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
