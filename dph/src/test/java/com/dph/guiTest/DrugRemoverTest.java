package com.dph.guiTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.exception.ComponentLookupException;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dph.gui.DrugRemover;

@RunWith(GUITestRunner.class)
public class DrugRemoverTest extends AssertJSwingJUnitTestCase {

	private static final String CONTENT_PANEL = "contentPanel";
	private static final String BUTTON_PANEL = "buttonPanel";
	private static final String CANCEL_BUTTON = "cancelButton";
	private static final String OK_BUTTON = "OKButton";
	private static final String DRUG_DOSAGE_LABEL = "drugDosageLabel";
	private static final String DRUG_CODE_LABEL = "drugCodeLabel";
	private static final String DRUG_NAME_LABEL = "drugNameLabel";
	private DialogFixture window;
	private JLabelFixture drugNameLabel;
	private JLabelFixture drugCodeLabel;
	private JLabelFixture drugDosageLabel;
	private JButtonFixture okButton;
	private JButtonFixture cancelButton;
	private JPanelFixture contentPanel;
	private JPanelFixture buttonPanel;

	@Override
	protected void onSetUp() throws Exception {
		DrugRemover dialog = GuiActionRunner.execute(() -> new DrugRemover("testCode", "testName", 1.0));
		window = new DialogFixture(robot(), dialog);
		robot().settings().eventPostingDelay(500);
		robot().settings().delayBetweenEvents(60);
		robot().showWindow(dialog);
		Awaitility.given().ignoreExceptions().await().atMost(60, TimeUnit.SECONDS).until(() -> setupVariables());
	}

	private boolean setupVariables() {
		boolean condition = false;
		while (!condition) {
			try {
				this.contentPanel = window.panel(CONTENT_PANEL);
				this.buttonPanel = window.panel(BUTTON_PANEL);
				this.drugNameLabel = contentPanel.label(DRUG_NAME_LABEL);
				this.drugCodeLabel = contentPanel.label(DRUG_CODE_LABEL);
				this.drugDosageLabel = contentPanel.label(DRUG_DOSAGE_LABEL);
				this.okButton = buttonPanel.button(OK_BUTTON);
				this.cancelButton = buttonPanel.button(CANCEL_BUTTON);
				condition = true;
			} catch (ComponentLookupException e) {
				condition = false;
			}
		}
		return condition;
	}

	@Override
	protected void onTearDown() {
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				window.cleanUp();
			}
		});
		this.contentPanel = null;
		this.buttonPanel = null;
		this.drugCodeLabel = null;
		this.drugCodeLabel = null;
		this.drugDosageLabel = null;
		this.okButton = null;
		this.cancelButton = null;
	}

	@Test
	@GUITest
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		drugCodeLabel.requireVisible().requireText("Drug Code: " + "testCode");
		drugNameLabel.requireVisible().requireText("Drug Name: " + "testName");
		drugDosageLabel.requireVisible().requireText("Drug Dosage: " + 1.0);
		okButton.requireVisible().requireEnabled().requireText("OK");
		cancelButton.requireVisible().requireEnabled().requireText("Cancel");
	}

	@Test
	@GUITest
	public void closeDialogOnClickOnOkButtonTest() {
		okButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugRemover) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("OK");
	}

	@Test
	@GUITest
	public void closeDialogOnClickOnCancelButtonTest() {
		cancelButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugRemover) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("Cancel");
	}

}
