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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
		setPreferredSize(new Dimension(330, 206));
		setSize(new Dimension(330, 206));
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(10, 10, 330, 206);
		setMinimumSize(new Dimension(330,206));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("contentPanel");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lastButtonPressed="";
		
		JLabel confirmation = new JLabel("Are you sure you wish to delete this entry?");
		confirmation.setName("comment");
		confirmation.setHorizontalAlignment(SwingConstants.CENTER);

		conditionCodeLabel = new JLabel("Condition Code: " + conditionCode);
		conditionCodeLabel.setName("conditionCodeLabel");

		conditionNameLabel = new JLabel("Condition Name: " + conditionName);
		conditionNameLabel.setName("conditionNameLabel");
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(48)
					.addComponent(confirmation))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(conditionCodeLabel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(conditionNameLabel, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addComponent(confirmation)
					.addGap(21)
					.addComponent(conditionCodeLabel)
					.addGap(11)
					.addComponent(conditionNameLabel))
		);
		contentPanel.setLayout(gl_contentPanel);

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
