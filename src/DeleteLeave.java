import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DeleteLeave extends Leave{
	
		private ViewLeave1 parentview;
		
		//constructor
		public DeleteLeave (ViewLeave1 parentview){
			this.parentview = parentview;
		}
		
		//Delete Leave record from table based on LeaveID
		public void deleteleave(String leaveIDToDelete){
			int selectedRow = parentview.getLeaveTable().getSelectedRow();
			parentview.getTableModel().removeRow(selectedRow);
			deleteRecordFromFile(leaveIDToDelete);
		}
		
		//Delete Leave record from file based on LeaveID
		private void deleteRecordFromFile(String leaveIDToDelete){
			try{
				String filePath = "applyleave.txt";
				File oriFile = new File (filePath);
				File tempFile = new File ("temp_applyleave.txt");
				
				try(BufferedReader reader = new BufferedReader(new FileReader(oriFile));
					BufferedWriter writer = new BufferedWriter (new FileWriter(tempFile))){
						
					String line;
						
					//read each line from the original file
					//splits the line into parts 
					while ((line = reader.readLine()) != null){
						String[] parts = line.split("\t");
						
						//extracts the first element from the parts aray, trimming any
						//leading or trailing whitespace
						String currentleaveID = parts.length > 0 ? parts[0].trim() : "";
							
						//write line which are not to be deleted into tempFile
						if(parts.length > 0 && !currentleaveID.equals(leaveIDToDelete)){
							writer.write(line + System.getProperty("line.separator"));
						}
					}
				}
			// Move the temporary file to replace the original file
			Path originalFilePath = Paths.get("applyleave.txt");
			Path tempFilePath = Paths.get("temp_applyleave.txt");
			Files.move(tempFilePath, originalFilePath, StandardCopyOption.REPLACE_EXISTING);

			JOptionPane.showMessageDialog(null, "Record deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error deleting record", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}