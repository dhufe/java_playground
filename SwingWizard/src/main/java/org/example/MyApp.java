package org.example;

import org.example.wizard.AbstractWizardPage;
import org.example.wizard.JFrameWizard;
import org.example.wizard.WizardController;
import org.example.wizardpages.StartPage;


public class MyApp {

    public static void main(String[] args) {
        JFrameWizard wizard = new JFrameWizard("My Wizard");
        AbstractWizardPage StartPage = new StartPage();

        WizardController wizardController = new WizardController(wizard);
        wizard.setVisible(true);
        wizardController.startWizard(StartPage);
    }
}