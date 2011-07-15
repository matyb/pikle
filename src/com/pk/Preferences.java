/*
 * Created on Aug 1, 2004
 *
 */
package com.pk;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.pk.preferences.ExportPanel;
import com.pk.preferences.GeneralPanel;
import com.pk.preferences.PropertiesPanel;

/**
 * @author Chris Taylor
 * 
 */
public class Preferences extends JPanel implements TreeSelectionListener,
		ActionListener {
	
	private static final long serialVersionUID = 7690494394227269192L;
	public static int WIDTH = 340;
	public static int HEIGHT = 570;

	private JSplitPane jSplitPane = null;
	private JTree jTree = null;
	private Config config = null;
	private PropertiesPanel propertiesPanel = null;
	private ExportPanel exportPanel = null;
	private GeneralPanel generalPanel = null;
	private DefaultMutableTreeNode topDatasourceNode = null;
	private DefaultMutableTreeNode topNode = null;
	private DefaultMutableTreeNode exportPrefencesNode = null;
	private DefaultMutableTreeNode generalPrefencesNode = null;
	private JDialog jDialog = null;
	private ConnectionConfig selConnectionConfig;
	private JButton saveButton;
	private JButton closeButton;

	private class CloseAction extends AbstractAction {

		/**
         * 
         */
		private static final long serialVersionUID = 8735012374912583312L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent arg0) {
			jDialog.dispose();
		}
	}

	/**
	 * This is the default constructor
	 */
	public Preferences() {
		super();
		CloseAction closeAction = new CloseAction();
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"ESCAPE");
		getActionMap().put("ESCAPE", closeAction);

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void initialize() {
		this.setSize(Preferences.HEIGHT, Preferences.WIDTH);
		this.setLayout(new java.awt.BorderLayout());
		this.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		JPanel tmp = new JPanel();
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		tmp.add(saveButton);
		tmp.add(closeButton);
		this.add(tmp, BorderLayout.SOUTH);

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	// private javax.swing.JPanel getJContentPane() {
	// if(jContentPane == null) {
	// jContentPane = new javax.swing.JPanel();
	// jContentPane.setLayout(new java.awt.BorderLayout());
	// jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
	// }
	// return jContentPane;
	// }
	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			ScrollPane tmp = new ScrollPane();
			tmp.add(getJTree());
			jSplitPane.setLeftComponent(tmp);
			propertiesPanel = new PropertiesPanel(this);
			jSplitPane.setRightComponent(propertiesPanel);
			jSplitPane.setDividerLocation(165);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jTree
	 * 
	 * @return javax.swing.JTree
	 */
	private JTree getJTree() {
		if (jTree == null) {
			topNode = new DefaultMutableTreeNode("Config");
			generalPrefencesNode = new DefaultMutableTreeNode("General");
			topNode.add(generalPrefencesNode);
			exportPrefencesNode = new DefaultMutableTreeNode("Export");
			topNode.add(exportPrefencesNode);
			topDatasourceNode = new DefaultMutableTreeNode("Datasources");
			topNode.add(topDatasourceNode);

			DefaultMutableTreeNode tmp2 = null;
			List<ConnectionConfig> configs = config.getConnections();
			int size = configs.size();
			for (int x = 0; x < size; x++) {
				tmp2 = new DefaultMutableTreeNode(configs.get(x));
				topDatasourceNode.add(tmp2);
			}
			jTree = new JTree(topNode);
			jTree.addTreeSelectionListener(this);
		}
		return jTree;
	}

	/**
	 * @return Returns the configs.
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param configs
	 *            The configs to set.
	 */
	public void setConfig(Config argConfig) {
		this.config = argConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
		if (source == saveButton) {
			if (jSplitPane.getRightComponent() == propertiesPanel) {

				if (selConnectionConfig != null) {
					selConnectionConfig
							.setDatabaseDialectName((String) propertiesPanel
									.getTxtDialect().getSelectedItem());
					selConnectionConfig.setDriver(propertiesPanel
							.getTxtDriver().getText());
					selConnectionConfig.setName(propertiesPanel.getTxtName()
							.getText());
					selConnectionConfig.setUrl(propertiesPanel.getTxtURL()
							.getText());

				}

				config.writeFile();

				config.orderConnections();
				jTree = null;
				jTree = getJTree();
				jTree.expandPath(jTree.getPathForRow(3));
				JScrollPane scrollPane = new JScrollPane(jTree);
				getJSplitPane().setLeftComponent(scrollPane);

			} else if (jSplitPane.getRightComponent() == exportPanel) {
				config.setExportDelimiterChar(exportPanel
						.getTxtExportDelimiter().getText());
				config.setEncloseByChar(exportPanel.getTxtEncloseBy().getText());
				config.setBorderColor(exportPanel.getBorderColor().getText());
				config.setHeaderColor(exportPanel.getHeaderColor().getText());
				config.setAltRowColor(exportPanel.getAltRowColor().getText());
				config.setUseCdata(exportPanel.getUseCdata().isSelected());
				config.writeFile();
			} else if (jSplitPane.getRightComponent() == generalPanel) {
				config.setSelectedLookAndFeel((LookAndFeel) config
						.getLookandFeels().get(
								generalPanel.getLookAndFeelComboBox()
										.getSelectedIndex()));
				config.setHistoryMode(generalPanel.getSelectedHistoryMode());
				config.writeFile();
			}
		} else if (source == propertiesPanel.getNewButton()) {
			selConnectionConfig = new ConnectionConfig();
			config.addConnectionConfig(selConnectionConfig);
			selConnectionConfig.setName("NewConfig");
			topDatasourceNode.add(new DefaultMutableTreeNode(
					selConnectionConfig));
			propertiesPanel.getTxtDialect().setSelectedItem(
					selConnectionConfig.getDatabaseDialectName());
			propertiesPanel.getTxtDriver().setText(
					selConnectionConfig.getDriver());
			propertiesPanel.getTxtName().setText(selConnectionConfig.getName());
			propertiesPanel.getTxtURL().setText(selConnectionConfig.getUrl());
		} else if (source == propertiesPanel.getRemoveButton()) {
			config.removeConnection(selConnectionConfig);
			config.writeFile();
			propertiesPanel.getTxtDialect().setSelectedItem("");
			propertiesPanel.getTxtDriver().setText("");
			propertiesPanel.getTxtName().setText("");
			propertiesPanel.getTxtURL().setText("");
			jTree = null;
			jTree = getJTree();
			JScrollPane scrollPane = new JScrollPane(jTree);
			jTree.expandPath(jTree.getPathForRow(3));
			getJSplitPane().setLeftComponent(scrollPane);
		} else if (source == closeButton) {
			jDialog.setVisible(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent arg0) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree
				.getLastSelectedPathComponent();
		Object tmp = node.getUserObject();
		if (tmp instanceof ConnectionConfig) {
			if (jSplitPane.getRightComponent() != propertiesPanel) {
				jSplitPane.setRightComponent(propertiesPanel);
			}
			selConnectionConfig = (ConnectionConfig) tmp;
			propertiesPanel.getTxtDialect().setSelectedItem(
					selConnectionConfig.getDatabaseDialectName());
			propertiesPanel.getTxtDriver().setText(
					selConnectionConfig.getDriver());
			propertiesPanel.getTxtName().setText(selConnectionConfig.getName());
			propertiesPanel.getTxtURL().setText(selConnectionConfig.getUrl());
		} else if (node == exportPrefencesNode) {
			if (exportPanel == null) {
				exportPanel = new ExportPanel(this);
			}
			if (jSplitPane.getRightComponent() != exportPanel) {
				jSplitPane.setRightComponent(exportPanel);
				exportPanel.getTxtExportDelimiter().setText(
						config.getExportDelimiterChar());
				exportPanel.getTxtEncloseBy()
						.setText(config.getEncloseByChar());
				exportPanel.getBorderColor().setText(config.getBorderColor());
				exportPanel.getHeaderColor().setText(config.getHeaderColor());
				exportPanel.getAltRowColor().setText(config.getAltRowColor());
				exportPanel.getUseCdata().setSelected(config.isUseCdata());
			}

		} else if (node == generalPrefencesNode) {
			if (generalPanel == null) {
				generalPanel = new GeneralPanel(this);
				generalPanel.setLookandFeels(config);

			}
			generalPanel.setSelectedHistoryMode(config.getHistoryMode());
			generalPanel.getLookAndFeelComboBox().setSelectedItem(
					config.getSelectedLookAndFeel().getName());
			jSplitPane.setRightComponent(generalPanel);
		}

	}

	/**
	 * @param dialog
	 *            The jDialog to set.
	 */
	public void setJDialog(JDialog dialog) {
		jDialog = dialog;
	}
} // @jve:decl-index=0:visual-constraint="65,28"
