package info.gamlor.icoodb.desktop.ui.views;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author roman.stoffel@gamlor.info
 * @since 02.08.2010
 */
public class TabFocusPolicy {
    /**
     * Patch the behaviour of a component.
     * TAB transfers focus to the next focusable component,
     * SHIFT+TAB transfers focus to the previous focusable component.
     *
     * @param c The component to be patched.
     */
    public static void patch(Component c) {
        Set<KeyStroke> strokes = new HashSet<KeyStroke>(asList(KeyStroke.getKeyStroke("pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, strokes);
        strokes = new HashSet<KeyStroke>(asList(KeyStroke.getKeyStroke("shift pressed TAB")));
        c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, strokes);
    }
}
