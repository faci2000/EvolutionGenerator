package UserInterface;

import Map.Map;
import MapElements.Animal;
import MapElements.Position;
import Observer.Observer;
import Observer.PiceOfInformation;
import Simulation.GlobalVariables;
import Statistics.Statistics;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class MapVisualisation implements Observer {
    private Map displayedMap;
    private Group[][] mapRepresentationGroup;
    private GridPane mapRepresentation;

    public MapVisualisation( Statistics statistics){
        setMapRepresentation(new GridPane());
        setMapRepresentationGroup(drawMap(statistics));
    }

    public Circle getAnimalRepresentation(int animalEnergy){
        Circle animal =new Circle((GlobalVariables.sceneWidth)/ GlobalVariables.mapWidth/2,(GlobalVariables.sceneHeight)/ GlobalVariables.mapHeight/2,(GlobalVariables.sceneHeight)/ GlobalVariables.mapHeight/2);
        animal.setFill(Color.DEEPPINK);
        animal.setOpacity(Math.min(1, ((float) animalEnergy)/GlobalVariables.initialAnimalEnergy));
        return animal;
    }

    public Circle getGrassRepresentation(){
        Circle grass =new Circle((GlobalVariables.sceneWidth)/GlobalVariables.mapWidth/2,(GlobalVariables.sceneHeight)/ GlobalVariables.mapHeight/2,(GlobalVariables.sceneHeight)/ GlobalVariables.mapHeight/2);
        grass.setFill(Color.YELLOW);
        return grass;
    }

    public void highlightPosition(int x, int y){
        ((Rectangle) getMapRepresentationGroup()[x][y].getChildren().get(0)).setFill(Color.BLUE);
    }

    public void removeHighlight(int x, int y){
        if(displayedMap.isInJungle(x,y))
            ((Rectangle) getMapRepresentationGroup()[x][y].getChildren().get(0)).setFill(Color.DARKGREEN);
        else
            ((Rectangle) getMapRepresentationGroup()[x][y].getChildren().get(0)).setFill(Color.GREEN);
    }

    public Group[][] drawMap(Statistics statistics){
        Group[][] field = new Group[GlobalVariables.mapWidth][GlobalVariables.mapHeight];
        for(int i=0;i<GlobalVariables.mapWidth;i++){
            for (int j=0;j<GlobalVariables.mapHeight;j++){
                field[i][j]=new Group(new Rectangle(0,0,(GlobalVariables.sceneWidth)/GlobalVariables.mapWidth, (GlobalVariables.sceneHeight)/ GlobalVariables.mapHeight));
                if(Map.isInJungle(i,j))
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.DARKGREEN);
                else
                    ((Rectangle)field[i][j].getChildren().get(0)).setFill(Color.GREEN);
                getMapRepresentation().add(field[i][j],i,GlobalVariables.mapHeight-1-j);
            }
        }
        ButtonType observeThisAnimal = new ButtonType("Observe this Animal", ButtonBar.ButtonData.APPLY);
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        getMapRepresentation().getChildren().forEach(item -> {
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

    @Override
    public void update(PiceOfInformation piceOfInformation) {
        //System.out.println(piceOfInformation);
        if(!piceOfInformation.isGrass()){
            if(piceOfInformation.getNewPosition()!=null){
                getMapRepresentationGroup()[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getAnimalRepresentation(((Animal) piceOfInformation.getMapItem()).getEnergy()));
                //mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().get(mapRepresentation[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().size()-1)
            }

            if(piceOfInformation.getOldPosition()!=null) {
                //mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().size()-1);
                for(Node node : getMapRepresentationGroup()[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren()){
                    if(node instanceof Circle && ((Circle) node).getFill()== Color.DEEPPINK){
                        getMapRepresentationGroup()[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(node);
                        break;
                    }
                }
            }
        }
        else{
            if(piceOfInformation.getNewPosition()!=null)
                getMapRepresentationGroup()[piceOfInformation.getNewPosition().getX()][piceOfInformation.getNewPosition().getY()].getChildren().add(getGrassRepresentation());
            if(piceOfInformation.getOldPosition()!=null){
                //mapRepresentation[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(1);
                for(Node node : getMapRepresentationGroup()[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren()){
                    if(node instanceof Circle && ((Circle) node).getFill()==Color.YELLOW){
                        getMapRepresentationGroup()[piceOfInformation.getOldPosition().getX()][piceOfInformation.getOldPosition().getY()].getChildren().remove(node);
                        break;
                    }
                }
            }
        }

    }

    public Group[][] getMapRepresentationGroup() {
        return mapRepresentationGroup;
    }

    public void setMapRepresentationGroup(Group[][] mapRepresentationGroup) {
        this.mapRepresentationGroup = mapRepresentationGroup;
    }

    public Map getDisplayedMap() {
        return displayedMap;
    }

    public void setDisplayedMap(Map displayedMap) {
        this.displayedMap = displayedMap;
    }

    public GridPane getMapRepresentation() {
        return mapRepresentation;
    }

    public void setMapRepresentation(GridPane mapRepresentation) {
        this.mapRepresentation = mapRepresentation;
    }
}
