package edu.ntnu.idatt2001.paths;

import javafx.application.Platform;

import java.util.concurrent.locks.ReentrantLock;

public class FXInitializer {

    private static boolean fxInitialized = false;
    private static boolean fxStartupRan = false;

    private static int numRefs = 0;

    private static final ReentrantLock mutex = new ReentrantLock();

    public static void addRef() {
        mutex.lock();
        numRefs++;

        if (fxInitialized) {
            mutex.unlock();
            return;
        }

        loadFX();
        mutex.unlock();
    }

    public static void release() {
        mutex.lock();

        if (numRefs <= 0) {
            mutex.unlock();
            return;
        }

        numRefs--;
        if (numRefs == 0)
            unloadFX();

        mutex.unlock();
    }

    private static void loadFX() {
        if (!fxStartupRan) {
            Platform.startup(() -> {});
            fxStartupRan = true;
        } else
            Platform.runLater(() -> {});

        fxInitialized = true;
    }

    private static void unloadFX() {
        Platform.exit();
    }
}
