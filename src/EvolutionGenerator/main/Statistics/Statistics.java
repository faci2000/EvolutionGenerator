package Statistics;

import Map.Map;
import MapElements.Animal;
import MapElements.Gene;
import Observer.Observer;
import Observer.PiceOfInformation;
import Visitor.Visitor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Statistics implements Observer, Visitor {
    private SimpleIntegerProperty numberOfAnimals;
    double averageNumberOfAnimals;
    private SimpleIntegerProperty numberOfGrasses;
    double averageNumberOfGrasses;
    private HashMap<Gene,Integer> allGenes;
    private HashMap<Gene,Integer> recentGenes;
    private Gene theMostPopularGene;
    private Gene theMostPopularGeneInOverall;
    private ArrayList<SimpleIntegerProperty> theMostPopularGeneTab;
    private SimpleDoubleProperty averageLiveAnimalsLifeSpan;
    double averageLiveAnimalsLifeSpanOverall;
    private SimpleDoubleProperty averageDeadAnimalsLifeSpan;
    double averageDeadAnimalsLifeSpanOverall;
    private SimpleDoubleProperty averageFertilityRate;
    double averageFertilityRateOverall;
    private Map describedMap;
    private AnimalStatisticsObserver animalToObserve;
    boolean newDay=true;


    public Statistics(){
        allGenes = new HashMap<>();
        recentGenes = new HashMap<>();
        this.numberOfAnimals = new SimpleIntegerProperty(0);
        this.numberOfGrasses = new SimpleIntegerProperty(0);
        this.theMostPopularGeneTab = new ArrayList<>(8);
        this.theMostPopularGeneTab = new ArrayList<>();
        this.averageLiveAnimalsLifeSpan = new SimpleDoubleProperty(0);
        this.averageDeadAnimalsLifeSpan = new SimpleDoubleProperty(0);
        this.averageFertilityRate = new SimpleDoubleProperty(0);

        averageNumberOfAnimals=0.0;
        averageNumberOfGrasses=0.0;
        averageDeadAnimalsLifeSpanOverall=0.0;
        averageLiveAnimalsLifeSpanOverall=0.0;
        averageFertilityRateOverall=0.0;

    }


    @Override
    public void update(PiceOfInformation piceOfInformation) {
        if(!piceOfInformation.isGrass()){
            newDay=false;
            if(piceOfInformation.getOldPosition()!=null) {
                setNumberOfAnimals(getNumberOfAnimals()-1);
                averageLiveAnimalsLifeSpan.set(removeFromAverage(averageLiveAnimalsLifeSpan.getValue(), ((Animal) piceOfInformation.getMapItem()).getLifeSpan()-1,numberOfAnimals.getValue()));

                if(piceOfInformation.getNewPosition()==null){
                    describedMap.accept(this,piceOfInformation.getOldPosition(),true);
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
            if(!newDay){
                newDay=true;
                updateOverallStatistics();
            }
        }
    }

    private void updateOverallStatistics(){
        averageNumberOfAnimals =  addNewToAverage(averageNumberOfAnimals,numberOfAnimals.getValue(),describedMap.getDay());
        averageNumberOfGrasses =  addNewToAverage(averageNumberOfGrasses,numberOfGrasses.getValue(),describedMap.getDay());
        averageDeadAnimalsLifeSpanOverall =  addNewToAverage(averageDeadAnimalsLifeSpanOverall,averageDeadAnimalsLifeSpan.getValue(),describedMap.getDay());
        averageLiveAnimalsLifeSpanOverall =  addNewToAverage(averageLiveAnimalsLifeSpanOverall,averageLiveAnimalsLifeSpan.getValue(),describedMap.getDay());
        averageFertilityRateOverall = addNewToAverage(averageFertilityRateOverall,averageFertilityRate.getValue(),describedMap.getDay());
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

    private double addNewToAverage(double average,double value,int animalAmount){
        return Math.max(0, Math.round((average+((value-average)/animalAmount))*100.0)/100.0);
    }

    private double removeFromAverage(double average,double value,int animalAmount){
        return Math.round((average+((average-value)/animalAmount))*100.0)/100.0;
    }


    @Override
    public void visitNewBornAnimal(Animal animal) {
        int numberOfGenes=0;
        if(allGenes.containsKey(animal.getGene())){
            numberOfGenes= allGenes.get(animal.getGene());
            allGenes.remove(animal.getGene());
        }
        allGenes.put(animal.getGene(),numberOfGenes+1);

        numberOfGenes=0;
        if(recentGenes.containsKey(animal.getGene())){
            numberOfGenes= recentGenes.get(animal.getGene());
            recentGenes.remove(animal.getGene());
        }
        recentGenes.put(animal.getGene(),numberOfGenes+1);

        if(getTheMostPopularGene() ==null)
            setTheMostPopularGene(animal.getGene());
        else if(recentGenes.get(getTheMostPopularGene())<recentGenes.get(animal.getGene()))
            setTheMostPopularGene(animal.getGene());

        if(getTheMostPopularGeneInOverall() ==null)
            setTheMostPopularGeneInOverall(animal.getGene());
        else if(allGenes.get(getTheMostPopularGeneInOverall())<allGenes.get(animal.getGene()))
            setTheMostPopularGeneInOverall(animal.getGene());

        System.out.println(getTheMostPopularGene() +"  "+ recentGenes.get(getTheMostPopularGene()));
        setTheMostPopularGeneTab(getTheMostPopularGene().getIntGenome());

    }

    @Override
    public void visitRecentlyDead(Animal animal) {

        int numberOfGenes=0;
        if(recentGenes.containsKey(animal.getGene())){
            numberOfGenes= recentGenes.get(animal.getGene());
            recentGenes.remove(animal.getGene());
        }
        if(numberOfGenes-1>0)
            recentGenes.put(animal.getGene(),numberOfGenes-1);

        theMostPopularGene=null;

        for(Gene gene : recentGenes.keySet()){
            if(theMostPopularGene==null)
                theMostPopularGene=gene;
            if(recentGenes.get(theMostPopularGene)<recentGenes.get(gene))
                theMostPopularGene = gene;
        }

        System.out.println(getTheMostPopularGene() +"  "+ recentGenes.get(getTheMostPopularGene()));

        if(theMostPopularGene!=null)
            setTheMostPopularGeneTab(getTheMostPopularGene().getIntGenome());
        else
            setTheMostPopularGeneTab(new int[]{0,0,0,0,0,0,0,0});
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

    public AnimalStatisticsObserver getAnimalToObserve() {
        return animalToObserve;
    }

    public void setAnimalToObserve(AnimalStatisticsObserver animalToObserve) {
        this.animalToObserve = animalToObserve;
    }

    public Gene getTheMostPopularGene() {
        return theMostPopularGene;
    }

    public void setTheMostPopularGene(Gene theMostPopularGene) {
        this.theMostPopularGene = theMostPopularGene;
    }

    public Gene getTheMostPopularGeneInOverall() {
        return theMostPopularGeneInOverall;
    }

    public void setTheMostPopularGeneInOverall(Gene theMostPopularGeneInOverall) {
        this.theMostPopularGeneInOverall = theMostPopularGeneInOverall;
    }

    public void writeStatisticsData() throws IOException{
        List<String> statisticsData = new ArrayList<>();
        statisticsData.add("Statistics from map " + describedMap.getMapId() +" after "+describedMap.getDay() +" days");
        statisticsData.add("Average number of animals: "+averageNumberOfAnimals);
        statisticsData.add("Average number of grasses: "+averageNumberOfGrasses);
        statisticsData.add("Average live animals' life span: "+averageLiveAnimalsLifeSpanOverall);
        statisticsData.add("Average dead animals' life span: "+averageDeadAnimalsLifeSpanOverall);
        statisticsData.add("Average live animals' fertility rate: "+averageFertilityRateOverall);
        statisticsData.add("The most popular genome / number of animals have had: " + theMostPopularGeneInOverall.toString() + " / " + allGenes.get(theMostPopularGeneInOverall));

        Files.write(Paths.get("statisticsDataMap"+describedMap.getMapId()+".txt"),statisticsData, StandardCharsets.UTF_8, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
    }
}
