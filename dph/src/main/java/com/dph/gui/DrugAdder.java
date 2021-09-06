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
		setPreferredSize(new Dimension(337, 448));
		setSize(new Dimension(337, 448));
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
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		lastButtonPressed = "";
		
		drugCodeLabel = new JLabel("Drug Code:");
		drugCodeLabel.setName("drugCodeLabel");
		
		drugCodeText = new JTextField();
		drugCodeText.setName("drugCodeText");
		drugCodeText.setColumns(10);
		
		scrollPaneComment = new JScrollPane();
		scrollPaneComment.setName("scrollPanel");
		comment = new JTextPane();
		comment.setBackground(SystemColor.control);
		comment.setEditable(false);
		comment.setText(
				"Insert the code of the drug you wish to associate with the selected condition. If the code isn't in the database, you'll have to give the necessary data of the drug you wish to add.");
		comment.setName("comment");
		scrollPaneComment.setViewportView(comment);

		drugNameLabel = new JLabel("Drug Name:");
		drugNameLabel.setName("drugNameLabel");
		
		codeChecker = new JLabel(AWAITING_CHECK);
		codeChecker.setName("codeChecker");
		codeChecker.setHorizontalAlignment(SwingConstants.CENTER);
		
		drugNameText = new JTextField();
		drugNameText.setEnabled(false);
		drugNameText.setName("drugNameText");
		drugNameText.setColumns(10);
		
		drugDosageLabel = new JLabel("Drug Dosage:");
		drugDosageLabel.setName("drugDosageLabel");
		
		drugDosageText = new JTextField();
		drugDosageText.setName("drugDosageText");
		drugDosageText.setColumns(10);
		
		dosageChecker = new JLabel(AWAITING_CHECK);
		dosageChecker.setName("dosageChecker");
		dosageChecker.setHorizontalAlignment(SwingConstants.CENTER);
		
		nameChecker = new JLabel(AWAITING_CHECK);
		nameChecker.setName("nameChecker");
		nameChecker.setHorizontalAlignment(SwingConstants.CENTER);

		drugDescLabel = new JLabel("Drug Description:");
		drugDescLabel.setName("drugDescLabel");
		
		descChecker = new JLabel(AWAITING_CHECK);
		descChecker.setName("descChecker");
		descChecker.setHorizontalAlignment(SwingConstants.CENTER);

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
		
		JScrollPane scrollPaneDesc = new JScrollPane();
		scrollPaneDesc.setName("drugDescScroll");
		
		drugDescText = new JTextField();
		drugDescText.setEnabled(false);
		drugDescText.setName("drugDescText");
		scrollPaneDesc.setViewportView(drugDescText);
		drugDescText.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneComment, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(drugCodeText, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
								.addComponent(drugCodeLabel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGap(159)
									.addComponent(findButton, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)))
							.addGap(17)
							.addComponent(codeChecker, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
						.addComponent(drugNameLabel, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(drugNameText, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(nameChecker, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(drugDosageText, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
								.addComponent(drugDosageLabel, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))
							.addGap(10)
							.addComponent(dosageChecker, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
						.addComponent(drugDescLabel, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(scrollPaneDesc, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(descChecker, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(2)
					.addComponent(scrollPaneComment, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addGap(19)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(12)
							.addComponent(drugCodeText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(drugCodeLabel)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(11)
							.addComponent(findButton, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(15)
							.addComponent(codeChecker)))
					.addGap(10)
					.addComponent(drugNameLabel)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(drugNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(3)
							.addComponent(nameChecker)))
					.addGap(23)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(13)
							.addComponent(drugDosageText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(drugDosageLabel)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(16)
							.addComponent(dosageChecker)))
					.addGap(11)
					.addComponent(drugDescLabel)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneDesc, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(32)
							.addComponent(descChecker))))
		);
		contentPanel.setLayout(gl_contentPanel);

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
