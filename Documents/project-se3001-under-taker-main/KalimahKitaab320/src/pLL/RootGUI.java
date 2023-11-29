package pLL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bLL.IBLLFascade;
import transferObject.RootDTO;

public class RootGUI {
	private JFrame frame;
	private JTextField rootField;
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton viewButton;
	private IBLLFascade fascade;
	private JButton getRootIdButton;
	private RootDTO rootDTO;

	//Constructor
	public RootGUI(IBLLFascade fascade, RootDTO rootDTO) {
		//Injection
		this.fascade = fascade;
		this.rootDTO = rootDTO;
		frame = new JFrame("Root Management");
		frame.setSize(600, 400);
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(0x5E7986));
		frame.setForeground(Color.WHITE);

		frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());
		frame.getContentPane().setBackground(new Color(0x5E7986));

		 JLabel label = new JLabel("Root:");
		rootField = new JTextField(55);
		label.setForeground(Color.WHITE);
		
		
		//Buttons Initialation
		getRootIdButton = createStyledButton("Get Root ID");
		addButton = createStyledButton("Add");
		updateButton = createStyledButton("Update");
		deleteButton = createStyledButton("Delete");
		viewButton = createStyledButton("View");

		// Set colors for buttons
		addButton.setBackground(new Color(0, 128, 0)); // Green
		addButton.setForeground(Color.WHITE);
		updateButton.setBackground(new Color(0, 0, 128)); // Blue
		updateButton.setForeground(Color.WHITE);
		deleteButton.setBackground(new Color(128, 0, 0)); // Red
		deleteButton.setForeground(Color.WHITE);
		viewButton.setBackground(new Color(128, 128, 128)); // Gray
		viewButton.setForeground(Color.WHITE);
		getRootIdButton.setBackground(new Color(128, 0, 128)); // Purple
		getRootIdButton.setForeground(Color.WHITE);

		frame.add(label);
		frame.add(rootField);
		frame.add(addButton);
		frame.add(updateButton);
		frame.add(deleteButton);
		frame.add(viewButton);
		frame.add(getRootIdButton);

		getRootIdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//getRootId button click
				String root = getRoot();
				rootDTO.setRoot(root);
				int rootID = fascade.getID(rootDTO);

				if (rootID != -1) {
					JOptionPane.showMessageDialog(frame, "Root ID for " + root + " is " + rootID, "Root ID",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame, "Root not found", "Root ID", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		frame.setVisible(true);

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//add button click
				String newRoot = getRoot();
				rootDTO.setRoot(newRoot);
				//Perform the add operation
				fascade.addRoot(rootDTO);
			}
		});

		updateButton.addActionListener(new ActionListener() {
			int rootID = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				// Handle the update button click
				String root = getRoot();
				rootDTO.setRoot(root);
				rootID = fascade.getID(rootDTO);
				// Show a dialog to input the new value
				String newRoot = JOptionPane.showInputDialog(frame, "Enter the new value for the root:", "Update Root",JOptionPane.PLAIN_MESSAGE);
				rootDTO.setRoot(newRoot);
				if (newRoot != null) {
					fascade.updateRoot(rootDTO, rootID);
				}
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Handle the delete button click
				String root = getRoot();
				rootDTO.setRoot(root);
				// Perform the delete operation
				fascade.deleteRoot(rootDTO);
			}
		});

		viewButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Handle the view button click
		        Map<Integer, RootDTO> rootsWithIDs = fascade.getRootsAndIDs(); // Assuming this method retrieves a Map

		        if (rootsWithIDs != null && !rootsWithIDs.isEmpty()) {
		            showRootsInTable(rootsWithIDs);
		        } else {
		            JOptionPane.showMessageDialog(frame, "No roots found.", "Roots", JOptionPane.INFORMATION_MESSAGE);
		        }
		    }
		});


		frame.setVisible(true);
	}

	public String getRoot() {
		return rootField.getText();
	}

	
	
	private void showRootsInTable(Map<Integer, RootDTO> rootDTOMap) {
	    // Create a new frame to display the roots and their IDs in a table
	    JFrame tableViewFrame = new JFrame("Roots");
	    tableViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    tableViewFrame.setSize(600, 400);
	    tableViewFrame.setLocationRelativeTo(frame);

	    // Create a table model to hold the data
	    DefaultTableModel tableModel = new DefaultTableModel();
	    tableModel.addColumn("Root ID");
	    tableModel.addColumn("Root Name");

	    // Iterate through the map of rootDTOMap and add the data to the table model
	    for (Map.Entry<Integer, RootDTO> entry : rootDTOMap.entrySet()) {
	        Integer rootID = entry.getKey();
	        RootDTO rootDTO = entry.getValue();
	        tableModel.addRow(new Object[] { rootID, rootDTO.getRoot() });
	    }

	    JTable rootTable = new JTable(tableModel);

	    JScrollPane scrollPane = new JScrollPane(rootTable);

	    tableViewFrame.add(scrollPane);

	    tableViewFrame.setVisible(true);
	}

	private JButton createStyledButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(90, 20));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setBackground(Color.WHITE);
		button.setForeground(new Color(0x5E7986));
		
		// Add hover effect to buttons
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
				button.setBackground(new Color(0x5E7986));
				button.setForeground(new Color(0x5E7986));
			}

			public void mouseExited(MouseEvent evt) {
				button.setBorder(BorderFactory.createEmptyBorder());
				button.setBackground(Color.WHITE);
				button.setForeground(new Color(0x5E7986));
			}
		});

		return button;
	}
}
