package UserInterface;

import Map.Map;
import MapElements.Animal;
import MapElements.Direction;
import MapElements.Gene;
import Simulation.Simulation;
import Statistics.Statistics;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.util.PriorityQueue;

public class StatisticsVisualisation {

    private VBox statisticsRepresentation;
    Statistics statistics;
    Map displayedMap;
    MapVisualisation mapVisualisation;

    public StatisticsVisualisation(Statistics statistics, Timeline timelineToStopStart, Simulation simulation, Map map,MapVisualisation mapVisualisation){
        this.statistics = statistics;
        this.displayedMap = map;
        this.mapVisualisation = mapVisualisation;
        setStatisticsRepresentation(createStatisticsRepresentation(statistics,timelineToStopStart,simulation));
    }

    private Text createFieldBondedTo(SimpleDoubleProperty simpleDoubleProperty){
        Text text  = new Text();
        text.textProperty().bindBidirectional(simpleDoubleProperty,new NumberStringConverter());
        return text;
    }
    private Text createFieldBondedTo(SimpleIntegerProperty simpleIntegerProperty){
        Text text  = new Text();
        text.textProperty().bindBidirectional(simpleIntegerProperty,new NumberStringConverter());
        return text;
    }

    private VBox createStatisticsRepresentation(Statistics statistics, Timeline timelineToStopStart,Simulation simulation){
        VBox statisticsVBox=new VBox();
        statisticsVBox.setPadding(new Insets(20));

        statisticsVBox.getChildren().add(new Label("---GENERAL STATISTICS---"));

        statisticsVBox.getChildren().add(new Label("Number of animals on map: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.numberOfAnimalsProperty()));

        statisticsVBox.getChildren().add(new Label("Number of grasses on map: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.numberOfGrassesProperty()));

        statisticsVBox.getChildren().add(new Label("Average live animals lifespan: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.averageLiveAnimalsLifeSpanProperty()));

        statisticsVBox.getChildren().add(new Label("Average dead animals lifespan: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAverageDeadAnimalsLifeSpan()));

        statisticsVBox.getChildren().add(new Label("Average live animals fertility rate: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAverageFertilityRateProperty()));

        statisticsVBox.getChildren().add(new Label("Dominating genome: "));
        HBox genome = new HBox();
        for(int i=0;i<8;i++){
            genome.getChildren().add(new Label(" " + Direction.values()[i].toString()+": "));
            genome.getChildren().add(createFieldBondedTo(statistics.TheMostPopularGeneTabProperty().get(i)));
        }
        statisticsVBox.getChildren().add(genome);

        Button dominatingGenomeButton = new Button();
        dominatingGenomeButton.setText("Highlight animals with dominating gene");
        dominatingGenomeButton.setDisable(true);
        dominatingGenomeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iterateOverAnimalsWithDominatingGenome(statistics.getTheMostPopularGene(),true);
            }
        });
        dominatingGenomeButton.setPrefWidth(200);
        dominatingGenomeButton.setPrefHeight(50);
        dominatingGenomeButton.setWrapText(true);
        statisticsVBox.getChildren().add(dominatingGenomeButton);

        Button startStopButton = new Button("Stop");
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(startStopButton.getText().equals("Stop")){
                    simulation.setStopDayCycle(true);
                    startStopButton.setText("Resume");
                    dominatingGenomeButton.setDisable(false);
                }
                else{
                    simulation.setStopDayCycle(false);
                    startStopButton.setText("Stop");
                    dominatingGenomeButton.setDisable(true);
                    iterateOverAnimalsWithDominatingGenome(statistics.getTheMostPopularGene(),false);
                    timelineToStopStart.play();

                }
            }
        });
        startStopButton.setPrefWidth(200);
        startStopButton.setPrefHeight(50);
        statisticsVBox.getChildren().add(startStopButton);

        statisticsVBox.getChildren().add(new Label("\n---SELECTED ANIMAL STATISTICS---"));

        statisticsVBox.getChildren().add(new Label("Observed animal: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAnimalToObserve().getAnimalIDProperty()));

        statisticsVBox.getChildren().add(new Label("Children: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAnimalToObserve().getChildrenProperty()));

        statisticsVBox.getChildren().add(new Label("Descendants: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAnimalToObserve().getDescendantsProperty()));

        statisticsVBox.getChildren().add(new Label("Death day: "));
        statisticsVBox.getChildren().add(createFieldBondedTo(statistics.getAnimalToObserve().getDeathDayProperty()));

        statisticsVBox.getChildren().add(new Label("Days lef to observe (for negative number program will wait to the death of the animal): "));
        ((Label) statisticsVBox.getChildren().get(statisticsVBox.getChildren().size() - 1)).setMaxWidth(200);
        ((Label) statisticsVBox.getChildren().get(statisticsVBox.getChildren().size() - 1)).setWrapText(true);
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(statistics.getAnimalToObserve().getDaysLeftToObserveProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(textField);

        statisticsVBox.getChildren().add(new Label("\n---SAVING OVERALL STATISTICS---"));

        Button statisticsButton = new Button("Save overall statistics");
        statisticsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    statistics.writeStatisticsData();

                }catch (IOException e){
                    e.printStackTrace();
                }
                System.out.println("Data saved!");
            }
        });
        statisticsButton.setPrefWidth(200);
        statisticsButton.setPrefHeight(50);
        statisticsVBox.getChildren().add(statisticsButton);

        return statisticsVBox;
    }

    public void iterateOverAnimalsWithDominatingGenome(Gene dominatingGene, boolean highlight){
        for(PriorityQueue<Animal> animals : displayedMap.getAnimalsSet().values()){
            for(Animal animal : animals){
                if(animal.getGene().equals(dominatingGene)){
                    if(highlight)
                        mapVisualisation.highlightPosition(animal.getPosition().getX(),animal.getPosition().getY());
                    else
                        mapVisualisation.removeHighlight(animal.getPosition().getX(),animal.getPosition().getY());
                }
            }
        }
    }

    public VBox getStatisticsRepresentation() {
        return statisticsRepresentation;
    }

    public void setStatisticsRepresentation(VBox statisticsRepresentation) {
        this.statisticsRepresentation = statisticsRepresentation;
    }
}
