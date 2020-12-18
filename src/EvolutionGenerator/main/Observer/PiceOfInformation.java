package Observer;

import MapElements.Grass;
import MapElements.MapItem;
import MapElements.Position;

public class PiceOfInformation {
    private Position oldPosition;
    private Position newPosition;
    private MapItem mapItem;


    public PiceOfInformation(Position oldPosition,Position newPosition,MapItem mapItem){
        this.setOldPosition(oldPosition);
        this.setNewPosition(newPosition);
        this.setMapItem(mapItem);
    }


    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }

    public boolean isGrass() {
        return getMapItem() instanceof Grass;
    }

    @Override
    public String toString() {
        return "{" +
                "oldPosition=" + oldPosition +
                ", newPosition=" + newPosition +
                ", isGrass=" + isGrass() +
                '}';
    }

    public MapItem getMapItem() {
        return mapItem;
    }

    public void setMapItem(MapItem mapItem) {
        this.mapItem = mapItem;
    }
}
