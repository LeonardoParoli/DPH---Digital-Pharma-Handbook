package com.dph.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import com.dph.dal.DBProxy;
import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class UserInterfaceWindow extends JFrame {

	private static final String DISCONNECT = "Disconnect";
	private static final String CONNECT = "Connect";
	private static final String MODIFY = "Modify";
	private static final String BUTTON_HIGHLIGHT = "Button.highlight";
	private static final String BUTTON_LIGHT = "Button.light";
	private static final long serialVersionUID = 1L;

	private JPanel mainContent;
	private JTable drugTable;
	private DBProxy proxy;
	private DefaultTableModel drugModel;
	private DefaultComboBoxModel<Condition> conditionModel;
	private JPanel drugDescriptionBox;
	private JTextArea drugDescription;
	private JLabel statusLabel;
	private JComboBox<Condition> conditionSelection;
	private JButton addConditionButton;
	private JButton removeConditionButton;
	private JButton addDrugButton;
	private JButton removeDrugButton;
	private JButton modifyDescriptionButton;
	private JButton connectButton;
	private JPanel conditionSelectionBox;
	private JPanel drugTableBox;
	private DrugRemover drugRemover = null;
	private ConditionRemover conditionRemover = null;
	private ConditionAdder conditionAdder = null;
	private DrugAdder drugAdder = null;
	private List<Drug> drugModelList;

	public void setDALProxy(DBProxy proxy) {
		this.proxy = proxy;
	}

	public boolean isAbleToConnect() {
		return proxy != null;
	}

	/**
	 * Create the frame.
	 * 
	 * @param proxy
	 */
	public UserInterfaceWindow() {
		setSize(new Dimension(500, 600));
		setResizable(false);
		setTitle("Digital Pharma Handbook");
		setForeground(new Color(0, 0, 0));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(10, 10, 471, 600);
		setMinimumSize(new Dimension(500, 600));
		mainContent = new JPanel();
		mainContent.setBackground(UIManager.getColor(BUTTON_LIGHT));
		mainContent.setName("mainContent");
		mainContent.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainContent);
		mainContent.setLayout(null);

		conditionSelectionBox = new JPanel();
		conditionSelectionBox.setBackground(UIManager.getColor(BUTTON_HIGHLIGHT));
		conditionSelectionBox.setName("conditionSelectionBox");
		conditionSelectionBox.setBounds(10, 0, 464, 44);
		mainContent.add(conditionSelectionBox);
		conditionSelectionBox.setLayout(null);

		conditionModel = new DefaultComboBoxModel<>();
		conditionSelection = new JComboBox<>(conditionModel);
		conditionSelection.addItemListener(e -> {
			if (e.getSource() == conditionSelection) {
				if (conditionSelection.getSelectedIndex() != -1) {
					Condition condition = (Condition) conditionModel.getSelectedItem();
					drugModel = new DefaultTableModel();
					drugModel.setColumnIdentifiers(columnNames());
					for (Entry<String, Dosage> dosage : condition.getDosageList().entrySet()) {
						Drug drug = dosage.getValue().getDrug();
						String dosageValue = "" + dosage.getValue().getDrugDosage();
						String[] row = new String[3];
						row[0] = drug.getCode();
						row[1] = drug.getName();
						row[2] = dosageValue;
						drugModel.addRow(row);
					}
					drugTable.setModel(drugModel);
					drugTable.setEnabled(true);
					removeConditionButton.setEnabled(true);
					addDrugButton.setEnabled(true);
				} else {
					drugModel = new DefaultTableModel(new String[0][0], columnNames());
					drugTable.setModel(drugModel);
					drugTable.setEnabled(false);
					drugDescription.setEnabled(false);
					removeConditionButton.setEnabled(false);
					addDrugButton.setEnabled(false);
				}
			}
		});
		conditionSelection.setBounds(10, 11, 230, 23);
		conditionSelection.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Condition condition = (Condition) value;
				if (condition == null) {
					return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
				} else {
					return super.getListCellRendererComponent(list, getConditionString(condition), index, isSelected,
							cellHasFocus);
				}
			}
		});
		conditionSelection.setEnabled(false);
		conditionSelection.setName("conditionSelection");
		conditionSelection.setSelectedIndex(-1);
		conditionSelectionBox.add(conditionSelection);

		addConditionButton = new JButton("Add");
		addConditionButton.addActionListener(e -> {
			conditionAdder = new ConditionAdder(conditionModel);
			conditionAdder.setVisible(true);
			if (conditionAdder.getLastButtonPressed().equals("OK")) {
				List<String> infos = conditionAdder.getNewEntryInfo();
				conditionModel.addElement(new Condition(infos.get(0), infos.get(1), new ArrayList<>()));
			}
		});
		addConditionButton.setName("addConditionButton");
		addConditionButton.setEnabled(false);
		addConditionButton.setBounds(247, 11, 89, 23);
		conditionSelectionBox.add(addConditionButton);

		removeConditionButton = new JButton("Remove");
		removeConditionButton.addActionListener(e -> {
			Condition selectedCondition = (Condition) conditionSelection.getSelectedItem();
			conditionRemover = new ConditionRemover(selectedCondition.getCode(), selectedCondition.getName());
			conditionRemover.setVisible(true);
			if (conditionRemover.getLastButtonPressed().equals("OK")) {
				conditionModel.removeElementAt(conditionSelection.getSelectedIndex());
			}
		});
		removeConditionButton.setName("removeConditionButton");
		removeConditionButton.setEnabled(false);
		removeConditionButton.setBounds(336, 11, 89, 23);
		conditionSelectionBox.add(removeConditionButton);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalStrut.setBounds(0, 45, 435, 12);
		conditionSelectionBox.add(horizontalStrut);

		drugTableBox = new JPanel();
		drugTableBox.setBackground(UIManager.getColor(BUTTON_HIGHLIGHT));
		drugTableBox.setName("drugTableBox");
		drugTableBox.setToolTipText("");
		drugTableBox.setBounds(10, 55, 464, 253);
		mainContent.add(drugTableBox);

		drugTable = new JTable();
		JScrollPane drugTableScrollPane = new JScrollPane(drugTable);
		modifyDescriptionButton = new JButton(MODIFY);
		drugTable.setFillsViewportHeight(true);
		drugTable.setEnabled(false);
		drugTable.setName("drugTable");
		drugTable.setBackground(Color.WHITE);
		drugTableScrollPane.setViewportView(drugTable);
		drugTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		drugModel = new DefaultTableModel(new String[0][0], columnNames());
		drugTable.setModel(drugModel);
		drugTable.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		drugTable.getSelectionModel().addListSelectionListener(e -> {
			if (drugTable.getSelectedRow() != -1) {
				modifyDescriptionButton.setEnabled(true);
				Condition selCondition = (Condition) conditionSelection.getSelectedItem();
				String description = selCondition.getDosageList()
						.get(drugTable.getValueAt(drugTable.getSelectedRow(), 0)).getDrug().getDescription();
				removeDrugButton.setEnabled(true);
				drugDescription.setEnabled(true);
				drugDescription.setText(description);
			} else {
				removeDrugButton.setEnabled(false);
				modifyDescriptionButton.setEnabled(false);
				drugDescription.setEnabled(false);
				drugDescription.setText("");
			}
		});

		addDrugButton = new JButton("Add");
		addDrugButton.addActionListener(e -> {
			drugAdder = new DrugAdder(drugModelList, (Condition) conditionSelection.getSelectedItem());
			drugAdder.setVisible(true);
			if (drugAdder.getLastButtonPressed().equals("OK")) {
				List<String> infos = drugAdder.getNewEntryInfo();
				Condition selCondition = (Condition) conditionModel.getSelectedItem();
				Drug drug = new Drug(infos.get(0), infos.get(1), infos.get(3));
				selCondition.addDrug(drug, Double.parseDouble(infos.get(2)));
				String[] row = new String[3];
				row[0] = drug.getCode();
				row[1] = drug.getName();
				row[2] = infos.get(2);
				drugModel.addRow(row);
				drugTable.setModel(drugModel);
			}
		});
		addDrugButton.setName("addDrugButton");
		addDrugButton.setEnabled(false);

		removeDrugButton = new JButton("Remove");
		removeDrugButton.addActionListener(e -> {
			String drugCode = (String) drugTable.getValueAt(drugTable.getSelectedRow(), 0);
			String drugName = (String) drugTable.getValueAt(drugTable.getSelectedRow(), 1);
			double drugdosage = Double.parseDouble((String) drugTable.getValueAt(drugTable.getSelectedRow(), 2));
			drugRemover = new DrugRemover(drugCode, drugName, drugdosage);
			drugRemover.setVisible(true);
			if (drugRemover.getLastButtonPressed().equals("OK")) {
				drugModel.removeRow(drugTable.getSelectedRow());
				drugTable.setModel(drugModel);
			}
		});
		removeDrugButton.setName("removeDrugButton");
		removeDrugButton.setEnabled(false);
		GroupLayout glDrugTableBox = new GroupLayout(drugTableBox);
		glDrugTableBox.setHorizontalGroup(glDrugTableBox.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				glDrugTableBox.createSequentialGroup().addContainerGap().addGroup(glDrugTableBox
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(drugTableScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 407,
								Short.MAX_VALUE)
						.addGroup(glDrugTableBox.createSequentialGroup()
								.addComponent(addDrugButton, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(removeDrugButton,
										GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)))
						.addGap(18)));
		glDrugTableBox
				.setVerticalGroup(glDrugTableBox.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						glDrugTableBox.createSequentialGroup().addContainerGap()
								.addComponent(drugTableScrollPane, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(glDrugTableBox.createParallelGroup(Alignment.BASELINE)
										.addComponent(removeDrugButton).addComponent(addDrugButton))
								.addContainerGap()));
		drugTableBox.setLayout(glDrugTableBox);

		drugDescriptionBox = new JPanel();
		drugDescriptionBox.setBackground(UIManager.getColor(BUTTON_HIGHLIGHT));
		drugDescriptionBox.setName("drugDescriptionBox");
		drugDescriptionBox.setBounds(10, 321, 464, 240);
		mainContent.add(drugDescriptionBox);

		drugDescription = new JTextArea();
		drugDescription.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		drugDescription.setLineWrap(true);
		drugDescription.setBackground(Color.WHITE);
		drugDescription.setName("drugDescription");
		drugDescription.setEditable(false);

		modifyDescriptionButton.addActionListener(e -> {
			if (modifyDescriptionButton.getText().equals(MODIFY)) {
				modifyDescriptionButton.setText("Done");
				drugDescription.setEnabled(true);
				drugDescription.setEditable(true);
				drugTable.setEnabled(false);
				conditionSelection.setEnabled(false);
				addConditionButton.setEnabled(false);
				removeConditionButton.setEnabled(false);
				addDrugButton.setEnabled(false);
				removeDrugButton.setEnabled(false);
			} else {
				modifyDescriptionButton.setText(MODIFY);
				drugDescription.setEnabled(true);
				drugDescription.setEditable(true);
				drugTable.setEnabled(true);
				conditionSelection.setEnabled(true);
				addConditionButton.setEnabled(true);
				removeConditionButton.setEnabled(true);
				addDrugButton.setEnabled(true);
				removeDrugButton.setEnabled(true);
				String newDesc = drugDescription.getText();
				Condition selCondition = (Condition) conditionSelection.getSelectedItem();
				selCondition.getDosageList().get(drugTable.getValueAt(drugTable.getSelectedRow(), 0)).getDrug()
						.setDescription(newDesc);
			}
		});
		modifyDescriptionButton.setName("modifyDescriptionButton");
		modifyDescriptionButton.setEnabled(false);

		statusLabel = new JLabel("Awaiting connection.");
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setName("statusLabel");

		connectButton = new JButton(CONNECT);
		connectButton.addActionListener(e -> {
			if (isAbleToConnect()) {
				if (connectButton.getText().equals(CONNECT)) {
					connectButton.setText(DISCONNECT);
					statusLabel.setText("Connected!");
					addConditionButton.setEnabled(true);
					try {
						List<Condition> conditionList = proxy.findAllConditions();
						conditionModel.removeAllElements();
						conditionModel.addAll(conditionList);
						drugModelList = new ArrayList<>();
						for (Condition condition : conditionList) {
							for (Entry<String, Dosage> entry : condition.getDosageList().entrySet()) {
								Drug drug = entry.getValue().getDrug();
								boolean alreadyInserted = false;
								for (Drug d : drugModelList) {
									if (d.equals(drug)) {
										alreadyInserted = true;
									}
								}
								if (!alreadyInserted) {
									drugModelList.add(drug);
								}
							}
						}
						conditionSelection.setModel(conditionModel);
						conditionSelection.setEnabled(true);

					} catch (IOException proxyException) {
						statusLabel.setText("Error, cannot load database!");
						addConditionButton.setEnabled(false);
					}
				} else {
					connectButton.setText(CONNECT);
					statusLabel.setText("Awaiting connection.");
					drugModelList = null;
					List<Condition> conditions = new ArrayList<>();
					for (int i = 0; i < conditionModel.getSize(); i++) {
						conditions.add(conditionModel.getElementAt(i));
					}
					try {
						proxy.updateDatabase(conditions);
					} catch (IOException e1) {
						statusLabel.setText("Error, cannot load database!");
					}
					conditionModel.removeAllElements();
					conditionSelection.setModel(conditionModel);
					conditionSelection.setEnabled(false);
					addConditionButton.setEnabled(false);
					drugModel = new DefaultTableModel(new String[0][0], columnNames());
					drugTable.setModel(drugModel);
					drugDescription.setText("");

				}
			} else {
				statusLabel.setText("ERROR.");
			}
		});
		connectButton.setName("connectButton");
		GroupLayout glDrugDescriptionBox = new GroupLayout(drugDescriptionBox);
		glDrugDescriptionBox.setHorizontalGroup(glDrugDescriptionBox.createParallelGroup(Alignment.LEADING)
				.addGroup(glDrugDescriptionBox.createSequentialGroup().addGroup(glDrugDescriptionBox
						.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,
								glDrugDescriptionBox.createSequentialGroup().addGap(25).addComponent(drugDescription,
										GroupLayout.PREFERRED_SIZE, 415, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, glDrugDescriptionBox.createSequentialGroup().addGap(19)
								.addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(glDrugDescriptionBox.createParallelGroup(Alignment.TRAILING)
										.addComponent(connectButton, GroupLayout.PREFERRED_SIZE, 109,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(modifyDescriptionButton, GroupLayout.PREFERRED_SIZE, 89,
												GroupLayout.PREFERRED_SIZE))))
						.addGap(24)));
		glDrugDescriptionBox.setVerticalGroup(glDrugDescriptionBox.createParallelGroup(Alignment.LEADING)
				.addGroup(glDrugDescriptionBox.createSequentialGroup().addContainerGap()
						.addGroup(glDrugDescriptionBox.createParallelGroup(Alignment.LEADING)
								.addGroup(glDrugDescriptionBox.createSequentialGroup()
										.addComponent(drugDescription, GroupLayout.PREFERRED_SIZE, 163,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(modifyDescriptionButton)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(connectButton))
								.addComponent(statusLabel, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 34,
										GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		drugDescriptionBox.setLayout(glDrugDescriptionBox);

		Component horizontalStrut1 = Box.createHorizontalStrut(20);
		horizontalStrut1.setBounds(10, 307, 464, 12);
		mainContent.add(horizontalStrut1);
	}

	private String getConditionString(Condition condition) {
		return condition.getCode() + "/" + condition.getName();
	}

	private String[] columnNames() {
		String[] columnNames = new String[3];
		columnNames[0] = "Drug Code";
		columnNames[1] = "Drug Name";
		columnNames[2] = "Drug Dosage";
		return columnNames;
	}
}
