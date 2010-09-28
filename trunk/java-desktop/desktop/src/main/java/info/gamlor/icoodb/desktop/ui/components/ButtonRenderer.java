package info.gamlor.icoodb.desktop.ui.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @version 1.0 11/09/98
 */
public class ButtonRenderer implements TableCellRenderer {
    private final String buttonLabel;
    private final ImageIcon icon;

    public ButtonRenderer(String buttonLabel, ImageIcon icon) {
        this.buttonLabel = buttonLabel;
        this.icon = icon;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return createButton();
    }

    public JButton createButton() {
        return new JButton(buttonLabel, icon);
    }


    public static ButtonRenderer create(String title, ImageIcon imageIcon) {
        return new ButtonRenderer(title, imageIcon);
    }
}
