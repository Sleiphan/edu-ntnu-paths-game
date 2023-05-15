package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.model.Link;
import edu.ntnu.idatt2001.paths.model.Passage;
import edu.ntnu.idatt2001.paths.model.Story;
import edu.ntnu.idatt2001.paths.model.action.*;
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
    void does_not_throw_exception_and_returns_null_at_failed_parsing() {
        PathsParser parser = new PathsParser();

        Story   o1 = parser.fromPathsFormatStory(null);
        Passage o2 = parser.fromPathsFormatPassage(null);
        Link    o3 = parser.fromPathsFormatLink(null);
        Action  o4 = parser.fromPathsFormatAction(null);
        Story   o5 = parser.fromPathsFormatStory("");
        Story   o6 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\n\n::Passage 3\nSome Text\n\n::Passage 4\n\n");
        Story   o7 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n([]");
        Story   o8 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n()]");
        Story   o9 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n()[]");
        Story   o10 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n(]");
        Story   o11 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n)[");
        Story   o12 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n(Link Text)[]");
        Story   o13 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\n::Passage 2\nText\n()[Link ref]");
        Story   o14 = parser.fromPathsFormatStory("Story\n\n::Passage 1\nSome Text\n\nPassage 2\nText\n(Link Text)[Link ref]{}");

        assert o1 == null;
        assert o2 == null;
        assert o3 == null;
        assert o4 == null;
        assert o5 == null;
        assert o6 == null;
        assert o7 == null;
        assert o8 == null;
        assert o9 == null;
        assert o10 == null;
        assert o11 == null;
        assert o12 == null;
        assert o13 == null;
        assert o14 == null;
    }

    @Test
    void parsing_a_complete_story_CRLF() {
        Story s = getTestStory();
        PathsParser parser = new PathsParser();

        final String CRLF = "\r\n";
        final String LF = "\n";

        String parsed = parser.toPathsFormat(s);
        String parsedCRLF = parsed.replaceAll(LF+"(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", CRLF);
        Story copy = parser.fromPathsFormatStory(parsedCRLF);
        String copyParsed = parser.toPathsFormat(copy);

        boolean equal = parsed.equals(copyParsed);
        assert equal;
    }

    @Test
    void parsing_a_complete_story() {
        Story s = getTestStory();
        PathsParser parser = new PathsParser();

        String parsed = parser.toPathsFormat(s);
        Story copy = parser.fromPathsFormatStory(parsed);
        String copyParsed = parser.toPathsFormat(copy);

        boolean equal = parsed.equals(copyParsed);
        assert equal;
    }

    @Test
    void parse_from_passage_object_to_string(){
        List<Link> linkList = new ArrayList<>();
        Link link = new Link("test","test");
        linkList.add(link);
        Passage passage = new Passage("test","test",linkList);
        PathsParser parser = new PathsParser();

        String passageString = parser.toPathsFormat(passage);
        String expected = "::test\ntest\n(test)[test]";

        assert passageString.equals(expected);
    }

    @Test
    void parse_from_string_object_to_passage_object(){
        String passageString = "::test\ntest\n(test)[test]";
        PathsParser parser = new PathsParser();

        Passage passage = parser.fromPathsFormatPassage(passageString);

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
        PathsParser parser = new PathsParser();

        String linkString = parser.toPathsFormat(link);
        Link linkCopy = parser.fromPathsFormatLink(linkString);

        assert link.equals(linkCopy);
    }

    @Test
    public void casting_to_correct_Action_type() {
        PathsParser parser = new PathsParser();

        Action a = parser.fromPathsFormatAction("{GoldAction:10}");
        Action b = parser.fromPathsFormatAction("{HealthAction:8}");
        Action c = parser.fromPathsFormatAction("{InventoryAction:\"Wicker's Blade\"}");
        Action d = parser.fromPathsFormatAction("{ScoreAction:12}");

        assert(a instanceof GoldAction);
        assert(b instanceof HealthAction);
        assert(c instanceof InventoryAction);
        assert(d instanceof ScoreAction);
    }

    @Test
    public void action_parsing_deals_with_invalid_input() {
        Action[] results = new Action[10];
        PathsParser parser = new PathsParser();

        Assertions.assertDoesNotThrow(() -> results[0] = parser.fromPathsFormatAction(""));
        Assertions.assertDoesNotThrow(() -> results[1] = parser.fromPathsFormatAction(null));
        Assertions.assertDoesNotThrow(() -> results[2] = parser.fromPathsFormatAction("{InventoryAction:tqer}"));
        Assertions.assertDoesNotThrow(() -> results[3] = parser.fromPathsFormatAction("{:15}"));
        Assertions.assertDoesNotThrow(() -> results[4] = parser.fromPathsFormatAction("{:}"));
        Assertions.assertDoesNotThrow(() -> results[5] = parser.fromPathsFormatAction("{}"));
        Assertions.assertDoesNotThrow(() -> results[6] = parser.fromPathsFormatAction("{GoldAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[7] = parser.fromPathsFormatAction("{HealthAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[8] = parser.fromPathsFormatAction("{ScoreAction:\"abc\"}"));
        Assertions.assertDoesNotThrow(() -> results[9] = parser.fromPathsFormatAction("{ScoreAction:\"abc\"}"));


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
        PathsParser parser = new PathsParser();
        InventoryAction[] results = new InventoryAction[3];
        String item1 = "Crossbow";
        String item2 = "Enraged rattlesnake";
        String item3 = "Bolts: use with crossbow";

        Assertions.assertDoesNotThrow(() -> results[0] = (InventoryAction) parser.fromPathsFormatAction("{InventoryAction:\""+item1+"\"}"));
        Assertions.assertDoesNotThrow(() -> results[1] = (InventoryAction) parser.fromPathsFormatAction("{InventoryAction:\""+item2+"\"}"));
        Assertions.assertDoesNotThrow(() -> results[2] = (InventoryAction) parser.fromPathsFormatAction("{InventoryAction:\""+item3+"\"}"));

        assert results[0] != null;
        assert results[1] != null;
        assert results[2] != null;

        assert results[0].getItem().equals(item1);
        assert results[1].getItem().equals(item2);
        assert results[2].getItem().equals(item3);
    }

    @Test
    public void link_with_script_and_condition() {
        PathsParser parser = new PathsParser();
        Link link1 = new Link("Unlock door", "Throne room");
        link1.setCondition("has_throne_room_key == true");
        Link link2 = new Link("Unlock door", "Throne room");
        link2.setScript("throne_room_unlocked = true");
        Link link3 = new Link("Unlock door", "Throne room");
        link3.setScript("throne_room_unlocked = true");
        link3.setCondition("has_throne_room_key == true");

        String parsed1 = parser.toPathsFormat(link1);
        String parsedCopy1 = parser.toPathsFormat(parser.fromPathsFormatLink(parsed1));
        String parsed2 = parser.toPathsFormat(link2);
        String parsedCopy2 = parser.toPathsFormat(parser.fromPathsFormatLink(parsed2));
        String parsed3 = parser.toPathsFormat(link3);
        String parsedCopy3 = parser.toPathsFormat(parser.fromPathsFormatLink(parsed3));

        assert parsed1.equals(parsedCopy1);
        assert parsed2.equals(parsedCopy2);
        assert parsed3.equals(parsedCopy3);
    }
}
