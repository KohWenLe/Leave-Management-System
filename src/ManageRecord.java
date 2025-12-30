import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;  
import java.io.FileWriter; 
import java.io.FileReader;
import java.io.IOException;


public class ManageRecord extends Leave{

    private JFrame manageFrame;
	private JLabel adminlabel, managelabel, searchlabel, userIDlabel;
	private JTextField userIDtf;
	private JButton searchButton;
    private JButton saveButton;
    private JButton deleteButton;
    private JTable recordTable;
    private JScrollPane scrollPane;

    // constructor
    // Admin way to manage record 
    // (View all records, search records based on ID, Delete Record, edit records in the table)
    public ManageRecord(){
	
		manageFrame = new JFrame("Manage Record");
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		manageFrame.setLayout(null);
		manageFrame.getContentPane().setBackground(new Color(173, 216, 230));
        manageFrame.setSize(600, 606);
        manageFrame.setLocationRelativeTo(null);
		manageFrame.setResizable(false);
		manageFrame.setVisible(true);
		
		adminlabel = new JLabel("Admin");
		adminlabel.setBounds(280, 30, 52, 20);
		adminlabel.setFont(new Font("Segoe UI", 0 , 14));
		manageFrame.add(adminlabel);
		
		managelabel = new JLabel("Manage Record");
		managelabel.setBounds(256, 59, 137, 20);
		managelabel.setFont(new Font("Segoe UI", 1, 14));
		manageFrame.add(managelabel);
		
		searchlabel = new JLabel("Search");
		searchlabel.setBounds(152, 103, 75, 16);
		manageFrame.add(searchlabel);
		
		userIDlabel = new JLabel("User ID: ");
		userIDlabel.setBounds(152, 128, 75, 16);
		manageFrame.add(userIDlabel);
		
		userIDtf = new JTextField();
		userIDtf.setBounds(213, 125, 135, 22);
		manageFrame.add(userIDtf);
		
		searchButton = new JButton("Search");
		searchButton.setBounds(366, 125, 78, 22);
		manageFrame.add(searchButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setBounds(6, 166, 72, 23);
		manageFrame.add(deleteButton);
		
		saveButton = new JButton("Save Changes");
		saveButton.setBounds(472, 166, 118, 23);
		manageFrame.add(saveButton);
		
		//Setting up table for displaying leave records
        String[] columnNames = {"LeaveID", "UserID", "Name", "Leave Type", "Reason", "From Date", "To Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0)
        {
            @Override 
            public boolean isCellEditable(int row, int column){
                // Make the first, second, and third columns non-editable
                return column != 0 && column != 1 && column != 2;
            }
        };
		
		loadRecordsIntoTable(tableModel,null);   //load txtfile records into model (method inherited from Leave)
        recordTable = new JTable(tableModel);
        scrollPane = new JScrollPane(recordTable);
        
        //Make 'Status' column to become combobox
        JComboBox<String> statusComboBox = new JComboBox<>(new String[] {"Pending","Approved","Rejected"});
        TableColumn statusColumn = recordTable.getColumnModel().getColumn(7);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

        //Make 'LeaveType' column to become combobox
        JComboBox<String> typeComboBox = new JComboBox<>(new String[] {"Sick leave", "Annual leave", "Maternity leave", "Study leave"});
        TableColumn typeColumn = recordTable.getColumnModel().getColumn(3);
        typeColumn.setCellEditor(new DefaultCellEditor (typeComboBox));

        //Make 'fromDate' column to become formatted textfield
        JFormattedTextField fromDateTF = new JFormattedTextField();
        TableColumn fromDateColumn = recordTable.getColumnModel().getColumn(5);
        fromDateColumn.setCellEditor(new DefaultCellEditor(fromDateTF));

        //Make 'toDate' column to become formatted textfield
        JFormattedTextField toDateTF = new JFormattedTextField();
        TableColumn toDateColumn = recordTable.getColumnModel().getColumn(6);
        toDateColumn.setCellEditor(new DefaultCellEditor(toDateTF));

        recordTable.setRowHeight(25);
        recordTable.setAutoCreateRowSorter(true);
        recordTable.getTableHeader().setReorderingAllowed(false);
		
		scrollPane.setBounds(0, 207, 600, 360);
		manageFrame.add(scrollPane);

        //Refresh the table if the page gain focus
        manageFrame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                loadRecordsIntoTable(tableModel, null); // Reload the records
            }
        });

        
		//search button
        searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                loadRecordsIntoTable(tableModel,userIDtf.getText());
            }
        });

		//save changes button 
        saveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                saveRecordFromTable((DefaultTableModel) recordTable.getModel());
            }
        });
		
		  //delete button
        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                deleteSelectedRecord(recordTable);
            }
        });
    }
	
	private void saveRecordFromTable(DefaultTableModel model) {
        // Iterate through allRecords and update it with changes from the model
        // This part needs custom logic to match records by some unique identifier, like LeaveID
        for (int i = 0; i < model.getRowCount(); i++) {
            String leaveID = (String) model.getValueAt(i, 0);
            for (String[] record : allRecords) {
                if (record[0].equals(leaveID)) {
                    for (int j = 0; j < record.length; j++) {
                        record[j] = (String) model.getValueAt(i, j);
                    }
                    break;
                }
            }
        }
    
        // Now save allRecords back to the file
        try (FileWriter fw = new FileWriter("applyleave.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (String[] record : allRecords) {
                String line = String.join("\t", record);
                bw.write(line);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(manageFrame, "Data saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(manageFrame, "Error saving file: " + e.getMessage());
        }
    }
    
	// Remove Selectedrow in the table 
	private void deleteSelectedRecord(JTable table){
        //ask user to confirm delete
		int confirmDelete = JOptionPane.showConfirmDialog(manageFrame,"Confirm Deletion?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(confirmDelete == JOptionPane.YES_OPTION) {
            int selectedRow = table.getSelectedRow();

            if(selectedRow == -1){
                JOptionPane.showMessageDialog(manageFrame, "Please select a row to delete. ", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String selectedLeaveID = (String) model.getValueAt(selectedRow, 0); //LeaveID is the first column
        
            // Remove the selected record from the list
            allRecords.removeIf(record -> record[0].equals(selectedLeaveID));
            
            // Remove the selected row from the table model
            model.removeRow(selectedRow);
            
            // Save the updated list to the file
            saveRecordFromTable(model);
        }
    }
	
	public JFrame getFrame() {
        return manageFrame;
    }
}