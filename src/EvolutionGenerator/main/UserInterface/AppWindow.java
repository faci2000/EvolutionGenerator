package UserInterface;

import Simulation.Simulation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        TabPane simulations = new TabPane();
        simulations.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Simulation simulation1 = new Simulation(1);
        Simulation simulation2 = new Simulation(2);

        Tab simulation1Tab = new Tab("Map1",simulation1.getSimulationPlayground());
        Tab simulation2Tab = new Tab("Map2",simulation2.getSimulationPlayground());



        simulations.getTabs().add(simulation1Tab);
        simulations.getTabs().add(simulation2Tab);

        VBox vbox = new VBox();
        vbox.getChildren().add(simulations);
        Scene scene = new Scene(vbox,1000,749);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Evolution Generator by faci2000");
        stage.show();
    }
}
