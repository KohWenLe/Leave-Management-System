import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class AddLeave extends Leave implements ActionListener {

	private JFrame addFrame;
    private JLabel title, labelleaveID, labeluserID, labelLeaveType, labelLeaveReason, labelLeaveStatus;
	private JLabel labelfromDate, labeltoDate;
	private JTextField tfleaveID, tfuserID, tfLeaveReason,tfleavestatus;
	private JFormattedTextField tffromDate, tftoDate;
    private JComboBox<String> comboLeave;
    private JButton confirmButton;
	private JButton backButton;
	
	private boolean isEditing= false; // flag to indicate edit mode
	
	//constructor for passing info from ViewLeave1.java
    public AddLeave(String leaveID, String userID,String name, String leaveType, String leaveReason, String fromDate, 
	String toDate, String leaveStatus) {
		
		this.userID = userID;
		this.name = name;

		addFrame = new JFrame("Leave Application");
		addFrame.getContentPane().setBackground(new Color (173, 216, 230));
        addFrame.setLayout(null);

        title = new JLabel("Apply Leave ");
        title.setBounds(190, 40, 130, 23);
        title.setFont(new Font("SansSerif", 1, 20));
        addFrame.add(title);

		labelleaveID = new JLabel("Leave ID: ");
        labelleaveID.setBounds(80, 80, 90, 16);
        labelleaveID.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelleaveID);

        tfleaveID = new JTextField();
		tfleaveID.setEditable(false);
        tfleaveID.setBounds(250, 80, 130, 22);
        addFrame.add(tfleaveID);
		
        labeluserID = new JLabel("User ID: ");
        labeluserID.setBounds(80, 120, 113, 16);
        labeluserID.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labeluserID);

        tfuserID = new JTextField();
		tfuserID.setEditable(false);
        tfuserID.setBounds(250, 120, 132, 22);
        addFrame.add(tfuserID);

        labelLeaveType = new JLabel("Leave Type");
        labelLeaveType.setBounds(80, 160, 113, 16);
        labelLeaveType.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelLeaveType);

        String[] leaveTypes = {"Sick leave", "Annual leave", "Maternity leave", "Study leave"};
        comboLeave = new JComboBox<>(leaveTypes);
        comboLeave.setBounds(250, 160, 132, 22);
        addFrame.add(comboLeave);

        labelLeaveReason = new JLabel("Reasons of leave: ");
        labelLeaveReason.setBounds(80, 200, 113, 22);
        labelLeaveReason.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelLeaveReason);

        tfLeaveReason = new JTextField();
        tfLeaveReason.setBounds(250, 200, 230, 22);
        addFrame.add(tfLeaveReason);
		
		labelfromDate = new JLabel ("From Date: ");
		labelfromDate.setBounds(80, 240, 113, 22);
		labelfromDate.setFont(new Font("serif", Font.PLAIN,15));
		addFrame.add(labelfromDate);

		labeltoDate = new JLabel ("To Date: ");
		labeltoDate.setBounds(80, 280, 113, 22);
		labeltoDate.setFont(new Font("serif", Font.PLAIN,15));
		addFrame.add(labeltoDate);
		
		tffromDate = new JFormattedTextField();
		tftoDate = new JFormattedTextField();
		try{
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            tffromDate.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
            tftoDate.setFormatterFactory(new DefaultFormatterFactory(dateFormatter));
        }catch(Exception e){
            e.printStackTrace();
        }

		tffromDate.setBounds (250, 240, 85, 22);
		addFrame.add(tffromDate);

		tftoDate.setBounds (250, 280, 85, 22);
		addFrame.add(tftoDate);
		
		labelLeaveStatus = new JLabel ("Leave Status: ");
		labelLeaveStatus.setBounds(80, 320, 100, 16);
		labelLeaveStatus.setFont(new Font("serif", Font.PLAIN,15));
		addFrame.add(labelLeaveStatus);
		
		tfleavestatus = new JTextField("");
        tfleavestatus.setBounds(250, 320, 130, 22);
		tfleavestatus.setEditable(false);
        addFrame.add(tfleavestatus);
		
		backButton = new JButton ("Back");
		backButton.setBounds(330, 360, 72, 23);
        addFrame.add(backButton);
		
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(410, 360, 90, 23);
        addFrame.add(confirmButton);

		backButton.addActionListener(this);
        confirmButton.addActionListener(this);

        addFrame.setSize(537, 443);
		addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addFrame.setResizable(false); //disable window resizing
		addFrame.setLocationRelativeTo(null); //center the frame on the screen
        addFrame.setVisible(true);
		
		tfuserID.setText(userID);
		comboLeave.setSelectedItem(leaveType);
		tfLeaveReason.setText(leaveReason);
		tffromDate.setValue(fromDate);
		tftoDate.setValue(toDate);
		if(leaveID !="")
			tfleaveID.setText(leaveID);
		else
			setLeaveIDincrement();

		if(leaveStatus != "")
			tfleavestatus.setText(leaveStatus);
		else
			tfleavestatus.setText("Pending");
    }
	
	private void setLeaveIDincrement(){
		leaveID = incrementLeaveID(getLastLeaveID());
        tfleaveID.setText(leaveID);
	}


	public JFrame getFrame(){
		return addFrame;
	}

	public JButton getconfirmButton(){
		return confirmButton;
	}

	public JTextField gettfuserID(){
		return tfuserID;
	}
	
	public JComboBox getcomboLeave(){
		return comboLeave;
	}

	public JTextField gettfLeaveReason(){
		return tfLeaveReason;
	}

	public JFormattedTextField gettffromDate(){
		return tffromDate;
	}

	public JFormattedTextField gettftoDate(){
		return tftoDate;
	}

	//When editPage is created from ViewLeave1
	public void changeEditMode(){
		isEditing = !isEditing;
	}
	

    public void actionPerformed(ActionEvent ae) {
		//confirm button
        if (ae.getSource() == confirmButton) {
			if(!isEditing){	//not editing
				if(validateFields(tfuserID,comboLeave,tfLeaveReason,tffromDate,tftoDate,addFrame)){
					addLeaveInfo();
					setLeaveIDincrement();
					clearFields();
				}
			}	
		}
		//back button
		if (ae.getSource()==backButton){
			int choice = JOptionPane.showConfirmDialog(addFrame,"Are you sure you want to go back? Any unsaved changes will be lost.", "Confirmation", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION){
				addFrame.dispose();
			}
		}
	}
	
	//clear all fields except tfleaveID
	private void clearFields() {
		comboLeave.setSelectedIndex(0);
		tfLeaveReason.setText("");
		tffromDate.setValue(null);
		tftoDate.setValue(null);
	}

	
	//Add leave info into applyleave.txt
	public void addLeaveInfo(){
        try (FileWriter fileWriter = new FileWriter("applyleave.txt", true))
		{
            fileWriter.write(tfleaveID.getText()+ "\t" + tfuserID.getText() + "\t"+ getUserName(tfuserID.getText()) + 
			"\t"+ comboLeave.getSelectedItem() + "\t" + tfLeaveReason.getText() + 
			"\t" + tffromDate.getText() + "\t" + tftoDate.getText()+ "\t" + tfleavestatus.getText() + "\n");
            JOptionPane.showMessageDialog(addFrame, "Leave details added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(addFrame, "Error writing to file.", "Error", JOptionPane.ERROR_MESSAGE);
        } 
	}
	
	// Update leave information in the file based on LeaveID
	public void updateLeaveInfo(){
		String tempFile = "temp_applyleave.txt";
		Path originalFilePath = Paths.get("applyleave.txt");
		Path tempFilePath = Paths.get("temp_applyleave.txt");
		
		try (BufferedReader reader = Files.newBufferedReader(originalFilePath);
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(tempFile))){
			/*read each line in applyleave.txt, splits them into parts,
			if the parts[0] match with the tfleaveID
			in the apply page, it updates aka writes the entire line with the new information
			entered in the page into tempFile. Then, rename tempFile to ori file*/
			String line;
			while((line = reader.readLine()) !=null){
				String [] parts = line.split("\t");
				if(parts.length >0 && parts[0].trim().equals(tfleaveID.getText().trim())){
					line = tfleaveID.getText() + "\t" + tfuserID.getText() + "\t" + getUserName(tfuserID.getText()) +
							"\t" + comboLeave.getSelectedItem() + "\t" + tfLeaveReason.getText() +
							"\t" + tffromDate.getText() + "\t" + tftoDate.getText() +
							"\t" + tfleavestatus.getText();
				}
				writer.write(line + "\n");
			}
			JOptionPane.showMessageDialog(addFrame, "Updating table successfully.", "Edit Successful", JOptionPane.INFORMATION_MESSAGE);
		}catch (IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(addFrame, "Error updating leave info", "Error", JOptionPane.ERROR_MESSAGE);
		}
			
		//rename the tempFile to ori file
		try{
			Files.move(tempFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING);
		}catch (IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(addFrame, "Error moving temp file to original file", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
}