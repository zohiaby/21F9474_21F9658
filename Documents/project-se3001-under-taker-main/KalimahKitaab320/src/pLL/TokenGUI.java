package pLL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bLL.TokenBO;

/**
 * TokenGUI class represents the graphical user interface for tokenizing verses.
 */
public class TokenGUI {
	private static final Logger logger = Logger.getLogger(TokenGUI.class.getName());

	private JFrame frame;
	private JTable verseTable;
	private DefaultTableModel verseTableModel;
	private JButton displayVersesButton;
	private JButton tokenizeButton;
	private JTable tokenTable;
	private DefaultTableModel tokenTableModel;
	private JTextField editTokenField;
	private JButton backButton;
	private int poemId;

	private TokenBO tokenBO;
	public int verseId;
	int bookId;

	/**
	 * Constructor for TokenGUI.
	 *
	 * @param tokenBO The TokenBO instance for business logic operations.
	 * @param verseId The ID of the verse to be processed.
	 */
	public TokenGUI(TokenBO tokenBO, int verseId, int poemId, int bookId) {
		this.tokenBO = tokenBO;
		this.verseId = verseId;
		this.poemId = poemId;
		this.bookId = bookId; 
		initialize();
	}

	/**
	 * Initializes the TokenGUI, setting up the graphical user interface.
	 */
	private void initialize() {
		setupLogger();

		frame = new JFrame("Verse Tokenizer App");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		verseTableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "Verse 1", "Verse 2" });
		verseTable = new JTable(verseTableModel);
		JScrollPane verseScrollPane = new JScrollPane(verseTable);
		verseScrollPane.setBounds(10, 11, 414, 120);
		frame.getContentPane().add(verseScrollPane);

		displayVersesButton = new JButton("Display Verses");
		displayVersesButton.setBounds(10, 142, 150, 23);
		frame.getContentPane().add(displayVersesButton);
		displayVersesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayVerses();
			}
		});

		tokenizeButton = new JButton("Tokenize");
		tokenizeButton.setBounds(170, 142, 150, 23);
		frame.getContentPane().add(tokenizeButton);
		tokenizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tokenizeVerses();
			}
		});

		tokenTableModel = new DefaultTableModel();
		tokenTable = new JTable(tokenTableModel);
		JScrollPane tokenScrollPane = new JScrollPane(tokenTable);
		tokenScrollPane.setBounds(10, 180, 414, 200);
		frame.getContentPane().add(tokenScrollPane);

		tokenTable.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				try {
					int row = tokenTable.rowAtPoint(evt.getPoint());
					int col = tokenTable.columnAtPoint(evt.getPoint());
					if (row >= 0 && col >= 0) {
						editTokenField.setText((String) tokenTable.getValueAt(row, col));
						logger.info("Token selected: " + editTokenField.getText());
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Error in mouseClicked", e);
				}
			}
		});

		editTokenField = new JTextField();
		editTokenField.setBounds(10, 400, 150, 20);
		frame.getContentPane().add(editTokenField);

		JButton editTokenButton = new JButton("Edit Token");
		editTokenButton.setBounds(170, 400, 150, 23);
		frame.getContentPane().add(editTokenButton);
		editTokenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editToken();
			}
		});

		backButton = new JButton("Back");
		backButton.setBounds(10, 450, 100, 23);
		frame.getContentPane().add(backButton);
		backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                VersesGUI versesGUI = VersesGUI.getInstance(verseTable, bookId, poemId);
                logger.info("Back button pressed. Returning to VersesGUI.");
            }
        });

		frame.setVisible(true);
	}

	/**
	 * Set up the logger for TokenGUI.
	 */
	private void setupLogger() {
		try {
			FileHandler fileHandler = new FileHandler("tokenGUI.log");
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
			logger.setLevel(Level.INFO);
			logger.info("TokenGUI logging initialized.");

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error setting up logging for TokenGUI", e);
		}
	}

	/**
	 * Displays verses in the table.
	 */
	private void displayVerses() {
		try {
			List<String> versesData = tokenBO.getVersesData(verseId);
			verseTableModel.setRowCount(0);
			if (versesData.size() >= 2) {
				verseTableModel.addRow(new Object[] { versesData.get(0), versesData.get(1) });
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in displayVerses", e);
		}
	}

	/**
	 * Tokenizes verses and displays the tokens.
	 */
	private void tokenizeVerses() {
		try {
			tokenBO.tokenizeVerses(verseId);
			displayTokens();
			logger.info("Verses tokenized successfully.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in tokenizeVerses", e);
		}
	}

	/**
	 * Displays tokens in the table.
	 */
	private void displayTokens() {
		try {
			List<String> tokens = tokenBO.getTokens(verseId);
			tokenTableModel.setDataVector(convertListToDataArray(tokens), new Object[] { "Token" });
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in displayTokens", e);
		}
	}

	/**
	 * Edits the selected token.
	 */
	private void editToken() {
		try {
			
			String selectedToken = editTokenField.getText();
			String newTokenText = editTokenField.getText();

			String[] newTokens = newTokenText.split("\\s+");
			for (String newToken : newTokens) {
				tokenBO.saveToken(verseId, newToken, tokenBO.getPOSTagForToken(newToken));
			}
			int selectedTokenId = tokenBO.getTokenId(verseId, selectedToken);
			tokenBO.deleteToken(selectedTokenId);

			displayTokens();
			logger.info("Token updated successfully.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error in editToken", e);
		}
	}

	/**
	 * Converts a list of strings to a two-dimensional array for the table model.
	 *
	 * @param data The list of strings to convert.
	 * @return The two-dimensional array.
	 */
	private Object[][] convertListToDataArray(List<String> data) {
		Object[][] dataArray = new Object[data.size()][1];
		for (int i = 0; i < data.size(); i++) {
			dataArray[i][0] = data.get(i);
		}
		return dataArray;
	}
}
