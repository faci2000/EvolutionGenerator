package Statistics;

import Map.Map;
import MapElements.Animal;
import Observer.Observer;
import Observer.PiceOfInformation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.HashMap;

public class AnimalStatisticsObserver implements Observer {

    SimpleIntegerProperty children;
    SimpleIntegerProperty descendants;
    SimpleIntegerProperty deathDay;
    SimpleIntegerProperty animalID;
    private Animal observedAnimal;
    private Map observedMap;
    private SimpleIntegerProperty daysLeftToObserve;

    public AnimalStatisticsObserver(Map map){
        map.addObserver(this);
        this.children = new SimpleIntegerProperty(0);
        this.deathDay = new SimpleIntegerProperty(0);
        this.descendants = new SimpleIntegerProperty(0);
        this.animalID = new SimpleIntegerProperty(0);
        this.daysLeftToObserve = new SimpleIntegerProperty(-1);
        this.setObservedMap(map);
    }

    private void displayStatistics(boolean full){
        this.animalID.set(getObservedAnimal().getId());
        this.children.set(getObservedAnimal().getChildren().size());
        HashMap<Integer,Boolean> visited = new HashMap<>();
        visited.put(getObservedAnimal().getId(),true);
        this.descendants.set(getNumberOfDescendants(getObservedAnimal(),visited)-1);
        if(full){
            this.deathDay.set(getObservedMap().getDay());
            ButtonType closeAndStart = new ButtonType("Close", ButtonBar.ButtonData.APPLY);
            Alert alert = new Alert(Alert.AlertType.WARNING,"Animal " + animalID.get() + " died in day " +
                    deathDay.get()+". It lived for " + observedAnimal.getLifeSpan() + " days.",
                    closeAndStart);
            alert.show();
        }
        if(daysLeftToObserve.getValue()==0){
            ButtonType closeAndStart = new ButtonType("Close", ButtonBar.ButtonData.APPLY);
            Alert alert = new Alert(Alert.AlertType.WARNING,"Animal " + animalID.get() + "is alive. It live for " + observedAnimal.getLifeSpan() + " days.",
                    closeAndStart);
            alert.show();
            daysLeftToObserve.set(-1);
        }


    }

    @Override
    public void update(PiceOfInformation piceOfInformation) {
        if(getObservedAnimal() != null && !piceOfInformation.isGrass() && getObservedAnimal().getId()== ((Animal) piceOfInformation.getMapItem()).getId()&&daysLeftToObserve.getValue()!=-1) {
            displayStatistics(false);
            if(daysLeftToObserve.getValue()>0)
                daysLeftToObserve.set(daysLeftToObserve.getValue()-1);
            if ((piceOfInformation.getOldPosition() != null && piceOfInformation.getNewPosition() == null)||daysLeftToObserve.getValue()==0) {
                displayStatistics(true);
            }
        }
    }

    private int getNumberOfDescendants(Animal animal, HashMap<Integer,Boolean> visited){
        if(animal.getChildren().size()==0){
            return 1;
        }
        int amount=1;
        for(int i =0;i<animal.getChildren().size();i++){
            if(!visited.containsKey(animal.getChildren().get(i).getId())){
                visited.put(animal.getChildren().get(i).getId(),true);
                amount+=getNumberOfDescendants(animal.getChildren().get(i),visited);
            }
        }
        return amount;
    }

    public SimpleIntegerProperty getChildrenProperty(){
        return children;
    }

    public SimpleIntegerProperty getDescendantsProperty(){
        return descendants;
    }

    public SimpleIntegerProperty getDeathDayProperty(){
        return deathDay;
    }

    public SimpleIntegerProperty getAnimalIDProperty(){
        return animalID;
    }

    public Animal getObservedAnimal() {
        return observedAnimal;
    }

    public void setObservedAnimal(Animal observedAnimal) {
        this.observedAnimal = observedAnimal;
        if(daysLeftToObserve.getValue()==-1)
            daysLeftToObserve.set(-2);
    }

    public Map getObservedMap() {
        return observedMap;
    }

    public void setObservedMap(Map observedMap) {
        this.observedMap = observedMap;
    }

    public SimpleIntegerProperty getDaysLeftToObserveProperty(){
        return daysLeftToObserve;
    }
}
