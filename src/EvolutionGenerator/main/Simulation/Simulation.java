package Simulation;

import Map.Map;
import Observer.Observer;
import Statistics.Statistics;
import Statistics.AnimalStatisticsObserver;
import UserInterface.MapVisualisation;
import UserInterface.StatisticsVisualisation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.ArrayList;

public class Simulation {

    Map map;
    private HBox simulationPlayground;
    MapVisualisation mapVisualisation;
    Statistics statistics;
    StatisticsVisualisation statisticsVisualisation;
    Timeline daysCycle;
    private boolean stopDayCycle=false;

    public Simulation(int simulationID){
        GlobalVariables.getInitialValuesFromFile();
        setSimulationPlayground(new HBox());
        ArrayList<Observer> mapObservers = new ArrayList<Observer>();

        statistics = new Statistics();
        mapObservers.add(statistics);

        mapVisualisation = new MapVisualisation(statistics);
        mapObservers.add(mapVisualisation);

        map = new Map(simulationID,mapObservers);
        statistics.setDescribedMap(map);
        statistics.setAnimalToObserve(new AnimalStatisticsObserver( map));
        mapVisualisation.setDisplayedMap(map);
        map.spawnPrecursors();


        daysCycle = new Timeline();

        statisticsVisualisation = new StatisticsVisualisation(statistics,daysCycle,this,map,mapVisualisation);

        simulationPlayground.getChildren().add(mapVisualisation.getMapRepresentation());
        simulationPlayground.getChildren().add(statisticsVisualisation.getStatisticsRepresentation());

        start();
    }

    private void  start(){
        map.spawnPrecursors();
        daysCycle.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.1),e->{
                    map.dayCycle();
                    if(isStoppedDayCycle())
                        daysCycle.stop();
                })
        );
        daysCycle.setCycleCount(Timeline.INDEFINITE);
        daysCycle.play();
    }

    public boolean isStoppedDayCycle() {
        return stopDayCycle;
    }

    public void setStopDayCycle(boolean stopDayCycle) {
        this.stopDayCycle = stopDayCycle;
    }

    public HBox getSimulationPlayground() {
        return simulationPlayground;
    }

    public void setSimulationPlayground(HBox simulationPlayground) {
        this.simulationPlayground = simulationPlayground;
    }
}
