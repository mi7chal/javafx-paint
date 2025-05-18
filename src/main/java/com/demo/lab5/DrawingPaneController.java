package com.demo.lab5;

import com.demo.lab5.shapes.DraggingCreatedShapeComponentView;
import com.demo.lab5.shapes.PolygonComponentView;
import com.demo.lab5.shapes.ShapeComponentFactory;
import com.demo.lab5.shapes.ShapeComponentView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Controller for the main view of application which contains drawing area, buttons and menu..
 */
public class DrawingPaneController {

    /**
     * State manager for the drawing area. It manages the current mode and state of the application.
     */
    private final DrawingPaneStateManager stateManager;
    /**
     * Drawing area
     */
    @FXML
    private Pane drawingBox;


    public DrawingPaneController() {
        this.stateManager = new DrawingPaneStateManager();
    }

    /**
     * Handles the clear button click event. Clears the drawing area.
     */
    @FXML
    protected void onClear() {
        this.drawingBox.getChildren().clear();
    }

    /**
     * Handles the mouse pressed event. Creates a new shape or adds a node to the existing shape.
     * <p>
     * Selection of a shape is created in the {@link ShapeComponentView} class.
     *
     * @param event mouse event
     */
    @FXML
    protected void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY || stateManager.getMode() == DrawingMode.SELECT_SHAPE) {
            return;
        }

        if (stateManager.getMode() == DrawingMode.POLYGON && stateManager.getDrawnShape() instanceof PolygonComponentView s) {
            s.addNode(event.getX(), event.getY());
            return;
        }

        stateManager.setDrawnShape(ShapeComponentFactory.create(stateManager.getMode(), event.getX(), event.getY()));
        stateManager.getDrawnShape().setStateManager(stateManager);

        drawingBox.getChildren().add(stateManager.getDrawnShape());
    }

    /**
     * Handles the mouse dragged event. Updates the size of the shape being drawn.
     *
     * @param event mouse event
     */
    @FXML
    protected void onMouseDragged(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY || stateManager.getMode() == DrawingMode.SELECT_SHAPE) {
            return;
        }

        if (stateManager.getDrawnShape() instanceof DraggingCreatedShapeComponentView s) {
            s.updateSize(event.getX(), event.getY());
        }
    }

    /**
     * Handles selection mode button click event. Sets the mode to select shape. Also updates the cursor if mode changes.
     *
     * @param event click event
     */
    @FXML
    protected void onModeSelect(ActionEvent event) {
        if (event.getSource() instanceof Button button) {
            drawingBox.setCursor(Cursor.CROSSHAIR);
            stateManager.setDrawnShape(null);

            switch (button.getId()) {
                case "selectModeButton" -> {
                    stateManager.setMode(DrawingMode.SELECT_SHAPE);
                    drawingBox.setCursor(Cursor.DEFAULT);
                }
                case "circleButton" -> stateManager.setMode(DrawingMode.CIRCLE);
                case "rectangleButton" -> stateManager.setMode(DrawingMode.RECTANGLE);
                case "polygonButton" -> stateManager.setMode(DrawingMode.POLYGON);
                default -> { /* ignore, we just don't change mode */ }
            }
        }
    }

    /**
     * Handles file save button click event. Opens a file chooser and saves the current drawing to the selected file.
     */
    @FXML
    protected void onSave() {
        // file selection
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Drawing Pane");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showSaveDialog(drawingBox.getScene().getWindow());

        // creating file variable
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (Exception e) {
            ErrorUtils.showError("File save error", "Unknown error", "File can't be saved due to unexpected error.");
            return;
        }

        Element root = document.createElement("shapes");
        document.appendChild(root);

        // building xml shapes
        for (Node nodes : drawingBox.getChildren()) {
            if (nodes instanceof ShapeComponentView shape) {
                Element xmlElement = document.createElement("shape");
                ShapesXmlEncoder.encodeShape(shape, xmlElement);
                root.appendChild(xmlElement);
            }
        }

        // transforming xml to file and saving
        try {
            DOMSource dom = new DOMSource(document);
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();

            StreamResult result = new StreamResult(new File(selectedFile.getPath()));
            transformer.transform(dom, result);
        } catch (Exception e) {
            ErrorUtils.showError("File save error", "Encoding error", "File can't be saved due to error with encoding. Try again.");
        }
    }

    /**
     * Handles file open button click event. Opens a file chooser and loads the selected file.
     */
    @FXML
    protected void onOpen() {
        // selects file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Drawing Pane");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(drawingBox.getScene().getWindow());

        // opens and parses xml file
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(selectedFile);
        } catch (Exception e) {
            ErrorUtils.showError("File decoding error", "Decoding failed", "This file can't be decoded. Check if it contains proper xml generated by this app.");

            return;
        }


        NodeList nodeList = document.getElementsByTagName("shape");

        drawingBox.getChildren().clear();

        // reads children and assigns them to the drawing area
        for (int i = 0; i < nodeList.getLength(); i++) {
            ShapeComponentView<?> component;
            try {
                component = ShapesXmlEncoder.decodeShape(nodeList.item(i));
            } catch (Exception e) {
                ErrorUtils.showError("File decoding error", "Encoding failed", "This file can't be encoded. Check if it contains proper xml generated by this app.");
                drawingBox.getChildren().clear();
                return;
            }
            component.setStateManager(stateManager);
            drawingBox.getChildren().add(component);
        }

    }

    /**
     * Closes app.
     */
    @FXML
    protected void onClose() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Displays basic info dialog about the app.
     */
    @FXML
    protected void onInfoClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Drawing application");
        alert.setContentText("This is a drawing application.\n" +
                "You can draw shapes, select them and save.\n" +
                "Created by: Michał Chruścielski 284009\n" +
                "See Help -> Instructions for more information.");

        alert.show();


    }

    /**
     * Displays app user manual.
     */
    @FXML
    protected void onInstructionClicked() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("Drawing app user manual");
        alert.setContentText("1. Select a shape from the toolbar.\n" +
                "2. Click and drag on the drawing area to create a cricle or a rectangle. Click to add a polygon nodes. " +
                "When you want to add another polygon, just click at its tab button once again.\n" +
                "3. Use the select mode to select, move, rotate and recolor shapes. Select shapes by clicking at them\n" +
                "4. Click and drag to move shape, scroll to resize it or rotate and recolor shape from the toolbar." +
                "5. Use the clear button to clear the drawing area.\n" +
                "6. Use the File -> Save to save your drawing or File -> Open to load existing one.");

        alert.show();
    }
}