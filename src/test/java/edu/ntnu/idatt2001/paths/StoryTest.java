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
}