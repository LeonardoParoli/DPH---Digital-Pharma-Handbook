package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.dph.informationModel.Condition;
import com.dph.informationModel.Dosage;
import com.dph.informationModel.Drug;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.SystemColor;

public class DrugAdder extends JDialog {

	private static final String NEW = "NEW";
	private static final String OKAY = "OKAY";
	private static final String AWAITING_CHECK = "---";
	private static final String ERR = "ERR";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField drugCodeText;
	private JTextField drugDosageText;
	private JTextField drugNameText;
	private String lastButtonPressed;
	private JButton cancelButton;
	private JButton okButton;
	private List<Drug> model;
	private JLabel drugCodeLabel;
	private JTextPane comment;
	private JLabel drugNameLabel;
	private JLabel codeChecker;
	private JLabel drugDosageLabel;
	private JLabel dosageChecker;
	private JLabel nameChecker;
	private JTextField drugDescText;
	private JScrollPane scrollPane;
	private JLabel drugDescLabel;
	private JLabel descChecker;
	private JButton findButton;
	private Condition selectedCondition;
	private JScrollPane scrollPane_1;

	/**
	 * Create the dialog.
	 */
	public DrugAdder(List<Drug> currentModel, Condition condition) {
		this.model = currentModel;
		this.selectedCondition= condition;
		setResizable(false);
		setAlwaysOnTop(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(10, 10, 337, 448);
		setMinimumSize(new Dimension(337, 448));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lastButtonPressed = "";

		drugCodeLabel = new JLabel("Drug Code:");
		drugCodeLabel.setName("drugCodeLabel");
		drugCodeLabel.setBounds(10, 80, 183, 14);
		contentPanel.add(drugCodeLabel);

		drugCodeText = new JTextField();
		drugCodeText.setName("drugCodeText");
		drugCodeText.setColumns(10);
		drugCodeText.setBounds(10, 92, 149, 20);
		contentPanel.add(drugCodeText);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setName("scrollPanel");
		scrollPane_1.setBounds(10, 7, 304, 54);
		contentPanel.add(scrollPane_1);

		comment = new JTextPane();
		scrollPane_1.setViewportView(comment);
		comment.setBackground(SystemColor.control);
		comment.setEditable(false);
		comment.setText(
				"Insert the code of the drug you wish to associate with the selected condition. If the code isn't in the database, you'll have to give the necessary data of the drug you wish to add.");
		comment.setName("comment");

		drugNameLabel = new JLabel("Drug Name:");
		drugNameLabel.setName("drugNameLabel");
		drugNameLabel.setBounds(10, 122, 149, 14);
		contentPanel.add(drugNameLabel);

		codeChecker = new JLabel(AWAITING_CHECK);
		codeChecker.setName("codeChecker");
		codeChecker.setHorizontalAlignment(SwingConstants.CENTER);
		codeChecker.setBounds(248, 95, 74, 14);
		contentPanel.add(codeChecker);

		drugNameText = new JTextField();
		drugNameText.setEnabled(false);
		drugNameText.setName("drugNameText");
		drugNameText.setColumns(10);
		drugNameText.setBounds(10, 136, 228, 20);
		contentPanel.add(drugNameText);

		drugDosageLabel = new JLabel("Drug Dosage:");
		drugDosageLabel.setName("drugDosageLabel");
		drugDosageLabel.setBounds(10, 179, 149, 14);
		contentPanel.add(drugDosageLabel);

		drugDosageText = new JTextField();
		drugDosageText.setName("drugDosageText");
		drugDosageText.setBounds(10, 192, 228, 20);
		contentPanel.add(drugDosageText);
		drugDosageText.setColumns(10);

		dosageChecker = new JLabel(AWAITING_CHECK);
		dosageChecker.setName("dosageChecker");
		dosageChecker.setHorizontalAlignment(SwingConstants.CENTER);
		dosageChecker.setBounds(248, 195, 74, 14);
		contentPanel.add(dosageChecker);

		nameChecker = new JLabel(AWAITING_CHECK);
		nameChecker.setName("nameChecker");
		nameChecker.setHorizontalAlignment(SwingConstants.CENTER);
		nameChecker.setBounds(248, 139, 74, 14);
		contentPanel.add(nameChecker);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 236, 228, 86);
		contentPanel.add(scrollPane);

		drugDescText = new JTextField();
		drugDescText.setEnabled(false);
		scrollPane.setViewportView(drugDescText);
		drugDescText.setName("drugDescText");
		drugDescText.setColumns(10);

		drugDescLabel = new JLabel("Drug Description:");
		drugDescLabel.setName("drugDescLabel");
		drugDescLabel.setBounds(10, 223, 149, 14);
		contentPanel.add(drugDescLabel);

		descChecker = new JLabel(AWAITING_CHECK);
		descChecker.setName("descChecker");
		descChecker.setHorizontalAlignment(SwingConstants.CENTER);
		descChecker.setBounds(248, 269, 74, 14);
		contentPanel.add(descChecker);

		findButton = new JButton("Find");
		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = drugCodeText.getText();
				if (isValidCode(code) && !hasLocalDuplicateOnCondition(selectedCondition,code)) {
					if (hasDuplicateInModel(code)) {
						for (Drug drug : model) {
							if (drug.getCode().equals(code)) {
								drugNameText.setText(drug.getName());
								drugDescText.setText(drug.getDescription());
								drugCodeText.setEnabled(false);
								findButton.setEnabled(false);
								codeChecker.setText(OKAY);
								okButton.setEnabled(true);
							}
						}
					}else {
						codeChecker.setText(NEW);
						drugCodeText.setEnabled(false);
						drugNameText.setEnabled(true);
						drugDescText.setEnabled(true);
						okButton.setEnabled(true);
						findButton.setEnabled(false);
					}
				}else {
					codeChecker.setText(ERR);
				}
			}
		});
		findButton.setName("findButton");
		findButton.setBounds(169, 91, 62, 21);
		contentPanel.add(findButton);

		JPanel buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.setEnabled(false);
		okButton.setName("OKButton");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isValidInfo()) {
					lastButtonPressed = okButton.getText();
					setVisible(false);
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drugCodeText.setText("");
				drugNameText.setText("");
				drugDosageText.setText("");
				drugDescText.setText("");
				codeChecker.setText(AWAITING_CHECK);
				nameChecker.setText(AWAITING_CHECK);
				dosageChecker.setText(AWAITING_CHECK);
				descChecker.setText(AWAITING_CHECK);
				lastButtonPressed = cancelButton.getText();
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	public String getLastButtonPressed() {
		return lastButtonPressed;
	}

	public List<String> getNewEntryInfo() {
		List<String> infos = new ArrayList<>();
		infos.add(drugCodeText.getText());
		infos.add(drugNameText.getText());
		infos.add(drugDosageText.getText());
		infos.add(drugDescText.getText());
		return infos;
	}

	public boolean isValidInfo() {
		boolean valid = true;
		if (drugNameText.getText().isEmpty() || drugNameText.getText().isBlank()) {
			nameChecker.setText(ERR);
			valid = false;
		} else {
			nameChecker.setText(AWAITING_CHECK);
		}
		if (Double.parseDouble(drugDosageText.getText()) <= 0) {
			dosageChecker.setText(ERR);
			valid = false;
		} else {
			dosageChecker.setText(AWAITING_CHECK);
		}
		if (drugDescText.getText().isEmpty() || drugDescText.getText().isBlank()) {
			descChecker.setText(ERR);
			valid = false;
		}
		return valid;
	}

	private boolean hasDuplicateInModel(String code) {
		for (Drug drug : model) {
			if (drug.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidCode(String code) {
		return !code.isEmpty() && !code.isBlank();
	}
	private boolean hasLocalDuplicateOnCondition(Condition condition, String code) {
		for(Entry<String,Dosage> entry : condition.getDosageList().entrySet()) {
			if(code.equals(entry.getKey())) {
				return true;
			}
		}
		return false;
	}
}
