package MapElements;

import java.util.ArrayList;

public class Animal extends MapItem implements Comparable{
    Gene gene;
    ArrayList<Animal> ancestors;
    ArrayList<Animal> children;
    int energy;
    Direction direction;
    int id;
    public Animal(Position position, int mapId, Gene gene,int energy, Animal stronger, Animal weaker) {
        super(position, mapId);
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
    }

    private void changeDirection(){
        this.direction=Direction.changeDirection(this.direction,this.gene.generateDirection());
    }

    public void moveYourself(int verticalMapMax, int horizontalMapMax){
        this.changeDirection();
        this.setPosition(this.getPosition().makeMoveInDirection(this.direction));
        this.getPosition().checkAndCorrectPosition(horizontalMapMax,verticalMapMax);
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
}
