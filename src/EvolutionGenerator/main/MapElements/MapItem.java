package MapElements;

public abstract class MapItem {
    protected Position position;
    protected int mapId;
    public MapItem(Position position,int mapId){
        this.setPosition(position);
        this.setMapId(mapId);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
}
