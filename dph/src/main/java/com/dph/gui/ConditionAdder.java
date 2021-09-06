package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.dph.informationModel.Condition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ConditionAdder extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField conditionCodeText;
	private JButton okButton;
	private JButton cancelButton;
	private JPanel buttonPane;
	private String lastButtonPressed;
	private DefaultComboBoxModel<Condition> model;
	private JLabel labelName;
	private JLabel labelCode;
	private JLabel comment;
	private JLabel codeChecker;
	private JTextField conditionNameText;
	private JLabel nameChecker;

	/**
	 * Create the dialog.
	 */
	public ConditionAdder(DefaultComboBoxModel<Condition> conditionModel) {
		this.model = conditionModel;
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		setBounds(10, 10, 330, 206);
		setMinimumSize(new Dimension(330, 206));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lastButtonPressed = "";

		conditionCodeText = new JTextField();
		conditionCodeText.setName("conditionCodeText");
		conditionCodeText.setBounds(10, 49, 228, 20);
		contentPanel.add(conditionCodeText);
		conditionCodeText.setColumns(10);

		conditionNameText = new JTextField();
		conditionNameText.setName("conditionNameText");
		conditionNameText.setColumns(10);
		conditionNameText.setBounds(10, 103, 228, 20);
		contentPanel.add(conditionNameText);

		codeChecker = new JLabel("---");
		codeChecker.setName("codeChecker");
		codeChecker.setHorizontalAlignment(SwingConstants.CENTER);
		codeChecker.setBounds(248, 52, 46, 14);
		contentPanel.add(codeChecker);

		comment = new JLabel("Insert new condition code and name:");
		comment.setName("comment");
		comment.setHorizontalAlignment(SwingConstants.CENTER);
		comment.setBounds(0, 11, 314, 14);
		contentPanel.add(comment);

		labelCode = new JLabel("Condition Code:");
		labelCode.setName("labelCode");
		labelCode.setBounds(10, 37, 183, 14);
		contentPanel.add(labelCode);

		labelName = new JLabel("Condition name:");
		labelName.setName("labelName");
		labelName.setText("Condition Name:");
		labelName.setBounds(10, 90, 149, 14);
		contentPanel.add(labelName);

		nameChecker = new JLabel("---");
		nameChecker.setName("nameChecker");
		nameChecker.setHorizontalAlignment(SwingConstants.CENTER);
		nameChecker.setBounds(248, 106, 46, 14);
		contentPanel.add(nameChecker);

		buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isValidInfo()) {
					lastButtonPressed = okButton.getText();
					setVisible(false);
				}
			}
		});
		okButton.setActionCommand("OK");
		okButton.setName("OKButton");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

	public boolean isValidInfo() {
		boolean valid = true;
		if ( this.hasDuplicateInModel(conditionCodeText.getText()) || conditionCodeText.getText().isBlank()
				|| conditionCodeText.getText().isEmpty()) {
			codeChecker.setText("ERR");
			valid = false;
		} else {
			codeChecker.setText("---");
		}
		if (conditionNameText.getText().isEmpty() || conditionNameText.getText().isBlank()) {
			nameChecker.setText("ERR");
			valid = false;
		} else {
			nameChecker.setText("---");
		}
		return valid;
	}

	public List<String> getNewEntryInfo() {
		List<String> infos = new ArrayList<>();
		infos.add(conditionCodeText.getText());
		infos.add(conditionNameText.getText());
		return infos;
	}
	
	private boolean hasDuplicateInModel(String code) {
		for(int i=0; i < model.getSize(); i++) {
			Condition condition = (Condition)model.getElementAt(i);
			if(condition.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

}
