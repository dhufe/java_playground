package org.example.wizard;

import javax.swing.AbstractButton;
import java.util.EmptyStackException;
import java.util.Stack;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WizardController {
    private final WizardInterface wizard;
    private final Stack<AbstractWizardPage> pageHistory = new Stack<>();
    private AbstractWizardPage currentPage = null;

    public WizardController(WizardInterface wizard) {
        if (wizard == null) {
            throw new IllegalArgumentException("wizard can't be null");
        }
        this.wizard = wizard;
        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        wizard.getNextButton().addActionListener(new NextPageListener());
        wizard.getPreviousButton().addActionListener(new PreviousPageListener());
    }

    public void showNextPage(AbstractWizardPage nextPage) {
        if (nextPage == null) {
            // Next page is null. Updating buttons and ignoring request.
            updateButtons();
            return;
        }
        if (currentPage != null) {
            pageHistory.push(currentPage);
        }
        setPage(nextPage);
    }

    public void showPreviousPage() {
        AbstractWizardPage previousPage;
        try {
            previousPage = pageHistory.pop();
        } catch (EmptyStackException e) {
            // Previous page is null. Updating buttons and ignoring request.
            updateButtons();
            return;
        }
        setPage(previousPage);
    }

    private void setPage(AbstractWizardPage newPage) {
        Container wizardPageContainer = this.wizard.getWizardPageContainer();
        if (currentPage != null) {
            wizardPageContainer.remove(currentPage);
        }
        currentPage = newPage;
        currentPage.setWizardController(this);
        wizardPageContainer.add(currentPage);
        wizardPageContainer.validate();
        wizardPageContainer.repaint();
        updateButtons();
    }

    public void startWizard(AbstractWizardPage startPage) {
        if (startPage == null) {
            throw new IllegalArgumentException("startPage can't be null");
        }
        if (currentPage != null) {
            wizard.getWizardPageContainer().remove(currentPage);
            pageHistory.clear();
            currentPage = null;
        }
        showNextPage(startPage);
    }

    public void updateButtons() {
        AbstractButton cancelButton = wizard.getCancelButton();
        if (cancelButton != null) {
            cancelButton.setEnabled(currentPage.isCancelAllowed());
        }
        AbstractButton previousButton = wizard.getPreviousButton();
        if (previousButton != null) {
            previousButton.setEnabled(currentPage.isPreviousAllowed() && !pageHistory.isEmpty());
        }
        AbstractButton nextButton = wizard.getNextButton();
        if (nextButton != null) {
            nextButton.setEnabled(currentPage.isNextAllowed() && (currentPage.getNextPage() != null));
        }
        AbstractButton finishButton = wizard.getFinishButton();
        if (finishButton != null) {
            finishButton.setEnabled(currentPage.isFinishAllowed());
        }
    }

    private class NextPageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showNextPage(currentPage.getNextPage());
        }
    }

    private class PreviousPageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showPreviousPage();
        }
    }

}
