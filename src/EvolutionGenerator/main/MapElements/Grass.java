package MapElements;

public class Grass extends MapItem{

    static int nutritionalValue=GlobalVariables.grassNutritionalValue;

    public Grass(Position position, int mapId) {
        super(position, mapId);
    }
}
