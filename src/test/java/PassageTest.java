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
        assertTrue(p.addLink(testLink));
        assertFalse(p.addLink(testLink));
    }

    @Test
    void has_links_test() {
        List<Link> testLinks = new ArrayList<>();
        Passage p = new Passage("Test title", "Test content", testLinks);
        assertFalse(p.hasLinks());
        Link testLink = new Link("Testing text", "Testing reference");
        p.addLink(testLink);
        assertTrue(p.hasLinks());

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

        assertEquals(title, p.getTitle());
        assertEquals(content, p.getContent());
        assertEquals(p, equal);
        assertNotEquals(p, unequal);
    }
}