package edu.ntnu.idatt2001.paths.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

public class ActionTest {

    @Test
    public void all_action_classes_contain_static_method_fromPathsFormat() throws NoSuchMethodException {
        Set<Class<? extends Action>> actionClasses = new Reflections(Action.class.getPackageName(), new SubTypesScanner(false))
                .getSubTypesOf(Action.class);

        for (Class<? extends Action> c : actionClasses)
            Assertions.assertDoesNotThrow(() -> c.getMethod(Action.parseFunctionName, String.class));
    }

    @Test
    public void fromPathsFormat_casting_to_correct_type() {
        String testString = "{goldAction:10}";
        Action a = Action.fromPathsFormat(testString);
        assert(a instanceof GoldAction);

        testString = "{healthAction:8}";
        a = Action.fromPathsFormat(testString);
        assert(a instanceof HealthAction);

        testString = "{inventoryAction:test}";
        a = Action.fromPathsFormat(testString);
        assert(a instanceof InventoryAction);

        testString = "{scoreAction:10}";
        a = Action.fromPathsFormat(testString);
        assert(a instanceof ScoreAction);
    }
}
