package edu.ntnu.idatt2001.paths;

import com.oracle.truffle.js.runtime.JSContextOptions;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import edu.ntnu.idatt2001.paths.action.Action;
import edu.ntnu.idatt2001.paths.action.InventoryAction;
import edu.ntnu.idatt2001.paths.goal.Goal;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a digital representation of a "paths"-game, connecting a player to a story with specific goals.
 * It comes with methods to both start and progress the game.
 * @author Håkon F. Fjellanger
 */
public class Game {
    private Player player;
    private Story story;
    private List<Goal> goals;
    private ScriptEngine scriptEngine;

    /**
     * Creates a new game with a specified player, story, and a set of goals.
     * @param player The player of this game.
     * @param story The story of this game.
     * @param goals The goals specifying parameters of victory in this game.
     */
    public Game(Player player, Story story, List<Goal> goals) {
        this.player = player;
        this.story = story;
        this.goals = goals;

        this.scriptEngine = GraalJSScriptEngine.create(
                Engine.newBuilder()
                        .option("engine.WarnInterpreterOnly", "false")
                        .build(),
                Context.newBuilder("js")
                        .allowIO(false)
                        .option(JSContextOptions.ECMASCRIPT_VERSION_NAME, "2022")
        );
    }

    /**
     * Returns the main player in this game.
     * @return the main player in this game.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the story of this game.
     * @return the story of this game.
     */
    public Story getStory() {
        return story;
    }

    /**
     * Returns the goals currently in this game.
     * @return the goals currently in this game.
     */
    public List<Goal> getGoals() {
        return goals;
    }

    /**
     * Starts this game.
     * @return the initial passage of this game.
     */
    public Passage begin() {
        return story.getOpeningPassage();
    }

    /**
     * Uses a link object to progress the story of this game to a new passage.
     * The link's associated actions are not performed. This means you can safely use this method as a way of
     * "looking ahead" in this story. To perform the actions associated with the link,
     * call Story::performLinkActions(Link link).
     * @param link The Link-object pointing to a new passage.
     * @return The new passage referenced by the submitted Link-object, or null if 1. the link does not point
     * to an existing passage or 2. if the link is closed due to its condition-script.
     */
    public Passage go(Link link) {
        return story.getPassage(link);
    }

    /**
     * Performs the actions associated with the submitted link object.
     * @param link The link object containing the actions to be performed.
     */
    public void performLinkActions(Link link) {
        for (Action a : link.getActions()) {
            a.execute(player);
            if (a instanceof InventoryAction ia)
                connectInventoryActionToScriptEngine(ia);
        }

        link.runScript(scriptEngine);
    }

    private void connectInventoryActionToScriptEngine(InventoryAction a) {
        if (scriptEngine.getContext().getAttribute(a.getItem()) == null)
            scriptEngine.put(a.getItem(), 0);

        String operator = "+";
        if (!a.addsItem())
            operator = "-";

        try {
            scriptEngine.eval(a.getItem() + operator + "=1");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Link> getOpenLinks(Passage passage) {
        List<Link> links = new ArrayList<>();
        for (Link l : passage.getLinks())
            if (l.isLinkOpen(scriptEngine))
                links.add(l);
        return links;
    }
}
