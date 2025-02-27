package org.example;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.example.wizard.AbstractWizardPage;
import org.example.wizard.JFrameWizard;
import org.example.wizard.WizardController;
import org.example.wizardpages.StartPage;

public class MyApp {

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        JFrameWizard wizard = new JFrameWizard("My Wizard");
        AbstractWizardPage StartPage = new StartPage();

        WizardController wizardController = new WizardController(wizard);
        wizard.setVisible(true);
        wizardController.startWizard(StartPage);
    }
}