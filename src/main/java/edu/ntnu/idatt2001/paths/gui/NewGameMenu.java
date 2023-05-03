package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.Game;
import edu.ntnu.idatt2001.paths.goal.Goal;
import edu.ntnu.idatt2001.paths.goal.GoldGoal;
import edu.ntnu.idatt2001.paths.goal.HealthGoal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Type;

public class NewGameMenu extends PathsMenu {



    public NewGameMenu() {

    }

    private void back(ActionEvent e) {
        changeState(new MainMenu());
    }

    private File start(ActionEvent e){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/Stories"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Paths Files", "*.path")
        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        return null;
    }

    private void add(ActionEvent e){

    }

    private void remove(ActionEvent e){

    }

    @Override
    public Scene getScene() {
        ImageView menuBackground = new ImageView();
        menuBackground.setId("MenuBackground");

        ImageView nameBackground = new ImageView();
        nameBackground.setId("nameBackground");
        nameBackground.setFitWidth(300);
        nameBackground.setFitHeight(100);

        ImageView healthBackground = new ImageView();
        healthBackground.setId("healthBackground");
        healthBackground.setFitWidth(300);
        healthBackground.setFitHeight(100);

        ImageView goldBackground = new ImageView();
        goldBackground.setId("goldBackground");
        goldBackground.setFitWidth(300);
        goldBackground.setFitHeight(100);

        Label nameLabel = new Label("Name:");

        Label healthLabel = new Label("Health:");

        Label goldLabel = new Label("Gold:");

        Button btBack = new Button("Back");
        btBack.setId("btBack");

        Button btStartGame = new Button("Start new game");
        btStartGame.setId("btStartGame");

        Button btAddGoal = new Button("Add goal");
        btAddGoal.setId("btAddGoal");

        Button btRemoveGoal = new Button("Remove goal");
        btRemoveGoal.setId("btRemoveGoal");

        TextField txName = new TextField();
        TextField txHealth = new TextField();
        TextField txGold = new TextField();

        TableView <Goal> tableView = new TableView<>();
        tableView.maxWidth(245);
        TableColumn<Goal, String> TypeColumn = new TableColumn<>("Type");
        TypeColumn.setPrefWidth(122.5);
        TypeColumn.setResizable(false);
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        TableColumn<Goal, String> GoalColumn = new TableColumn<>("Goal");
        GoalColumn.setPrefWidth(122.5);
        GoalColumn.setResizable(false);
        GoalColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        tableView.getColumns().add(TypeColumn);
        tableView.getColumns().add(GoalColumn);

        btBack.setTranslateX(20);
        btBack.setTranslateY(20);
        btBack.setOnAction(this::back);

        int buttonWidth = 200;
        int sceneWidth = handler.getSceneWidth();
        int sceneHeight = handler.getSceneHeight();
        int buttonX = sceneWidth / 2 - buttonWidth / 2;

        menuBackground.setTranslateX(270);
        menuBackground.setTranslateY(50);

        btStartGame.setTranslateX(400);
        btStartGame.setTranslateY(535);
        btStartGame.setOnAction(this::start);

        btAddGoal.setTranslateX(758.5);
        btAddGoal.setTranslateY(490);
        btAddGoal.setPrefWidth(225);
        btAddGoal.setOnAction(this::add);

        btRemoveGoal.setTranslateX(758.5);
        btRemoveGoal.setTranslateY(560);
        btRemoveGoal.setPrefWidth(225);
        btRemoveGoal.setOnAction(this::remove);

        txName.setTranslateX(425);
        txName.setTranslateY(175);

        nameBackground.setTranslateX(375);
        nameBackground.setTranslateY(125);

        nameLabel.setTranslateX(400);
        nameLabel.setTranslateY(150);

        txHealth.setTranslateX(425);
        txHealth.setTranslateY(300);

        healthBackground.setTranslateX(375);
        healthBackground.setTranslateY(250);

        healthLabel.setTranslateX(400);
        healthLabel.setTranslateY(275);

        txGold.setTranslateX(425);
        txGold.setTranslateY(425);

        goldBackground.setTranslateX(375);
        goldBackground.setTranslateY(375);

        goldLabel.setTranslateX(400);
        goldLabel.setTranslateY(400);

        tableView.setTranslateX(750);
        tableView.setTranslateY(80);

        ObservableList<Goal> test = FXCollections.observableArrayList(
                new GoldGoal(4),
                new HealthGoal(10)
        );

        tableView.setItems(test);

        AnchorPane root = new AnchorPane();
        root.getChildren().add(menuBackground);
        root.getChildren().add(nameBackground);
        root.getChildren().add(healthBackground);
        root.getChildren().add(goldBackground);
        root.getChildren().add(nameLabel);
        root.getChildren().add(healthLabel);
        root.getChildren().add(goldLabel);
        root.getChildren().add(btBack);
        root.getChildren().add(btStartGame);
        root.getChildren().add(btAddGoal);
        root.getChildren().add(btRemoveGoal);
        root.getChildren().add(txName);
        root.getChildren().add(txHealth);
        root.getChildren().add(txGold);
        root.getChildren().add(tableView);

        Scene scene = new Scene(root, handler.getSceneWidth(), handler.getSceneHeight(), handler.getBgColor());

        scene.getStylesheets().add("NewGame.css");

        return scene;
    }



    @Override
    public void setup() {

    }

    @Override
    public void cleanup() {

    }
}
