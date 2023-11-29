package pLL;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bLL.PoemsBO;

class AddPoemsGUI extends JFrame {

    private static final long serialVersionUID = 1L;
	private JLabel titleLabel;
    private JTextField title;
    private JLabel verse1Label;
    private JTextField verse1;
    private JLabel verse2Label;
    private JTextField verse2;
    private JButton addVerse;
    private JButton save;
    private JButton returnButton;

    private PoemsBO poems;
    
    private static AddPoemsGUI instance;

    public static AddPoemsGUI getInstance(int bookId) {
        if (instance == null) {
            instance = new AddPoemsGUI(bookId);
        }
        return instance;
    }

    public AddPoemsGUI(int bookId) {
        poems = new PoemsBO();

        setSize(600, 400);
		setResizable(false); 
		getContentPane().setBackground(new Color(0x5E7986)); 
		setForeground(Color.WHITE); 

        this.setLayout(new BorderLayout());
        this.setTitle("Add New Poem");

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel versePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        buttonPanel.setBackground(new Color(0x5E7986)); 
        titlePanel.setBackground(new Color(0x5E7986)); 
        versePanel.setBackground(new Color(0x5E7986)); 
        titleLabel = new JLabel("Title");
        title = new JTextField(20);
        verse1Label = new JLabel("Verse 1");
        verse1 = new JTextField(20);
        verse2Label = new JLabel("Verse 2");
        verse2 = new JTextField(20);
        addVerse = createStyledButton("Add Verse");
        save = createStyledButton("Save");
        returnButton = createStyledButton("Back");
        
        titleLabel.setForeground(Color.WHITE);
        verse2Label.setForeground(Color.WHITE);
        verse1Label.setForeground(Color.WHITE);
        

        titlePanel.add(titleLabel);
        titlePanel.add(title);
        versePanel.add(verse1Label);
        versePanel.add(verse1);
        versePanel.add(verse2Label);
        versePanel.add(verse2);
        buttonPanel.add(addVerse);
        buttonPanel.add(save);
        buttonPanel.add(returnButton);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(versePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        addVerse.addActionListener(e -> {
            String verse1Text = verse1.getText();
            String verse2Text = verse2.getText();

            poems.addVerse(verse1Text, verse2Text);

            verse1.setText("");
            verse2.setText("");
        });

        save.addActionListener(e -> {
            String poemTitle = title.getText();
            title.setText(poemTitle);
            title.setEditable(false);

            int poemId = poems.addPoem(poemTitle, bookId);

            poems.assignPoemIdToVerses(poemId);

            addVerse.setEnabled(true);
            save.setEnabled(false);
        });

        returnButton.addActionListener(e -> {
            PoemsGUI poemTitlesGUI = new PoemsGUI(bookId);
            poemTitlesGUI.setVisible(true);
            dispose(); 
        });

        this.setVisible(false);
    }

    public void setGUIVisibility(boolean isVisible) {
        this.setVisible(isVisible);
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

