import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import java.awt.Image;


public class Login extends JFrame implements ActionListener{

	private String userID, password;

	private JLabel title, userIDlabel, passwordlabel, doyouhaveaccLabel, imagelabel;
	private JTextField tfuserID;
	private JPasswordField pfpassword;
	private JButton logInButton, signupButton;
	
	//construtor
	public Login(){		
		getContentPane().setBackground(new Color (173, 216, 230)); //baby blue color
        setLayout(null);
		setTitle("Leave Application System");
		
		//load and scale the image
		ImageIcon imageIcon = new ImageIcon ("logoTOP.jpg");
		Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		
		imagelabel = new JLabel (imageIcon);
		imagelabel.setBounds(80, 40, 320, 130);
		add(imagelabel);
		
		title = new JLabel ("BreakBuddy Leave Application System");
		title.setBounds(80, 170, 320, 40);
		title.setFont(new Font("Times New Roman", 1, 18));
		add(title);
		
		userIDlabel = new JLabel ("User ID: ");
		userIDlabel.setFont(new Font("Times New Roman", 1, 14));
		userIDlabel.setBounds(90, 250, 89, 17);
		add(userIDlabel);
		
		tfuserID = new JTextField();
		tfuserID.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		tfuserID.setBounds(190, 240, 167, 33);
		add(tfuserID);
		
		passwordlabel = new JLabel("Password: ");
		passwordlabel.setFont(new Font("Times New Roman", 1, 14));
		passwordlabel.setBounds(90, 300, 89, 17);
		add(passwordlabel);
		
		pfpassword = new JPasswordField ();
		pfpassword.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		pfpassword.setBounds(190, 290, 167, 33);
		add(pfpassword);
		
		doyouhaveaccLabel = new JLabel("Do not have an account?");
		doyouhaveaccLabel.setFont(new Font("Segoe UI", 1, 10));
		doyouhaveaccLabel.setBounds(205, 350, 124, 18);
		add(doyouhaveaccLabel);
		
		signupButton = new JButton ("Sign up");
		signupButton.setBounds(330, 350, 80, 20);
		add(signupButton);
		
		signupButton.addActionListener(this);
		
		logInButton = new JButton("Log in");
		logInButton.setBounds(130, 390, 200, 23);
		add(logInButton);
		
		logInButton.addActionListener(this);
		
		setSize(472, 500);
		setLocationRelativeTo(null);	//center the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		//login
		if(e.getSource() == logInButton){
			userID = tfuserID.getText();
			password= String.valueOf(pfpassword.getPassword());
			
			if(verifyAdmin(userID, password)){	//login as Admin
				JOptionPane.showMessageDialog(this, "Welcome To Admin Page!");
				openWelcomePage();	//admin page
			}
			else if(verifyLogin(userID, password)){	//login as User
				JOptionPane.showMessageDialog(this, "Login successful!");
				openWelcomePage(userID);
			}else{
				JOptionPane.showMessageDialog(this, "Invalid User ID or Password. Please type again.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		//sign up / register
		if(e.getSource() == signupButton){
			openRegistrationForm();
		}
	}

	//check if the id and pw are Admin
	//Admin ID and password stored in registerInfo.txt 
	private boolean verifyAdmin(String userID,String password){
		try (BufferedReader reader = new BufferedReader(new FileReader("registerInfo.txt"))){
			reader.readLine();	//skip the first line in the txtfile
			String line;
			if((line = reader.readLine() )!=null){		//second line represent admin info
				String []adminInfo = line.split("\t");
				String adminID = adminInfo[0].trim();
				String adminPW = adminInfo[1].trim();

				if(userID.equals(adminID) && password.equals(adminPW)){
					return true;
				}
			}
		}catch (IOException e){
			JOptionPane.showMessageDialog(this, "Error during Verifying", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			}
		return false;
	}
	
	//check if id and pw are User(employee)
	private boolean verifyLogin(String userID,String password){
		try (BufferedReader reader = new BufferedReader(new FileReader("registerInfo.txt"))){
			reader.readLine();	//skip the first line in the txtfile
			reader.readLine(); // skip the second line admin info
			String line;
			while((line = reader.readLine()) !=null){
				String [] registerInfo = line.split("\t");
				String storedID = registerInfo[1].trim();
				String storedpassword = registerInfo[7].trim();

				if(userID.equals(storedID) && password.equals(storedpassword)){
					return true;
				}
			}
		}catch (IOException e){
			JOptionPane.showMessageDialog(this, "Error during Verifying", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			}
		return false;
	}
	
	//admin page
	private void openWelcomePage(){
		WelcomePage welcomePage = new WelcomePage();
		this.dispose(); //close login page
	}

	// user page // method overloadding
	private void openWelcomePage(String userID){
		WelcomePage welcomePage = new WelcomePage(userID);
		this.dispose(); //close login page
	}
	
	//register page
	public void openRegistrationForm(){
		new RegistrationForm();
		this.dispose(); //close current login page
	}


	public static void main (String[] args){
		SwingUtilities.invokeLater(() -> {
			Login login = new Login();
			login.setVisible(true);	
	});
	}	
}
