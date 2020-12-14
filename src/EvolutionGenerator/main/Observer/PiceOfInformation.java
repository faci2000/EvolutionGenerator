package Observer;

import MapElements.Position;

public class PiceOfInformation {
    private Position oldPosition;
    private Position newPosition;
    public PiceOfInformation(Position oldPosition,Position newPosition){
        this.setOldPosition(oldPosition);
        this.setNewPosition(newPosition);
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
}
