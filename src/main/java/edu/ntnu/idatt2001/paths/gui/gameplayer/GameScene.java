package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.asset.AssetFinder;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import edu.ntnu.idatt2001.paths.gui.InGameMenu;
import edu.ntnu.idatt2001.paths.gui.MainMenu;
import edu.ntnu.idatt2001.paths.gui.PathsMenu;
import edu.ntnu.idatt2001.paths.gui.SceneConfig;
import edu.ntnu.idatt2001.paths.io.StoryLoader;
import edu.ntnu.idatt2001.paths.model.Game;
import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.action.Action;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameScene extends PathsMenu {

    private static final int LINK_ENTRY_HEIGHT = 20;
    private static final int LINK_ENTRY_HEIGHT_DISTANCE = 5;
    private static final int LINK_ENTRY_HEIGHT_INTERVAL = LINK_ENTRY_HEIGHT + LINK_ENTRY_HEIGHT_DISTANCE;
    private static final int LINK_ENTRY_LABEL_X = 30;

    private static final int STATS_XPOS = 15;
    private static final int STATS_YPOS = 20;
    private static final int STATS_YPOS_INTERVAL = 40;
    private static final int STATS_ICON_PADDING = 10;
    private static final int STATS_ICON_SIZE = STATS_YPOS_INTERVAL - STATS_ICON_PADDING;
    private static final int STATS_LABEL_XPOS = STATS_XPOS + STATS_YPOS_INTERVAL;

    private final Game game;
    private final Scene scene;
    private final SceneConfig sceneConfig;
    private final PathsAssetStore assetStore;
    private final AssetFinder assetFinder;
    private ScrollPane linkSelector;
    private TextArea textViewer;
    private Label healthLabel;
    private Label goldLabel;
    private Label scoreLabel;
    private Label fileNameLabel;
    private Label pathLabel;
    private Label brokenLinksLabel;
    private AnchorPane root;
    private ImageView passageViewer;
    private ImageView playerViewer;
    private ImageView lookAtViewer;
    private ImageView healthIcon;
    private ImageView goldIcon;
    private ImageView scoreIcon;
    private ItemViewer itemViewer;
    private boolean openingInGameMenu;

    private MediaPlayer audioPlayer;


    public GameScene(Game game, StoryLoader s, SceneConfig sceneConfig) {
        this.game = game;
        this.sceneConfig = sceneConfig;
        this.assetStore = s.getAssetStore();

        if (hasAssets())
            this.assetFinder = new AssetFinder(assetStore);
        else
            this.assetFinder = null;

        this.scene = createScene(sceneConfig);

        updateContent(game.begin());
        if (hasAssets()) {
            if (!s.isUsingDefaultAssets())
                updateAssets(game.begin());
            else
                updateAssets(new Passage(StoryLoader.DEFAULT_ASSET_STORE_PASSAGE_TITLE, "", new ArrayList<>()));
        }
    }

    public boolean hasAssets() {
        return assetStore != null;
    }

    @Override
    public void setup() {
        if (openingInGameMenu) {
            if (audioPlayer != null)
                audioPlayer.play();
            openingInGameMenu = false;
            return;
        }

        if (hasAssets())
            assetFinder.loadAllGlobalAssets();

        updateInfoLabels();
    }

    @Override
    public void cleanup() {
        if (openingInGameMenu)
            return;

        if (assetStore != null)
            assetStore.unloadAll();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    /**
     * The method called when the user selects a link.
     * It updates the GUI-components with the contents of the next passage.
     * If the submitted link is a broken link (a link that does not point
     * to a Passage),
     * then the transition does not happen.
     *
     * @param link The link that the user has chosen.
     */
    private void goLink(Link link) {

        Passage newPassage = game.go(link);

        if (newPassage == null) {
            reachedBrokenLink();
            return;
        }

        performLinkActions(link);
        updateContent(newPassage);

        if (game.checkGameOver()) {
            gameEnd("Game Over");
            return;
        }

        if (game.checkGameWon()) {
            gameEnd("Game Won");
            return;
        }

        if (hasAssets())
            updateAssets(newPassage);

    }

    /**
     * Creates an overlay over the current game scene. To be used when the user
     * wins or loses the game
     *
     * @param type the game result. For example "Game Over" if game lost or
     *             "Game won" if game won
     */
    private void gameEnd(String type) {
        getScene().getStylesheets().add("GameEnd.css");
        ImageView imageView = new ImageView();
        imageView.setId("Overlay");
        imageView.setFitWidth(handler.getSceneWidth());
        imageView.setFitHeight(handler.getSceneHeight());
        root.getChildren().add(imageView);

        ImageView gameEndBackground = new ImageView();
        gameEndBackground.setId("GameEndBG");
        gameEndBackground.setFitWidth(500);
        gameEndBackground.setFitHeight(handler.getSceneHeight());
        gameEndBackground.setX(handler.getSceneWidth() / 2 - gameEndBackground.getFitWidth() / 2);
        root.getChildren().add(gameEndBackground);

        ImageView healthBG = new ImageView();
        healthBG.setId("BG");
        healthBG.setX(485);
        healthBG.setY(150);
        healthBG.setFitWidth(300);
        ImageView goldBG = new ImageView();
        goldBG.setId("BG");
        goldBG.setX(485);
        goldBG.setY(250);
        goldBG.setFitWidth(300);
        ImageView scoreBG = new ImageView();
        scoreBG.setId("BG");
        scoreBG.setX(485);
        scoreBG.setY(350);
        scoreBG.setFitWidth(300);
        root.getChildren().add(healthBG);
        root.getChildren().add(goldBG);
        root.getChildren().add(scoreBG);

        ImageView healthIcon = new ImageView();
        healthIcon.setId("Health");
        healthIcon.setX(500);
        healthIcon.setY(160);
        healthIcon.setFitWidth(50);
        healthIcon.setFitHeight(50);
        root.getChildren().add(healthIcon);
        ImageView goldIcon = new ImageView();
        goldIcon.setId("Gold");
        goldIcon.setX(500);
        goldIcon.setY(260);
        goldIcon.setFitHeight(50);
        goldIcon.setFitWidth(50);
        root.getChildren().add(goldIcon);
        ImageView scoreIcon = new ImageView();
        scoreIcon.setId("Score");
        scoreIcon.setX(500);
        scoreIcon.setY(360);
        scoreIcon.setFitWidth(50);
        scoreIcon.setFitHeight(50);
        root.getChildren().add(scoreIcon);

        Label healthLabel = new Label(Integer.toString(game.getPlayer().getHealth()));
        healthLabel.setTranslateX(575);
        healthLabel.setTranslateY(167);
        healthLabel.setId("finalValue");
        root.getChildren().add(healthLabel);
        Label goldLabel = new Label(Integer.toString(game.getPlayer().getGold()));
        goldLabel.setTranslateX(575);
        goldLabel.setTranslateY(267);
        goldLabel.setId("finalValue");
        root.getChildren().add(goldLabel);
        Label scoreLabel = new Label(Integer.toString(game.getPlayer().getScore()));
        scoreLabel.setTranslateX(575);
        scoreLabel.setTranslateY(367);
        scoreLabel.setId("finalValue");
        root.getChildren().add(scoreLabel);

        Label finalHealth = new Label("Final Health");
        finalHealth.setId("finalValueIdentifier");
        finalHealth.setTranslateX(460);
        finalHealth.setTranslateY(120);
        root.getChildren().add(finalHealth);
        Label finalGold = new Label("Final Gold");
        finalGold.setId("finalValueIdentifier");
        finalGold.setTranslateX(460);
        finalGold.setTranslateY(220);
        root.getChildren().add(finalGold);
        Label finalScore = new Label("Final Score");
        finalScore.setId("finalValueIdentifier");
        finalScore.setTranslateX(460);
        finalScore.setTranslateY(320);
        root.getChildren().add(finalScore);

        Button quitBt = new Button("Quit");
        quitBt.setPrefWidth(300);
        quitBt.setTranslateX(485);
        quitBt.setTranslateY(575);
        quitBt.setOnAction(this::quit);
        Button mainMenuBt = new Button("Main Menu");
        mainMenuBt.setPrefWidth(300);
        mainMenuBt.setTranslateX(485);
        mainMenuBt.setTranslateY(475);
        mainMenuBt.setOnAction(this::goToMainMenu);
        root.getChildren().add(quitBt);
        root.getChildren().add(mainMenuBt);

        Label label = new Label(type);
        label.setId("Result");
        label.setTranslateX(520);
        label.setTranslateY(50);
        root.getChildren().add(label);
    }

    private void goToMainMenu(ActionEvent e) {
        audioPlayer.stop();
        changeState(new MainMenu());
    }

    private void quit(ActionEvent e) {
        Platform.exit();
    }

    private void reachedBrokenLink() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Broken link");
        alert.setHeaderText("You have reached a broken link");
        alert.setContentText("This option does not lead anywhere in this story.");
        alert.showAndWait();
    }

    private void performLinkActions(Link link) {
        game.performLinkActions(link);

        if (!hasAssets())
            return;

        for (Action a : link.getActions())
            itemViewer.processAction(a);
    }


    private void updateContent(Passage newPassage) {
        updateLinkSelector(newPassage);
        updatePlayerStats();
        updateText(newPassage);
    }

    private void updateLinkSelector(Passage newPassage) {
        AnchorPane root = new AnchorPane();
        List<Link> links = game.getOpenLinks(newPassage);

        int y = 0;
        for (Link l : links) {
            Label label = new Label(l.getText());
            label.setId("Link_entry_label");
            label.setTranslateX(LINK_ENTRY_LABEL_X);
            label.setTranslateY(y);
            label.setOnMouseReleased(e -> this.goLink(l));

            Button b = new Button("X");
            b.setId("Link_entry_button");
            b.setTranslateX(0);
            b.setTranslateY(y);
            b.setPrefHeight(LINK_ENTRY_HEIGHT);

            b.setOnAction(e -> this.goLink(l));

            root.getChildren().add(b);
            root.getChildren().add(label);


            y += LINK_ENTRY_HEIGHT_INTERVAL;
        }

        scene.setOnKeyReleased(e -> {
            int key;
            try {
                key = Integer.parseInt(e.getText()) - 1;
            } catch (NumberFormatException err) {
                return;
            }

            if (key >= 0 && key < links.size())
                goLink(links.get(key));
        });

        linkSelector.setContent(root);
    }

    private void updatePlayerStats() {
        healthLabel.setText("" + game.getPlayer().getHealth());
        goldLabel.setText("" + game.getPlayer().getGold());
        scoreLabel.setText("" + game.getPlayer().getScore());
    }

    private void updateText(Passage newPassage) {
        textViewer.setText(newPassage.getContent());
    }

    private void updateAssets(Passage newPassage) {
        updateBackground(newPassage);
        updatePlayerAsset(newPassage);
        updateInteractionObject(newPassage);
        updateItemAreaBG(newPassage);
        playAudio(newPassage);
    }

    private void playAudio(Passage newPassage) {
        MediaPlayer newAudio = assetFinder.getAudio(newPassage.getTitle());
        if (newAudio == null)
            return;

        if (audioPlayer == null)
            audioPlayer = newAudio;

        else {
            String currURI = audioPlayer.getMedia().getSource();
            String newURI = newAudio.getMedia().getSource();
            if (currURI.equals(newURI))
                return;
        }

        audioPlayer.stop();
        audioPlayer = newAudio;
        audioPlayer.setVolume(0.10);
        audioPlayer.setOnEndOfMedia(() -> audioPlayer.seek(Duration.ZERO));
        audioPlayer.play();
    }

    private void updateBackground(Passage newPassage) {
        Image background = assetFinder.getBackground(newPassage.getTitle());
        if (background != null)
            passageViewer.setImage(background);
    }

    private void updatePlayerAsset(Passage newPassage) {
        Image playerImg = assetFinder.getPlayer(newPassage.getTitle());
        if (playerImg == null)
            return;

        final int playerX = (int) (sceneConfig.getWidth() / 5 - playerImg.getWidth() / 2);
        final int playerY = (int) (sceneConfig.getHeight() * 2 / 3 - playerImg.getHeight());

        playerViewer.setImage(playerImg);
        playerViewer.setX(playerX);
        playerViewer.setY(playerY);
    }

    private void updateInteractionObject(Passage newPassage) {
        Image lookAtImg = assetFinder.getLookAt(newPassage.getTitle());
        if (lookAtImg == null)
            return;

        final int playerX = (int) ((sceneConfig.getWidth() * 4) / 5 - lookAtImg.getWidth() / 2);
        final int playerY = (int) ((sceneConfig.getHeight() * 2) / 3 - lookAtImg.getHeight());

        lookAtViewer.setImage(lookAtImg);
        lookAtViewer.setX(playerX);
        lookAtViewer.setY(playerY);
    }

    private void updateItemAreaBG(Passage newPassage) {
        Image img = assetFinder.getItemArea(newPassage.getTitle());
        if (img != null)
            itemViewer.setInventoryAreaImage(img);
    }

    private void updateInfoLabels() {
        fileNameLabel.setText(handler.getCurrentFileName());
        pathLabel.setText(handler.getCurrentPath());

        StringBuilder sb = new StringBuilder();
        for (Link l : handler.getCurrentBrokenLinks())
            sb.append("(").append(l.getText()).append(")[").append(l.getReference()).append("]").append("\n");

        brokenLinksLabel.setText(sb.toString());
    }


    private Scene createScene(SceneConfig sceneConfig) {
        linkSelector = createLinkSelector(sceneConfig);
        Button menuBt = createMenuButton(sceneConfig);
        textViewer = createTextArea(sceneConfig);
        createPlayerStatus();
        createFileNameLabel();
        createPathLabel();
        createBrokenLinksLabel();

        root = new AnchorPane();

        if (hasAssets()) {
            itemViewer = createItemViewer(sceneConfig);
            passageViewer = createPassageViewer(sceneConfig);
            playerViewer = createPlayerViewer(sceneConfig);
            lookAtViewer = createLookAtViewer(sceneConfig);
            ImageView playerStatusBackground = createPlayerStatsBackground();

            ImageView interactionArea = createInteractionArea(sceneConfig);
            createPlayerStatsIcons();


            root.getChildren().add(passageViewer);
            root.getChildren().add(playerStatusBackground);
            root.getChildren().add(interactionArea);
            root.getChildren().add(healthIcon);
            root.getChildren().add(goldIcon);
            root.getChildren().add(scoreIcon);
            root.getChildren().add(playerViewer);
            root.getChildren().add(lookAtViewer);
            itemViewer.addToPane(root);
        }

        root.getChildren().add(textViewer);
        root.getChildren().add(healthLabel);
        root.getChildren().add(goldLabel);
        root.getChildren().add(scoreLabel);
        root.getChildren().add(fileNameLabel);
        root.getChildren().add(pathLabel);
        root.getChildren().add(brokenLinksLabel);
        root.getChildren().add(linkSelector);
        root.getChildren().add(menuBt);


        Scene scene = new Scene(root, sceneConfig.getWidth(), sceneConfig.getHeight());
        scene.getStylesheets().add("GameScene.css");
        return scene;
    }

    private TextArea createTextArea(SceneConfig sceneConfig) {
        final int height = 150;
        final int width = 350;
        final int y = 0;
        final int x = 100;

        TextArea textViewer = new TextArea();
        textViewer.setWrapText(true);
        textViewer.setEditable(false);
        textViewer.setTranslateX(x);
        textViewer.setTranslateY(y);
        textViewer.setPrefWidth(width);
        textViewer.setPrefHeight(height);
        if (hasAssets()) {
            Image img = assetFinder.getTextArea();
            if (img != null) {
                String url = img.getUrl();
                textViewer.setStyle("-fx-background-color: null;\n"
                        + "-fx-background-repeat: no-repeat;\n"
                        + "-fx-background-size: " + width + " " + height + ";\n"
                        + "-fx-background-image: url(" + url + ");");
            }
        }

        return textViewer;
    }

    private ScrollPane createLinkSelector(SceneConfig sceneConfig) {
        int paneWidth = 250;
        int paneHeight = 150;

        linkSelector = new ScrollPane();
        linkSelector.setTranslateX(sceneConfig.getWidth() / 2 - paneWidth / 2);
        linkSelector.setTranslateY(sceneConfig.getHeight() - paneHeight);
        linkSelector.setPrefWidth(paneWidth);
        linkSelector.setPrefHeight(paneHeight);
        linkSelector.setMinHeight(paneHeight);
        return linkSelector;
    }

    private Button createMenuButton(SceneConfig sceneConfig) {
        int xPos = sceneConfig.getWidth() - 150;
        int yPos = sceneConfig.getHeight() - 150;

        Button menuButton = new Button("Menu");
        menuButton.setOnAction(this::openInGameMenu);
        menuButton.setTranslateX(xPos);
        menuButton.setTranslateY(yPos);
        return menuButton;
    }

    private void createPlayerStatus() {
        healthLabel = new Label();
        healthLabel.setTranslateX(STATS_LABEL_XPOS);
        healthLabel.setTranslateY(STATS_YPOS + STATS_ICON_PADDING);
        healthLabel.setId("Status_label");

        goldLabel = new Label();
        goldLabel.setTranslateX(STATS_LABEL_XPOS);
        goldLabel.setTranslateY(STATS_XPOS + STATS_YPOS_INTERVAL + STATS_ICON_PADDING);
        goldLabel.setId("Status_label");

        scoreLabel = new Label();
        scoreLabel.setTranslateX(STATS_LABEL_XPOS);
        scoreLabel.setTranslateY(STATS_XPOS + STATS_YPOS_INTERVAL * 2 + STATS_ICON_PADDING);
        scoreLabel.setId("Status_label");
    }


    private ItemViewer createItemViewer(SceneConfig sceneConfig) {
        ItemViewer itemViewer = new ItemViewer(sceneConfig, assetStore);
        itemViewer.setInventorySlotImage(assetFinder.getItemSlot());
        return itemViewer;
    }

    private ImageView createPassageViewer(SceneConfig sceneConfig) {
        final int width = sceneConfig.getWidth();
        final int height = (sceneConfig.getHeight() * 2) / 3;

        ImageView viewer = new ImageView();
        viewer.setFitWidth(width);
        viewer.setFitHeight(height);

        return viewer;
    }


    private ImageView createInteractionArea(SceneConfig sceneConfig) {
        final int x = 0;
        final int y = (sceneConfig.getHeight() * 2) / 3 + 80;
        final int width = sceneConfig.getWidth();
        final int height = sceneConfig.getHeight() - y;


        ImageView viewer = new ImageView();
        viewer.setX(x);
        viewer.setY(y);
        viewer.setFitWidth(width);
        viewer.setFitHeight(height);
        viewer.setImage(assetFinder.getInteractionArea());

        return viewer;
    }

    private void createPlayerStatsIcons() {
        healthIcon = new ImageView();
        goldIcon = new ImageView();
        scoreIcon = new ImageView();

        healthIcon.setImage(assetFinder.getHealthIcon());
        goldIcon.setImage(assetFinder.getGoldIcon());
        scoreIcon.setImage(assetFinder.getScoreIcon());

        healthIcon.setX(STATS_XPOS);
        goldIcon.setX(STATS_XPOS);
        scoreIcon.setX(STATS_XPOS);


        healthIcon.setY(STATS_YPOS + STATS_ICON_PADDING);
        goldIcon.setY(STATS_YPOS + STATS_YPOS_INTERVAL + STATS_ICON_PADDING);
        scoreIcon.setY(STATS_YPOS + STATS_YPOS_INTERVAL * 2 + STATS_ICON_PADDING);

        healthIcon.setFitWidth(STATS_ICON_SIZE);
        goldIcon.setFitWidth(STATS_ICON_SIZE);
        scoreIcon.setFitWidth(STATS_ICON_SIZE);

        healthIcon.setFitHeight(STATS_ICON_SIZE);
        goldIcon.setFitHeight(STATS_ICON_SIZE);
        scoreIcon.setFitHeight(STATS_ICON_SIZE);
    }

    private ImageView createPlayerStatsBackground() {
        ImageView viewer = new ImageView();
        viewer.setImage(assetFinder.getItemSlot());
        viewer.setX(0);
        viewer.setY(0);
        viewer.setFitWidth(100);
        viewer.setFitHeight(180);

        return viewer;
    }

    private ImageView createPlayerViewer(SceneConfig sceneConfig) {
        return new ImageView();
    }

    private ImageView createLookAtViewer(SceneConfig sceneConfig) {
        return new ImageView();
    }

    private void openInGameMenu(Event e) {
        InGameMenu menu = new InGameMenu(this);
        openingInGameMenu = true;
        if (audioPlayer != null)
            audioPlayer.stop();
        changeState(menu);
    }

    private void createFileNameLabel() {
        fileNameLabel = new Label();
        fileNameLabel.setTranslateX(15);
        fileNameLabel.setTranslateY(600);
        fileNameLabel.setId("infoLabel");
    }

    private void createPathLabel() {
        pathLabel = new Label();
        pathLabel.setTranslateX(15);
        pathLabel.setTranslateY(625);
        pathLabel.setId("infoLabel");
    }

    private void createBrokenLinksLabel() {
        brokenLinksLabel = new Label();
        brokenLinksLabel.setTranslateX(15);
        brokenLinksLabel.setTranslateY(650);
        brokenLinksLabel.setId("infoLabel");
    }
}
