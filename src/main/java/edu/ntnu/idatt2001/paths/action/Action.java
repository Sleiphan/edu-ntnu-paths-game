package edu.ntnu.idatt2001.paths.action;

import edu.ntnu.idatt2001.paths.Player;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

public interface Action {
    boolean execute(Player player);

    String toPathsFormat();

    String parseFunctionName = "fromPathsFormat";
    Set<Class<? extends Action>> actionClasses = new Reflections(Action.class.getPackageName(), new SubTypesScanner(false))
            .getSubTypesOf(Action.class);

    static Action fromPathsFormat(String pathsString) {
        final String FIELD_DELIMITER = ":";
        final String parseFunctionName = "fromPathsFormat";

        String typeName = pathsString
                .substring(1, pathsString.length() - 1)
                .split(FIELD_DELIMITER)[0];
        typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1); // Capitalize first letter.

        Class<?> actionClass = null;
        for (Class<?> c : actionClasses)
            if (typeName.equals(c.getSimpleName()))
                actionClass = c;
        if (actionClass == null)
            throw new IllegalArgumentException("No action with the specified name exists: " + typeName);

        try {
            Method parseFunction = actionClass.getMethod(parseFunctionName, String.class);
            return (Action) parseFunction.invoke(null, pathsString);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
