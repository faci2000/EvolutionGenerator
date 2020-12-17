package Statistics;

import Map.Map;
import MapElements.Animal;
import MapElements.Gene;
import Observer.Observer;
import Observer.PiceOfInformation;
import Visitor.Visitor;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Statistics implements Observer, Visitor {
    private SimpleIntegerProperty numberOfAnimals;
    private SimpleIntegerProperty numberOfGrasses;
    private HashMap<Gene,Integer> Genes;
    private ArrayList<SimpleIntegerProperty> theMostPopularGeneTab;
    private SimpleDoubleProperty averageLiveAnimalsLifeSpan;
    private SimpleDoubleProperty averageDeadAnimalsLifeSpan;
    private SimpleDoubleProperty averageFertilityRate;
    private Map describedMap;


    public Statistics(){
        Genes = new HashMap<>();
        this.numberOfAnimals = new SimpleIntegerProperty(0);
        this.numberOfGrasses = new SimpleIntegerProperty(0);
        this.theMostPopularGeneTab = new ArrayList<>(8);
        this.theMostPopularGeneTab = new ArrayList<>();
    }


    @Override
    public void update(PiceOfInformation piceOfInformation) {
        if(!piceOfInformation.isGrass()){
            if(piceOfInformation.getNewPosition()!=null){
                setNumberOfAnimals(getNumberOfAnimals()+1);
                if(piceOfInformation.getOldPosition()==null)
                    describedMap.accept(this,piceOfInformation.getNewPosition(),false);

            }

            if(piceOfInformation.getOldPosition()!=null) {
                setNumberOfAnimals(getNumberOfAnimals()-1);
                if(piceOfInformation.getOldPosition()==null)
                    describedMap.accept(this,piceOfInformation.getNewPosition(),true);
            }
        }
        else{
            if(piceOfInformation.getNewPosition()!=null)
                setNumberOfGrasses(getNumberOfGrasses()+1);
            if(piceOfInformation.getOldPosition()!=null){
                setNumberOfGrasses(getNumberOfGrasses()-1);
            }
        }
    }

    public SimpleIntegerProperty numberOfAnimalsProperty() {
        return numberOfAnimals;
    }

    public SimpleIntegerProperty numberOfGrassesProperty(){
        return numberOfGrasses;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals.get();
    }

    public void setNumberOfAnimals(int numberOfAnimals) {
        this.numberOfAnimals.set(numberOfAnimals);
    }

    public int getNumberOfGrasses() {
        return numberOfGrasses.get();
    }

    public void setNumberOfGrasses(int numberOfGrasses) {
        this.numberOfGrasses.set(numberOfGrasses);
    }

    @Override
    public void visitNewBornAnimal(Animal animal) {
        int numberOfGenes=0;
        if(Genes.containsKey(animal.getGene())){
            numberOfGenes=Genes.get(animal.getGene());
            Genes.remove(animal.getGene());
        }
        Genes.put(animal.getGene(),numberOfGenes+1);
        Gene theMostPopularGene=null;
        for(Gene gene : Genes.keySet()){
            if(theMostPopularGene==null||Genes.get(gene)>Genes.get(theMostPopularGene))
                theMostPopularGene=gene;
        }
        System.out.println(theMostPopularGene);
        setTheMostPopularGeneTab(theMostPopularGene.getIntGenome());
    }

    @Override
    public void visitRecentlyDead(Animal animal) {

    }

    public ArrayList<SimpleIntegerProperty> TheMostPopularGeneTabProperty() {
        return theMostPopularGeneTab;
    }

    public void setTheMostPopularGeneTab(int[] theMostPopularGeneTab) {

        if(this.theMostPopularGeneTab.size()==0){
            for(int i=0;i<theMostPopularGeneTab.length;i++)
                this.theMostPopularGeneTab.add(i,new SimpleIntegerProperty(theMostPopularGeneTab[i]));
        }
        else{
            for(int i=0;i<theMostPopularGeneTab.length;i++)
                this.theMostPopularGeneTab.get(i).set(theMostPopularGeneTab[i]);
        }

    }

    public Map getDescribedMap() {
        return describedMap;
    }

    public void setDescribedMap(Map describedMap) {
        this.describedMap = describedMap;
    }
}
