package org.example.wizard;

import java.awt.Container;
import javax.swing.AbstractButton;

public interface WizardInterface {

     Container getWizardPageContainer();

     AbstractButton getCancelButton();

     AbstractButton getPreviousButton();

     AbstractButton getNextButton();

     AbstractButton getFinishButton();
}
