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

import com.dph.gui.ConditionRemover;

@RunWith(GUITestRunner.class)
public class ConditionRemoverTest extends AssertJSwingJUnitTestCase {

	private static final String CONTENT_PANEL = "contentPanel";
	private static final String BUTTON_PANEL = "buttonPanel";
	private static final String COMMENT = "comment";
	private static final String CONDITION_NAME = "conditionNameLabel";
	private static final String CONDITION_CODE = "conditionCodeLabel";
	private static final String CANCEL_BUTTON = "cancelButton";
	private static final String OK_BUTTON = "OKButton";
	private DialogFixture window;
	private JButtonFixture okButton;
	private JButtonFixture cancelButton;
	private JLabelFixture conditionCodeLabel;
	private JLabelFixture conditionNameLabel;
	private JPanelFixture buttonPanel;
	private JPanelFixture contentPanel;
	private JLabelFixture comment;

	@Override
	protected void onSetUp() throws Exception {
		ConditionRemover dialog = GuiActionRunner.execute(() -> new ConditionRemover("testCode", "testName"));
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
				this.contentPanel=window.panel(CONTENT_PANEL);
				this.buttonPanel= window.panel(BUTTON_PANEL);
				this.conditionCodeLabel = contentPanel.label(CONDITION_CODE);
				this.conditionNameLabel = contentPanel.label(CONDITION_NAME);
				this.comment = contentPanel.label(COMMENT);
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
				window.target().dispose();
			}
		});
		this.contentPanel=null;
		this.comment=null;
		this.buttonPanel=null;
		this.okButton = null;
		this.cancelButton = null;
		this.conditionCodeLabel = null;
		this.conditionNameLabel = null;
	}

	@Test
	@GUITest
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		conditionCodeLabel.requireVisible().requireText("Condition Code: " + "testCode");
		conditionNameLabel.requireVisible().requireText("Condition Name: " + "testName");
		comment.requireVisible();
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
				return ((ConditionRemover) window.target()).getLastButtonPressed();
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
				return ((ConditionRemover) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("Cancel");

	}

}
