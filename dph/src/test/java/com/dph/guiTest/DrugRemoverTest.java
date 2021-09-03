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

import com.dph.gui.DrugRemover;

@RunWith(GUITestRunner.class)
public class DrugRemoverTest extends AssertJSwingJUnitTestCase {

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

	@Override
	protected void onSetUp() throws Exception {
		DrugRemover dialog = GuiActionRunner.execute(() -> new DrugRemover("testCode", "testName", 1.0));
		window = new DialogFixture(robot(),dialog);
		window.show();
		drugNameLabel = window.label(DRUG_NAME_LABEL);
		drugCodeLabel = window.label(DRUG_CODE_LABEL);
		drugDosageLabel = window.label(DRUG_DOSAGE_LABEL);
		okButton = window.button(OK_BUTTON);
		cancelButton = window.button(CANCEL_BUTTON);
	}
	
	@Override
	protected void onTearDown() {
		this.drugCodeLabel=null;
		this.drugCodeLabel=null;
		this.drugDosageLabel=null;
		this.okButton=null;
		this.cancelButton=null;
	}
	
	@Test
	public void everythingDisplayedCorrectlyOnPopUpTest() {
		window.requireVisible();
		drugCodeLabel.requireVisible().requireText("Drug Code: " + "testCode");	
		drugNameLabel.requireVisible().requireText("Drug Name: " + "testName");
		drugDosageLabel.requireVisible().requireText("Drug Dosage: " + 1.0);
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
				return ((DrugRemover) window.target()).getLastButtonPressed();
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
				return ((DrugRemover) window.target()).getLastButtonPressed();
			}
		});
		assertThat(lastButtonPressed).isInstanceOf(String.class).isEqualTo("Cancel");
	}
	

}
