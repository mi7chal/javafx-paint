package com.demo.lab5.shapes;

import javafx.scene.shape.Rectangle;


/**
 * RectangleComponentView is a component that provides a rectangle shape.
 *
 * @see ShapeComponentView
 */
public class RectangleComponentView extends DraggingCreatedShapeComponentView<Rectangle> {

    public RectangleComponentView(double x, double y) {
        super(new Rectangle(0, 0), x, y);
    }

    @Override
    public void updateSize(double x, double y) {
        Rectangle rectangle = shape;

        if (this.startCoords.x() > x) {
            this.setLayoutX(x);
        }

        if (this.startCoords.y() > y) {
            this.setLayoutY(y);
        }

        rectangle.setWidth(Math.abs(x - startCoords.x()));
        rectangle.setHeight(Math.abs(y - startCoords.y()));
    }

    @Override
    public void resize(double multiplier) {
        Rectangle rectangle = shape;
        rectangle.setWidth(multiplier * rectangle.getWidth());
        rectangle.setHeight(multiplier * rectangle.getHeight());
    }
}
