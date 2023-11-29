package pLL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import bLL.BLLFascade;
import bLL.BooksBO;
import bLL.IBLLFascade;
import bLL.IBooks;
import bLL.IRoot;
import bLL.RootBO;
import dAL.BooksDAO;
import dAL.DALFascade;
import dAL.IDALBooks;
import dAL.IDALFascade;
import dAL.IDALRoot;
import dAL.RootDAO;
import dAL.SinglePoemVIewDAO;
import transferObject.MainGUIDTO;
import transferObject.RootDTO;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());

	private JTextField searchField;
	private JTextArea bookDisplay;

	private JTable resultTable;
	private DefaultTableModel tableModel;
	private JPanel versePanel;
	private boolean navigationOccurred = false;

	/**
	 * Constructs the main GUI window for the application.
	 */
	public MainGUI() {
		bookDisplay = new JTextArea(); // Adding the J text area for the root search reslut

		// Initializing versePanel
		versePanel = new JPanel();
		versePanel.setLayout(new BoxLayout(versePanel, BoxLayout.Y_AXIS));
		versePanel.setBackground(new Color(0x5E7986));

		setTitle("كلمه كتاب");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setResizable(false);
		getContentPane().setBackground(new Color(0x5E7986));
		setForeground(Color.WHITE);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting look and feel", e);
		}
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1, 0, 10));
		buttonPanel.setBackground(new Color(0x5E7986));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		JButton addBookButton = createStyledButton("Book");
		JButton assignRootButton = createStyledButton("Assign Root");
		JButton addRootButton = createStyledButton("Root");
		JButton importPoemButton = createStyledButton("Import Poem");

		buttonPanel.add(addBookButton);
		buttonPanel.add(assignRootButton);
		buttonPanel.add(addRootButton);
		buttonPanel.add(importPoemButton);

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.setBackground(new Color(0x5E7986));
		searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

		JLabel titleLabel = new JLabel("كلمه كتاب");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(Color.WHITE);

		JPanel titlePanel = new JPanel();
		titlePanel.add(titleLabel);
		titlePanel.setBackground(new Color(0x5E7986));

		JPanel searchFieldPanel = new JPanel();
		searchFieldPanel.setLayout(new BorderLayout());
		searchFieldPanel.setBackground(new Color(0x5E7986));

		JLabel searchLabel = new JLabel("Search : ");
		searchLabel.setForeground(Color.WHITE);

		searchField = new JTextField(20);
		searchField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchField.getText().equals("Search :")) {
					searchField.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("Search :");
				}
			}
		});

		searchField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					searchRoots();
				}
			}
		});

		searchFieldPanel.add(searchLabel, BorderLayout.WEST);
		searchFieldPanel.add(searchField, BorderLayout.CENTER);

		// Initialize JTable and DefaultTableModel
		tableModel = new DefaultTableModel();
		resultTable = new JTable(tableModel);
		resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Set up the columns for your table (adjust as needed)
		tableModel.addColumn("Verse 1");
		tableModel.addColumn("Verse 2");
		tableModel.addColumn("ID");

		// Add the table to a JScrollPane
		JScrollPane tableScrollPane = new JScrollPane(resultTable);
		tableScrollPane.setPreferredSize(new Dimension(300, 400));

		// Add the JScrollPane to the main GUI
		add(tableScrollPane, BorderLayout.CENTER);

		hideColumn(resultTable, 2);

		searchField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					// Call searchRoots without arguments
					searchRoots();
				}
			}
		});

		add(buttonPanel, BorderLayout.WEST);
		searchPanel.add(titlePanel, BorderLayout.NORTH);
		searchPanel.add(searchFieldPanel, BorderLayout.CENTER);
		add(searchPanel, BorderLayout.NORTH);
		add(new JScrollPane(versePanel), BorderLayout.EAST);

		addBookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IBooks bookbll = new BooksBO();
				IDALBooks bookdal = new BooksDAO();
				IDALRoot root = new RootDAO();

				IDALFascade dal = new DALFascade();
				IBLLFascade bll = new BLLFascade();

				dal.setIDALbook(bookdal);

				bll.setIBook(bookbll);
				bll.setIDAL(dal);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						new BookUI(bll);
					}
				});
			}
		});

		assignRootButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = resultTable.getSelectedRow();
				if (selectedRow != -1) {
					String verse1 = (String) resultTable.getValueAt(selectedRow, 0);
					String verse2 = (String) resultTable.getValueAt(selectedRow, 1);
					String id = (String) resultTable.getValueAt(selectedRow, 2);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							new AssignRootPO(Integer.parseInt(id),verse1,verse2);
						}
					});
				}
			}

		});

		addRootButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						IRoot rootBO = new RootBO();
						RootDTO rootDTO = new RootDTO();

						IDALRoot root = new RootDAO();
						IDALFascade dalFascade = new DALFascade();
						dalFascade.setDTO(rootDTO);
						IBLLFascade bllFascade = new BLLFascade();

						dalFascade.setDALRoot(root);
						bllFascade.setBLLRoot(rootBO);

						bllFascade.setFascade(dalFascade);

						new RootGUI(bllFascade, rootDTO);
					}
				});
			}
		});

		importPoemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportPoemGUI.SelectPoem();
			}
		});

		setVisible(true);
	}

	private JButton createStyledButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(90, 20));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setBackground(Color.WHITE);
		button.setForeground(new Color(0x5E7986));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				button.setBackground(new Color(0x5E7986));
				button.setForeground(new Color(0x5E7986));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBorder(BorderFactory.createEmptyBorder());
				button.setBackground(Color.WHITE);
				button.setForeground(new Color(0x5E7986));
			}
		});

		return button;
	}

	private void searchRoots() {
		// Create MainGUIDTO instance with the search term
		MainGUIDTO mainGUIDTO = new MainGUIDTO(searchField.getText(), "");
		String searchTerm = mainGUIDTO.getSearchTerm();

		// Clearing the versePanel before populating it with new data
		versePanel.removeAll();

		SinglePoemVIewDAO verseSearchApp = new SinglePoemVIewDAO();
		ArrayList<String> verses = verseSearchApp.searchVersesList(searchTerm);

		// Clearing the table before populating it with new data
		tableModel.setRowCount(0);

		if (verses.isEmpty()) {
			// Displaying the message in the versePanel
			JTextArea noVersesMessage = new JTextArea("No verses found.");
			noVersesMessage.setEditable(false);
			noVersesMessage.setBackground(new Color(0x5E7986));
			noVersesMessage.setForeground(Color.WHITE);
			versePanel.add(noVersesMessage);
		} else {
			for (int i = 0; i < verses.size(); i++) {
				String verse = verses.get(i);

				// Spiting the verse into verse1 and verse2
				String[] verseParts = verse.split("\n");
				if (verseParts.length >= 3) {
					String verse1 = verseParts[0];
					String verse2 = verseParts[1];
					String poem_id = verseParts[2];

					tableModel.addRow(new Object[] { verse1, verse2, poem_id });
				}
			}
		}

		// Repaint the versePanel to reflect the changes
		versePanel.revalidate();
		// versePanel.repaint();
		resultTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int selectedRow = resultTable.getSelectedRow();
					if (selectedRow != -1 && !navigationOccurred) {
						String verse1 = (String) resultTable.getValueAt(selectedRow, 0);
						String verse2 = (String) resultTable.getValueAt(selectedRow, 1);
						String id = (String) resultTable.getValueAt(selectedRow, 2);

						// Update the MainGUIDTO instance with the selected id
						mainGUIDTO.setId(id);

						navigationOccurred = true;

						// Pass the MainGUIDTO instance to the viewPoemVersesGUI constructor
						navigateToViewPoemVerseScreen(verse1, verse2, mainGUIDTO);
					}
				}
			}
		});

	}

	/**
	 * Navigates to the view poem verse screen with the selected poem_id.
	 *
	 * @param verse1 The first verse.
	 * @param verse2 The second verse.
	 * @param id     The poem_id.
	 */
	private void navigateToViewPoemVerseScreen(String verse1, String verse2, MainGUIDTO mainGUIDTO) {
		try {
			LOGGER.log(Level.INFO, "Navigating to view poem verse screen for verses: {0}, {1}, {2}",
					new Object[] { verse1, verse2, mainGUIDTO.getId() });

			setVisible(false);
			ViewPoemVersesGUI viewPoemVersesGUI = new ViewPoemVersesGUI(mainGUIDTO);
			viewPoemVersesGUI.addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosed(java.awt.event.WindowEvent windowEvent) {
					setVisible(true);
					navigationOccurred = false;
				}
			});
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error navigating to view poem verse screen", e);
		}
	}

	// hiding the column for poem id
	private void hideColumn(JTable table, int columnIndex) {
		TableColumnModel tcm = table.getColumnModel();
		TableColumn column = tcm.getColumn(columnIndex);
		column.setMinWidth(0);
		column.setMaxWidth(0);
		column.setWidth(0);
		column.setPreferredWidth(0);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainGUI();
			}
		});
	}
}
