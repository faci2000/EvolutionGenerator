package Simulation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

public class GlobalVariables {
    public static int INITIAL_ANIMAL_ENERGY;
    public static int ANIMALS_AMOUNT =0;
    public static int ANIMALS_MOVEMENT_ENERGY;
    public static int GRASS_NUTRITIONAL_VALUE;
    public static int INITIAL_NUMBER_OF_ANIMALS;
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static double SCENE_WIDTH =720;
    public static double SCENE_HEIGHT =720;
    public static double JUNGLE_RATIO;
    private static final String FILE_PATH ="parameters.json";

    public static void getInitialValuesFromFile(){
        JSONParser parser = new JSONParser();
        try(FileReader reader = new FileReader(FILE_PATH)){
            JSONObject initialValues = (JSONObject)parser.parse(reader);
            System.out.println(initialValues);
            try {
                INITIAL_NUMBER_OF_ANIMALS = ((Long) initialValues.get("startingAnimals")).intValue();
                MAP_WIDTH = ((Long) initialValues.get("width")).intValue();
                MAP_HEIGHT = ((Long) initialValues.get("height")).intValue();
                INITIAL_ANIMAL_ENERGY = ((Long) initialValues.get("startEnergy")).intValue();
                ANIMALS_MOVEMENT_ENERGY = ((Long) initialValues.get("moveEnergy")).intValue();
                GRASS_NUTRITIONAL_VALUE = ((Long) initialValues.get("plantEnergy")).intValue();
                JUNGLE_RATIO = (Double) initialValues.get("jungleRatio");
            }catch (Exception ex){
                throw new EOFException();
            }

        }catch (ParseException | IOException e){
            e.printStackTrace();
            INITIAL_NUMBER_OF_ANIMALS = 10;
            MAP_WIDTH = 20;
            MAP_HEIGHT = 20;
            INITIAL_ANIMAL_ENERGY = 10;
            ANIMALS_MOVEMENT_ENERGY = 1;
            GRASS_NUTRITIONAL_VALUE = 10;
            JUNGLE_RATIO = 0.3;
        }
    }

}

