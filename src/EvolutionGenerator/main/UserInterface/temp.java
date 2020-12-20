package UserInterface;
import Map.Map;
import MapElements.Animal;
import MapElements.Direction;
import MapElements.Gene;
import MapElements.Position;
import Observer.Observer;
import Observer.PiceOfInformation;
import Simulation.GlobalVariables;
import Statistics.Statistics;
import Statistics.AnimalStatisticsObserver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;
import org.json.simple.parser.ParseException;


import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class temp extends Application implements Observer {

    public static void main(String[] args) {
        Application.launch(args);
    }

    HBox layout;
    Group[][] mapRepresentation;
    GridPane mapGrid;
    Map displayedMap;
    Scene scene;
    double mapSceneWidth;
    double mapSceneHeight;
    Statistics statistics;
    private boolean stopTimeline=false;
    private Timeline timeline;


    @Override
    public void start( Stage primaryStage) throws Exception {
        primaryStage.setTitle("EvolutionGenerator");
        GlobalVariables.getInitialValuesFromFile();
        setTimeline(new Timeline());
        layout = new HBox();
        scene = new Scene(layout,1000,720);
        mapSceneHeight=scene.getHeight();
        mapSceneWidth =720;
        mapRepresentation = drawMap();
        statistics=new Statistics();
        ArrayList<Observer> temp = new ArrayList<Observer>();
        temp.add(this);
        temp.add(statistics);
        displayedMap=new Map(0,temp);

        statistics.setDescribedMap(displayedMap);
        statistics.setAnimalToObserve(new AnimalStatisticsObserver( displayedMap));

        displayedMap.spawnPrecursors();
//        Animal a1 = new Animal(new Position(10,11),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),100,null,null);
//        a1.addObserver(displayedMap);
//        displayedMap.addAnimalToAnimalsSet(a1);
//        Animal a2 = new Animal(new Position(11,10),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),100,null,null);
//        a2.addObserver(displayedMap);
//        displayedMap.addAnimalToAnimalsSet(a2);
//        Animal a3 = new Animal(new Position(11,11),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),100,null,null);
//        a3.addObserver(displayedMap);
//        displayedMap.addAnimalToAnimalsSet(a3);
//        Animal a4 = new Animal(new Position(10,10),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),100,null,null);
//        a4.addObserver(displayedMap);
//        displayedMap.addAnimalToAnimalsSet(a4);

        //displayedMap.addObserver(this);



        layout.getChildren().add(mapGrid);
        layout.getChildren().add(createStatistics(getTimeline()));

        primaryStage.setScene(scene);
        primaryStage.show();


        getTimeline().getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    displayedMap.dayCycle();
                    if(isStopTimeline())
                        getTimeline().stop();
        }));

        getTimeline().setCycleCount(Timeline.INDEFINITE);
        getTimeline().play();
    }

    private VBox createStatistics(Timeline timelineToStopStart){
        VBox statisticsVBox=new VBox();
        statisticsVBox.setPadding(new Insets(20));

        statisticsVBox.getChildren().add(new Label("---GENERAL STATISTICS---"));
        statisticsVBox.getChildren().add(new Label("Number of animals on map: "));
        Text text  = new Text();
        text.textProperty().bindBidirectional(statistics.numberOfAnimalsProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Number of grasses on map: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.numberOfGrassesProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Average live animals lifespan: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.averageLiveAnimalsLifeSpanProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Average dead animals lifespan: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAverageDeadAnimalsLifeSpan(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Average live animals fertility rate: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAverageFertilityRateProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Dominating genome: "));
        HBox genome = new HBox();
        for(int i=0;i<8;i++){
            genome.getChildren().add(new Label(" " + Direction.values()[i].toString()+": "));
            text = new Text();
            text.textProperty().bindBidirectional(statistics.TheMostPopularGeneTabProperty().get(i),new NumberStringConverter());
            genome.getChildren().add(text);
        }
        statisticsVBox.getChildren().add(genome);

        Button dominatingGenomeButton = new Button();
        dominatingGenomeButton.setDisable(true);
        dominatingGenomeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                iterateOverAnimalsWithDominatingGenome(statistics.getTheMostPopularGene(),true);
            }
        });
        dominatingGenomeButton.setPrefWidth(200);
        dominatingGenomeButton.setPrefHeight(50);
        dominatingGenomeButton.setText("Highlight animals with dominating gene");
        dominatingGenomeButton.setWrapText(true);
        statisticsVBox.getChildren().add(dominatingGenomeButton);

        Button startStopButton = new Button("Stop");
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(startStopButton.getText().equals("Stop")){
                    setStopTimeline(true);
                    startStopButton.setText("Resume");
                    dominatingGenomeButton.setDisable(false);
                }
                else{
                    setStopTimeline(false);
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
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAnimalToObserve().getAnimalIDProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Children: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAnimalToObserve().getChildrenProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Descendants: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAnimalToObserve().getDescendantsProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Death day: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.getAnimalToObserve().getDeathDayProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Days lef to observe (for negative number program will wait to the death of the animal): "));
        ((Label) statisticsVBox.getChildren().get(statisticsVBox.getChildren().size() - 1)).setMaxWidth(200);
        ((Label) statisticsVBox.getChildren().get(statisticsVBox.getChildren().size() - 1)).setWrapText(true);
        TextField  textField = new TextField();
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

    public void highlightPosition(int x, int y){
        ((Rectangle) mapRepresentation[x][y].getChildren().get(0)).setFill(Color.BLUE);
    }

    public void removeHighlight(int x, int y){
        if(displayedMap.isInJungle(x,y))
            ((Rectangle)mapRepresentation[x][y].getChildren().get(0)).setFill(Color.DARKGREEN);
        else
            ((Rectangle)mapRepresentation[x][y].getChildren().get(0)).setFill(Color.GREEN);
    }

    public void iterateOverAnimalsWithDominatingGenome(Gene dominatingGene,boolean highlight){
        for(PriorityQueue<Animal> animals : displayedMap.getAnimalsSet().values()){
            for(Animal animal : animals){
                if(animal.getGene().equals(dominatingGene)){
                    if(highlight)
                        highlightPosition(animal.getPosition().getX(),animal.getPosition().getY());
                    else
                        removeHighlight(animal.getPosition().getX(),animal.getPosition().getY());
                }
            }
        }
    }

    public Group[][] drawMap(){
        mapGrid = new GridPane();
        Group[][] field = new Group[GlobalVariables.mapWidth][GlobalVariables.mapHeight];
        for(int i=0;i<GlobalVariables.mapWidth;i++){
            for (int j=0;j<GlobalVariables.mapHeight;j++){
                field[i][j]=new Group(new Rectangle(0,0,(mapSceneWidth)/GlobalVariables.mapWidth, (mapSceneHeight)/ GlobalVariables.mapHeight));
                if(Map.isInJungle(i,j))
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.DARKGREEN);
                else
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.GREEN);
                mapGrid.add(field[i][j],i,GlobalVariables.mapHeight-1-j);
            }
        }
        ButtonType observeThisAnimal = new ButtonType("Observe this Animal", ButtonBar.ButtonData.APPLY);
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        mapGrid.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Node clickedNode = event.getPickResult().getIntersectedNode();
                    Integer colIndex = GridPane.getColumnIndex(clickedNode.getParent());
                    Integer rowIndex = GridPane.getRowIndex(clickedNode.getParent());
                    System.out.println(colIndex + ":" + Math.abs(rowIndex-GlobalVariables.mapWidth+1));

                    if(clickedNode instanceof Circle){
                        if(((Circle) clickedNode).getFill()==Color.DEEPPINK){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION,"",observeThisAnimal,close);
                            alert.setTitle("Animal Information");
                            alert.setHeaderText("You clicked and animal!");
                            alert.setContentText(displayedMap.getAnimalsSet().get(new Position(colIndex,Math.abs(rowIndex-GlobalVariables.mapWidth+1))).peek().toString()+"Genome:\n"+
                                    displayedMap.getAnimalsSet().get(new Position(colIndex,Math.abs(rowIndex-GlobalVariables.mapWidth+1))).peek().getGene().toString());

                            alert.showAndWait().ifPresent(response -> {
                                if (response == observeThisAnimal){
                                    System.out.println("Observing started");
                                    statistics.getAnimalToObserve().setObservedAnimal(displayedMap.getAnimalsSet().get(new Position(colIndex,Math.abs(rowIndex-GlobalVariables.mapWidth+1))).peek());
                                }
                            });
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Grass!");
                            alert.setContentText("Ops.. You clicked on grass!");
                            alert.showAndWait();
                        }

                    }

                }
            });

        });
        return field;
    }
    
    public Circle getAnimalRepresentation(int animalEnergy){
        Circle animal =new Circle((mapSceneWidth)/GlobalVariables.mapWidth/2,(mapSceneHeight)/ GlobalVariables.mapHeight/2,(mapSceneHeight)/ GlobalVariables.mapHeight/2);
        animal.setFill(Color.DEEPPINK);
        animal.setOpacity(Math.min(1, ((float) animalEnergy)/GlobalVariables.initialAnimalEnergy));
        return animal;
    }

    public Circle getGrassRepresentation(){
        Circle grass =new Circle((mapSceneWidth)/GlobalVariables.mapWidth/2,(mapSceneHeight)/ GlobalVariables.mapHeight/2,(mapSceneHeight)/ GlobalVariables.mapHeight/2);
        grass.setFill(Color.YELLOW);
        return grass;
    }

    @Override
    public void update(PiceOfInformation piceOfInformation) {
        //System.out.println(piceOfInformation);
        if(!piceOfInformation.isGrass()){
            if(piceOfInformation.getNewPosition()!=null){
                mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getAnimalRepresentation(((Animal) piceOfInformation.getMapItem()).getEnergy()));
                //mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().get(mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().size()-1)
            }

            if(piceOfInformation.getOldPosition()!=null) {
                //mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().size()-1);
                for(Node node : mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren()){
                    if(node instanceof Circle && ((Circle) node).getFill()==Color.DEEPPINK){
                        mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(node);
                        break;
                    }
                }
            }
        }
        else{
            if(piceOfInformation.getNewPosition()!=null)
                mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getGrassRepresentation());
            if(piceOfInformation.getOldPosition()!=null){
                //mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(1);
                for(Node node : mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren()){
                    if(node instanceof Circle && ((Circle) node).getFill()==Color.YELLOW){
                        mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(node);
                        break;
                    }
                }
            }
        }

    }

    public boolean isStopTimeline() {
        return stopTimeline;
    }

    public void setStopTimeline(boolean stopTimeline) {
        this.stopTimeline = stopTimeline;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }
}
