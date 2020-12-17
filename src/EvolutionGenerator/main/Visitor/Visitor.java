package Visitor;

import MapElements.Animal;

public interface Visitor {
    public void visitNewBornAnimal(Animal animal);
    public void visitRecentlyDead(Animal animal);
}
