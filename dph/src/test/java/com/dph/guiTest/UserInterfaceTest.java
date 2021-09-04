package com.dph.guiTest;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.timing.Pause;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.dph.dal.DBProxy;
import com.dph.gui.ConditionAdder;
import com.dph.gui.ConditionRemover;
import com.dph.gui.DrugAdder;
import com.dph.gui.DrugRemover;
import com.dph.gui.UserInterfaceWindow;
import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

@RunWith(GUITestRunner.class)
public class UserInterfaceTest extends AssertJSwingJUnitTestCase {
	private static final String MAIN_CONTENT = "mainContent";
	private JPanelFixture mainContent;
	private JPanelFixture conditionSelectionBox;
	private JPanelFixture drugTableBox;
	private JPanelFixture drugDescBox;
	private JComboBoxFixture conditionSelection;
	private JTableFixture drugTable;
	private JTextComponentFixture drugDesc;
	private JLabelFixture statusLabel;
	private JButtonFixture addConditionButton;
	private JButtonFixture removeConditionButton;
	private JButtonFixture modifyDrugDescButton;
	private JButtonFixture addDrugButton;
	private JButtonFixture removeDrugButton;
	private JButtonFixture connectButton;
	private static final String CONDITION_SELECTION_BOX = "conditionSelectionBox";
	private static final String DRUG_TABLE_BOX = "drugTableBox";
	private static final String DRUG_DESCRIPTION_BOX = "drugDescriptionBox";
	private static final String CONDITION_SELECTION = "conditionSelection";
	private static final String DRUG_TABLE = "drugTable";
	private static final String MODIFY_DESCRIPTION_BUTTON = "modifyDescriptionButton";
	private static final String REMOVE_DRUG_BUTTON = "removeDrugButton";
	private static final String REMOVE_CONDITION_BUTTON = "removeConditionButton";
	private static final String ADD_DRUG_BUTTON = "addDrugButton";
	private static final String STATUS_LABEL = "statusLabel";
	private static final String ADD_CONDITION_BUTTON = "addConditionButton";
	private static final String CONNECT_BUTTON = "connectButton";
	private static final String DRUG_DESCRIPTION = "drugDescription";
	private FrameFixture window;
	private DBProxy proxy;

	@Override
	protected void onSetUp() {
		UserInterfaceWindow frame = GuiActionRunner.execute(() -> new UserInterfaceWindow());
		window = new FrameFixture(robot(), frame);
		robot().settings().eventPostingDelay(1000);
		robot().settings().delayBetweenEvents(200);
		window.show();
		Pause.pause(1000);
		this.mainContent = window.panel(MAIN_CONTENT);
		this.conditionSelectionBox = mainContent.panel(CONDITION_SELECTION_BOX);
		this.drugTableBox = mainContent.panel(DRUG_TABLE_BOX);
		this.drugDescBox = mainContent.panel(DRUG_DESCRIPTION_BOX);
		this.conditionSelection = conditionSelectionBox.comboBox(CONDITION_SELECTION);
		this.drugTable = drugTableBox.table(DRUG_TABLE);
		this.drugDesc = drugDescBox.textBox(DRUG_DESCRIPTION);
		this.statusLabel = drugDescBox.label(STATUS_LABEL);
		this.addConditionButton = conditionSelectionBox.button(ADD_CONDITION_BUTTON);
		this.removeConditionButton = conditionSelectionBox.button(REMOVE_CONDITION_BUTTON);
		this.addDrugButton = drugTableBox.button(ADD_DRUG_BUTTON);
		this.removeDrugButton = drugTableBox.button(REMOVE_DRUG_BUTTON);
		this.modifyDrugDescButton = drugDescBox.button(MODIFY_DESCRIPTION_BUTTON);
		this.connectButton = drugDescBox.button(CONNECT_BUTTON);
	}

	@Override
	protected void onTearDown() {
		this.mainContent = null;
		this.conditionSelectionBox = null;
		this.drugTableBox = null;
		this.drugDescBox = null;
		this.conditionSelection = null;
		this.drugTable = null;
		this.drugDesc = null;
		this.statusLabel = null;
		this.addConditionButton = null;
		this.removeConditionButton = null;
		this.addDrugButton = null;
		this.removeDrugButton = null;
		this.modifyDrugDescButton = null;
		this.connectButton = null;
	}

	@Test
	public void isEverythingDisplayedCorrectlyOnStartupTest() {
		window.requireTitle("Digital Pharma Handbook");
		mainContent.requireVisible();
		conditionSelectionBox.requireVisible();
		drugTableBox.requireVisible();
		conditionSelection.requireVisible().requireDisabled().requireItemCount(0);
		addConditionButton.requireVisible().requireDisabled().requireText("Add");
		removeConditionButton.requireVisible().requireDisabled().requireText("Remove");
		addDrugButton.requireVisible().requireDisabled().requireText("Add");
		removeDrugButton.requireVisible().requireDisabled().requireText("Remove");
		;
		drugTable.requireVisible().requireDisabled().requireColumnCount(3).requireRowCount(0);
		modifyDrugDescButton.requireVisible().requireDisabled().requireText("Modify");
		drugDesc.requireVisible().requireEnabled().requireNotEditable();
		statusLabel.requireVisible().requireText("Awaiting connection.");
		connectButton.requireVisible().requireEnabled().requireText("Connect");
	}

	@Test
	public void isAbleToConnectTest() {
		boolean isAble = GuiActionRunner.execute(new GuiQuery<Boolean>() {
			@Override
			protected Boolean executeInEDT() throws Throwable {
				return ((UserInterfaceWindow) window.target()).isAbleToConnect();
			}
		});
		assertThat(isAble).isInstanceOf(Boolean.class).isFalse();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(Mockito.mock(DBProxy.class));
			}
		});
		isAble = GuiActionRunner.execute(new GuiQuery<Boolean>() {
			@Override
			protected Boolean executeInEDT() throws Throwable {
				return ((UserInterfaceWindow) window.target()).isAbleToConnect();
			}
		});
		assertThat(isAble).isInstanceOf(Boolean.class).isTrue();
	}

	@Test
	public void connectionToDALTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		statusLabel.requireVisible().requireEnabled().requireText("Awaiting connection.");
		connectButton.requireText("Connect");
		connectButton.click();
		connectButton.requireText("Disconnect");
		statusLabel.requireVisible().requireEnabled().requireText("Connected!");
		addConditionButton.requireVisible().requireEnabled();
		addDrugButton.requireVisible().requireDisabled();
		drugTable.requireVisible().requireRowCount(0);
		drugDesc.requireVisible().requireText("");
		removeConditionButton.requireVisible().requireDisabled();
		removeDrugButton.requireVisible().requireDisabled();
		modifyDrugDescButton.requireVisible().requireDisabled();
		drugTable.requireVisible().requireDisabled().requireColumnCount(3).requireRowCount(0);
		conditionSelection.requireVisible().requireEnabled().requireNotEditable().requireItemCount(2);
		assertThat(conditionSelection.contents()).containsExactly("testCode/testCondition", "2ndtestCode/2ndtestCondition");
		conditionSelection.requireNoSelection();
	}

	@Test
	public void disconnectFromDALTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		connectButton.requireText("Disconnect");
		connectButton.click();
		addConditionButton.requireVisible().requireDisabled();
		addDrugButton.requireVisible().requireDisabled();
		removeConditionButton.requireVisible().requireDisabled();
		removeDrugButton.requireVisible().requireDisabled();
		modifyDrugDescButton.requireVisible().requireDisabled();
		drugTable.requireVisible().requireDisabled().requireColumnCount(3);
		conditionSelection.requireVisible().requireNotEditable().requireDisabled();
	}

	@Test
	public void connectionToDALFailTest() {
		statusLabel.requireVisible().requireEnabled().requireText("Awaiting connection.");
		connectButton.requireText("Connect");
		connectButton.click();
		statusLabel.requireVisible().requireEnabled().requireText("ERROR.");
	}

	@Test
	public void removeConditionButtonShouldBeEnabledWhenSelectingConditionInComboBoxTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		removeConditionButton.requireDisabled();
		conditionSelection.requireItemCount(2).requireNoSelection();
		conditionSelection.selectItem(0);
		conditionSelection.requireSelection(0).requireSelection("testCode/testCondition");
		removeConditionButton.requireEnabled();
	}

	@Test
	public void drugTableShouldBeEnabledWhenConditionIsSelectedInComboBoxTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.requireEnabled().requireColumnCount(3).requireColumnNamed("Drug Code").requireColumnNamed("Drug Name")
				.requireColumnNamed("Drug Dosage").requireRowCount(2);
	}
	
	@Test
	public void drugTableShouldBeEmptyIfSelectedConditionHasNoDrugTest() throws IOException {
		DBProxy proxy = Mockito.mock(DBProxy.class);
		List<Dosage> dosages = new ArrayList<>();
		Condition condition = new Condition("testCode", "testCondition", dosages);
		List<Condition> conditions = new ArrayList<>();
		conditions.add(condition);
		when(proxy.findAllConditions()).thenReturn(conditions);
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.requireEnabled().requireVisible().requireColumnCount(3).requireColumnNamed("Drug Code").requireColumnNamed("Drug Name")
				.requireColumnNamed("Drug Dosage").requireRowCount(0);
	}

	@Test
	public void showAddDrugButtonWhenConditionIsSelectedInComboBoxTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		addDrugButton.requireDisabled();
		conditionSelection.requireItemCount(2).requireNoSelection();
		conditionSelection.selectItem(0);
		conditionSelection.requireSelection(0).requireSelection("testCode/testCondition");
		addDrugButton.requireEnabled();
	}

	@Test
	public void drugDescriptionShouldBeEnabledWhenSelectingDrugEntryFromDrugTable() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		drugDesc.requireEnabled().requireNotEditable();
	}
	
	@Test
	public void removeDrugButtonShouldBeEnabledWhenSelectingDrugEntryFromDrugTableTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		removeDrugButton.requireVisible().requireEnabled();
	}
	
	@Test
	public void removeDrugButtonShouldBeDisabledWhenDeselectingDrugTableTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		removeDrugButton.requireVisible().requireEnabled();
		drugTable.unselectRows(0);
		removeDrugButton.requireVisible().requireDisabled();
	}

	@Test
	public void showDescriptionWhenSelectingDrugEntryFromDrugTableTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.requireRowCount(2);
		drugTable.selectRows(0);
		drugTable.requireSelectedRows(0);
		drugDesc.requireText("desc1");
		drugTable.selectRows(1);
		drugTable.requireSelectedRows(1);
		drugDesc.requireText("desc2");
	}

	@Test
	public void enableModifyButtonWhenDrugIsSelectedFromDrugListTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		modifyDrugDescButton.requireEnabled();
	}

	@Test
	public void clickEnabledModifyButtonShouldChangeButtonTextTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		modifyDrugDescButton.click();
		modifyDrugDescButton.requireText("Done");
	}
	
	@Test
	public void clickEnabledModifyButtonShouldDisableEverythingElseTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		modifyDrugDescButton.click();
				drugTable.requireDisabled();
		conditionSelection.requireDisabled();
		addConditionButton.requireDisabled();
		removeConditionButton.requireDisabled();;
		addDrugButton.requireDisabled();
		removeDrugButton.requireDisabled();
		modifyDrugDescButton.click();
		drugTable.requireEnabled();
		conditionSelection.requireEnabled();
		addConditionButton.requireEnabled();
		removeConditionButton.requireEnabled();;
		addDrugButton.requireEnabled();
		removeDrugButton.requireEnabled();
	}
	
	@Test
	public void modifyDrugDescWhenDrugIsSelectedAndButtonPressedTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		drugDesc.requireText("desc1");
		modifyDrugDescButton.click();
		drugDesc.requireText("desc1").requireEditable();
		drugDesc.enterText(" testType");
		drugDesc.requireText("desc1 testType");
		modifyDrugDescButton.click();
		conditionSelection.clearSelection();
		drugDesc.requireDisabled().requireVisible().requireText("");
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		drugDesc.requireEditable().requireVisible().requireText("desc1 testType");
	}
	
	@Test
	public void failByBlankDescriptionModifyDrugDescWhenDrugIsSelectedAndButtonPressedTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		drugDesc.requireText("desc1");
		modifyDrugDescButton.click();
		drugDesc.requireText("desc1").requireEditable();
		drugDesc.enterText(" testType");
		drugDesc.requireText("desc1 testType");
		modifyDrugDescButton.click();
	}
	
	@Test
	public void failByEmptyDescriptionModifyDrugDescWhenDrugIsSelectedAndButtonPressedTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.selectItem(0);
		drugTable.selectRows(0);
		drugDesc.requireText("desc1");
		modifyDrugDescButton.click();
		drugDesc.requireText("desc1").requireEditable();
		drugDesc.enterText(" testType");
		drugDesc.requireText("desc1 testType");
		modifyDrugDescButton.click();
	}

	@Test
	public void clickingConditionAddButtonOpensNewConditionAdderWindowTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		addConditionButton.requireEnabled().click();
		DialogFixture adder = WindowFinder.findDialog(ConditionAdder.class).withTimeout(5000).using(robot());
		adder.requireVisible();
	}
	
	@Test
	public void addingConditionThroughDialogTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		addConditionButton.requireEnabled().click();
		DialogFixture adder = WindowFinder.findDialog(ConditionAdder.class).withTimeout(5000).using(robot());
		adder.requireVisible();
		adder.textBox("conditionCodeText").enterText("condition2Code");
		adder.textBox("conditionNameText").enterText("condition2Name");
		adder.button("OKButton").click();
		conditionSelection.requireSelection("testCode/testCondition").requireItemCount(3);
	}

	@Test
	public void clickingConditionRemoveButtonOpensNewConditionRemoverWindowTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		removeConditionButton.requireEnabled().click();
		DialogFixture remover = WindowFinder.findDialog(ConditionRemover.class).withTimeout(5000).using(robot());
		remover.requireVisible();
	}
	
	@Test
	public void removingConditionThroughDialogTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		removeConditionButton.requireEnabled().click();
		DialogFixture remover = WindowFinder.findDialog(ConditionRemover.class).withTimeout(5000).using(robot());
		remover.requireVisible();
		remover.button("OKButton").click();
		conditionSelection.requireItemCount(1).requireSelection("2ndtestCode/2ndtestCondition");
	}
	

	@Test
	public void clickingDrugAddButtonopensNewDrugAdderWindowTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		drugTable.requireRowCount(2);
		addDrugButton.requireEnabled().click();
		DialogFixture adder = WindowFinder.findDialog(DrugAdder.class).withTimeout(5000).using(robot());
		adder.requireVisible();
	}
	
	@Test
	public void addingDrugThroughDialogTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		drugTable.requireNoSelection().requireRowCount(2);
		addDrugButton.requireEnabled().click();
		DialogFixture adder = WindowFinder.findDialog(DrugAdder.class).withTimeout(5000).using(robot());
		adder.textBox("drugCodeText").enterText("d3");
		adder.button("findButton").click();
		adder.textBox("drugNameText").enterText("dn3");
		adder.textBox("drugDosageText").enterText("3.0");
		adder.textBox("drugDescText").enterText("desc3");
		adder.button("OKButton").click();
		drugTable.requireNoSelection().requireRowCount(3);
	}
	
	@Test
	public void clickingDrugRemoveButtonOpensNewDrugRemoverWindowTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		drugTable.requireNoSelection().requireRowCount(2);
		drugTable.selectRows(0);
		removeDrugButton.click();
		DialogFixture remover = WindowFinder.findDialog(DrugRemover.class).withTimeout(5000).using(robot());
		remover.requireVisible();
	}
	
	@Test
	public void removingDrugThroughDialogTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		conditionSelection.requireItemCount(2);
		conditionSelection.selectItem(0);
		drugTable.requireNoSelection().requireRowCount(2);
		drugTable.selectRows(0);
		removeDrugButton.click();
		DialogFixture remover = WindowFinder.findDialog(DrugRemover.class).withTimeout(5000).using(robot());
		remover.button("OKButton").click();
		drugTable.requireRowCount(1);
	}
	
	@Test
	public void failDisconnectFromDALOnsavingChangesTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		doThrow(IOException.class).when(proxy).updateDatabase(Mockito.anyList());
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		connectButton.click();
		statusLabel.requireVisible().requireText("Error, cannot load database!");
	}
	
	@Test
	public void failConnectionToDALWhenRetrievingDrugListTest() throws IOException {
		DBProxy proxy = this.setupProxyMock();
		doThrow(IOException.class).when(proxy).findAllConditions();
		GuiActionRunner.execute(new GuiTask() {
			@Override
			protected void executeInEDT() throws Throwable {
				((UserInterfaceWindow) window.target()).setDALProxy(proxy);
			}
		});
		connectButton.click();
		statusLabel.requireVisible().requireText("Error, cannot load database!");
	}

	private DBProxy setupProxyMock() throws IOException {
		DBProxy proxy = Mockito.mock(DBProxy.class);
		List<Dosage> dosages = new ArrayList<>();
		dosages.add(new Dosage(new Drug("d1", "dn1", "desc1"), 1.0));
		dosages.add(new Dosage(new Drug("d2", "dn2", "desc2"), 2.0));
		Condition condition = new Condition("testCode", "testCondition", dosages);
		Condition condition2 = new Condition("2ndtestCode", "2ndtestCondition", dosages);
		List<Condition> conditions = new ArrayList<>();
		conditions.add(condition);
		conditions.add(condition2);
		when(proxy.findAllConditions()).thenReturn(conditions);
		return proxy;
	}
}
