package edu.ntnu.idatt2001.paths.gui.gameplayer;

import edu.ntnu.idatt2001.paths.asset.PathsAssetStore;
import edu.ntnu.idatt2001.paths.gui.SceneConfig;
import edu.ntnu.idatt2001.paths.model.action.Action;
import edu.ntnu.idatt2001.paths.model.action.InventoryAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays items along a line of item-slots inside a Pane. This is a specific
 * object that is exclusively part of GameScene. It defines a connection between item names and
 * assets in PathsAssetStore, retrieving images from an asset store by using the item name as the
 * key.
 */
public class ItemViewer {

    public static final int NUM_ITEM_SPOTS = 14;

    private final PathsAssetStore assetStore;
    private final ImageView[] itemSpots = new ImageView[NUM_ITEM_SPOTS];
    private final ImageView[] itemSlots = new ImageView[NUM_ITEM_SPOTS];
    private final List<String> items = new ArrayList<>();
    private final ImageView inventoryArea;

    /**
     * Creates a new ItemViewer to display items from an asset store.
     * @param sceneConfig The width and height of the scene.
     * @param assetStore The asset store to read images from.
     */
    public ItemViewer(SceneConfig sceneConfig, PathsAssetStore assetStore) {
        this.assetStore = assetStore;

        final int xPos = 0;
        final int yPos = (sceneConfig.getHeight() * 2) / 3;
        final int width = sceneConfig.getWidth();
        final int height = 80;
        final int itemPadX = 5;
        final int itemPadY = 5;
        final int itemWidth = height - itemPadX * 2;
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

    /**
     * Adds this ItemViewer to a JavaFX pane.
     * @param pane The pane to add this ItemViewer to.
     */
    public void addToPane(Pane pane) {
        pane.getChildren().add(inventoryArea);
        for (int i = 0; i < NUM_ITEM_SPOTS; i++) {
            pane.getChildren().add(itemSlots[i]);
            pane.getChildren().add(itemSpots[i]);
        }
    }

    /**
     * A helper method for iterating over a set of Actions. This method updates this ItemViewer
     * based on any InventoryActions received as an argument.
     * @param a An Action. Only InventoryActions changes the state of this ItemViewer.
     */
    public void processAction(Action a) {
        if (a instanceof InventoryAction inventoryAction) {
            if (inventoryAction.addsItem())
                addItem(inventoryAction.getItem());
            else
                removeItem(inventoryAction.getItem());
        }
    }

    /**
     * Adds an item to this ItemViewer, adding its image to the item spots. The new item is placed
     * to the right of any existing items.
     * @param item The new item to add to this ItemViewer.
     */
    public void addItem(String item) {
        items.add(item);

        if (!assetStore.images().isLoaded(item))
            assetStore.images().loadAsset(item);

        updateSpots();
    }

    /**
     * Removes an item from this ItemViewer, removing its image from the item spots. If two items
     * with the same asset key exists in this ItemViewer, the rightmost item is removed from the
     * view.
     * @param item The name of the item to remove, equal to its key in the asset store.
     * @return <code>true</code> if an item with the submitted name was found and removed.
     *         Otherwise, returns false.
     */
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

    /**
     * Updates the item spots with the images returned from the asset store. This method is
     * called after adding or removing items from this viewer.
     */
    private void updateSpots() {
        int imageCount = Math.min(items.size(), itemSpots.length);

        for (int i = 0; i < imageCount; i++)
            itemSpots[i].setImage(assetStore.images().getAsset(items.get(i)));

        for (int i = imageCount; i < itemSpots.length; i++)
            itemSpots[i].setImage(null);
    }

    /**
     * Changes the image representing the item-slots.
     * @param img the new image to represent the item-slots.
     */
    public void setInventorySlotImage(Image img) {
        for (ImageView i : itemSlots)
            i.setImage(img);
    }

    /**
     * Changes the background of this ItemViewer to the submitted image.
     * @param img the new background of this ItemViewer.
     */
    public void setInventoryAreaImage(Image img) {
        inventoryArea.setImage(img);
    }
}
