package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class DrugRemover extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel drugNameLabel;
	private JLabel drugDosageLabel;
	private JLabel drugCodeLabel;
	private JButton okButton;
	private JButton cancelButton;
	private String lastButtonPressed;

	/**
	 * Create the dialog.
	 */
	public DrugRemover(String drugCode, String drugName, double drugDosage) {
		setPreferredSize(new Dimension(330, 206));
		setSize(new Dimension(330, 206));
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(10, 10, 330, 206);
		setMinimumSize(new Dimension(330, 206));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lastButtonPressed = "";

		JLabel confirmation = new JLabel("Are you sure you wish to delete this entry?");
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);

		drugCodeLabel = new JLabel("Drug Code: " + drugCode);
		drugCodeLabel.setName("drugCodeLabel");

		drugNameLabel = new JLabel("Drug Name: " + drugName);
		drugNameLabel.setName("drugNameLabel");

		drugDosageLabel = new JLabel("Drug Dosage: " + drugDosage);
		drugDosageLabel.setName("drugDosageLabel");
		GroupLayout glContentPanel = new GroupLayout(contentPanel);
		glContentPanel.setHorizontalGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(glContentPanel.createSequentialGroup().addGap(5).addGroup(glContentPanel
						.createParallelGroup(Alignment.LEADING)
						.addComponent(confirmation, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
						.addComponent(drugCodeLabel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
						.addComponent(drugNameLabel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
						.addComponent(drugDosageLabel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE))));
		glContentPanel.setVerticalGroup(glContentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(glContentPanel.createSequentialGroup().addGap(6).addComponent(confirmation).addGap(23)
						.addComponent(drugCodeLabel).addGap(11).addComponent(drugNameLabel).addGap(11)
						.addComponent(drugDosageLabel)));
		contentPanel.setLayout(glContentPanel);

		JPanel buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(e -> {
			lastButtonPressed = okButton.getText();
			setVisible(false);
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

}
