package com.demo.lab5.shapes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * ShapeToolbarComponentView is component that provides a toolbar for modifying the properties of a shape like
 * rotation, stroke color and fill color.
 */
public class ShapeToolbarComponentView extends HBox {

    /**
     * Initializes component and draws it over the shape. Shape argument is used to modify its properties.
     *
     * @param shape target shape
     */
    public ShapeToolbarComponentView(Shape shape) {
        this.setWidth(50);
        this.setHeight(0);
        this.setLayoutX(0);
        this.setLayoutY(-50);

        this.setStyle("-fx-background-color: #c1c1c1; -fx-border-color: #515151; -fx-border-width: 1px;");
        this.setAlignment(Pos.BASELINE_CENTER);
        this.setPadding(new Insets(5));
        this.setSpacing(5);

        TextField rotationField = new TextField(String.valueOf(shape.getRotate()));

        rotationField.setOnAction(event -> {
            try {
                double angle = Double.parseDouble(rotationField.getText());
                shape.setRotate(angle);
            } catch (NumberFormatException e) {
                // reset on invalid value
                shape.setRotate(0);
                rotationField.setText("0");
            }
        });

        ColorPicker strokeColorPicker = new ColorPicker((Color) shape.getStroke());
        strokeColorPicker.setOnAction(event -> {
            shape.setStroke(strokeColorPicker.getValue());
        });

        ColorPicker fillColorPicker = new ColorPicker((Color) shape.getFill());
        fillColorPicker.setOnAction(event -> {
            shape.setFill(fillColorPicker.getValue());
        });

        this.getChildren().add(new Label("Rotation:"));
        this.getChildren().add(rotationField);
        this.getChildren().add(new Label("Stroke:"));
        this.getChildren().add(strokeColorPicker);
        this.getChildren().add(new Label("Fill:"));
        this.getChildren().add(fillColorPicker);
    }
}
