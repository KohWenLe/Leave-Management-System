import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.IOException;

public class AddRecord extends Leave{

    private JFrame addFrame;
    private JLabel addTitle, labeluserID, labelName, labelLeaveType, labelLeaveReason;
    private JLabel labelfromDate, labeltoDate;
    private JTextField tfuserID,tfName, tfLeaveReason;
    private JFormattedTextField tffromDate;
    private JFormattedTextField tftoDate;
    private JComboBox<String> comboLeave;
    private JButton confirmButton, backButton;

    //constructor
    //Admin way to add record
    public AddRecord(){
        addFrame = new JFrame("Add Record");
        addFrame.getContentPane().setBackground(new Color(173, 216, 230));
        addFrame.setLayout(null);

        addTitle = new JLabel("Add Leave Record");
        addTitle.setBounds(190, 20, 200, 22);
        addTitle.setFont(new Font("SansSerif", 1, 20));
        addFrame.add(addTitle);

        labeluserID = new JLabel("User ID: ");
        labeluserID.setBounds(81, 79, 113, 16);
        labeluserID.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labeluserID);

        tfuserID = new JTextField();
        tfuserID.setBounds(249, 79, 132, 28);
        tfuserID.addFocusListener(new FocusAdapter(){
            @Override
            public void focusLost(FocusEvent e){
                tfName.setText(getUserName(tfuserID.getText()));
            }
        });
        
        addFrame.add(tfuserID);

        labelName = new JLabel("Name: ");
        labelName.setBounds(81, 122, 113, 16);
        labelName.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelName);

        tfName = new JTextField();
        tfName.setBounds(249, 122, 132, 22);
        tfName.setEditable(false);
        addFrame.add(tfName);

        labelLeaveType = new JLabel("Leave Type");
        labelLeaveType.setBounds(81, 165, 113, 16);
        labelLeaveType.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelLeaveType);

        String[] leaveTypes = {"Sick leave", "Annual leave", "Maternity leave", "Study leave"};
        comboLeave = new JComboBox<>(leaveTypes);
        comboLeave.setBounds(249, 165, 132, 22);
        addFrame.add(comboLeave);

        labelLeaveReason = new JLabel("Reasons of leave: ");
        labelLeaveReason.setBounds(81, 209, 113, 22);
        labelLeaveReason.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelLeaveReason);

        tfLeaveReason = new JTextField();
        tfLeaveReason.setBounds(249, 209, 230, 22);
        addFrame.add(tfLeaveReason);
        
        labelfromDate = new JLabel ("From Date: ");
        labelfromDate.setBounds(81, 249, 113, 22);
        labelfromDate.setFont(new Font("serif", Font.PLAIN,15));
        addFrame.add(labelfromDate);

        labeltoDate = new JLabel ("To Date: ");
        labeltoDate.setBounds(81, 289, 113, 22);
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
        tffromDate.setBounds (249, 249, 85, 22);
        tftoDate.setBounds (249, 289, 85, 22);
        addFrame.add(tffromDate);
        addFrame.add(tftoDate);
        
        backButton = new JButton ("Back");
        backButton.setBounds(329, 345, 72, 23);
        addFrame.add(backButton);
        
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(407, 345, 90, 23);
        addFrame.add(confirmButton);

        addFrame.setSize(537, 443);
        addFrame.setLocationRelativeTo(null); //center the frame 
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setResizable(false); 
        addFrame.setVisible(true);

        //confirm to add record
        confirmButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if (validateFields(tfuserID,comboLeave,tfLeaveReason,tffromDate,tftoDate,addFrame)){

                    leaveID = incrementLeaveID(getLastLeaveID());
                    
                    // Make the record into a line
                    String record = leaveID + "\t" + tfuserID.getText() + "\t" + tfName.getText() + "\t" + comboLeave.getSelectedItem() + "\t" + tfLeaveReason.getText() + "\t" + tffromDate.getText() + "\t" + tftoDate.getText() + "\tPending\n";
                    
                    saveLeaveRecord(record);
                    
                    // clear all fields after confirming
                    tfuserID.setText(null);
                    tfName.setText(null);
                    comboLeave.setSelectedIndex(-1);
                    tfLeaveReason.setText(null);
                    tffromDate.setValue(null);
                    tftoDate.setValue(null);
                }
			}
        });

        //back -> dispose this frame
		backButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int choice = JOptionPane.showConfirmDialog(addFrame,"Are you sure you want to go back? Any unsaved changes will be lost.", "Confirmation", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION){
				addFrame.dispose();
			}
			}
		});
	}
	
    //Override method in Leave class
    @Override public boolean validateFields(JTextField userID, JComboBox leaveType, JTextField leaveReason, JFormattedTextField fromDate, JFormattedTextField toDate, JFrame frame) 
    {
        //check if all fields are filled in & fromDate is before toDate
        boolean superValidation = super.validateFields(userID,leaveType,leaveReason,fromDate,toDate,frame);

        if (!superValidation) { // If the original validation failed
            return false; 
        }

        // input UserID must exist
        if(!isUserIDRegistered(userID.getText().trim())){
            JOptionPane.showMessageDialog(frame, "The UserID is not registered", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
         return true;
	}

    //Check if userID exist in the system (registerInfo.txt)
    private boolean isUserIDRegistered(String userID){
		try(BufferedReader reader = new BufferedReader(new FileReader("registerInfo.txt"))){
			String line;
			while((line = reader.readLine()) != null){
				String[]info = line.split("\t");
				//check if the line contains enough parts and if the userID is in registerInfo.txt
				if(info.length >= 2 && info[1].trim().equals(userID)){
					return true;
				}
			}
		}catch (IOException ioException){
			JOptionPane.showMessageDialog(addFrame, "Error reading user info: " + ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false; //userID not found
	}
    
    //save a line of record into applyleave.txt
    private void saveLeaveRecord(String record) {
        String leaveFile = "applyleave.txt";
        try (FileWriter fw = new FileWriter(leaveFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(record);
            JOptionPane.showMessageDialog(addFrame, "Record Added Successfully!", "Success",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(addFrame, "Error saving record: " + ioException.getMessage());
        }
    }

    public JFrame getFrame() {
        return addFrame;
    }
}
