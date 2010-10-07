/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainPanel.java
 *
 * Created on 20.07.2010, 22:31:26
 */

package info.gamlor.icoodb.desktop.ui.views;

import com.google.inject.Inject;
import info.gamlor.icoodb.desktop.ui.viewmodel.MainViewModel;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;

import javax.swing.*;

public class MainPanel extends javax.swing.JFrame {
    private MainViewModel model;
    private JPanel lastPanel;

    @Inject
    public MainPanel(MainViewModel model) {
        initComponents();
        setModel(model);

        AutoBinding panelBinding = Bindings.createAutoBinding(
                AutoBinding.UpdateStrategy.READ,
                getModel(), BeanProperty.create("viewPanel"),
                this, BeanProperty.create("viewPanel"));
        panelBinding.bind();

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbar = new javax.swing.JToolBar();
        listDinnersButton = new javax.swing.JButton();
        createDinnerButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        listDinnersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/info/gamlor/icoodb/desktop/ui/views/find-dinner.png"))); // NOI18N
        listDinnersButton.setText("Find Dinner");
        listDinnersButton.setFocusable(false);
        listDinnersButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        listDinnersButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        listDinnersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listDinnersButtonActionPerformed(evt);
            }
        });
        toolbar.add(listDinnersButton);

        createDinnerButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/info/gamlor/icoodb/desktop/ui/views/dinner.png"))); // NOI18N
        createDinnerButton.setText("Create Dinner");
        createDinnerButton.setFocusable(false);
        createDinnerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        createDinnerButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        createDinnerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDinnerButtonActionPerformed(evt);
            }
        });
        toolbar.add(createDinnerButton);

        contentPanel.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(contentPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addComponent(toolbar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                        .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createDinnerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDinnerButtonActionPerformed
        getModel().executeCreateDinner();
    }//GEN-LAST:event_createDinnerButtonActionPerformed

    private void listDinnersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listDinnersButtonActionPerformed
        getModel().listDinners();
    }//GEN-LAST:event_listDinnersButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton createDinnerButton;
    private javax.swing.JButton listDinnersButton;
    private javax.swing.JToolBar toolbar;
    // End of variables declaration//GEN-END:variables


    public void setViewPanel(final JPanel panel) {
        if (null == panel) {
            return;
        }
        removeExistingPanel();
        contentPanel.add(panel);
        contentPanel.validate();
        this.lastPanel = panel;

    }

    private void removeExistingPanel() {
        if (null != lastPanel) {
            contentPanel.remove(lastPanel);
        }
    }

    public JPanel getViewPanel() {
        return this.contentPanel;
    }

    public MainViewModel getModel() {
        return model;
    }

    public void setModel(MainViewModel model) {
        this.model = model;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.model.dispose();
    }
}