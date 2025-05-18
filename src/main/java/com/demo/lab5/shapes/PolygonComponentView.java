package com.demo.lab5.shapes;

import javafx.scene.shape.Polygon;

/**
 * PolygonComponentView is a component that provides a polygon shape.
 *
 * @see ShapeComponentView
 */
public class PolygonComponentView extends ShapeComponentView<Polygon> {

    public PolygonComponentView(double x, double y) {
        super(new Polygon(0, 0), x, y);
    }

    /**
     * Adds node of a polygon.
     *
     * @param x node x coordinate
     * @param y node y coordinate
     */
    public void addNode(double x, double y) {
        Polygon polygon = shape;

        polygon.getPoints().addAll(x - getLayoutX(), y - getLayoutY());

        // All points are relative to the base layout Pane and sometimes node coordinates are less than currently added
        // node coordinates, so they are kinda negative after calculating relative positions. Due to this relative
        // positions we need to update node coordinates.

        if (x < this.getLayoutX()) {
            double diffX = this.getLayoutX() - x;
            this.setLayoutX(x);

            for (int i = 0; i < polygon.getPoints().size(); i += 2) {
                polygon.getPoints().set(i, polygon.getPoints().get(i) + diffX);
            }
        }

        if (y < this.getLayoutY()) {
            double diffY = this.getLayoutY() - y;

            this.setLayoutY(y);

            for (int i = 1; i < polygon.getPoints().size(); i += 2) {
                polygon.getPoints().set(i, polygon.getPoints().get(i) + diffY);
            }
        }
    }

    @Override
    public void resize(double multiplier) {
        Polygon polygon = shape;

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            polygon.getPoints().set(i, polygon.getPoints().get(i) * multiplier);
        }
    }
}
