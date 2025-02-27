package org.example.wizardpages;

import org.example.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.*;

public class ChooseActionPage extends AbstractWizardPage {

    private final AbstractWizardPage creationPage = new CreationPage();
    private final AbstractWizardPage validationPage = new ValidatePage();

    private final JRadioButton CreateBagit;
    private final JRadioButton ValidateBagit;

    public ChooseActionPage() {
        setLayout(new FlowLayout());
        add(new Label("This is the second page of the wizard. Press Next to continue."));

        ButtonGroup group = new ButtonGroup();
        this.CreateBagit = new JRadioButton("Create Bagit");
        this.CreateBagit.setSelected(true);
        this.ValidateBagit = new JRadioButton("Validate Bagit");

        group.add(CreateBagit);
        group.add(ValidateBagit);
        add(CreateBagit);
        add(ValidateBagit);

    }

    @Override
    protected AbstractWizardPage getNextPage() {
        if (CreateBagit.isSelected()) {
            return this.creationPage;
        } else if (ValidateBagit.isSelected()) {
            return this.validationPage;
        }
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
