package com.demo.lab5;

import com.demo.lab5.shapes.ShapeComponentView;

import java.util.HashSet;
import java.util.Set;

/**
 * DrawingPaneStateManager holds state for the drawing pane. It also manages selection events. It is used to share this
 * state between different shapes by just passing it to them.
 */
public class DrawingPaneStateManager {
    /**
     * List of listeners for selected shape events. It is used to notify listeners when the selected shape changes.
     * These listeners which should belong mainly to shapes are used to for example hide the toolbar etc.
     *
     *
     * It prevents two shapes being selected at the same time.
     */
    private final Set<SelectedShapeListener> selectedShapeListeners;
    /**
     * Current drawing mode.
     */
    private DrawingMode mode;
    /**
     * Currently selected shape. It is null if no shape is selected.
     */
    private ShapeComponentView<?> selectedShape;
    /**
     * Shape which is currently drawn by the user.
     */
    private ShapeComponentView<?> drawnShape;

    public DrawingPaneStateManager() {
        this.mode = DrawingMode.SELECT_SHAPE;
        this.selectedShapeListeners = new HashSet<>();
    }

    public DrawingMode getMode() {
        return mode;
    }

    public void setMode(DrawingMode mode) {
        this.mode = mode;
    }

    public ShapeComponentView<?> getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(ShapeComponentView<?> selectedShape) {
        this.selectedShape = selectedShape;

        for (SelectedShapeListener listener : this.selectedShapeListeners) {
            listener.run(selectedShape);
        }
    }

    public void addSelectedShapeListener(SelectedShapeListener listener) {
        this.selectedShapeListeners.add(listener);
    }

    public void removeSelectedShapeListener(SelectedShapeListener listener) {
        this.selectedShapeListeners.remove(listener);
    }

    public ShapeComponentView<?> getDrawnShape() {
        return this.drawnShape;
    }

    public void setDrawnShape(ShapeComponentView<?> drawnShape) {
        this.drawnShape = drawnShape;
    }

    public interface SelectedShapeListener {
        void run(ShapeComponentView<?> newShape);
    }
}
