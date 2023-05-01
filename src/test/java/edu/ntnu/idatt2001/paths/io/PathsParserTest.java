package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.action.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PathsParserTest {

    private Story getTestStory() {
        // First, create a story we can do parsing with.
        String titleTitleScreen = "Title screen";
        String titleTutorialIsland = "Tutorial Island";
        String titleTutorial1 = "Tutorial 1";
        String titleSelfRef = "Self referencing passage";

        Link toTutorialIsland = new Link("Go to Tutorial Island", titleTutorialIsland);
        Link toTutorial1 = new Link("Go to tutorial 1", titleTutorial1);
        Link backToTutorialIsland = new Link("Go back to Tutorial Island", titleTutorialIsland);
        Link linkSelfRef = new Link("Go back to this passage", titleSelfRef);
        Link linkTitleScreen = new Link("Go to this title screen", titleTitleScreen);

        ScoreAction tut1score = new ScoreAction(40);
        toTutorial1.addAction(tut1score);
        GoldAction tut1gold = new GoldAction(60);
        toTutorial1.addAction(tut1gold);

        ArrayList<Link> linksTitleScreen = new ArrayList<>();
        linksTitleScreen.add(toTutorialIsland);
        linksTitleScreen.add(linkTitleScreen);
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

        return s;
    }

    @Test
    void parsing_a_complete_story_CRLF() {
        Story s = getTestStory();

        final String CRLF = "\r\n";
        final String LF = "\n";

        String parsed = PathsParser.toPathsFormat(s);
        String parsedCRLF = parsed.replaceAll(LF+"(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", CRLF);
        Story copy = PathsParser.fromPathsFormatStory(parsedCRLF);
        String copyParsed = PathsParser.toPathsFormat(copy);

        boolean equal = parsed.equals(copyParsed);
        assert equal;
    }

    @Test
    void parsing_a_complete_story() {
        Story s = getTestStory();

        String parsed = PathsParser.toPathsFormat(s);
        Story copy = PathsParser.fromPathsFormatStory(parsed);
        String copyParsed = PathsParser.toPathsFormat(copy);

        boolean equal = parsed.equals(copyParsed);
        assert equal;
    }

    @Test
    void parse_from_passage_object_to_string(){
        List<Link> linkList = new ArrayList<>();
        Link link = new Link("test","test");
        linkList.add(link);
        Passage passage = new Passage("test","test",linkList);

        String passageString = PathsParser.toPathsFormat(passage);
        String expected = "::test\ntest\n(test)[test]";

        assert passageString.equals(expected);
    }

    @Test
    void parse_from_string_object_to_passage_object(){
        String passageString = "::test\ntest\n(test)[test]";

        Passage passage = PathsParser.fromPathsFormatPassage(passageString);

        assert "test".equals(passage.getTitle());
        assert "test".equals(passage.getContent());
        assert "test".equals(passage.getLinks().get(0).getText());
        assert "test".equals(passage.getLinks().get(0).getReference());
    }

    @Test
    public void parsing_to_and_from_link_object() {
        Link link = new Link("Open chest", "Dungeon Room 5");
        link.addAction(new GoldAction(0));
        link.addAction(new HealthAction(-3));
        link.addAction(new InventoryAction("Sack of gold"));
        link.addAction(new ScoreAction(0));

        String linkString = PathsParser.toPathsFormat(link);
        Link linkCopy = PathsParser.fromPathsFormatLink(linkString);

        assert link.equals(linkCopy);
    }

    @Test
    public void casting_to_correct_Action_type() {
        Action a = PathsParser.fromPathsFormatAction("{GoldAction:10}");
        Action b = PathsParser.fromPathsFormatAction("{HealthAction:8}");
        Action c = PathsParser.fromPathsFormatAction("{InventoryAction:\"Wicker's Blade\"}");
        Action d = PathsParser.fromPathsFormatAction("{ScoreAction:12}");

        assert(a instanceof GoldAction);
        assert(b instanceof HealthAction);
        assert(c instanceof InventoryAction);
        assert(d instanceof ScoreAction);
    }

    @Test
    public void action_parsing_deals_with_invalid_input() {
        Action[] results = new Action[10];


        Assertions.assertDoesNotThrow(() -> results[0] = PathsParser.fromPathsFormatAction(""));
        Assertions.assertDoesNotThrow(() -> results[1] = PathsParser.fromPathsFormatAction(null));
        Assertions.assertDoesNotThrow(() -> results[2] = PathsParser.fromPathsFormatAction("{InventoryAction:tqer}"));
        Assertions.assertDoesNotThrow(() -> results[3] = PathsParser.fromPathsFormatAction("{:15}"));
        Assertions.assertDoesNotThrow(() -> results[4] = PathsParser.fromPathsFormatAction("{:}"));
        Assertions.assertDoesNotThrow(() -> results[5] = PathsParser.fromPathsFormatAction("{}"));
        Assertions.assertDoesNotThrow(() -> results[6] = PathsParser.fromPathsFormatAction("{GoldAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[7] = PathsParser.fromPathsFormatAction("{HealthAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[8] = PathsParser.fromPathsFormatAction("{ScoreAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[9] = PathsParser.fromPathsFormatAction("{ScoreAction:\"abc\"}"));


        assert results[0] == null;
        assert results[1] == null;
        assert results[2] == null;
        assert results[3] == null;
        assert results[4] == null;
        assert results[5] == null;
        assert results[6] == null;
        assert results[7] == null;
        assert results[8] == null;
        assert results[9] == null;
    }

    @Test
    public void parsing_inventory_action_does_not_change_content() {
        InventoryAction[] results = new InventoryAction[3];
        String item1 = "Crossbow";
        String item2 = "Enraged rattlesnake";
        String item3 = "Bolts: use with crossbow";

        Assertions.assertDoesNotThrow(() -> results[0] = (InventoryAction) PathsParser.fromPathsFormatAction("{InventoryAction:\""+item1+"\"}"));
        Assertions.assertDoesNotThrow(() -> results[1] = (InventoryAction) PathsParser.fromPathsFormatAction("{InventoryAction:\""+item2+"\"}"));
        Assertions.assertDoesNotThrow(() -> results[2] = (InventoryAction) PathsParser.fromPathsFormatAction("{InventoryAction:\""+item3+"\"}"));

        assert results[0] != null;
        assert results[1] != null;
        assert results[2] != null;

        assert results[0].getItem().equals(item1);
        assert results[1].getItem().equals(item2);
        assert results[2].getItem().equals(item3);
    }
}
