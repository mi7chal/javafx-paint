package com.demo.lab5.shapes;

import javafx.scene.shape.Shape;

/**
 * Abstract class representing a shape component view that is created by dragging. For example rectangle or circle can't
 * be created by dragging from start point to the end point, but polygon requires creation of multiple points separately.
 * <p>
 * This class shares functionality of shapes created by dragging.
 */
public abstract class DraggingCreatedShapeComponentView<TargetShape extends Shape> extends ShapeComponentView<TargetShape> {
    protected DraggingCreatedShapeComponentView(TargetShape shape, double x, double y) {
        super(shape, x, y);
    }

    /**
     * Updates the size of the shape during its creation based on the current mouse position.
     *
     * @param x current mouse x position
     * @param y current mouse y position
     */
    public abstract void updateSize(double x, double y);
}
