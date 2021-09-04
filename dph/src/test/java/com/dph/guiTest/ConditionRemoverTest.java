package com.dph.guiTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dph.gui.ConditionRemover;

@RunWith(GUITestRunner.class)
public class ConditionRemoverTest extends AssertJSwingJUnitTestCase {
	
	private static final String CONDITION_NAME = "conditionNameLabel";
	private static final String CONDITION_CODE = "conditionCodeLabel";
	private static final String CANCEL_BUTTON = "cancelButton";
	private static final String OK_BUTTON = "OKButton";
	private DialogFixture window;
	private JButtonFixture okButton;
	private JButtonFixture cancelButton;
	private JLabelFixture conditionCodeLabel;
	private JLabelFixture conditionNameLabel;
	
	@Override
	protected void onSetUp() throws Exception {
		ConditionRemover dialog = GuiActionRunner.execute(() -> new ConditionRemover("testCode","testName"));
		window = new DialogFixture(robot(),dialog);
		window.show();
		conditionCodeLabel = window.label(CONDITION_CODE);
		conditionNameLabel = window.label(CONDITION_NAME);
		okButton = window.button(OK_BUTTON);
		cancelButton = window.button(CANCEL_BUTTON);
	}
	
	@Override
	protected void onTearDown() {
		this.okButton=null;
		this.cancelButton=null;
		this.conditionCodeLabel=null;
		this.conditionNameLabel=null;
	}
	
	@Test
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		conditionCodeLabel.requireVisible().requireText("Condition Code: " + "testCode");	
		conditionNameLabel.requireVisible().requireText("Condition Name: " + "testName");
		okButton.requireVisible().requireEnabled().requireText("OK");
		cancelButton.requireVisible().requireEnabled().requireText("Cancel");
	}
	
	@Test
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