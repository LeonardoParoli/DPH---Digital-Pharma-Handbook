package com.dph.guiTest;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.dph.gui.ConditionAdder;
import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;

@RunWith(GUITestRunner.class)
public class ConditionAdderTest extends AssertJSwingJUnitTestCase {

	private static final String LABEL_NAME = "labelName";
	private static final String LABEL_CODE = "labelCode";
	private static final String COMMENT = "comment";
	private static final String NAME_CHECKER = "nameChecker";
	private static final String CODE_CHECKER = "codeChecker";
	private static final String CONDITION_NAME_TEXT = "conditionNameText";
	private static final String CONDITION_CODE_TEXT = "conditionCodeText";
	private static final String CANCEL_BUTTON = "cancelButton";
	private static final String OK_BUTTON = "OKButton";
	private DialogFixture window;
	private JButtonFixture cancelButton;
	private JButtonFixture okButton;
	private JLabelFixture labelName;
	private JLabelFixture labelCode;
	private JLabelFixture comment;
	private JLabelFixture codeChecker;
	private JTextComponentFixture conditionNameText;
	private JTextComponentFixture conditionCodeText;
	private JLabelFixture nameChecker;
	private DefaultComboBoxModel<Condition> model;

	@Override
	protected void onSetUp() throws Exception {
		model = new DefaultComboBoxModel<>();
		ConditionAdder dialog = GuiActionRunner.execute(() -> new ConditionAdder(model));
		window = new DialogFixture(robot(),dialog);
		robot().settings().eventPostingDelay(500);
		robot().settings().delayBetweenEvents(60);
		robot().showWindow(dialog);
		Awaitility.given().ignoreExceptions().await().atMost(10, TimeUnit.SECONDS).until(() -> setupVariables());
	}
	
	private boolean setupVariables() {
		labelName = window.label(LABEL_NAME);
		labelCode = window.label(LABEL_CODE);
		comment = window.label(COMMENT);
		nameChecker = window.label(NAME_CHECKER);
		codeChecker = window.label(CODE_CHECKER);
		conditionNameText = window.textBox(CONDITION_NAME_TEXT);
		conditionCodeText = window.textBox(CONDITION_CODE_TEXT);
		okButton = window.button(OK_BUTTON);
		cancelButton = window.button(CANCEL_BUTTON);
		return true;
	}

	@Override
	protected void onTearDown() {
		window=null;
		labelName=null;
		labelCode=null;
		comment=null;
		codeChecker = null;
		conditionNameText =null;
		conditionCodeText=null;
		okButton=null;
		cancelButton=null;
		model=null;
	}
	
	@Test @GUITest
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		labelCode.requireVisible().requireText("Condition Code:");
		labelName.requireVisible().requireText("Condition Name:");
		comment.requireVisible().requireText("Insert new condition code and name:");
		nameChecker.requireVisible().requireText("---");
		codeChecker.requireVisible().requireText("---");
		okButton.requireVisible().requireText("OK").requireEnabled();
		cancelButton.requireVisible().requireText("Cancel").requireEnabled();
		conditionNameText.requireVisible().requireText("");
		conditionCodeText.requireVisible().requireText("");
	}
	
	@Test @GUITest
	public void succesfullCloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("OK");
	}
	
	@Test @GUITest
	public void failByDuplicateCodeCloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		model.addElement(new Condition(testCode,testName, new ArrayList<Dosage>()));
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}
	
	@Test @GUITest
	public void failByWrongEmptyCodeloseDialogOnClickOnOkButtonTest() {
		String testCode = "";
		String testName = "testName";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}
	
	@Test @GUITest
	public void failByWrongBlankCodeloseDialogOnClickOnOkButtonTest() {
		String testCode = "   ";
		String testName = "testName";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}
	
	@Test @GUITest
	public void failByWrongEmptyNameloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}
	
	@Test @GUITest
	public void failByWrongBlankNameloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "   ";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		okButton.click();
		window.requireVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}
	
	@Test @GUITest
	public void closeDialogOnClickOnCancelButtonTest() {
		cancelButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("Cancel");
		
	}
	
	@Test @GUITest
	public void getNewEntryInfosTest() {
		String testCode = "testCode";
		String testName = "testName";
		conditionCodeText.enterText(testCode);
		conditionNameText.enterText(testName);
		conditionCodeText.requireText(testCode);
		conditionNameText.requireText(testName);
		List<String> infos = GuiActionRunner.execute(new GuiQuery<List<String>>() {
			@Override
			protected List<String> executeInEDT() throws Throwable {
				return ((ConditionAdder) window.target()).getNewEntryInfo();
			}
		});
		assertThat(infos).containsExactly(testCode, testName);
	}

}
