package com.demo.lab5;

import javafx.scene.control.Alert;

/**
 * Error utility class
 */
public class ErrorUtils {
    /**
     * Shows custom error alert
     * @param title error title
     * @param header error dialog header
     * @param message error message
     */
    public static void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
}
