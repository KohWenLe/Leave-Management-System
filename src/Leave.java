import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;  
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader; 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;



public class Leave{
    protected String leaveID,leaveType,leaveReason,leaveStatus,userID,name;
    protected Date fromDate, toDate; 
    protected List<String[]> allRecords = new ArrayList<>();

    //Return Username in the file based on UserID
    public static String getUserName(String userID) {
		try (BufferedReader reader = new BufferedReader(new FileReader("registerInfo.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] info = line.split("\t");
				// Check if the line contains enough parts and if the userID matches
				if (info.length >= 4 && info[1].trim().equals(userID)) {
					return info[0].trim(); // Return the username associated with the userID
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error reading user info file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return "Unknown"; 
	}

    // retrieve last leaveID in the file
    public String getLastLeaveID() {
        String lastLeaveID = "L000"; // Default ID if no records are found
        try (BufferedReader reader = new BufferedReader(new FileReader("applyleave.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lastLeaveID = line.split("\t")[0]; // Assuming leaveID is the first column
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file, starting with default ID L000");
            e.printStackTrace();
        }
        return lastLeaveID;
    }    

    //increase leaveID L###
    public String incrementLeaveID(String lastLeaveID) {
        String prefix = lastLeaveID.substring(0, 1); // the prefix is one character
        int idNumber = Integer.parseInt(lastLeaveID.substring(1)) + 1;
        return prefix + String.format("%03d", idNumber); // Formats the ID with leading zeros
    }

    //Ensure input fields are empty & fromDate is before toDate
    public boolean validateFields(JTextField userID, JComboBox leaveType, JTextField leaveReason, 
                            JFormattedTextField fromDate, JFormattedTextField toDate, JFrame frame)
    {
        //check if all the fields are filled in
        if (userID.getText().trim().isEmpty() || leaveType.getSelectedIndex() == -1 || leaveReason.getText().trim().isEmpty() || 
            toDate.getText().replaceAll("\\W", "").isEmpty() || fromDate.getText().replaceAll("\\W", "").isEmpty()) 
        {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try{
			//convert string into java 'Date' objects
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date fromDateobj = dateFormat.parse(fromDate.getText());
			Date toDateobj = dateFormat.parse(toDate.getText());
			
			//compare the dates
			if(!fromDateobj.before(toDateobj)){
                JOptionPane.showMessageDialog(frame, "Invalid data range. From date must smaller than To date.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
		}catch(ParseException e){
			e.printStackTrace();
			return false; 
		}
         return true;
	}

    //load Records from leave file into tablemodel
    public void loadRecordsIntoTable(DefaultTableModel tableModel, String userID) {
        tableModel.setRowCount(0); // Clear existing data
        allRecords.clear(); // Clear the previous records
        try (BufferedReader reader = new BufferedReader(new FileReader("applyleave.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] record = line.split("\t");
                allRecords.add(record); // save all records
                
                // If userID is null or empty, load all records; else, load records matching the userID
                if (userID == null || userID.isEmpty() || record[1].trim().equals(userID)) {
                    tableModel.addRow(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}