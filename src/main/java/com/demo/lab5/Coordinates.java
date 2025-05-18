package com.demo.lab5;

/**
 * Coordinates class represents a point in 2D space. It helps to manage coords.
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public record Coordinates(double x, double y) {
    /**
     * Calculates distance between these coordinates and given ones
     *
     * @param other target coordinates
     * @return distance between coords
     */
    public double distanceTo(Coordinates other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }


    /**
     * Calculates distance between points
     *
     * @param x x pos
     * @param y y pos
     * @return distance
     * @see Coordinates#distanceTo(Coordinates)
     */
    public double distanceTo(double x, double y) {
        return this.distanceTo(new Coordinates(x, y));
    }
}
