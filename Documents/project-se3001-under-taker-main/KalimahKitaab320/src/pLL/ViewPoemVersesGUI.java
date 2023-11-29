package pLL;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dAL.VerseDAO;
import transferObject.MainGUIDTO;

@SuppressWarnings("serial")
public class ViewPoemVersesGUI extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(ViewPoemVersesGUI.class.getName());

    private JTable table;
    private DefaultTableModel tableModel;
    private VerseDAO verse;

    /**
     * Constructs a ViewPoemVersesGUI window to display verses for a given poem.
     *
     * @param poemIdDTO The object containing the poem ID and verses.
     */
    public ViewPoemVersesGUI(MainGUIDTO mainGUIDTO) {
        setTitle("View Poem Verses");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initializing the table and table model
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Set up the columns for your table (adjust as needed)
        tableModel.addColumn("Verse 1");
        tableModel.addColumn("Verse 2");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Adding a back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handling back button click (you may need to customize this part)
                dispose(); // Close the current window
            }
        });

        // Creating a panel for the back button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

        // Creating an instance of the VerseRepository implementation
        verse = new VerseDAO();

        // Fetching verses from the repository and populate the table
        try {
            List<String[]> verses = verse.getVersesByPoemId(mainGUIDTO.getId());
            for (String[] verse : verses) {
                tableModel.addRow(verse);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching verses from the repository", e);
            // Optionally display an error message to the user
            JOptionPane.showMessageDialog(this, "Error fetching verses. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
