package Observer;

import MapElements.Position;

public class PiceOfInformation {
    private Position oldPosition;
    private Position newPosition;
    private boolean isGrass=false;

    public PiceOfInformation(Position oldPosition,Position newPosition){
        this.setOldPosition(oldPosition);
        this.setNewPosition(newPosition);
    }

    public PiceOfInformation(Position oldPosition,Position newPosition,boolean isGrass){
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
        this.setGrass(isGrass);

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
        return isGrass;
    }

    public void setGrass(boolean grass) {
        isGrass = grass;
    }

    @Override
    public String toString() {
        return "{" +
                "oldPosition=" + oldPosition +
                ", newPosition=" + newPosition +
                ", isGrass=" + isGrass +
                '}';
    }
}
