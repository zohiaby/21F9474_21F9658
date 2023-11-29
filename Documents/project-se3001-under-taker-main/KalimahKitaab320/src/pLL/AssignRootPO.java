package pLL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import bLL.IUpdateRootBo;
import bLL.PoemsBO;
import bLL.UpdateRootBO;
import transferObject.PoemsTO;

/**
 * This class represents the AssignRootPO frame for assigning or updating roots.
 */
public class AssignRootPO extends JFrame {
	private static final Logger LOGGER = Logger.getLogger(AssignRootPO.class.getName());

	private JTextField misra1TextField;
	private JTextField misra2TextField;
	private JTable versesTable;
	private JLabel misra1;
	private JLabel misra2;
	private DefaultTableModel versesTableModel;
	private DefaultTableModel rootsTableModel;
	private JButton rootGeneratorButton;
	private JButton rootUpdateButton;
	private PoemsTO poemDTO;
	private PoemsBO poemobj;
	private UpdateRootBO businessFascade;
	private List<String[]> data;
	private int PoemID;
	private String selectedMisra1;
	private String selectedMisra2;
	private JTable suggestedRootsJTable;
	private JButton backButton;

	/**
	 * Constructor for AssignRootPO class.
	 * 
	 * @param currentPoemId The current poem ID.
	 * @param misra11       The first misra.
	 * @param misra12       The second misra.
	 */
	public AssignRootPO(int currentPoemId, String misra11, String misra12) {
		this.PoemID = currentPoemId;
		setTitle("Assign/Update Roots");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		poemDTO = new PoemsTO();
		setResizable(false);
		getContentPane().setBackground(Color.WHITE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
		poemobj = new PoemsBO();
		// mainPanel.setBorder(BorderFactory.createTitledBorder("Main Panel"));

		misra1TextField = new JTextField(10);
		misra2TextField = new JTextField(10);

		misra1 = new JLabel("Misra 1");
		misra2 = new JLabel("Misra 2");

		misra1TextField.setEditable(true);
		misra2TextField.setEditable(true);

		JPanel misraPanel = new JPanel(new FlowLayout());
		misraPanel.setBackground(Color.WHITE);
		// misraPanel.setBorder(BorderFactory.createTitledBorder("Misra Panel"));

		misraPanel.add(misra1);
		misraPanel.add(misra1TextField);
		misraPanel.add(misra2);
		misraPanel.add(misra2TextField);

		JPanel versesPanel = new JPanel(new FlowLayout());
		versesPanel.setPreferredSize(new Dimension(450, 380));
		versesPanel.setBackground(Color.WHITE);
		// versesPanel.setBorder(BorderFactory.createTitledBorder("Verse Panel"));

		versesTableModel = new DefaultTableModel();
		versesTableModel.addColumn("Poem");
		versesTableModel.addColumn("Misra 1");
		versesTableModel.addColumn("Misra 2");

		// Populating sample data for verses (replace this with your actual data)
		ArrayList<String> verses = new ArrayList<>();
		verses.add("Verse 1");
		verses.add("Verse 2");
		verses.add("Verse 3");

		for (String verse : verses) {
			versesTableModel.addRow(new Object[] { verse, verse, verse });
		}

		versesTable = new JTable(versesTableModel);
		versesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane versesScrollPane = new JScrollPane(versesTable);
		versesScrollPane.setPreferredSize(new Dimension(450, 380));
		versesPanel.add(versesScrollPane);

		// Adding the panel with the SR and Root columns
		JPanel rootsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rootsPanel.setPreferredSize(new Dimension(310, 380)); // Set the preferred width and length
		rootsPanel.setBackground(Color.WHITE);

		// rootsPanel.setBorder(BorderFactory.createTitledBorder("Root Panel"));
		rootsTableModel = new DefaultTableModel();
		rootsTableModel.addColumn("SR");
		rootsTableModel.addColumn("Root");

		JTable rootsTable = new JTable(rootsTableModel);
		rootsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane rootsScrollPane = new JScrollPane(rootsTable);
		rootsScrollPane.setPreferredSize(new Dimension(310, 380)); // Set the preferred size for the JScrollPane
		rootsPanel.add(rootsScrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
		buttonPanel.setBackground(Color.WHITE);

		rootGeneratorButton = createStyledButton("Root Generator");
		rootUpdateButton = createStyledButton("Root Update");
		backButton = createStyledButton("Back");
		buttonPanel.add(rootGeneratorButton);
		buttonPanel.add(rootUpdateButton);

		JPanel textFieldsPanel = new JPanel(new GridLayout(3, 3));
		textFieldsPanel.setBackground(Color.WHITE);
		textFieldsPanel.setBounds(300, 450, 250, 90);
		// textFieldsPanel.setBorder(BorderFactory.createTitledBorder("Misra Panel"));
		textFieldsPanel.add(misra1);
		textFieldsPanel.add(misra1TextField);
		textFieldsPanel.add(misra2);
		textFieldsPanel.add(misra2TextField);

		mainPanel.add(textFieldsPanel, BorderLayout.SOUTH);
		mainPanel.add(versesPanel, BorderLayout.WEST);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
		// mainPanel.add(misraPanel, BorderLayout.CENTER);
		mainPanel.add(rootsPanel, BorderLayout.EAST);
		IUpdateRootBo roots1 = null;
		businessFascade = new UpdateRootBO(roots1);
		data = new ArrayList<>();

		String[] columnsForToken = { "Sr.", "Roots" };
		Object[][] dataForTokens = null;

		suggestedRootsJTable = new JTable();
		JScrollPane suggestScrollPane = new JScrollPane(suggestedRootsJTable);
		suggestScrollPane.setPreferredSize(new Dimension(310, 380)); // Set the preferred size for the JScrollPane
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(suggestScrollPane, BorderLayout.CENTER);
		suggestedRootsJTable.setModel(new DefaultTableModel(dataForTokens, columnsForToken));
		suggestedRootsJTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));

		// Add containerPanel to your mainPanel or frame instead of adding
		// suggestedRootsJTable directly
		mainPanel.add(containerPanel, BorderLayout.EAST);

		// Add action listener to rootGeneratorButton
		rootGeneratorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = versesTable.getSelectedRow();
				if (selectedRow != -1) {
					poemDTO.setPoem_id(currentPoemId);
					selectedMisra1 = misra1TextField.getText();
					selectedMisra2 = misra2TextField.getText();
					businessFascade.checkInsertionOrNot(poemDTO, selectedMisra1, selectedMisra2);

					splitProcessOnPL(poemDTO, columnsForToken);

					List<String[]> data = poemobj.getPoemContentByID(poemDTO.getPoem_id());
					rootsTableModel.setRowCount(0);

					for (String[] rowData : data) {
						rootsTableModel.addRow(new Object[] { rowData, selectedMisra1, selectedMisra2 });
					}
				}
			}
		});

		rootUpdateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (selectedMisra1.isEmpty() || selectedMisra2.isEmpty()) {
					JOptionPane.showMessageDialog(new JFrame(), "Failure! Input Fields are Empty", "Save Words Failure",
							JOptionPane.ERROR_MESSAGE);
				} else {

					splitProcessOnPL(poemDTO, columnsForToken);

					businessFascade.saveWordsForRoots(poemDTO);

					JOptionPane.showMessageDialog(new JFrame(), "Success! Tokens are Assigned to Verses Successfully",
							"Success:", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		// Add selection listener to copy data to text fields on row selection
		versesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = versesTable.getSelectedRow();
					if (selectedRow != -1) {
						String selectedMisra1 = (String) versesTableModel.getValueAt(selectedRow, 0);
						String selectedMisra2 = (String) versesTableModel.getValueAt(selectedRow, 1);

						misra1TextField.setText(selectedMisra1);
						misra2TextField.setText(selectedMisra2);
					}
				}
			}
		});

		backButton.addActionListener(e -> {
			setVisible(false);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new MainGUI();
				}
			});
		});

		setLocationRelativeTo(null);
		add(mainPanel);
		setVisible(true);

		// Log initialization
		LOGGER.log(Level.INFO, "AssignRootPO initialized.");
	}

	/**
	 * Creates and styles a JButton.
	 * 
	 * @param label The label of the button.
	 * @return The created JButton.
	 */
	private JButton createStyledButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(120, 30));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setBackground(Color.WHITE);
		button.setForeground(Color.BLACK);

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				button.setBackground(Color.BLACK);
				button.setForeground(Color.WHITE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBorder(BorderFactory.createEmptyBorder());
				button.setBackground(Color.WHITE);
				button.setForeground(Color.BLACK);
			}
		});

		return button;
	}

	/**
	 * Splits the process on PL and updates suggested roots.
	 * 
	 * @param poemT           The PoemsTO object.
	 * @param columnsForToken The columns for the tokens.
	 * @return The list of tokens.
	 */
	public ArrayList<String> splitProcessOnPL(PoemsTO poemT, String[] columnsForToken) {
		poemT.setPoem_id(PoemID);

		poemT.setMisra_1(selectedMisra1);
		poemT.setMisra_2(selectedMisra1);

		ArrayList<String> tokens = businessFascade.suggestRoots(poemT);

		Object[][] twoDArray = new Object[tokens.size()][2];

		for (int i = 0; i < tokens.size(); i++) {
			twoDArray[i][0] = (i + 1);
			twoDArray[i][1] = tokens.get(i);
		}

		suggestedRootsJTable.setModel(new DefaultTableModel(twoDArray, columnsForToken));

		return tokens;
	}
}
