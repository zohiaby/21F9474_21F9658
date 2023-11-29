package pLL;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bLL.PoemsBO;
import bLL.TokenBO;

public class VersesGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
    private DefaultTableModel tableModel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton backButton;
    private JButton tokenizeButton;
    private PoemsBO poems;
    private static VersesGUI instance;

    
    public static VersesGUI getInstance(JTable verseTable, int bookId,int poemId) {
        if (instance == null) {
            instance = new VersesGUI(verseTable, bookId, poemId);
        }
        return instance;
    }

    public VersesGUI(JTable verseTable, int bookId,int poemId) {
        poems = new PoemsBO();
        
        setResizable(false);
        getContentPane().setBackground(new Color(0x5E7986));
        setForeground(Color.WHITE);

        
        setTitle("Poem Verses Table");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int verseIdColumnIndex = 0;
        int verse1ColumnIndex = 1;
        int verse2ColumnIndex = 2;

        tableModel = new DefaultTableModel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Verse ID");
        tableModel.addColumn("Verse 1");
        tableModel.addColumn("Verse 2");

        for (int row = 0; row < verseTable.getRowCount(); row++) {
            Vector<Object> rowData = new Vector<>();
            for (int col = 0; col < verseTable.getColumnCount(); col++) {
                rowData.add(verseTable.getValueAt(row, col));
            }
            tableModel.addRow(rowData);
        }
        table = new JTable(tableModel) {
            private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.getColumnModel().getColumn(verseIdColumnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(verseIdColumnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(verseIdColumnIndex).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);

        editButton = createStyledButton("Edit");
        deleteButton = createStyledButton("Delete");
        addButton = createStyledButton("Add");
        backButton = createStyledButton("Back");
        tokenizeButton = createStyledButton("Tokenize");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);
        buttonPanel.add(tokenizeButton);

        tokenizeButton.addActionListener(e -> {
        	setVisible(false);
        	TokenBO tokenBo = new TokenBO();
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int verseId = (int) table.getValueAt(selectedRow, verseIdColumnIndex);

                TokenGUI tokenGui = new TokenGUI(tokenBo, verseId,poemId, bookId);
            }
        });

        backButton.addActionListener(e -> {
            setVisible(false);
            PoemsGUI poems = new PoemsGUI(bookId);
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int verseId = (int) table.getValueAt(selectedRow, verseIdColumnIndex);
                String verse1 = (String) table.getValueAt(selectedRow, verse1ColumnIndex);
                String verse2 = (String) table.getValueAt(selectedRow, verse2ColumnIndex);

                String newVerse1 = JOptionPane.showInputDialog("Enter new Verse 1:", verse1);
                String newVerse2 = JOptionPane.showInputDialog("Enter new Verse 2:", verse2);

                if (newVerse1 != null && newVerse2 != null) {
                    poems.updateVerse(verse1, verse2, newVerse1, newVerse2);
                    tableModel.setValueAt(newVerse1, selectedRow, verse1ColumnIndex);
                    tableModel.setValueAt(newVerse2, selectedRow, verse2ColumnIndex);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int verseId = (int) table.getValueAt(selectedRow, verseIdColumnIndex);
                poems.deleteVerse(verseId,poemId);
                tableModel.removeRow(selectedRow);
            }
        });

        addButton.addActionListener(e -> {
            JFrame inputFrame = new JFrame("Enter New Verse");
            inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            inputFrame.setSize(300, 150);
            inputFrame.setLayout(new GridLayout(3, 2));

            JTextField verse1Field = new JTextField();
            JTextField verse2Field = new JTextField();
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            inputFrame.add(new JLabel("Verse 1:"));
            inputFrame.add(verse1Field);
            inputFrame.add(new JLabel("Verse 2:"));
            inputFrame.add(verse2Field);
            inputFrame.add(okButton);
            inputFrame.add(cancelButton);

            okButton.addActionListener(n -> {
                String newVerse1 = verse1Field.getText();
                String newVerse2 = verse2Field.getText();

                if (!newVerse1.isEmpty() && !newVerse2.isEmpty()) {
                    poems.addVerseToPoem(newVerse1, newVerse2, poemId); // Use the poemId
                    int lastInsertedVerseId = poems.getLastInsertedVerseId();

                    Object[] newRowData = {lastInsertedVerseId, newVerse1, newVerse2};
                    tableModel.addRow(newRowData);
                }

                inputFrame.dispose();
            });

            cancelButton.addActionListener(n -> {
                inputFrame.dispose();
            });

            inputFrame.setVisible(true);
        });

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setLocation(0,0);
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
}

