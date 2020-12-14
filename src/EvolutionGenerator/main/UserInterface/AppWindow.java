package UserInterface;
import Map.Map;
import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;

public class AppWindow extends Application {

    Button button;

    @Override
    public void start( Stage primaryStage) throws Exception {
        primaryStage.setTitle("EvolutionGenerator");
       button=new Button();
       button.setText("ClickMe");



        VBox layout = new VBox();
        Scene scene = new Scene(layout,720,720);
        layout.getChildren().add(displayMap(new Map(80,100,20,50,0),scene.getWidth(),scene.getHeight()));


        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    public GridPane displayMap(Map map,double sceneWidth, double sceneHeight){
        Group[][] field = new Group[map.getMapWidth()][map.getMapHeight()];
        GridPane mapRepresentation = new GridPane();
        mapRepresentation.setHgap(sceneHeight/map.getMapHeight()*0.2);
        mapRepresentation.setVgap(sceneWidth/map.getMapWidth()*0.2);
        for(int i=0;i< map.getMapWidth();i++){
            for (int j=0;j< map.getMapHeight();j++){
                field[i][j]=new Group(new Rectangle(0,0,(sceneWidth-map.getMapWidth())/map.getMapWidth(), (sceneHeight-map.getMapHeight())/ map.getMapHeight()));
                if(map.isInJungle(i,j))
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.DARKGREEN);
                else
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.GREEN);
                mapRepresentation.add(field[i][j],i,j);
            }
        }
        return mapRepresentation;

    }
}
