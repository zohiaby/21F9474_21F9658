package pLL;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import bLL.BLLFascade;
import bLL.BooksBO;
import bLL.IBLLFascade;
import bLL.IBooks;
import bLL.PoemsBO;
import dAL.BooksDAO;
import dAL.DALFascade;
import dAL.IDALBooks;
import dAL.IDALFascade;

public class PoemsGUI extends JFrame {
	
	
	
	private static final long serialVersionUID = 1L;
    private JLabel titleLabel;
    private JTable poemTable; // Replace JList with JTable
    private JScrollPane scrollPane;
    private JButton viewButton;
    private JButton addPoemButton;
    private JButton deletePoemButton;
    private JButton updatePoemTitle;
    private JButton backButton;
    private AddPoemsGUI addPoemsGUI;

    private PoemsBO poems;
    
    private static PoemsGUI instance;


    public static PoemsGUI getInstance(int bookId) {
        if (instance == null) {
            instance = new PoemsGUI(bookId);
        }
        return instance;
    }


    public PoemsGUI(int bookId) {
        poems = new PoemsBO();

        this.setSize(600, 400);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0x5E7986));
        this.setForeground(Color.WHITE);

        this.setLayout(new BorderLayout());
        this.setTitle("Poems Library");

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        titlePanel.setBackground(new Color(0x5E7986));
        buttonPanel.setBackground(new Color(0x5E7986));

        titleLabel = new JLabel("Poem Titles");
        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Poem ID");
        tableModel.addColumn("Title");
        poemTable = new JTable(tableModel);
        poemTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scrollPane = new JScrollPane(poemTable);

        poemTable.getColumnModel().getColumn(0).setMinWidth(0);
        poemTable.getColumnModel().getColumn(0).setMaxWidth(0);
        poemTable.getColumnModel().getColumn(0).setWidth(0);
        
        viewButton = createStyledButton("View Poem");
        addPoemButton = createStyledButton("Add New Poem");
        deletePoemButton = createStyledButton("Delete Poem");
        updatePoemTitle = createStyledButton("Update Title");
        backButton = createStyledButton("Back");

        titlePanel.add(titleLabel);
        buttonPanel.add(viewButton);
        buttonPanel.add(addPoemButton);
        buttonPanel.add(deletePoemButton);
        buttonPanel.add(updatePoemTitle);
        buttonPanel.add(backButton);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        updatePoemList(bookId);

        poemTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewButton.setEnabled(true);
            }
            
        });
		
        backButton.addActionListener(e -> {
            setVisible(false);

            IBooks bookbll = new BooksBO();
            IDALBooks bookdal = new BooksDAO();
            IDALFascade dal = new DALFascade();
            IBLLFascade bll = new BLLFascade();

            dal.setIDALbook(bookdal);
            bll.setIBook(bookbll);
            bll.setIDAL(dal);


            SwingUtilities.invokeLater(() -> {
                BookUI.getInstance(bll);
            });
        });

		

		
		
		viewButton.addActionListener(e -> {
			setVisible(false);
		    int selectedRow = poemTable.getSelectedRow();

		    if (selectedRow != -1) {
		        int poemId = (int) poemTable.getValueAt(selectedRow, poemTable.getColumnModel().getColumnIndex("Poem ID"));
		        String selectedTitle = (String) poemTable.getValueAt(selectedRow, poemTable.getColumnModel().getColumnIndex("Title"));

		        System.out.println("Selected Title: " + selectedTitle + "  " + poemId);

		        JTable verseTable = poems.getPoemVerses(poemId);

	            if (verseTable != null) {
	                new VersesGUI(verseTable, bookId, poemId);
	            } else {
	                System.out.println("Verse Table is null.");
	            
	        }
		    } else {
		        System.out.println("No row selected.");
		    }
		});






		addPoemButton.addActionListener(e -> {
		    setVisible(false);
		    addPoemsGUI = new AddPoemsGUI(bookId);
		    addPoemsGUI.setVisible(true);
		});

		deletePoemButton.addActionListener(e -> {
		    int selectedRow = poemTable.getSelectedRow();

		    if (selectedRow != -1) {
		        int poemId = (int) poemTable.getValueAt(selectedRow, 0); 
		        poems.deletePoem(poemId);
		        updatePoemList(bookId);
		    } else {
		        System.out.println("No row selected.");
		    }
		});

		updatePoemTitle.addActionListener(e -> {
		    int selectedRow = poemTable.getSelectedRow();

		    if (selectedRow != -1) {
		        int poemId = (int) poemTable.getValueAt(selectedRow, 0); 
		        String oldTitle = (String) poemTable.getValueAt(selectedRow, 1); 

		        String newTitle = showUpdateTitleDialog(oldTitle);

		        if (newTitle != null && !newTitle.isEmpty()) {
		            poems.updatePoemTitle(poemId, newTitle);
		            updatePoemList(bookId);
		        } else {
		            System.out.println("Invalid new title. Operation canceled.");
		        }
		    } else {
		        System.out.println("No row selected. Operation canceled.");
		    }
		});







		this.setVisible(true);
	}

    private void updatePoemList(int bookId) {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Poem ID");
        tableModel.addColumn("Title");

        JTable displayedPoemTable = poems.getAllPoemTitles(bookId);

        for (int i = 0; i < displayedPoemTable.getRowCount(); i++) {
            Object poemId = displayedPoemTable.getValueAt(i, 0);
            Object title = displayedPoemTable.getValueAt(i, 1);
            tableModel.addRow(new Object[]{poemId, title});
        }

        this.poemTable.setModel(tableModel);

        TableColumn poemIdColumn = poemTable.getColumnModel().getColumn(0);
        poemIdColumn.setMinWidth(0);
        poemIdColumn.setMaxWidth(0);
        poemIdColumn.setWidth(0);
    }





	private JButton createStyledButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(90, 20));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setBackground(Color.WHITE);
		button.setForeground(new Color(0x5E7986));

		// Adding hover effect to buttons
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
	
	private String showUpdateTitleDialog(String oldTitle) {
	    JTextField newTitleField = new JTextField();
	    JButton saveButton = new JButton("Save");

	    JDialog dialog = new JDialog((JFrame) null, "Update Title", true); // true for modal

	    saveButton.addActionListener(e -> {
	        dialog.dispose();  // Close the dialog after saving
	    });

	    JPanel panel = new JPanel(new GridLayout(3, 2));
	    panel.add(new JLabel("New Title: "));
	    panel.add(newTitleField);
	    panel.add(saveButton);

	    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    dialog.getContentPane().add(panel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null); 
	    dialog.setVisible(true);

	    return newTitleField.getText();
	}





}


