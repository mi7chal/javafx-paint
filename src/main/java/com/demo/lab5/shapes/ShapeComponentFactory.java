package com.demo.lab5.shapes;

import com.demo.lab5.DrawingMode;

/**
 * Factory class to create {@link ShapeComponentView} instances based on the drawing mode.
 */
public class ShapeComponentFactory {
    private ShapeComponentFactory() {
    }

    /**
     * Creates a {@link ShapeComponentView} based on the specified drawing mode.
     *
     * @param mode drawing mode
     * @param x    start x position
     * @param y    start y position
     * @return created shape
     */
    public static ShapeComponentView<?> create(DrawingMode mode, double x, double y) {
        return switch (mode) {
            case CIRCLE -> new CircleComponentView(x, y);
            case RECTANGLE -> new RectangleComponentView(x, y);
            case POLYGON -> new PolygonComponentView(x, y);
            default -> throw new IllegalArgumentException("Unsupported Drawing mode");
        };
    }
}
