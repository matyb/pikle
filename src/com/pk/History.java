package com.pk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * Created on Mar 14, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Isabelle
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class History extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2626675347388205019L;
	private JTable table = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private MyInternalFrame parentFram = null;
	private JDialog jDialog = null;
	private JTextArea sqlTextArea = null;

	private class CloseAction extends AbstractAction {
		private static final long serialVersionUID = -2872623569240568050L;

		public void actionPerformed(ActionEvent arg0) {
			jDialog.dispose();
		}
	}

	public History(MyInternalFrame argMif, JDialog argJDialog) {
		parentFram = argMif;
		jDialog = argJDialog;

		CloseAction closeAction = new CloseAction();
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
		getActionMap().put("ESCAPE", closeAction);

		String tmp = null;
		List<String> dataList = new ArrayList<String>();
		for (int x = parentFram.getSqls().size() - 1; x >= 0; x--) {
			tmp = (String) parentFram.getSqls().get(x);
			if (tmp != null && tmp.length() > 0) {
				dataList.add(tmp);
			}
		}
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		HistoryTableModel historyTableModel = new HistoryTableModel();
		historyTableModel.setStatementHistory(dataList);
		table = new JTable(historyTableModel);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						// Ignore extra messages.
						if (e.getValueIsAdjusting())
							return;

						ListSelectionModel lsm = (ListSelectionModel) e
								.getSource();
						if (!lsm.isSelectionEmpty()) {
							sqlTextArea.setText((String) table.getValueAt(
									table.getSelectedRow(),
									table.getSelectedColumn()));
						}
					}
				});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.ipady = 40;
		gridBagConstraints.weightx = 3.0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		add(scrollPane, gridBagConstraints);

		JLabel tmpJLabel = new JLabel("Preview");
		// tmpJLabel.setBorder(new LineBorder(Color.black));
		tmpJLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tmpJLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.ipadx = 50;
		gridBagConstraints.ipady = 40;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		add(tmpJLabel, gridBagConstraints);

		sqlTextArea = new JTextArea(10, 45);
		gridBagConstraints = new GridBagConstraints();
		sqlTextArea.setEditable(false);
		JScrollPane sqlScrollPane = new JScrollPane(sqlTextArea);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 40;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		add(sqlScrollPane, gridBagConstraints);

		JPanel buttonPanel = new JPanel(new BorderLayout(0, 2));

		okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setMnemonic(KeyEvent.VK_K);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setMnemonic(KeyEvent.VK_C);

		buttonPanel.add(okButton, BorderLayout.NORTH);
		buttonPanel.add(cancelButton, BorderLayout.SOUTH);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.gridwidth = 1;
		add(buttonPanel, gridBagConstraints);

	}

	public void test() {
		UIManager.getDefaults();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(okButton)) {
			if (table.getSelectedRow() > -1 && table.getSelectedColumn() > -1
					&& parentFram != null) {
				System.out.print(table.getValueAt(table.getSelectedRow(),
						table.getSelectedColumn()));
				parentFram.sqlTextArea.setText((String) table.getValueAt(
						table.getSelectedRow(), table.getSelectedColumn()));
			}

		}
		jDialog.dispose();
	}
}
