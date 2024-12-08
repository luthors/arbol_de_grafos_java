package com.luthors.binarytreeapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BinaryTreeApp extends Application {

    // Node class
    static class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.left = this.right = null;
        }
    }

    // Binary Search Tree class
    static class BinarySearchTree {
        Node root;

        void insert(int value) {
            root = insertRec(root, value);
        }

        private Node insertRec(Node root, int value) {
            if (root == null) {
                return new Node(value);
            }
            if (value < root.value) {
                root.left = insertRec(root.left, value);
            } else if (value > root.value) {
                root.right = insertRec(root.right, value);
            }
            return root;
        }

        void preOrder(Node node, StringBuilder result) {
            if (node != null) {
                result.append(node.value).append(" ");
                preOrder(node.left, result);
                preOrder(node.right, result);
            }
        }

        void inOrder(Node node, StringBuilder result) {
            if (node != null) {
                inOrder(node.left, result);
                result.append(node.value).append(" ");
                inOrder(node.right, result);
            }
        }

        void postOrder(Node node, StringBuilder result) {
            if (node != null) {
                postOrder(node.left, result);
                postOrder(node.right, result);
                result.append(node.value).append(" ");
            }
        }

        void clear() {
            root = null;
        }
    }

    private BinarySearchTree tree = new BinarySearchTree();
    private Canvas canvas = new Canvas(800, 600);



    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        int[] initialValues = {150, 44, 180, 20, 11, 15, 147, 8};
        // {150, 44, 180, 20, 11, 15, 147, 8, 146,152,189,174,175,201}
        for (int value : initialValues) {
            tree.insert(value);
        }

        drawTree();

        // Create input and button UI elements
        VBox topControls = createTopControls();
        VBox mainContainer = new VBox();
        mainContainer.getChildren().addAll(topControls, canvas);

        // Set up the scene
        Scene scene = new Scene(mainContainer, 900, 700);
        primaryStage.setTitle("Visualización de Árbol Binario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTopControls() {
        // Text input
        TextField inputField = new TextField();
        inputField.setPromptText("Ingrese números separados por comas");

        // Buttons
        Button insertButton = new Button("Insertar");
        Button preOrderButton = new Button("Recorrido Preorden");
        Button inOrderButton = new Button("Recorrido Inorden");
        Button postOrderButton = new Button("Recorrido Postorden");
        Button resetButton = new Button("Limpiar Árbol");

        insertButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        preOrderButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        inOrderButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
        postOrderButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
        resetButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        HBox buttonGroup = new HBox(10, insertButton, preOrderButton, inOrderButton, postOrderButton, resetButton);
        buttonGroup.setStyle("-fx-padding: 10; -fx-alignment: center;");

        VBox topContainer = new VBox(10, inputField, buttonGroup);
        topContainer.setStyle("-fx-background-color: #ECEFF1; -fx-padding: 10;");

        // Event handling
        insertButton.setOnAction(e -> handleInsert(inputField));
        preOrderButton.setOnAction(e -> handlePreOrder());
        inOrderButton.setOnAction(e -> handleInOrder());
        postOrderButton.setOnAction(e -> handlePostOrder());
        resetButton.setOnAction(e -> handleReset());

        return topContainer;
    }

    private void handleInsert(TextField inputField) {
        try {
            String[] values = inputField.getText().split(",");
            for (String value : values) {
                tree.insert(Integer.parseInt(value.trim()));
            }
            inputField.clear();
            drawTree();
            showPopup("Números insertados exitosamente.");
        } catch (NumberFormatException ex) {
            showPopup("Entrada inválida. Ingrese solo números separados por comas.");
        }
    }

    private void handlePreOrder() {
        StringBuilder result = new StringBuilder();
        tree.preOrder(tree.root, result);
        showPopup("Recorrido Preorden: " + result.toString());
    }

    private void handleInOrder() {
        StringBuilder result = new StringBuilder();
        tree.inOrder(tree.root, result);
        showPopup("Recorrido Inorden: " + result.toString());
    }

    private void handlePostOrder() {
        StringBuilder result = new StringBuilder();
        tree.postOrder(tree.root, result);
        showPopup("Recorrido Postorden: " + result.toString());
    }

    private void handleReset() {
        tree.clear();
        drawTree();
        showPopup("El árbol ha sido limpiado.");
    }

    private void showPopup(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawTree() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawNode(gc, tree.root, canvas.getWidth() / 2, 110, canvas.getWidth() / 8);
    }

    private void drawNode(GraphicsContext gc, Node node, double x, double y, double offset) {
        if (node != null) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(x - 25, y - 25, 50, 50);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 25, y - 25, 50, 50);
            gc.setFill(Color.RED);
            gc.setFont(new javafx.scene.text.Font(20)); // Fuente más grande
            gc.fillText(String.valueOf(node.value), x - 15, y + 8);

            if (node.left != null) {
                gc.strokeLine(x, y, x - offset, y + 40);
                drawNode(gc, node.left, x - offset, y + 40, offset / 1.1);
            }

            if (node.right != null) {
                gc.strokeLine(x, y, x + offset, y + 40);
                drawNode(gc, node.right, x + offset, y + 40, offset / 1.1);

            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
