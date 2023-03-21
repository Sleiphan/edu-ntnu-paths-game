package edu.ntnu.idatt2001.paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StoryTest {

    @Test
    void constructor() {
        Passage p = new Passage("", "", new ArrayList<Link>());
        assertThrows(IllegalArgumentException.class, () -> new Story(null, p));
        assertThrows(IllegalArgumentException.class, () -> new Story("", null));
        assertDoesNotThrow(() -> new Story("", p));
    }

    @Test
    void addPassage_creates_correct_mapping() {
        String titleP1 = "edu.ntnu.idatt2001.paths.Passage 1";
        String titleP2 = "edu.ntnu.idatt2001.paths.Passage 2";

        Link l1 = new Link("Testing text", titleP2);
        Link l2 = new Link("Testing text", titleP1);

        ArrayList<Link> linksP1 = new ArrayList<>();
        ArrayList<Link> linksP2 = new ArrayList<>();
        linksP1.add(l1);
        linksP2.add(l2);

        Passage p1 = new Passage(titleP1, "Testing content", linksP1);
        Passage p2 = new Passage(titleP2, "Testing content", linksP2);

        Story s = new Story("Test story", p1);
        s.addPassage(p2);
        Passage result = s.getPassage(l1);

        Assertions.assertEquals(s.getPassage(l1), p2);
        Assertions.assertEquals(s.getPassage(l2), p1);
    }

    @Test
    void removePassage() {
        String titleTitleScreen = "Title screen";
        String titleTutorialIsland = "Tutorial Island";
        String titleTutorial1 = "Tutorial 1";
        String titleSelfRef = "Self referencing passage";

        Link toTutorialIsland = new Link("Go to Tutorial Island", titleTutorialIsland);
        Link toTutorial1 = new Link("Go to tutorial 1", titleTutorial1);
        Link backToTutorialIsland = new Link("Go back to Tutorial Island", titleTutorialIsland);
        Link linkSelfRef = new Link("Go back to this passage", titleSelfRef);

        ArrayList<Link> linksTitleScreen = new ArrayList<>();
        linksTitleScreen.add(toTutorialIsland);
        ArrayList<Link> linksTutorialIsland = new ArrayList<>();
        linksTutorialIsland.add(toTutorial1);
        ArrayList<Link> linksTutorial1 = new ArrayList<>();
        linksTutorial1.add(backToTutorialIsland);
        ArrayList<Link> linksSelfRef = new ArrayList<>();
        linksSelfRef.add(linkSelfRef);

        Passage titleScreen = new Passage(titleTitleScreen, "Welcome to the game!", linksTitleScreen);
        Passage tutorialIsland = new Passage(titleTutorialIsland, "You are at Tutorial Island", linksTutorialIsland);
        Passage tutorial1 = new Passage(titleTutorial1, "This is tutorial 1", linksTutorial1);
        Passage selfRef = new Passage(titleSelfRef, "This is a self referring passage", linksSelfRef);


        Story s = new Story("Test story", titleScreen);
        s.addPassage(tutorialIsland);
        s.addPassage(tutorial1);
        s.addPassage(selfRef);



        // Here, we are trying to remove the passage "Tutorial Island".
        // This should not work, as that passage is pointed to by both links 'toTutorialIsland' and 'backToTutorialIsland'.
        int lastPassageCount = s.getPassages().size();
        s.removePassage(toTutorialIsland);
        assertEquals(lastPassageCount, s.getPassages().size());

        // Here, we are trying to remove the passage "Tutorial 1".
        // This should not work, as it is pointed to by the link 'toTutorial1'.
        lastPassageCount = s.getPassages().size();
        s.removePassage(toTutorial1);//new Link("Placeholder to remove tutorial 1", titleTutorial1));
        assertEquals(lastPassageCount, s.getPassages().size());

        // Here, we are trying to remove the passage "Title screen".
        // This should work, as it is not pointed to by any link.
        lastPassageCount = s.getPassages().size();
        s.removePassage(new Link("Placeholder to remove title screen", titleTitleScreen));
        assertEquals(lastPassageCount - 1, s.getPassages().size());

        // Here, we are trying to remove the passage "Self referencing passage".
        // This should work, as it is only pointed to by itself, aka. the link 'linkSelfRef'
        lastPassageCount = s.getPassages().size();
        s.removePassage(new Link("Placeholder to remove self referencing passage", titleSelfRef));
        assertEquals(lastPassageCount - 1, s.getPassages().size());
    }
}