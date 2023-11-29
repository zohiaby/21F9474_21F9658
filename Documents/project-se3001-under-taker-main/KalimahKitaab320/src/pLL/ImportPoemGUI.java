package pLL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dAL.ImportPoemDAO;

public class ImportPoemGUI {
    

    public static void SelectPoem() {
        JFrame frame = new JFrame("Poem Added Notification");
        ImportPoemDAO poemParser = new ImportPoemDAO();

		JButton addButton = createStyledButton("Add Poem");

       
        addButton.setBounds(50, 50, 100, 30);

        // Adding an action listener to the button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                
                // Setting the initial directory to "D:" drive
                File initialDirectory = new File("D:");
                fileChooser.setCurrentDirectory(initialDirectory);

                // Showing the file selection dialog
                int returnValue = fileChooser.showOpenDialog(null);
                
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        String filePath = selectedFile.getCanonicalPath();

                        // Checking if the selected file is a TXT file
                        if (filePath.toLowerCase().endsWith(".txt")) {
                            int confirmationResult = JOptionPane.showConfirmDialog(frame, "Confirm the File Path: " + filePath, "Confirm File Path", JOptionPane.YES_NO_OPTION);

                            if (confirmationResult == JOptionPane.YES_OPTION) {
                                boolean poemsAdded = poemParser.parsePoem(filePath);

                                // Displaying a success message
                                if (poemsAdded) {
                                    JOptionPane.showMessageDialog(frame, "Poems have been added! Path: " + filePath);
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "Operation canceled by the user.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "File path must end with .txt");
                        }

                    } catch (IOException e1) {
                        // Handling and printing any file-related errors
                        e1.printStackTrace();
                    }
                } else {
                	// If no file was selected
                    JOptionPane.showMessageDialog(frame, "No file was selected.");
                }
            }
        });

        frame.add(addButton);

        // Adjusting frame properties
        frame.setSize(600, 400);
        frame.setResizable(false); // Make the frame non-resizable
		frame.getContentPane().setBackground(new Color(0x5E7986)); // Set the background color to #5E7986
		frame.setForeground(Color.WHITE); // Set text color to white

        frame.setLayout(null);
        frame.setVisible(true);
    }
    private static JButton createStyledButton(String label) {
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