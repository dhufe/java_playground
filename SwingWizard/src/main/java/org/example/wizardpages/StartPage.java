package org.example.wizardpages;

import org.example.wizard.AbstractWizardPage;
import java.awt.FlowLayout;
import java.awt.Label;

public class StartPage extends AbstractWizardPage {

    private final AbstractWizardPage nextPage = new FinalPage();

    public StartPage() {
        setLayout(new FlowLayout());
        add(new Label("This is the first page of the wizard. Press Next to continue."));
    }

    @Override
    protected AbstractWizardPage getNextPage() {
        return this.nextPage;
    }

    @Override
    protected boolean isCancelAllowed() {
        return true;
    }

    @Override
    protected boolean isPreviousAllowed() {
        return false;
    }

    @Override
    protected boolean isNextAllowed() {
        return true;
    }

    @Override
    protected boolean isFinishAllowed() {
        return false;
    }
}
