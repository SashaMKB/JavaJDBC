package org.example;

public class Place {
    private int floor;
    private int wardrobe;
    private int shelf;

    public Place(int floor, int wardrobe, int shelf) {
        this.floor = floor;
        this.wardrobe = wardrobe;
        this.shelf = shelf;
    }

    public int getFloor() {
        return floor;
    }

    public int getWardrobe() {
        return wardrobe;
    }

    public int getShelf() {
        return shelf;
    }

}
