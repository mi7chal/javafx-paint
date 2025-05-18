package com.demo.lab5;

import com.demo.lab5.shapes.CircleComponentView;
import com.demo.lab5.shapes.PolygonComponentView;
import com.demo.lab5.shapes.RectangleComponentView;
import com.demo.lab5.shapes.ShapeComponentView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for encoding and decoding shapes to and from XML.
 */
public class ShapesXmlEncoder {
    /**
     * Assigns shape to a given {@link Element}
     * @param shape shape to encode (assign)
     * @param element element to assign shape to
     */
    public static void encodeShape(ShapeComponentView shape, Element element) {
        if (shape instanceof RectangleComponentView rectangle) {
            element.setAttribute("type", "rectangle");
            element.setAttribute("width", String.valueOf((rectangle.getTargetShape()).getWidth()));
            element.setAttribute("height", String.valueOf((rectangle.getTargetShape()).getHeight()));
        } else if (shape instanceof CircleComponentView circle) {
            element.setAttribute("type", "circle");
            element.setAttribute("radius", String.valueOf(circle.getTargetShape().getRadius()));
        } else if (shape instanceof PolygonComponentView polygon) {
            element.setAttribute("type", "polygon");
            Polygon targetPolygon = polygon.getTargetShape();

            element.setAttribute("points", String.join(",", targetPolygon.getPoints().stream().map(String::valueOf).toList()));
        } else {
            throw new IllegalArgumentException("Shape not supported");
        }


        element.setAttribute("hash", String.valueOf(shape.hashCode()));
        element.setAttribute("x", String.valueOf(shape.getLayoutX()));
        element.setAttribute("y", String.valueOf(shape.getLayoutY()));
        element.setAttribute("fill", shape.getTargetShape().getFill().toString());
        element.setAttribute("stroke", shape.getTargetShape().getStroke().toString());
        element.setAttribute("rotation", String.valueOf(shape.getTargetShape().getRotate()));

    }

    /**
     * Decodes shape from given xml {@link Node}
     * @param node shape node from xml file
     * @return decoded shape
     */
    public static ShapeComponentView<?> decodeShape(Node node) {
        String shape = node.getAttributes().getNamedItem("type").getNodeValue();

        ShapeComponentView<?> shapeComponent;

        double x = Double.parseDouble(node.getAttributes().getNamedItem("x").getNodeValue());
        double y = Double.parseDouble(node.getAttributes().getNamedItem("y").getNodeValue());
        Color fill = Color.valueOf(node.getAttributes().getNamedItem("fill").getNodeValue());
        Color stroke = Color.valueOf(node.getAttributes().getNamedItem("stroke").getNodeValue());
        double rotation = Double.parseDouble(node.getAttributes().getNamedItem("rotation").getNodeValue());


        switch (shape) {
            case "rectangle":
                shapeComponent = new RectangleComponentView(x, y);
                double width = Double.parseDouble(node.getAttributes().getNamedItem("width").getNodeValue());
                double height = Double.parseDouble(node.getAttributes().getNamedItem("height").getNodeValue());

                ((Rectangle) shapeComponent.getTargetShape()).setWidth(width);
                ((Rectangle) shapeComponent.getTargetShape()).setHeight(height);

                break;
            case "circle":
                shapeComponent = new CircleComponentView(x, y);

                double radius = Double.parseDouble(node.getAttributes().getNamedItem("radius").getNodeValue());
                ((Circle) shapeComponent.getTargetShape()).setRadius(radius);
                ((Circle) shapeComponent.getTargetShape()).setCenterX(radius);
                ((Circle) shapeComponent.getTargetShape()).setCenterY(radius);


                break;
            case "polygon":
                shapeComponent = new PolygonComponentView(x, y);

                String points = node.getAttributes().getNamedItem("points").getNodeValue();

                List<Double> pointList = Arrays.stream(points.split(",")).map(Double::valueOf).toList();

                ((Polygon) shapeComponent.getTargetShape()).getPoints().addAll(pointList);
                break;
            default:
                throw new IllegalArgumentException("Shape not supported");
        }

        shapeComponent.setLayoutX(x);
        shapeComponent.setLayoutY(y);
        shapeComponent.getTargetShape().setRotate(rotation);
        shapeComponent.getTargetShape().setFill(fill);
        shapeComponent.getTargetShape().setStroke(stroke);

        return shapeComponent;
    }

}
