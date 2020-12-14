package UserInterface;
import Map.Map;
import MapElements.Animal;
import MapElements.Gene;
import MapElements.Position;
import Observer.Observer;
import Observer.PiceOfInformation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppWindow extends Application implements Observer {

    Button button;
    VBox layout;
    Group[][] mapRepresentation;
    GridPane mapGrid;
    Map displayedMap;
    Scene scene;
    double sceneWidth;
    double sceneHeight;

    @Override
    public void start( Stage primaryStage) throws Exception {
        primaryStage.setTitle("EvolutionGenerator");


        layout = new VBox();
        scene = new Scene(layout,720,720);
        sceneHeight=scene.getHeight();
        sceneWidth=scene.getWidth();

        displayedMap=new Map(20,20,10,8,0);
        Animal a1 = new Animal(new Position(5,5),0,new Gene(new int[]{4,4,4,4,4,4,4,4}),10,null,null);
        a1.addObserver(displayedMap);
        Animal a2 = new Animal(new Position(5,6),0,new Gene(new int[]{25,1,1,1,1,1,1,1}),10,null,null);
        a2.addObserver(displayedMap);
        displayedMap.addAnimalToAnimalsSet(a1);
        displayedMap.addAnimalToAnimalsSet(a2);
        displayedMap.addObserver(this);


        mapRepresentation = drawMap();
        layout.getChildren().add(mapGrid);

        primaryStage.setScene(scene);
        primaryStage.show();


        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    System.out.println(a1);
                    System.out.println(a2);
                    displayedMap.collectDeadAnimals();
                    displayedMap.rotateAndMoveEachAnimal();
                    displayedMap.feedAnimals();
                    //displayedMap.reproduceAnimals();
                    displayedMap.growGrassesOnMap();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



    private void dayBreak() {
        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO),
                new KeyFrame(Duration.seconds(0.5))
        );
        beat.setCycleCount(1);
        beat.play();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


    public Group[][] drawMap(){
        mapGrid = new GridPane();
        Group[][] field = new Group[displayedMap.getMapWidth()][displayedMap.getMapHeight()];
        mapGrid = new GridPane();
        mapGrid.setHgap(sceneHeight/displayedMap.getMapHeight()*0.1);
        mapGrid.setVgap(sceneWidth/displayedMap.getMapWidth()*0.1);
        for(int i=0;i<displayedMap.getMapWidth();i++){
            for (int j=0;j<displayedMap.getMapHeight();j++){
                field[i][j]=new Group(new Rectangle(0,0,(sceneWidth-displayedMap.getMapWidth())/displayedMap.getMapWidth(), (sceneHeight-displayedMap.getMapHeight())/ displayedMap.getMapHeight()));
                if(displayedMap.isInJungle(i,j))
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.DARKGREEN);
                else
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.GREEN);
                mapGrid.add(field[i][j],i,displayedMap.getMapHeight()-1-j);

                if(displayedMap.getAnimalsSet().containsKey(new Position(i,j)))
                    field[i][j].getChildren().add(getAnimalRepresentation());

                if(displayedMap.getGrassesSet().containsKey(new Position(i,j)))
                    field[i][j].getChildren().add(getGrassRepresentation());
            }
        }

        return field;
    }

    public Circle getAnimalRepresentation(){
        Circle animal =new Circle((sceneWidth-displayedMap.getMapWidth())/displayedMap.getMapWidth()/2,(sceneHeight-displayedMap.getMapHeight())/ displayedMap.getMapHeight()/2,(sceneHeight-displayedMap.getMapHeight())/ displayedMap.getMapHeight()/2);
        animal.setFill(Color.DEEPPINK);
        return animal;
    }

    public Circle getGrassRepresentation(){
        Circle grass =new Circle((sceneWidth-displayedMap.getMapWidth())/displayedMap.getMapWidth()/2,(sceneHeight-displayedMap.getMapHeight())/ displayedMap.getMapHeight()/2,(sceneHeight-displayedMap.getMapHeight())/ displayedMap.getMapHeight()/2);
        grass.setFill(Color.YELLOW);
        return grass;
    }

    @Override
    public void update(PiceOfInformation piceOfInformation) {
        //System.out.println(piceOfInformation);
        if(!piceOfInformation.isGrass()){
            if(piceOfInformation.getNewPosition()!=null)
                mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getAnimalRepresentation());
            if(piceOfInformation.getOldPosition()!=null)
                mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(1);
        }
        else{
            if(piceOfInformation.getNewPosition()!=null)
                mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getGrassRepresentation());
            if(piceOfInformation.getOldPosition()!=null)
                mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(1);
        }

    }
}
