package org.example.wizard;

import java.awt.LayoutManager;
import javax.swing.JPanel;

public abstract class AbstractWizardPage extends JPanel {
    private static final long serialVersionUID = 1000143453163604518L;

    private WizardController wizardController;

    public AbstractWizardPage(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public AbstractWizardPage(LayoutManager layout) {
        super(layout);
    }

    public AbstractWizardPage(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public AbstractWizardPage() {
        super();
    }

    public void updateWizardButtons() {
        wizardController.updateButtons();
    }

    void setWizardController(WizardController wizardController) {
        this.wizardController = wizardController;
    }

    protected abstract AbstractWizardPage getNextPage();

    protected abstract boolean isCancelAllowed();

    protected abstract boolean isPreviousAllowed();

    protected abstract boolean isNextAllowed();

    protected abstract boolean isFinishAllowed();
}
