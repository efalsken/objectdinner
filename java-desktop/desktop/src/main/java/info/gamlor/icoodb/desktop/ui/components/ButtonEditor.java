package info.gamlor.icoodb.desktop.ui.components;

import info.gamlor.icoodb.desktop.utils.OneArgAction;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ButtonEditor<T> extends DefaultCellEditor {
    private final JButton button;
    private final OneArgAction<T> actionPerformed;
    private boolean isPushed;
    private T data;


    public static <T> TableCellEditor create(JButton button,
                                             OneArgAction<T> oneArgAction) {
        return new ButtonEditor<T>(button, oneArgAction);
    }

    public ButtonEditor(JButton theButton, OneArgAction<T> actionPerformed) {
        super(new JCheckBox());
        this.actionPerformed = actionPerformed;
        button = theButton;
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }


    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.data = (T) value;
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            pressButton(data);
        }
        isPushed = false;
        return data;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    private void pressButton(T data) {
        actionPerformed.invoke(data);
    }
}
