package Simulation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class GlobalVariables {
    public static int initialAnimalEnergy;
    public static int animalsAmount=0;
    public static int animalsMoveEnergy;
    public static int grassNutritionalValue;
    public static int initialNumberOfAnimals;
    public static int mapWidth;
    public static int mapHeight;
    public static double  sceneWidth=720;
    public static double  sceneHeight=720;
    public static double jungleRatio;
    private static final String filePath="parameters.json";

    public static void getInitialValuesFromFile(){
        JSONParser parser = new JSONParser();
        try(FileReader reader = new FileReader(filePath)){
            JSONObject initialValues = (JSONObject)parser.parse(reader);
            System.out.println(initialValues);
            initialNumberOfAnimals = ((Long) initialValues.get("startingAnimals")).intValue();
            mapWidth = ((Long) initialValues.get("width")).intValue();
            mapHeight = ((Long) initialValues.get("height")).intValue();
            initialAnimalEnergy = ((Long) initialValues.get("startEnergy")).intValue();
            animalsMoveEnergy = ((Long) initialValues.get("moveEnergy")).intValue();
            grassNutritionalValue = ((Long) initialValues.get("plantEnergy")).intValue();
            jungleRatio = (Double) initialValues.get("jungleRatio");
        }catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

}

