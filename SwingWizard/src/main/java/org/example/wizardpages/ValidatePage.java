package org.example.wizardpages;

import org.example.wizard.AbstractWizardPage;

import java.awt.*;

public class ValidatePage extends AbstractWizardPage {
    
    public ValidatePage() {
        setLayout(new FlowLayout());
        add(new Label("This is the page of the wizard for validating existing Bagits . Press Next to continue."));
    }

    @Override
    protected AbstractWizardPage getNextPage() {
        return null;
    }

    @Override
    protected boolean isCancelAllowed() {
        return true;
    }

    @Override
    protected boolean isPreviousAllowed() {
        return true;
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
