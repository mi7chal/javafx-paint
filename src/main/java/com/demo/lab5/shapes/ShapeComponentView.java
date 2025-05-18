package com.demo.lab5.shapes;

import com.demo.lab5.Coordinates;
import com.demo.lab5.DrawingMode;
import com.demo.lab5.DrawingPaneStateManager;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Base class for all shape components. It provides basic UI config, dragging, rotation, selection and resizing.
 */
public abstract class ShapeComponentView<TargetShape extends Shape> extends Pane {
    /**
     * Configured JavaFX shape
     */
    protected final TargetShape shape;

    /**
     * Coordinates of the shape when it was created. Used for resizing and dragging.
     */
    protected final Coordinates startCoords;

    /**
     * Reference to the {@link DrawingPaneStateManager} which is used to manage and share state between different shapes.
     *
     * @see DrawingPaneStateManager
     */
    protected DrawingPaneStateManager stateManager;

    /**
     * Shape toolbar. Null if shape is not selected.
     *
     * @see ShapeToolbarComponentView
     */
    private ShapeToolbarComponentView toolbar;

    /**
     * Coordinates of the point where dragging started relative to this `Pane`. Used only for dragging. If dragging
     * is not in progress this field is null.
     */
    private Coordinates draggingOffset;

    /**
     * Constructs a new shape from the given shape and coords.
     *
     * @param shape target shape
     * @param x     x start coordinate
     * @param y     y start coordinate
     */
    protected ShapeComponentView(TargetShape shape, double x, double y) {
        this.shape = shape;
        this.startCoords = new Coordinates(x, y);

        draw();
    }

    /**
     * Updates the size of the shape. This method is called when the mouse is dragged.
     * It must be implemented differently for each shape, in order to avoid using `scaleX()` and `scaleY()` methods.
     *
     * @param multiplier multiplier for the size of the shape
     */
    public abstract void resize(double multiplier);

    /**
     * Initializes and draws the shape. Adds listeners for required events.
     */
    public void draw() {
        assert shape != null;
        assert startCoords != null;

        this.setLayoutX(startCoords.x());
        this.setLayoutY(startCoords.y());

        this.getChildren().add(shape);

        shape.setFill(Color.TRANSPARENT);
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(2);

        this.setOnMouseClicked(this::onMouseClicked);
        this.setOnMousePressed(this::onMousePressed);
        this.setOnMouseDragged(this::onMouseDragged);
        this.setOnMouseReleased(this::onMouseReleased);
        this.setOnScroll(this::onScroll);
    }

    /**
     * Checks if shape is currently selected using {@link DrawingPaneStateManager}.
     *
     * @return true if shape is selected, false otherwise
     */
    public boolean isSelected() {
        return this.stateManager.getSelectedShape() == this;
    }

    /**
     * Listener for a new shape selection. If different shape is selected it removes the toolbar
     * and sets the cursor to default.
     *
     * @param selected newly shape
     */
    private void selectedListener(ShapeComponentView<?> selected) {
        if (selected != this && toolbar != null) {
            this.getChildren().remove(toolbar);
            this.setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Used to set the shared state manager. It should be used <b>instantly</b> after the shape is created.
     * State manager will be later used to handle selection, resizing etc.
     *
     * @param stateManager state manager to be set
     */
    public void setStateManager(DrawingPaneStateManager stateManager) {
        if (stateManager != null) {
            stateManager.removeSelectedShapeListener(this::selectedListener);
        }

        this.stateManager = stateManager;

        if (stateManager != null) {
            stateManager.addSelectedShapeListener(this::selectedListener);
        }
    }

    public TargetShape getTargetShape() {
        return this.shape;
    }


    /**
     * This method handle `onMouseClicked` event. It selects current shape and shows the toolbar.
     *
     * @param event mouse event
     */
    public void onMouseClicked(MouseEvent event) {
        if (this.stateManager.getMode() != DrawingMode.SELECT_SHAPE
                && (event.getSource() != this.shape || event.getSource() != this)) {
            return;
        }

        stateManager.setSelectedShape(this);

        if (toolbar == null) {
            this.toolbar = new ShapeToolbarComponentView(shape);
        }

        if (!this.getChildren().contains(toolbar)) {
            this.getChildren().add(this.toolbar);
        }

        this.setCursor(Cursor.HAND);


        // avoiding propagation to other shapes
        event.consume();
    }

    /**
     * This method handles `onScroll` event. It resizes the shape using abstract `resize()` method.
     *
     * @param event scroll event
     */
    public void onScroll(ScrollEvent event) {
        if (!isSelected()) {
            return;
        }

        this.resize(1 + event.getDeltaY() * 0.02);
    }

    /**
     * `onMousePressed` events starts dragging if shape is currently selected. Does nothing otherwise.
     *
     * @param event mouse event
     */
    public void onMousePressed(MouseEvent event) {
        if (!isSelected()) {
            return;
        }

        draggingOffset = new Coordinates(this.getLayoutX() - event.getSceneX(), this.getLayoutY() - event.getSceneY());
        this.setCursor(Cursor.CLOSED_HAND);
    }

    /**
     * Continues and updates shape dragging if it is in progress. Otherwise does nothing.
     *
     * @param event mouse event
     */
    public void onMouseDragged(MouseEvent event) {
        if (this.draggingOffset == null) {
            return;
        }

        this.setLayoutX(event.getSceneX() + draggingOffset.x());
        this.setLayoutY(event.getSceneY() + draggingOffset.y());
    }

    /**
     * Finishes dragging if is in progress. Otherwise, does nothing.
     *
     * @param event mouse event
     */
    public void onMouseReleased(MouseEvent event) {
        if (draggingOffset == null) {
            return;
        }

        draggingOffset = null;
        this.setCursor(Cursor.HAND);
    }
}
