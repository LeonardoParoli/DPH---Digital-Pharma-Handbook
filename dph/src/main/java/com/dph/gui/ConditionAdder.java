package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.dph.informationModel.Condition;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
		setPreferredSize(new Dimension(330, 206));
		setSize(new Dimension(330, 206));
		this.model = conditionModel;
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(10, 10, 330, 206);
		setMinimumSize(new Dimension(330, 206));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lastButtonPressed = "";

		conditionCodeText = new JTextField();
		conditionCodeText.setName("conditionCodeText");
		conditionCodeText.setColumns(10);

		conditionNameText = new JTextField();
		conditionNameText.setName("conditionNameText");
		conditionNameText.setColumns(10);

		codeChecker = new JLabel("---");
		codeChecker.setName("codeChecker");
		codeChecker.setHorizontalAlignment(SwingConstants.CENTER);

		comment = new JLabel("Insert new condition code and name:");
		comment.setName("comment");
		comment.setHorizontalAlignment(SwingConstants.CENTER);

		labelCode = new JLabel("Condition Code:");
		labelCode.setName("labelCode");

		labelName = new JLabel("Condition name:");
		labelName.setName("labelName");
		labelName.setText("Condition Name:");

		nameChecker = new JLabel("---");
		nameChecker.setName("nameChecker");
		nameChecker.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout glContentPanel = new GroupLayout(contentPanel);
		glContentPanel
				.setHorizontalGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(comment, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE)
						.addGroup(glContentPanel.createSequentialGroup().addGap(10)
								.addGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(labelCode, GroupLayout.PREFERRED_SIZE, 183,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(conditionCodeText, GroupLayout.PREFERRED_SIZE, 228,
												GroupLayout.PREFERRED_SIZE))
								.addGap(10)
								.addComponent(codeChecker, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
						.addGroup(glContentPanel.createSequentialGroup().addGap(10)
								.addGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(conditionNameText, GroupLayout.PREFERRED_SIZE, 228,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(labelName, GroupLayout.PREFERRED_SIZE, 149,
												GroupLayout.PREFERRED_SIZE))
								.addGap(10).addComponent(nameChecker, GroupLayout.PREFERRED_SIZE, 46,
										GroupLayout.PREFERRED_SIZE)));
		glContentPanel.setVerticalGroup(glContentPanel.createParallelGroup(Alignment.LEADING).addGroup(glContentPanel
				.createSequentialGroup().addGap(6).addComponent(comment).addGap(12)
				.addGroup(glContentPanel.createParallelGroup(Alignment.LEADING).addComponent(labelCode)
						.addGroup(glContentPanel.createSequentialGroup().addGap(12).addComponent(conditionCodeText,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(glContentPanel.createSequentialGroup().addGap(15).addComponent(codeChecker)))
				.addGap(21)
				.addGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(glContentPanel.createSequentialGroup().addGap(13).addComponent(conditionNameText,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(labelName)
						.addGroup(glContentPanel.createSequentialGroup().addGap(16).addComponent(nameChecker)))));
		contentPanel.setLayout(glContentPanel);

		buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(e -> {
			if (isValidInfo()) {
				lastButtonPressed = okButton.getText();
				setVisible(false);
			}
		});
		okButton.setActionCommand("OK");
		okButton.setName("OKButton");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setName("cancelButton");
		cancelButton.addActionListener(e -> {
			lastButtonPressed = cancelButton.getText();
			setVisible(false);
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	public String getLastButtonPressed() {
		return lastButtonPressed;
	}

	public boolean isValidInfo() {
		boolean valid = true;
		if (this.hasDuplicateInModel(conditionCodeText.getText()) || conditionCodeText.getText().isBlank()
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
		for (int i = 0; i < model.getSize(); i++) {
			Condition condition = model.getElementAt(i);
			if (condition.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

}
