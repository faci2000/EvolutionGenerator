package Visitor;

import MapElements.Position;

public interface VisitableElement {
    public void accept(Visitor visitor, Position key,boolean dead);
}
