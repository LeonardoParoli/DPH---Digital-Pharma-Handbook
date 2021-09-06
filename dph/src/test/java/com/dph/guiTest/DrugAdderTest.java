package com.dph.guiTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
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
import org.assertj.swing.fixture.JScrollPaneFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.dph.gui.DrugAdder;
import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

@RunWith(GUITestRunner.class)
public class DrugAdderTest extends AssertJSwingJUnitTestCase {

	private static final String CONTENT_PANEL = "contentPanel";
	private static final String SCROLL_PANEL = "scrollPanel";
	private static final String BUTTON_PANEL = "buttonPanel";
	private static final String FIND_BUTTON = "findButton";
	private static final String DRUG_DESC_LABEL = "drugDescLabel";
	private static final String DRUG_DESC_TEXT = "drugDescText";
	private static final String DESC_CHECKER = "descChecker";
	private static final String NAME_CHECKER = "nameChecker";
	private static final String DOSAGE_CHECKER = "dosageChecker";
	private static final String CODE_CHECKER = "codeChecker";
	private static final String DRUG_DOSAGE_LABEL = "drugDosageLabel";
	private static final String DRUG_NAME_LABEL = "drugNameLabel";
	private static final String DRUG_CODE_LABEL = "drugCodeLabel";
	private static final String DRUG_NAME_TEXT = "drugNameText";
	private static final String DRUG_DOSAGE_TEXT = "drugDosageText";
	private static final String DRUG_CODE_TEXT = "drugCodeText";
	private static final String CANCEL_BUTTON = "cancelButton";
	private static final String OK_BUTTON = "OKButton";
	private DialogFixture window;
	private JButtonFixture okButton;
	private JButtonFixture cancelButton;
	private JTextComponentFixture drugCodeText;
	private JTextComponentFixture drugDosageText;
	private JTextComponentFixture drugNameText;
	private JLabelFixture drugCodeLabel;
	private JLabelFixture drugNameLabel;
	private JLabelFixture codeChecker;
	private JLabelFixture drugDosageLabel;
	private JLabelFixture dosageChecker;
	private List<Drug> model;
	private JLabelFixture nameChecker;
	private JLabelFixture drugDescLabel;
	private JTextComponentFixture drugDescText;
	private JLabelFixture descChecker;
	private JButtonFixture findButton;
	private Condition condition;
	private JPanelFixture contentPanel;
	private JScrollPaneFixture scrollPanel;
	private JPanelFixture buttonPanel;
	
	@Override
	protected void onSetUp() throws Exception {
		model = new ArrayList<>();
		condition = new Condition("conditionCode", "conditionName", new ArrayList<Dosage>());
		DrugAdder dialog = GuiActionRunner.execute(() -> new DrugAdder(model, condition));
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
				this.scrollPanel = contentPanel.scrollPane(SCROLL_PANEL);
				this.drugCodeText = contentPanel.textBox(DRUG_CODE_TEXT);
				this.drugDosageText = contentPanel.textBox(DRUG_DOSAGE_TEXT);
				this.drugNameText = contentPanel.textBox(DRUG_NAME_TEXT);
				this.drugCodeLabel = contentPanel.label(DRUG_CODE_LABEL);
				this.drugNameLabel = contentPanel.label(DRUG_NAME_LABEL);
				this.drugDosageLabel = contentPanel.label(DRUG_DOSAGE_LABEL);
				this.codeChecker = contentPanel.label(CODE_CHECKER);
				this.dosageChecker = contentPanel.label(DOSAGE_CHECKER);
				this.nameChecker = contentPanel.label(NAME_CHECKER);
				this.buttonPanel = window.panel(BUTTON_PANEL);
				this.okButton = buttonPanel.button(OK_BUTTON);
				this.cancelButton = buttonPanel.button(CANCEL_BUTTON);
				this.drugDescLabel = contentPanel.label(DRUG_DESC_LABEL);
				this.drugDescText = contentPanel.textBox(DRUG_DESC_TEXT);
				this.descChecker = contentPanel.label(DESC_CHECKER);
				this.findButton = contentPanel.button(FIND_BUTTON);
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
		contentPanel = null;
		buttonPanel = null;
		scrollPanel = null;
		drugCodeText = null;
		drugDosageText = null;
		drugNameText = null;
		drugCodeLabel = null;
		drugNameLabel = null;
		drugDosageLabel = null;
		codeChecker = null;
		dosageChecker = null;
		nameChecker = null;
		okButton = null;
		cancelButton = null;
		model = null;
		drugDescLabel = null;
		drugDescText = null;
		descChecker = null;
		findButton = null;
	}

	@Test
	@GUITest
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		drugCodeText.requireVisible().requireText("").requireEditable().requireEnabled();
		drugDosageText.requireVisible().requireText("").requireEditable().requireEnabled();
		drugNameText.requireVisible().requireText("").requireDisabled().requireEditable();
		drugCodeLabel.requireVisible().requireText("Drug Code:").requireEnabled();
		drugNameLabel.requireVisible().requireText("Drug Name:").requireEnabled();
		drugDosageLabel.requireVisible().requireText("Drug Dosage:").requireEnabled();
		scrollPanel.requireVisible();
		codeChecker.requireVisible().requireText("---");
		nameChecker.requireVisible().requireText("---");
		dosageChecker.requireVisible().requireText("---");
		okButton.requireVisible().requireDisabled().requireText("OK");
		cancelButton.requireVisible().requireEnabled().requireText("Cancel");
		drugDescLabel.requireVisible().requireText("Drug Description:").requireEnabled();
		drugDescText.requireVisible().requireText("").requireDisabled();
		descChecker.requireVisible().requireText("---");
		findButton.requireVisible().requireText("Find");
	}

	@Test
	@GUITest
	public void successfulFindButtonClickWithNewCodeTest() {
		String testCode = "testCode";
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireVisible().requireDisabled();
		drugNameText.requireVisible().requireEnabled();
		drugDescText.requireVisible().requireEnabled();
		drugDosageText.requireVisible().requireEnabled();
		codeChecker.requireText("NEW");
		okButton.requireVisible().requireEnabled();
	}

	@Test
	@GUITest
	public void successfulFindButtonClickWithStoredCodeTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDesc = "testDesc";
		model.add(new Drug(testCode, testName, testDesc));
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireVisible().requireDisabled();
		drugNameText.requireVisible().requireDisabled();
		drugDescText.requireVisible().requireDisabled();
		drugDosageText.requireVisible().requireEnabled().requireEditable();
		codeChecker.requireText("OKAY");
		okButton.requireVisible().requireEnabled();
	}

	@Test
	@GUITest
	public void failFindButtonClickByInvalidEmptyCodeTest() {
		String testCode = "";
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireVisible().requireEnabled();
		drugNameText.requireVisible().requireDisabled();
		drugDescText.requireVisible().requireDisabled();
		codeChecker.requireText("ERR");
		okButton.requireVisible().requireDisabled();
	}

	@Test
	@GUITest
	public void failFindButtonClickByInvalidBlankCodeTest() {
		String testCode = "   ";
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireVisible().requireEnabled();
		drugNameText.requireVisible().requireDisabled();
		drugDescText.requireVisible().requireDisabled();
		codeChecker.requireText("ERR");
		okButton.requireVisible().requireDisabled();
	}

	@Test
	@GUITest
	public void failFindButtonClickByInvalidDuplicatedCodeWithinConditionModelTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDesc = "testDesc";
		condition.addDrug(new Drug(testCode, testName, testDesc), 2.0);
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireVisible().requireEnabled();
		drugNameText.requireVisible().requireDisabled();
		drugDescText.requireVisible().requireDisabled();
		codeChecker.requireText("ERR");
		okButton.requireVisible().requireDisabled();
	}

	@Test
	@GUITest
	public void successfulOperationAddNewDrugCloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "2.0";
		String testDesc = "testDesc";
		drugCodeText.enterText(testCode);
		findButton.click();
		findButton.requireDisabled();
		drugCodeText.requireVisible().requireDisabled();
		drugNameText.requireVisible().requireEnabled();
		drugDescText.requireVisible().requireEnabled();
		drugDosageText.requireVisible().requireEnabled();
		drugNameText.enterText(testName);
		drugDosageText.enterText(testDosage);
		drugCodeText.requireText(testCode);
		drugNameText.requireText(testName);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("OK");
	}

	@Test
	@GUITest
	public void successfulOperationWithCopiedDrugCloseDialogOnClickOnOkButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "2.0";
		String testDesc = "testDesc";
		model.add(new Drug(testCode, testName, testDesc));
		drugCodeText.enterText(testCode);
		findButton.click();
		findButton.requireDisabled();
		drugCodeText.requireVisible().requireDisabled().requireText(testCode);
		drugNameText.requireVisible().requireDisabled().requireText(testName);
		drugDescText.requireVisible().requireDisabled().requireText(testDesc);
		drugDosageText.requireVisible().requireEnabled();
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		okButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("OK");
	}

	@Test
	@GUITest
	public void failByWrongEmptyNameCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "";
		String testDosage = "2.0";
		String testDesc = "testDesc";
		findButton.click();
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		nameChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void failByWrongBlankNameCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "    ";
		String testDosage = "2.0";
		String testDesc = "testDesc";
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		nameChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void failByWrongZeroDosageCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "0.0";
		String testDesc = "testDesc";
		drugCodeText.enterText(testCode);
		findButton.click();
		drugCodeText.requireText(testCode);
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		dosageChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void failByWrongNegativeDosageCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "0.0";
		String testDesc = "testDesc";
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		dosageChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void failByWrongEmptyDescriptionCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "2.0";
		String testDesc = "";
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		descChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void failByWrongBlankDescriptionCloseDialogOnClickOnOKButtonTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "2.0";
		String testDesc = "   ";
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		okButton.click();
		window.requireVisible();
		descChecker.requireText("ERR");
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("");
	}

	@Test
	@GUITest
	public void closeDialogOnClickOnCancelButtonTest() {
		cancelButton.click();
		window.requireNotVisible();
		String lastButtonPressed = GuiActionRunner.execute(new GuiQuery<String>() {
			@Override
			protected String executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("Cancel");

	}

	@Test
	@GUITest
	public void getAllEntryInfoTest() {
		String testCode = "testCode";
		String testName = "testName";
		String testDosage = "2.0";
		String testDesc = "testDesc";
		drugCodeText.enterText(testCode);
		drugCodeText.requireText(testCode);
		findButton.click();
		drugNameText.enterText(testName);
		drugNameText.requireText(testName);
		drugDosageText.enterText(testDosage);
		drugDosageText.requireText(testDosage);
		drugDescText.enterText(testDesc);
		drugDescText.requireText(testDesc);
		List<String> infos = GuiActionRunner.execute(new GuiQuery<List<String>>() {
			@Override
			protected List<String> executeInEDT() throws Throwable {
				return ((DrugAdder) window.target()).getNewEntryInfo();
			}
		});
		assertThat(infos).containsExactly(testCode, testName, testDosage, testDesc);
	}
}
