package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.action.Action;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import edu.ntnu.idatt2001.paths.gui.SceneConfig;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class ItemViewer {

    public static final int NUM_ITEM_SPOTS = 14;

    private final PathsAssetStore assetStore;
    private final ImageView[] itemSpots = new ImageView[NUM_ITEM_SPOTS];
    private final ImageView[] itemSlots = new ImageView[NUM_ITEM_SPOTS];
    private ImageView inventoryArea;
    private final List<String> items = new ArrayList<>();

    public ItemViewer(SceneConfig sceneConfig, PathsAssetStore assetStore) {
        this.assetStore = assetStore;

        final int xPos = 0;
        final int yPos = (sceneConfig.getHeight() * 2) / 3;
        final int width  = sceneConfig.getWidth();
        final int height = 80;
        final int itemPadX = 5;
        final int itemPadY = 5;
        final int itemWidth  = height - itemPadX * 2;
        final int itemHeight = height - itemPadY * 2;

        inventoryArea = new ImageView();
        inventoryArea.setTranslateX(xPos);
        inventoryArea.setTranslateY(yPos);
        inventoryArea.setFitWidth(width);
        inventoryArea.setFitHeight(height);

        final int itemY = yPos + itemPadY;

        for (int i = 0; i < NUM_ITEM_SPOTS; i++) {
            int itemX = i * width / (NUM_ITEM_SPOTS) + itemPadX;

            itemSlots[i] = new ImageView();
            itemSlots[i].setFitWidth(itemWidth);
            itemSlots[i].setFitHeight(itemHeight);
            itemSlots[i].setX(itemX);
            itemSlots[i].setY(itemY);
            itemSlots[i].setId("Inventory_slot");

            itemSpots[i] = new ImageView();
            itemSpots[i].setFitWidth(itemWidth);
            itemSpots[i].setFitHeight(itemHeight);
            itemSpots[i].setX(itemX);
            itemSpots[i].setY(itemY);
        }

        updateSpots();
    }

    private void initializeWithoutAssets() {

    }

    public void addToPane(Pane pane) {
        pane.getChildren().add(inventoryArea);
        for (int i = 0; i < NUM_ITEM_SPOTS; i++) {
            pane.getChildren().add(itemSlots[i]);
            pane.getChildren().add(itemSpots[i]);
        }
    }

    public void processAction(Action a) {
        if (a instanceof InventoryAction inventoryAction)
            addItem(inventoryAction.getItem());
    }

    public void addItem(String item) {
        items.add(item);

        if (!assetStore.images().isLoaded(item))
            assetStore.images().loadAsset(item);

        updateSpots();
    }

    public boolean removeItem(String item) {
        boolean found = false;

        for (int i = 0; i < items.size(); i++)
            if (items.get(i).equals(item)) {
                items.remove(i);
                found = true;
                break;
            }

        if (items.stream().noneMatch(item::equals) && assetStore.images().isLoaded(item))
            assetStore.images().unloadAsset(item);

        updateSpots();

        return found;
    }

    private void updateSpots() {
        int imageCount = Math.min(items.size(), itemSpots.length);

        for (int i = 0; i < imageCount; i++)
            itemSpots[i].setImage(assetStore.images().getAsset(items.get(i)));

        for (int i = imageCount; i < itemSpots.length; i++)
            itemSpots[i].setImage(null);
    }

    public void setInventorySlotImage(Image img) {
        for (ImageView i : itemSlots)
            i.setImage(img);
    }

    public void setInventoryAreaImage(Image img) {
        inventoryArea.setImage(img);
    }
}
