import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;


public class ViewLeave1 extends Leave implements ActionListener {
	
	private JFrame viewFrame;
	private JLabel title, labeluserID;
	private JTextField tfuserID;
	private JButton viewButton;
	private JTable leaveTable;
	private DefaultTableModel tableModel;
	private JButton editButton, deleteButton;

	private DeleteLeave DeleteLeaveobj;
	private static AddLeave editPage;
	
	public ViewLeave1(String userID){
		this.userID = userID;
		this.name = getUserName(userID);

		viewFrame = new JFrame("Manage Leave");
		viewFrame.getContentPane().setBackground(new Color (173, 216, 230));
        viewFrame.setLayout(null);
		
		title = new JLabel("Manage Leave ");
        title.setBounds(255, 30, 140, 30);
        title.setFont(new Font("SansSerif", 1, 18));
        viewFrame.add(title);

        labeluserID = new JLabel("User ID: ");
        labeluserID.setBounds(210, 70, 63, 16);
        labeluserID.setFont(new Font("serif", Font.PLAIN,15));
        viewFrame.add(labeluserID);

        tfuserID = new JTextField(userID);
        tfuserID.setBounds(320, 70, 114, 22);
		tfuserID.setEditable(false);
        viewFrame.add(tfuserID);
		
		viewButton = new JButton("View");
        viewButton.setBounds(230, 110, 170, 23);
		viewButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        viewFrame.add(viewButton);
		
		editButton = new JButton("Edit");
        editButton.setBounds(230, 140, 72, 23);
		editButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        viewFrame.add(editButton);
		
		deleteButton = new JButton("Delete");
        deleteButton.setBounds(320, 140, 78, 23);
		deleteButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        viewFrame.add(deleteButton);
		
		//create an instance for DeleteLeave
		DeleteLeaveobj = new DeleteLeave(this);
		
		viewButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		
		String[] leaveInfo = {"Leave ID", "User ID","Name","Leave Type", "Leave Reasons", "From Date", "To Date", "Leave Status"};
		tableModel = new DefaultTableModel(leaveInfo,0){
		//to set all cells are non-editable
			public boolean isCellEditable (int row, int column){
				return false;
			}
	};
		leaveTable = new JTable (tableModel);
		leaveTable.setAutoCreateRowSorter(true);
		leaveTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scrollPane = new JScrollPane(leaveTable);
		scrollPane.setBounds(0, 190, 635, 280);
		viewFrame.add(scrollPane);
		
		viewFrame.setSize(648, 450);
        viewFrame.setLocationRelativeTo(null); //center the frame on the screen
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		viewFrame.setResizable(false); //disable window resizing
        viewFrame.setVisible(true);
		
	}
	

	public void actionPerformed(ActionEvent e){
		//View Leave Records
		if(e.getSource() == viewButton){
			userID = tfuserID.getText();
			if(!userID.isEmpty()){
				loadRecordsIntoTable(tableModel,userID);
			}else{
				JOptionPane.showMessageDialog(viewFrame, "Please fill in your User ID.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		//Edit
		else if (e.getSource() == editButton){
			int selectedRow = leaveTable.getSelectedRow();
			if (selectedRow != -1){
				//record cannot be edited if approved or rejected
				String leaveStatus = (String) leaveTable.getValueAt(selectedRow, 7); 
				if (leaveStatus.equals("Approved") || leaveStatus.equals("Rejected")) {	
					JOptionPane.showMessageDialog(viewFrame, "This record cannot be edited as it has been " + leaveStatus.toLowerCase(), "Edit Denied", JOptionPane.WARNING_MESSAGE);
				} else {
					openEditPage(selectedRow);
				}
			}else{
				JOptionPane.showMessageDialog(viewFrame, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		//Delete
		else if (e.getSource() == deleteButton){
			int selectedRow = leaveTable.getSelectedRow();
			if(selectedRow != -1){
				//record cannot be deleted if approved or rejected
				String leaveStatus = (String) leaveTable.getValueAt(selectedRow, 7); 
				if (leaveStatus.equals("Approved") || leaveStatus.equals("Rejected")) {	
					JOptionPane.showMessageDialog(viewFrame, "This record cannot be deleted as it has been " + leaveStatus.toLowerCase(), "Edit Denied", JOptionPane.WARNING_MESSAGE);
				} else {
					//ask user to confirm delete
					int confirmDelete = JOptionPane.showConfirmDialog(viewFrame,"Confirm Deletion?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
					if(confirmDelete == JOptionPane.YES_OPTION) {
						String leaveIDToDelete = (String) leaveTable.getValueAt(selectedRow,0);
						DeleteLeaveobj.deleteleave(leaveIDToDelete); //call the deleteleave function in DeleteLeave class
					}
				}
			}else{
				JOptionPane.showMessageDialog(viewFrame, "Please select a row to delete.","Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	// Open AddLeave  page with pre-filled information for editing
	private void openEditPage(int selectedRow) {

		// If another editPage is already opened
		if (editPage != null && editPage.getFrame().isVisible()) {
			editPage.getFrame().repaint();
			editPage.getFrame().toFront();
			return;
		}

		// Retrieve data from selected row
		String leaveID = (String) leaveTable.getValueAt(selectedRow, 0);
		String userID = (String) leaveTable.getValueAt(selectedRow, 1);
		String name = (String) leaveTable.getValueAt(selectedRow, 2);
		String leaveType = (String) leaveTable.getValueAt(selectedRow, 3);
		String leaveReason = (String) leaveTable.getValueAt(selectedRow, 4);
		String fromDate = (String) leaveTable.getValueAt(selectedRow, 5);
		String toDate = (String) leaveTable.getValueAt(selectedRow, 6);
		String leaveStatus = (String) leaveTable.getValueAt(selectedRow, 7);

		// Open AddLeave.java page with pre-filled information
		editPage = new AddLeave(leaveID,userID,name,leaveType,leaveReason,fromDate,toDate,leaveStatus);
		editPage.changeEditMode();

		//add action listener for confirm button in edit page
		editPage.getconfirmButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Validate fields
				if (editPage.validateFields(editPage.gettfuserID(), editPage.getcomboLeave(), editPage.gettfLeaveReason(), editPage.gettffromDate(), editPage.gettftoDate(), editPage.getFrame())) 
				{
					// Update the file
					editPage.updateLeaveInfo();
					// Dispose the edit frame
					editPage.getFrame().dispose();
					// Load records into the table
					loadRecordsIntoTable(tableModel, userID);
				}
			}
		});	

	}

	public static void disposeEditPage() {
		if (editPage != null && editPage.getFrame().isVisible()) {
			editPage.getFrame().dispose();
		}
	}
	
	public JTable getLeaveTable(){
		return leaveTable;
	}
	
	public DefaultTableModel getTableModel(){
		return tableModel;
	}

	public JFrame getFrame(){
		return viewFrame;
	}
}