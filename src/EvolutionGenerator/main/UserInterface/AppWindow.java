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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;


import java.util.ArrayList;

public class AppWindow extends Application implements Observer {

    public static void main(String[] args) {
        Application.launch(args);
    }

    Button button;
    HBox layout;
    Group[][] mapRepresentation;
    GridPane mapGrid;
    Map displayedMap;
    Scene scene;
    double mapSceneWidth;
    double mapSceneHeight;
    Statistics statistics;
    boolean stopTimeline=false;


    @Override
    public void start( Stage primaryStage) throws Exception {
        primaryStage.setTitle("EvolutionGenerator");
        GlobalVariables.getInitialValuesFromFile();

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


        Timeline timeline = new Timeline();
        layout.getChildren().add(mapGrid);
        layout.getChildren().add(createStatistics(timeline));

        primaryStage.setScene(scene);
        primaryStage.show();


        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    displayedMap.collectDeadAnimals();
                    displayedMap.rotateAndMoveEachAnimal();
                    displayedMap.feedAnimals();
                    displayedMap.reproduceAnimals();
                    displayedMap.growGrassesOnMap();
                    if(stopTimeline)
                        timeline.stop();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private VBox createStatistics(Timeline timelineToStopStart){
        VBox statisticsVBox=new VBox();
        statisticsVBox.setPadding(new Insets(20));


        statisticsVBox.getChildren().add(new Label("Number of animals on map: "));
        Text text  = new Text();
        text.textProperty().bindBidirectional(statistics.numberOfAnimalsProperty(),new NumberStringConverter());
        statisticsVBox.getChildren().add(text);

        statisticsVBox.getChildren().add(new Label("Number of grasses on map: "));
        text  = new Text();
        text.textProperty().bindBidirectional(statistics.numberOfGrassesProperty(),new NumberStringConverter());
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

        Button startStopButton = new Button("STOP");
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(startStopButton.getText().equals("STOP")){
                    stopTimeline=true;
                    startStopButton.setText("START");
                }
                else{
                    stopTimeline=false;
                    timelineToStopStart.play();
                    startStopButton.setText("STOP");
                }
            }
        });
        startStopButton.setPrefWidth(100);
        startStopButton.setPrefHeight(50);
        statisticsVBox.getChildren().add(startStopButton);

        return statisticsVBox;
    }



    public Group[][] drawMap(){
        mapGrid = new GridPane();
        Group[][] field = new Group[GlobalVariables.mapWidth][GlobalVariables.mapHeight];
        mapGrid = new GridPane();
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
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Animal Information");
                            alert.setHeaderText("You clicked and animal!");
                            alert.setContentText(displayedMap.getAnimalsSet().get(new Position(colIndex,Math.abs(rowIndex-GlobalVariables.mapWidth+1))).peek().toString()+"Genome:\n"+
                                    displayedMap.getAnimalsSet().get(new Position(colIndex,Math.abs(rowIndex-GlobalVariables.mapWidth+1))).peek().getGene().toString());

                            alert.showAndWait();
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

}
