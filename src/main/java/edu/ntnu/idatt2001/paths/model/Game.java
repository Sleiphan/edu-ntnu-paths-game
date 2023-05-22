package edu.ntnu.idatt2001.paths.model;

import com.oracle.truffle.js.runtime.JSContextOptions;
import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import edu.ntnu.idatt2001.paths.model.action.*;
import edu.ntnu.idatt2001.paths.model.goal.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;

import javax.script.ScriptEngine;
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

        initialiseScriptingEngine(player);
    }

    private void initialiseScriptingEngine(Player player) {
        this.scriptEngine = GraalJSScriptEngine.create(
                Engine.newBuilder()
                        .option("engine.WarnInterpreterOnly", "false")
                        .build(),
                Context.newBuilder("js")
                        .allowIO(false)
                        .option(JSContextOptions.ECMASCRIPT_VERSION_NAME, "2022")
        );

        this.scriptEngine.put(HealthAction.SCRIPT_VARIABLE_KEY, player.getHealth());
        this.scriptEngine.put(GoldAction.SCRIPT_VARIABLE_KEY, player.getGold());
        this.scriptEngine.put(ScoreAction.SCRIPT_VARIABLE_KEY, player.getScore());
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
            a.execute(scriptEngine);
        }

        link.runScript(scriptEngine);
    }

    public List<Link> getOpenLinks(Passage passage) {
        List<Link> links = new ArrayList<>();
        for (Link l : passage.getLinks())
            if (l.isLinkOpen(scriptEngine))
                links.add(l);
        return links;
    }

    /**
     * Checks if the game is lost. The game is lost if the health of the player falls to 0 or below
     * @return true if game over, false if game not over
     */
    public boolean checkGameOver(){
        return player.getHealth() <= 0;
    }

    /**
     * Checks if the game has been won. Check is if all goals of the different goal types have been achieved.
     * Sets boolean values for each goal type achieved. Also checks if there are instances of all goals in the
     * list of goals. If there is a goal type with no instances it updates the instance of boolean. This allows
     * a game to be completed even if there aren't goals of all the types present in the list
     * @return true if game won, false if game not won
     */
    public boolean checkGameWon(){
        boolean healthGoalAchieved = false;
        boolean goldGoalAchieved = false;
        boolean scoreGoalAchieved = false;
        boolean inventoryGoalAchieved = false;
        boolean instanceOfHealthGoal = false;
        boolean instanceOfGoldGoal = false;
        boolean instanceOfScoreGoal = false;
        boolean instanceOfInventoryGoal = false;

        for (Goal g : goals){
                //Checks if goal is a health goal, and then if it has been achieved
            if (g.getClass() == HealthGoal.class){
                instanceOfHealthGoal = true;
                healthGoalAchieved = (Integer) ((HealthGoal) g).getValue() <= player.getHealth();

                //Checks if goal is a gold goal, and then if it has been achieved
            } else if (g.getClass() == GoldGoal.class){
                instanceOfGoldGoal = true;
                goldGoalAchieved = (Integer) ((GoldGoal) g).getValue() <= player.getGold();

                //Checks if goal is a score goal, and then if it has been achieved
            } else if (g.getClass() == ScoreGoal.class){
                instanceOfScoreGoal = true;
                scoreGoalAchieved = (Integer) ((ScoreGoal) g).getValue() <= player.getScore();

                //Checks if goal is an inventory goal, and then if it has been achieved
            } else if (g.getClass() == InventoryGoal.class){
                instanceOfInventoryGoal = true;
                List<String> inv = List.of(player.getInventory().toArray(new String[0]));
                inventoryGoalAchieved = true;
                for (int i = 0; i < ((InventoryGoal) g).getItems().size(); i++){
                    if (!inv.contains(((InventoryGoal) g).getItems().get(i))){
                        inventoryGoalAchieved = false;
                        break;
                    }
                }
            }
        }

        if (!instanceOfHealthGoal)
            healthGoalAchieved = true;

        if (!instanceOfGoldGoal)
            goldGoalAchieved = true;

        if (!instanceOfScoreGoal)
            scoreGoalAchieved = true;

        if (!instanceOfInventoryGoal)
            inventoryGoalAchieved = true;

        return healthGoalAchieved && goldGoalAchieved && scoreGoalAchieved && inventoryGoalAchieved;
    }
}
