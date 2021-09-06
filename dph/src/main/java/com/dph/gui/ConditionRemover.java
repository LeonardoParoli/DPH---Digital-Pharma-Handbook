package com.dph.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConditionRemover extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private String lastButtonPressed;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel conditionNameLabel;
	private JLabel conditionCodeLabel;

	/**
	 * Create the dialog.
	 */
	public ConditionRemover(String conditionCode, String conditionName) {
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
		confirmation.setName("comment");
		confirmation.setBounds(53, 10, 207, 14);
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(confirmation);

		conditionCodeLabel = new JLabel("Condition Code: " + conditionCode);
		conditionCodeLabel.setName("conditionCodeLabel");
		conditionCodeLabel.setBounds(10, 45, 294, 14);
		contentPanel.add(conditionCodeLabel);

		conditionNameLabel = new JLabel("Condition Name: " + conditionName);
		conditionNameLabel.setName("conditionNameLabel");
		conditionNameLabel.setBounds(10, 70, 294, 14);
		contentPanel.add(conditionNameLabel);

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
