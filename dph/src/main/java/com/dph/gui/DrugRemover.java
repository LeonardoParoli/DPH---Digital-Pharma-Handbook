package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Font;

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
		setAlwaysOnTop(true);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(10, 10, 330, 206);
		setMinimumSize(new Dimension(330,206));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		lastButtonPressed="";
		
		JLabel confirmation = new JLabel("Are you sure you wish to delete this entry?");
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);
		confirmation.setBounds(10, 11, 294, 14);
		contentPanel.add(confirmation);

		drugCodeLabel = new JLabel("Drug Code: " + drugCode);
		drugCodeLabel.setBounds(10, 48, 294, 14);
		drugCodeLabel.setName("drugCodeLabel");
		contentPanel.add(drugCodeLabel);

		drugNameLabel = new JLabel("Drug Name: " + drugName);
		drugNameLabel.setBounds(10, 73, 294, 14);
		drugNameLabel.setName("drugNameLabel");
		contentPanel.add(drugNameLabel);

		drugDosageLabel = new JLabel("Drug Dosage: " + drugDosage);
		drugDosageLabel.setBounds(10, 98, 294, 14);
		drugDosageLabel.setName("drugDosageLabel");
		contentPanel.add(drugDosageLabel);

		JPanel buttonPane = new JPanel();
		buttonPane.setName("buttonPanel");
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
	
}
