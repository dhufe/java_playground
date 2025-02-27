package org.example.wizardpages;

import org.example.wizard.AbstractWizardPage;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class FinalPage extends AbstractWizardPage {

    private final TextField finishTextField = new TextField("no", 5);

    public FinalPage() {
        setLayout(new FlowLayout());

        add(new Label("Do you want to finish? Type in your answer:"));
        add(finishTextField);

        // Add a listener for updating the wizard buttons
        finishTextField.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
                updateWizardButtons();
            }
        });
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
        return false;
    }

    @Override
    protected boolean isFinishAllowed() {
        return finishTextField.getText().trim().equalsIgnoreCase("yes");
    }
}
