package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.Game;
import edu.ntnu.idatt2001.paths.Player;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import edu.ntnu.idatt2001.paths.goal.*;
import edu.ntnu.idatt2001.paths.gui.gameplayer.GameScene;
import edu.ntnu.idatt2001.paths.io.StoryLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class NewGameMenu extends PathsMenu {

    TableView <Goal> tableView = new TableView<>();

    TextField txName = new TextField();
    TextField txHealth = new TextField();
    TextField txGold = new TextField();

    TextField textField = new TextField();

    Stage stage = new Stage();

    String[] types = {"Health", "Gold", "Score", "Inventory"};
    ComboBox<String> type = new ComboBox<>(FXCollections.observableArrayList(types));


    public NewGameMenu() {

    }

    private void back(ActionEvent e) {
        changeState(new MainMenu());
    }

    private void start(ActionEvent e){
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/Stories"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Paths Files", "*.paths")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null)
            return; // The user aborted choosing a story file.

        StoryLoader loader = null;
        try {
            loader = new StoryLoader(selectedFile.getAbsolutePath());
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (!loader.load())
            showErrorsToUser(loader.readAllErrors());

        Player player;
        String goldText = txGold.getText();
        if(goldText.trim().isEmpty()){
            player = new Player.PlayerBuilder(txName.getText(), Integer.parseInt(txHealth.getText()))
                    .build();
        } else {
            player = new Player.PlayerBuilder(txName.getText(), Integer.parseInt(txHealth.getText()))
                    .setGold(Integer.parseInt(goldText)).build();
        }


        Game game = new Game(player, loader.getStory(), getGoals());
        SceneConfig sceneConfig = new SceneConfig(1270, 720);

        GameScene gameScene = new GameScene(game, loader, sceneConfig);

        changeState(gameScene);
    }

    private void showErrorsToUser(String[] errors) {

    }

    private List<Goal> getGoals() {
        return tableView.getItems();
    }

    private void add(ActionEvent e){

        stage.setWidth(400);
        stage.setHeight(200);



        Button cancel = new Button("Cancel");
        cancel.setId("cancel");
        Button add = new Button("Add");
        add.setId("add");

        type.setMaxWidth(147);


        cancel.setTranslateX(50);
        cancel.setTranslateY(100);
        cancel.setOnAction(this::cancelAddGoal);

        add.setTranslateX(250);
        add.setTranslateY(100);
        add.setOnAction(this::addGoal);

        type.setTranslateX(10);
        type.setTranslateY(31);

        textField.setTranslateX(170);
        textField.setTranslateY(30);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setId("AddGoal");
        anchorPane.getChildren().add(cancel);
        anchorPane.getChildren().add(add);
        anchorPane.getChildren().add(type);
        anchorPane.getChildren().add(textField);

        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add("AddGoal.css");
        stage.setScene(scene);
        stage.show();

    }

    private void addGoal(ActionEvent e){
        ObservableList<Goal> goals = tableView.getItems();
        if(type.getValue().equals("Health")){
            HealthGoal healthGoalToAdd = new HealthGoal(Integer.parseInt(textField.getText()));
            goals.add(healthGoalToAdd);
        } else if(type.getValue().equals("Gold")){
            GoldGoal goldGoalToAdd = new GoldGoal(Integer.parseInt(textField.getText()));
            goals.add(goldGoalToAdd);
        } else if(type.getValue().equals("Score")){
            ScoreGoal scoreGoalToAdd = new ScoreGoal(Integer.parseInt(textField.getText()));
            goals.add(scoreGoalToAdd);
        } else if(type.getValue().equals("Inventory")){
            List<String> items = new ArrayList<>();
            items.add(textField.getText());
            InventoryGoal inventoryGoalToAdd = new InventoryGoal(items);
            goals.add(inventoryGoalToAdd);
        }
        tableView.setItems(goals);
        stage.close();
    }

    private void cancelAddGoal(ActionEvent e){
        stage.close();
    }

    private void remove(ActionEvent e){
        Goal selected = tableView.getSelectionModel().getSelectedItem();
        ObservableList<Goal> goals =tableView.getItems();
        goals.remove(selected);
        tableView.setItems(goals);
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




        tableView.maxWidth(245);
        TableColumn<Goal, String> TypeColumn = new TableColumn<>("Type");
        TypeColumn.setPrefWidth(122.5);
        TypeColumn.setResizable(false);
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Goal, String> GoalColumn = new TableColumn<>("Goal");
        GoalColumn.setPrefWidth(122.5);
        GoalColumn.setResizable(false);
        GoalColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
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

        ArrayList<String> mand = new ArrayList<>();
        mand.add("a");
        ObservableList<Goal> test = FXCollections.observableArrayList(
                new GoldGoal(4),
                new HealthGoal(10),
                new InventoryGoal(mand),
                new ScoreGoal(10)
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
