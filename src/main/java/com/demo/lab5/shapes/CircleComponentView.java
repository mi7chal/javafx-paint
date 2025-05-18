package com.demo.lab5.shapes;

import com.demo.lab5.Coordinates;
import javafx.scene.shape.Circle;

public class CircleComponentView extends DraggingCreatedShapeComponentView<Circle> {

    public CircleComponentView(double x, double y) {
        super(new Circle(0, 0, 0), x, y);
    }

    @Override
    public void updateSize(double x, double y) {
        Circle circle = shape;

        var radius = new Coordinates(x, y).distanceTo(this.startCoords.x(), this.startCoords.y());

        this.setLayoutX(startCoords.x() - radius);
        this.setLayoutY(startCoords.y() - radius);

        circle.setCenterX(radius);
        circle.setCenterY(radius);

        circle.setRadius(new Coordinates(x, y).distanceTo(this.startCoords.x(), this.startCoords.y()));
    }

    @Override
    public void resize(double multiplier) {
        Circle circle = shape;

        circle.setRadius(multiplier * circle.getRadius());
        circle.setCenterX(circle.getRadius());
        circle.setCenterY(circle.getRadius());

    }
}
