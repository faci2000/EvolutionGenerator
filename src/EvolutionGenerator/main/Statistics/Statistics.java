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
    Gene theMostPopularGene;
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
        this.averageLiveAnimalsLifeSpan = new SimpleDoubleProperty(0);
        this.averageDeadAnimalsLifeSpan = new SimpleDoubleProperty(0);
        this.averageFertilityRate = new SimpleDoubleProperty(0);
    }


    @Override
    public void update(PiceOfInformation piceOfInformation) {
        if(!piceOfInformation.isGrass()){
            if(piceOfInformation.getOldPosition()!=null) {
                setNumberOfAnimals(getNumberOfAnimals()-1);
                averageLiveAnimalsLifeSpan.set(removeFromAverage(averageLiveAnimalsLifeSpan.getValue(), ((Animal) piceOfInformation.getMapItem()).getLifeSpan()-1,numberOfAnimals.getValue()));

                if(piceOfInformation.getNewPosition()==null){
                    //describedMap.accept(this,piceOfInformation.getNewPosition(),true);
                    averageFertilityRate.set(removeFromAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getChildren().size(),numberOfAnimals.getValue()));
                    averageDeadAnimalsLifeSpan.set(addNewToAverage(averageDeadAnimalsLifeSpan.getValue(),((Animal) piceOfInformation.getMapItem()).getLifeSpan(),describedMap.getAllAnimalsAmount()-numberOfAnimals.get()));
                }
            }

            if(piceOfInformation.getNewPosition()!=null){
                setNumberOfAnimals(getNumberOfAnimals()+1);
                averageLiveAnimalsLifeSpan.set(addNewToAverage(averageLiveAnimalsLifeSpan.getValue(),((Animal) piceOfInformation.getMapItem()).getLifeSpan(),numberOfAnimals.getValue()));

                if(piceOfInformation.getOldPosition()==null){
                    describedMap.accept(this,piceOfInformation.getNewPosition(),false);
                    averageFertilityRate.set(addNewToAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getChildren().size(),numberOfAnimals.getValue()));
                    if(((Animal) piceOfInformation.getMapItem()).getAncestors().size()==2){
                        averageFertilityRate.set(removeFromAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getAncestors().get(0).getChildren().size()-1,numberOfAnimals.getValue()));
                        averageFertilityRate.set(removeFromAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getAncestors().get(1).getChildren().size()-1,numberOfAnimals.getValue()));
                        averageFertilityRate.set(addNewToAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getAncestors().get(0).getChildren().size(),numberOfAnimals.getValue()));
                        averageFertilityRate.set(addNewToAverage(averageFertilityRate.getValue(),((Animal) piceOfInformation.getMapItem()).getAncestors().get(1).getChildren().size(),numberOfAnimals.getValue()));
                    }

                }
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

    public SimpleDoubleProperty averageLiveAnimalsLifeSpanProperty(){
        return averageLiveAnimalsLifeSpan;
    }

    public SimpleDoubleProperty getAverageDeadAnimalsLifeSpan(){
        return averageDeadAnimalsLifeSpan;
    }
    public SimpleDoubleProperty getAverageFertilityRateProperty() {
        return averageFertilityRate;
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

    private double addNewToAverage(double average,int value,int animalAmount){
        return Math.round((average+((value-average)/animalAmount))*100.0)/100.0;
    }

    private double removeFromAverage(double average,int value,int animalAmount){
        return Math.round((average+((average-value)/animalAmount))*100.0)/100.0;
    }


    @Override
    public void visitNewBornAnimal(Animal animal) {
        int numberOfGenes=0;
        if(Genes.containsKey(animal.getGene())){
            numberOfGenes=Genes.get(animal.getGene());
            Genes.remove(animal.getGene());
        }
        Genes.put(animal.getGene(),numberOfGenes+1);

        if(theMostPopularGene==null)
            theMostPopularGene=animal.getGene();
        else if(Genes.get(theMostPopularGene)<Genes.get(animal.getGene()))
            theMostPopularGene=animal.getGene();
//        Gene theMostPopularGene=null;
//        for(Gene gene : Genes.keySet()){
//            if(theMostPopularGene==null||Genes.get(gene)>Genes.get(theMostPopularGene))
//                theMostPopularGene=gene;
//        }
        System.out.println(theMostPopularGene+"  "+Genes.get(theMostPopularGene));
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
