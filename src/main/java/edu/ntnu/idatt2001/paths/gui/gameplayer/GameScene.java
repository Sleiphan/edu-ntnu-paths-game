package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.Game;
import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.action.Action;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import edu.ntnu.idatt2001.paths.gui.InGameMenu;
import edu.ntnu.idatt2001.paths.gui.PathsMenu;
import edu.ntnu.idatt2001.paths.gui.SceneConfig;
import edu.ntnu.idatt2001.paths.io.StoryLoader;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameScene extends PathsMenu {

    private static final int LINK_ENTRY_HEIGHT = 20;
    private static final int LINK_ENTRY_HEIGHT_DISTANCE = 5;
    private static final int LINK_ENTRY_HEIGHT_INTERVAL = LINK_ENTRY_HEIGHT + LINK_ENTRY_HEIGHT_DISTANCE;
    private static final int LINK_ENTRY_LABEL_X = 30;

    private static final int STATS_XPOS = 15;
    private static final int STATS_YPOS = 20;
    private static final int STATS_YPOS_INTERVAL = 40;
    private static final int STATS_ICON_PADDING = 10;
    private static final int STATS_ICON_SIZE =  STATS_YPOS_INTERVAL - STATS_ICON_PADDING;
    private static final int STATS_LABEL_XPOS = STATS_XPOS + STATS_YPOS_INTERVAL;
    private static final int PLAYER_MIN_SIZE = 256;

    private final Game game;
    private ScrollPane linkSelector;
    private TextArea textViewer;
    private Label healthLabel;
    private Label goldLabel;
    private Label scoreLabel;
    private AnchorPane root;
    private final Scene scene;


    private final SceneConfig sceneConfig;
    private final PathsAssetStore assetStore;
    private final AssetFinder assetFinder;
    private ImageView passageViewer;
    private ImageView playerViewer;
    private ImageView lookAtViewer;
    private ImageView healthIcon;
    private ImageView goldIcon;
    private ImageView scoreIcon;
    private ItemViewer itemViewer;
    private boolean openingInGameMenu;


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
        if (hasAssets())
            updateAssets(game.begin());
    }

    public boolean hasAssets() {
        return assetStore != null;
    }

    @Override
    public void setup() {
        if (openingInGameMenu) {
            openingInGameMenu = false;
            return;
        }
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
     * Returns the root pane of this scene.
     * @return the root pane of this scene.
     */
    public AnchorPane getRootPane() {
        return root;
    }

    /**
     * The method called when the user selects a link.
     * It updates the GUI-components with the contents of the next passage.
     * If the submitted link is a broken link (a link that does not point to a Passage),
     * then the transition does not happen.
     * @param link The link that the user has chosen.
     */
    private void goLink(Link link) {
        Passage newPassage = game.go(link);

        if (newPassage == null)
            return;

        performLinkActions(link);
        updateContent(newPassage);

        if (hasAssets())
            updateAssets(newPassage);
    }

    private void performLinkActions(Link link) {
        for (Action a : link.getActions()) {
            a.execute(game.getPlayer());

            if (hasAssets())
                itemViewer.processAction(a);
        }
    }



    private void updateContent(Passage newPassage) {
        updateLinkSelector(newPassage);
        updatePlayerStats();
        updateText(newPassage);
    }

    private void updateLinkSelector(Passage newPassage) {
        AnchorPane root = new AnchorPane();

        int y = 0;
        for (Link l : newPassage.getLinks()) {
            Label label = new Label(l.getText());
            label.setId("Link_entry_label");
            label.setTranslateX(LINK_ENTRY_LABEL_X);
            label.setTranslateY(y);

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

        linkSelector.setContent(root);
    }

    private void updatePlayerStats() {
        healthLabel.setText("" + game.getPlayer().getHealth());
        goldLabel  .setText("" + game.getPlayer().getGold());
        scoreLabel .setText("" + game.getPlayer().getScore());
    }

    private void updateText(Passage newPassage) {
        textViewer.setText(newPassage.getContent());
    }

    private void updateAssets(Passage newPassage) {
        updateBackground(newPassage);
        updatePlayerAsset(newPassage);
        updateInteractionObject(newPassage);
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

        /*if (playerImg.getWidth() < PLAYER_MIN_SIZE)
            playerViewer.setFitWidth(PLAYER_MIN_SIZE);
        else
            playerViewer.setFitWidth(0);
        if (playerImg.getHeight() < PLAYER_MIN_SIZE)
            playerViewer.setFitHeight(PLAYER_MIN_SIZE);
        else
            playerViewer.setFitHeight(0);
         */

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

        /*if (lookAtImg.getWidth() < PLAYER_MIN_SIZE)
            lookAtViewer.setFitWidth(PLAYER_MIN_SIZE);
        else
            lookAtViewer.setFitWidth(0);
        if (lookAtImg.getHeight() < PLAYER_MIN_SIZE)
            lookAtViewer.setFitHeight(PLAYER_MIN_SIZE);
        else
            lookAtViewer.setFitHeight(0);

         */

        final int playerX = (int) ((sceneConfig.getWidth() * 4) / 5 - lookAtImg.getWidth() / 2);
        final int playerY = (int) ((sceneConfig.getHeight() * 2) / 3 - lookAtImg.getHeight());

        System.out.println(playerX);
        System.out.println(playerY);

        lookAtViewer.setImage(lookAtImg);
        lookAtViewer.setX(playerX);
        lookAtViewer.setY(playerY);
    }



    private Scene createScene(SceneConfig sceneConfig) {
        linkSelector = createLinkSelector(sceneConfig);
        Button menuBt = createMenuButton(sceneConfig);
        textViewer = createTextArea(sceneConfig);
        createPlayerStatus();

        root = new AnchorPane();

        if (hasAssets()) {
            itemViewer = createItemViewer(sceneConfig);
            passageViewer = createPassageViewer(sceneConfig);
            playerViewer = createPlayerViewer(sceneConfig);
            lookAtViewer = createLookAtViewer(sceneConfig);
            ImageView interactionArea = createInteractionArea(sceneConfig);
            createPlayerStatsIcons();


            root.getChildren().add(passageViewer);
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
        final int x = sceneConfig.getWidth() / 2 - width / 2;

        TextArea textViewer = new TextArea();
        textViewer.setWrapText(true);
        textViewer.setEditable(false);
        textViewer.setTranslateX(x);
        textViewer.setTranslateY(y);
        textViewer.setPrefWidth (width);
        textViewer.setPrefHeight(height);
        if (hasAssets()) {
            Image img = assetFinder.getTextArea();
            if (img != null) {
                String url = img.getUrl();
                textViewer.setStyle("-fx-background-color: null;\n" +
                        "-fx-background-repeat: no-repeat;\n" +
                        "-fx-background-size: "+width+" "+height+";\n" +
                        "-fx-background-image: url(" + url + ");");
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
        final int width = 70;
        final int height = 70;

        Button menuButton = new Button("Menu");
        menuButton.setOnAction(this::openInGameMenu);
        menuButton.setTranslateX(xPos);
        menuButton.setTranslateY(yPos);
        //menuButton.setPrefWidth(width);
        //menuButton.setPrefHeight(height);
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

        Image itemSlot = assetFinder.getItemSlot();
        Image itemArea = assetFinder.getItemArea();

        itemViewer.setInventorySlotImage(itemSlot);
        itemViewer.setInventoryAreaImage(itemArea);
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

    private ImageView createPlayerViewer(SceneConfig sceneConfig) {
        return new ImageView();
    }

    private ImageView createLookAtViewer(SceneConfig sceneConfig) {
        return new ImageView();
    }

    private void openInGameMenu(Event e) {
        InGameMenu menu = new InGameMenu(this);
        openingInGameMenu = true;
        changeState(menu);
    }
}
