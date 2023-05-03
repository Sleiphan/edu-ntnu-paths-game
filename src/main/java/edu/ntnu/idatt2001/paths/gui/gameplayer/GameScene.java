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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameScene extends PathsMenu {

    private static final int LINK_ENTRY_HEIGHT = 20;
    private static final int LINK_ENTRY_HEIGHT_DISTANCE = 5;
    private static final int LINK_ENTRY_HEIGHT_INTERVAL = LINK_ENTRY_HEIGHT + LINK_ENTRY_HEIGHT_DISTANCE;
    private static final int LINK_ENTRY_LABEL_X = 30;

    private final Game game;
    private ScrollPane linkSelector;
    private TextArea textViewer;
    private Label healthLabel;
    private Label goldLabel;
    private Label scoreLabel;
    private final Scene scene;


    private final PathsAssetStore assetStore;
    private final AssetFinder assetFinder;
    private ImageView passageViewer;
    private ItemViewer itemViewer;
    private boolean openingInGameMenu;


    public GameScene(Game game, StoryLoader s, SceneConfig sceneConfig) {
        this.game = game;
        this.assetStore = s.getAssetStore();

        if (hasAssets())
            this.assetFinder = new AssetFinder(assetStore);
        else
            this.assetFinder = null;

        this.scene = createScene(sceneConfig);

        updateContent(game.begin());
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
        passageViewer.setImage(assetFinder.getBackground(newPassage.getTitle()));
    }

    private Scene createScene(SceneConfig sceneConfig) {
        linkSelector = createLinkSelector(sceneConfig);
        Button menuBt = createMenuButton(sceneConfig);
        textViewer = createTextArea(sceneConfig);
        createPlayerStatus();

        AnchorPane root = new AnchorPane();
        root.getChildren().add(linkSelector);
        root.getChildren().add(menuBt);
        root.getChildren().add(textViewer);
        root.getChildren().add(healthLabel);
        root.getChildren().add(goldLabel);
        root.getChildren().add(scoreLabel);


        if (hasAssets()) {
            itemViewer = createItemViewer(sceneConfig);
            passageViewer = createPassageViewer(sceneConfig);

            itemViewer.addToPane(root);
            root.getChildren().add(passageViewer);
        }

        Scene scene = new Scene(root, sceneConfig.getWidth(), sceneConfig.getHeight());
        scene.getStylesheets().add("GameScene.css");
        return scene;
    }

    private TextArea createTextArea(SceneConfig sceneConfig) {
        final int height = 150;
        final int width = 350;
        final int y = sceneConfig.getHeight() - height;

        TextArea textViewer = new TextArea();
        textViewer.setWrapText(true);
        textViewer.setEditable(false);
        textViewer.setTranslateX(0);
        textViewer.setTranslateY(y);
        textViewer.setPrefWidth (width);
        textViewer.setPrefHeight(height);
        textViewer.setId("Text_viewer");

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
        healthLabel.setTranslateX(60);
        healthLabel.setTranslateY(20);
        healthLabel.setId("Status_label");

        goldLabel = new Label();
        goldLabel.setTranslateX(60);
        goldLabel.setTranslateY(40);
        goldLabel.setId("Status_label");

        scoreLabel = new Label();
        scoreLabel.setTranslateX(60);
        scoreLabel.setTranslateY(60);
        scoreLabel.setId("Status_label");
    }

    private ItemViewer createItemViewer(SceneConfig sceneConfig) {
        ItemViewer itemViewer = new ItemViewer(sceneConfig, assetStore);
        itemViewer.setInventorySlotImage(assetFinder.getItemSlot());
        itemViewer.setInventoryAreaImage(assetFinder.getItemArea());
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


    private void openInGameMenu(Event e) {
        InGameMenu menu = new InGameMenu(this);
        openingInGameMenu = true;
        changeState(menu);
    }
}
