package edu.ntnu.idatt2001.paths.gui;

import edu.ntnu.idatt2001.paths.gui.gameplayer.GameScene;
import edu.ntnu.idatt2001.paths.io.StoryLoader;
import edu.ntnu.idatt2001.paths.model.Game;
import edu.ntnu.idatt2001.paths.model.Player;
import edu.ntnu.idatt2001.paths.model.goal.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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

    TableView<Goal> tableView = new TableView<>();
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

    /**
     * Starts a new game. First has the user chose which .paths file to play through a file chooser.
     * Then presents any errors that may have occurred with the loading og the story.
     * Next the method tries to make the player object for the game from the data in the
     * text fields. If the data is not inputted the new game operation will fail, and an error
     * message telling the user what went wrong will be presented.
     * If alle the inputs are correct a Game object will be initialized, and the user will be
     * transported to the opening passage gameScene.
     */
    private void start(ActionEvent e) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("Stories"));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Paths Files", "*.paths")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile == null)
            return; // The user aborted choosing a story file.

        handler.setCurrentFileName(selectedFile.getName());
        handler.setCurrentPath(selectedFile.getPath());

        StoryLoader loader = null;
        try {
            loader = new StoryLoader(selectedFile.getAbsolutePath());
            handler.setStoryLoader(loader);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        if (!loader.load()) {
            boolean storyLoadSuccess = loader.getStory() != null;
            boolean abort = !showErrorsToUser(loader.readAllErrors(), storyLoadSuccess);

            if (!storyLoadSuccess)
                return;
            if (abort)
                return;
        }

        if (loader.getStory() == null)
            return;

        handler.setCurrentBrokenLinks(loader.getStory().getBrokenLinks());

        Player player;
        String goldText = txGold.getText();
        String nameText = txName.getText();
        String healthText = txHealth.getText();


        if (!nameText.trim().isEmpty() && !healthText.trim().isEmpty() && getGoals().size() > 0) {
            if (goldText.trim().isEmpty()) {
                player = new Player.PlayerBuilder(txName.getText(), Integer.parseInt(txHealth.getText()))
                        .build();
                handler.setInitialPlayer(new Player.PlayerBuilder(player.getName(), player.getHealth()).build());
            } else {
                player = new Player.PlayerBuilder(txName.getText(), Integer.parseInt(txHealth.getText()))
                        .setGold(Integer.parseInt(goldText)).build();
                handler.setInitialPlayer(new Player.PlayerBuilder(player.getName(), player.getHealth())
                        .setGold(player.getGold()).build());

                handler.setInitialGoals(getGoals());

                Game game = new Game(player, loader.getStory(), getGoals());


                SceneConfig sceneConfig = handler.getSceneConfig();

                GameScene gameScene = new GameScene(game, loader, sceneConfig);

                handler.setCurrentAudio("");
                handler.stopMenuMusic();

                changeState(gameScene);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing input");
            alert.setHeaderText("Missing input");
            String missingContent = "";
            if (nameText.trim().isEmpty()) {
                missingContent += "You have to fill the name field" + '\n';
            }
            if (nameText.trim().isEmpty()) {
                missingContent += "You have to fill the health field" + '\n';
            }
            if (getGoals().size() == 0) {
                missingContent += "You have to have a minimum of one goal";
            }
            alert.setContentText(missingContent);
            alert.showAndWait();
        }
    }

    /**
     * Presents errors to the user. Takes inn a list of errors and builds a
     * string from the list. Then presents the list of errors to the user.
     * If the story-file was loaded successfully, but some assets could not be
     * loaded the user is asked whether they want to play the story without
     * some assets.
     *
     * @param errors           The errors to show to the user
     * @param storyLoadSuccess Indicates whether the story-file was loaded
     *                         successfully.
     * @return True if the user wants to continue despite some assets not being
     *      loaded successfully.
     */
    private boolean showErrorsToUser(String[] errors, boolean storyLoadSuccess) {
        Alert alert;
        if (storyLoadSuccess)
            alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
        else
            alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Syntax error");
        alert.setHeaderText("Failed to load the selected story.");
        if (storyLoadSuccess)
            alert.setHeaderText("Some assets could not be loaded. Continue?");

        StringBuilder allErrors = new StringBuilder();
        for (String error : errors)
            allErrors.append(error).append('\n').append('\n');

        TextArea area = new TextArea(allErrors.toString());
        area.setWrapText(true);
        area.setEditable(false);

        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        alert.showAndWait();

        if (storyLoadSuccess)
            return alert.getResult() == ButtonType.YES;
        else
            return false;
    }

    private List<Goal> getGoals() {
        return tableView.getItems();
    }

    /**
     * Allows the user to define a new goal to add to the list of goals for the new story.
     * Opens an overlay where the user can define the type of goal, and how much.
     */
    private void add(ActionEvent e) {

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

    /**
     * Updates the list of goals with a new user defined goal.
     */
    private void addGoal(ActionEvent e) {
        ObservableList<Goal> goals = tableView.getItems();
        if (type.getValue().equals("Health")) {
            HealthGoal healthGoalToAdd = new HealthGoal(Integer.parseInt(textField.getText()));
            goals.add(healthGoalToAdd);
        } else if (type.getValue().equals("Gold")) {
            GoldGoal goldGoalToAdd = new GoldGoal(Integer.parseInt(textField.getText()));
            goals.add(goldGoalToAdd);
        } else if (type.getValue().equals("Score")) {
            ScoreGoal scoreGoalToAdd = new ScoreGoal(Integer.parseInt(textField.getText()));
            goals.add(scoreGoalToAdd);
        } else if (type.getValue().equals("Inventory")) {
            List<String> items = new ArrayList<>();
            items.add(textField.getText());
            InventoryGoal inventoryGoalToAdd = new InventoryGoal(items);
            goals.add(inventoryGoalToAdd);
        }
        tableView.setItems(goals);
        stage.close();
    }

    private void cancelAddGoal(ActionEvent e) {
        stage.close();
    }

    /**
     * Allows the user to remove a goal from the list of goals for the game.
     * To remove the user selects the goal in the list they want to remove
     * Then they press the remove button.
     */
    private void remove(ActionEvent e) {
        Goal selected = tableView.getSelectionModel().getSelectedItem();
        ObservableList<Goal> goals = tableView.getItems();
        goals.remove(selected);
        tableView.setItems(goals);
    }

    /**
     * Creates the new game menu scene. Styled through the NewGame.css file
     *
     * @return the new game scene.
     */
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
        mand.add("The_orb_of_life");
        ObservableList<Goal> test = FXCollections.observableArrayList(
                new InventoryGoal(mand)
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


    /**
     * Sets up restrictions and default values for the new game menu
     */
    @Override
    public void setup() {
        txHealth.textProperty().addListener(new ChangeListener<String>() {

            // Makes it so that the user can only add numbers to the health field
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txHealth.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        txGold.textProperty().addListener(new ChangeListener<String>() {
            // Makes it so that the user can only add numbers to the gold field
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txGold.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // Sets some initial values
        txName.setText("Roy");
        txHealth.setText("100");
        txGold.setText("50");
    }

    @Override
    public void cleanup() {

    }
}
