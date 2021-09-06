package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
	private JScrollPane scrollPane;
	private JLabel drugDescLabel;
	private JLabel descChecker;
	private JButton findButton;
	private Condition selectedCondition;
	private JScrollPane scrollPaneComment;
	private JTextField drugDescText;

	/**
	 * Create the dialog.
	 */
	public DrugAdder(List<Drug> currentModel, Condition condition) {
		this.model = currentModel;
		this.selectedCondition= condition;
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(10, 10, 338, 451);
		setMinimumSize(new Dimension(337, 448));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lastButtonPressed = "";
		
		drugCodeLabel = new JLabel("Drug Code:");
		drugCodeLabel.setBounds(10, 80, 183, 14);
		drugCodeLabel.setName("drugCodeLabel");
		contentPanel.add(drugCodeLabel);
		
		drugCodeText = new JTextField();
		drugCodeText.setBounds(10, 92, 149, 20);
		drugCodeText.setName("drugCodeText");
		drugCodeText.setColumns(10);
		contentPanel.add(drugCodeText);
		
		scrollPaneComment = new JScrollPane();
		scrollPaneComment.setBounds(10, 7, 304, 54);
		scrollPaneComment.setName("scrollPanel");
		comment = new JTextPane();
		comment.setBackground(SystemColor.control);
		comment.setEditable(false);
		comment.setText(
				"Insert the code of the drug you wish to associate with the selected condition. If the code isn't in the database, you'll have to give the necessary data of the drug you wish to add.");
		comment.setName("comment");
		scrollPaneComment.setViewportView(comment);
		contentPanel.add(scrollPaneComment);

		drugNameLabel = new JLabel("Drug Name:");
		drugNameLabel.setBounds(10, 122, 149, 14);
		drugNameLabel.setName("drugNameLabel");
		contentPanel.add(drugNameLabel);
		
		codeChecker = new JLabel(AWAITING_CHECK);
		codeChecker.setBounds(248, 95, 74, 14);
		codeChecker.setName("codeChecker");
		codeChecker.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(codeChecker);
		
		drugNameText = new JTextField();
		drugNameText.setBounds(10, 136, 228, 20);
		drugNameText.setEnabled(false);
		drugNameText.setName("drugNameText");
		drugNameText.setColumns(10);
		contentPanel.add(drugNameText);
		
		drugDosageLabel = new JLabel("Drug Dosage:");
		drugDosageLabel.setBounds(10, 179, 149, 14);
		drugDosageLabel.setName("drugDosageLabel");
		contentPanel.add(drugDosageLabel);
		
		drugDosageText = new JTextField();
		drugDosageText.setBounds(10, 192, 228, 20);
		drugDosageText.setName("drugDosageText");
		drugDosageText.setColumns(10);
		contentPanel.add(drugDosageText);
		
		dosageChecker = new JLabel(AWAITING_CHECK);
		dosageChecker.setBounds(248, 195, 74, 14);
		dosageChecker.setName("dosageChecker");
		dosageChecker.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(dosageChecker);
		
		nameChecker = new JLabel(AWAITING_CHECK);
		nameChecker.setBounds(248, 139, 74, 14);
		nameChecker.setName("nameChecker");
		nameChecker.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(nameChecker);

		drugDescLabel = new JLabel("Drug Description:");
		drugDescLabel.setBounds(10, 223, 149, 14);
		drugDescLabel.setName("drugDescLabel");
		contentPanel.add(drugDescLabel);
		
		descChecker = new JLabel(AWAITING_CHECK);
		descChecker.setBounds(248, 269, 74, 14);
		descChecker.setName("descChecker");
		descChecker.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(descChecker);

		findButton = new JButton("Find");
		findButton.setBounds(169, 91, 62, 21);
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
		contentPanel.add(findButton);
		
		JScrollPane scrollPaneDesc = new JScrollPane();
		scrollPaneDesc.setName("drugDescScroll");
		scrollPaneDesc.setBounds(10, 237, 228, 128);
		contentPanel.add(scrollPaneDesc);
		
		drugDescText = new JTextField();
		drugDescText.setEnabled(false);
		drugDescText.setName("drugDescText");
		scrollPaneDesc.setViewportView(drugDescText);
		drugDescText.setColumns(10);

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
