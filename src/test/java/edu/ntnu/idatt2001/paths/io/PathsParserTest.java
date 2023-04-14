package edu.ntnu.idatt2001.paths.io;

import edu.ntnu.idatt2001.paths.Link;
import edu.ntnu.idatt2001.paths.Passage;
import edu.ntnu.idatt2001.paths.Story;
import edu.ntnu.idatt2001.paths.action.GoldAction;
import edu.ntnu.idatt2001.paths.action.HealthAction;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import edu.ntnu.idatt2001.paths.action.ScoreAction;
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
}
