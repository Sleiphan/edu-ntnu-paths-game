package edu.ntnu.idatt2001.paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassageTest {

    @Test
    void add_link_test() {
        List<Link> testLinks = new ArrayList<>();
        Passage p = new Passage("Test title", "Test content", testLinks);
        Link testLink = new Link("Testing text", "Testing reference");
        Assertions.assertTrue(p.addLink(testLink));
        Assertions.assertFalse(p.addLink(testLink));
    }

    @Test
    void has_links_test() {
        List<Link> testLinks = new ArrayList<>();
        Passage p = new Passage("Test title", "Test content", testLinks);
        Assertions.assertFalse(p.hasLinks());
        Link testLink = new Link("Testing text", "Testing reference");
        p.addLink(testLink);
        Assertions.assertTrue(p.hasLinks());

    }

    @Test
    void passage_class_equality_test() {
        List<Link> testLinks = new ArrayList<>();
        String title = "Testing title";
        String content = "Testing content";

        Passage p = new Passage(title , content, testLinks);
        Passage equal = new Passage(title, content, testLinks);
        Passage unequal = new Passage("unequal test", "unequal content", testLinks);
        Link testLink = new Link("Testing text", "Testing reference");
        unequal.addLink(testLink);

        Assertions.assertEquals(title, p.getTitle());
        Assertions.assertEquals(content, p.getContent());
        Assertions.assertEquals(p, equal);
        Assertions.assertNotEquals(p, unequal);
    }

    @Test
    void toPathsFormat(){
        List<Link> linkList = new ArrayList<>();
        Link link = new Link("test","test");
        linkList.add(link);
        Passage passage = new Passage("test","test",linkList);
        String referenceString = "::test\ntest\n(test)[test]";
        Assertions.assertEquals(referenceString,passage.toPathsFormat());
    }

    @Test
    void fromPathsFormat(){
        String referenceString = "::test\ntest\n(test)[test]";
        Passage passage = Passage.fromPathsFormat(referenceString);
        Assertions.assertEquals("test",passage.getTitle());
        Assertions.assertEquals("test",passage.getContent());
        Assertions.assertEquals("test",passage.getLinks().get(0).getText());
        Assertions.assertEquals("test",passage.getLinks().get(0).getReference());
    }
}