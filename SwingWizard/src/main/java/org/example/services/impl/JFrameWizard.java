package org.example.services.impl;

import org.example.services.WizardInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;


public class JFrameWizard extends JFrame implements WizardInterface {
    @Serial
    private static final long serialVersionUID = 2818290889333414291L;

    private static final Dimension defaultminimumSize = new Dimension(500, 500);

    private final JPanel wizardPageContainer = new JPanel(new GridLayout(1, 1));
    private final JButton cancelButton = new JButton("Abbrechen");
    private final JButton previousButton = new JButton("Vorheriger Schritt");
    private final JButton nextButton = new JButton("Nächster Schritt");
    private final JButton finishButton = new JButton("Fertigstellen");

    public JFrameWizard(String title) {
        super(title);
        setupWizard();
    }

    private void setupWizard() {
        setupComponents();
        layoutComponents();

        setMinimumSize(defaultminimumSize);

        // Center on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPosition = (screenSize.width / 2) - (defaultminimumSize.width / 2);
        int yPosition = (screenSize.height / 2) - (defaultminimumSize.height / 2);
        setLocation(xPosition, yPosition);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setupComponents() {
        cancelButton.addActionListener(e -> dispose());

        finishButton.addActionListener(e -> {
            //JOptionPane.showMessageDialog(getContentPane(), "Wizard finished!");
            dispose();
        });

        cancelButton.setMnemonic(KeyEvent.VK_C);
        previousButton.setMnemonic(KeyEvent.VK_P);
        nextButton.setMnemonic(KeyEvent.VK_N);
        finishButton.setMnemonic(KeyEvent.VK_F);

        wizardPageContainer.addContainerListener(new MinimumSizeAdjuster());
    }

    private void layoutComponents() {
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{1.0, 0.0, 0.0};
        layout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
        layout.rowHeights = new int[]{0, 0, 0};
        layout.columnWidths = new int[]{0, 0, 0, 0, 0};
        getContentPane().setLayout(layout);

        GridBagConstraints wizardPageContainerConstraint = new GridBagConstraints();
        wizardPageContainerConstraint.gridwidth = 5;
        wizardPageContainerConstraint.fill = GridBagConstraints.BOTH;
        wizardPageContainerConstraint.gridx = 0;
        wizardPageContainerConstraint.gridy = 0;
        wizardPageContainerConstraint.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(wizardPageContainer, wizardPageContainerConstraint);

        GridBagConstraints separatorConstraints = new GridBagConstraints();
        separatorConstraints.gridwidth = 5;
        separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraints.gridx = 0;
        separatorConstraints.gridy = 1;
        separatorConstraints.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(new JSeparator(), separatorConstraints);

        GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
        cancelButtonConstraints.gridx = 1;
        cancelButtonConstraints.gridy = 2;
        cancelButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(cancelButton, cancelButtonConstraints);

        GridBagConstraints previousButtonConstraints = new GridBagConstraints();
        previousButtonConstraints.gridx = 2;
        previousButtonConstraints.gridy = 2;
        previousButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(previousButton, previousButtonConstraints);

        GridBagConstraints nextButtonConstraints = new GridBagConstraints();
        nextButtonConstraints.gridx = 3;
        nextButtonConstraints.gridy = 2;
        nextButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(nextButton, nextButtonConstraints);

        GridBagConstraints finishButtonConstraints = new GridBagConstraints();
        finishButtonConstraints.gridx = 4;
        finishButtonConstraints.gridy = 2;
        finishButtonConstraints.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(finishButton, finishButtonConstraints);
    }

    @Override
    public JPanel getWizardPageContainer() {
        return wizardPageContainer;
    }

    @Override
    public AbstractButton getCancelButton() {
        return this.cancelButton;
    }

    @Override
    public AbstractButton getPreviousButton() {
        return this.previousButton;
    }

    @Override
    public AbstractButton getNextButton() {
        return this.nextButton;
    }

    @Override
    public AbstractButton getFinishButton() {
        return this.finishButton;
    }

    private class MinimumSizeAdjuster implements ContainerListener {

        @Override
        public void componentAdded(ContainerEvent e) {
            Dimension currentSize = getSize();
            Dimension preferredSize = getPreferredSize();

            Dimension newSize = new Dimension(currentSize);
            newSize.width = Math.max(currentSize.width, preferredSize.width);
            newSize.height = Math.max(currentSize.height, preferredSize.height);

            setMinimumSize(newSize);
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
        }

    }
}
