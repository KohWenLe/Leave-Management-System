import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class WelcomePage extends JFrame{

	private String userID,name;
	
    private JLabel titleLabel,welcomeLabel;
    private JButton manageButton;
    private JButton addButton;
    private JButton logoutButton;

	private JFrame addRecordFrame;
	private JFrame manageRecordFrame;
	private JFrame addLeaveFrame;
	private JFrame manageLeaveFrame;

	//constructor without parameter -> admin
	public WelcomePage(){	
		initComponentsAdmin();
	}

	//Welcome page for Admin
	private void initComponentsAdmin() {
		setTitle("Leave Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(472, 421);
        setResizable(false);
        setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(173, 216, 230));
        setLayout(null); // Using null layout

        welcomeLabel = new JLabel("Welcome Admin");
        welcomeLabel.setFont(new Font("Times Roman", 0, 26));
        welcomeLabel.setBounds(80, 60, 312, 30);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel("Leave Management");
        titleLabel.setFont(new Font("Times Roman", 1, 20));
        titleLabel.setBounds(80, 120, 312, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        manageButton = new JButton("Manage Records");
		manageButton.setFont(new Font("Segoe UI",Font.BOLD,18));
		manageButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        manageButton.setBounds(136, 180, 200, 40);

        addButton = new JButton("Add Records");
		addButton.setFont(new Font("Segoe UI",Font.BOLD,18));
		addButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        addButton.setBounds(136, 250, 200, 40);

        logoutButton = new JButton("Log out");
		logoutButton.setFont(new Font("Segoe UI",Font.BOLD,14));
        logoutButton.setBounds(342, 331, 100, 30);

        add(welcomeLabel);
        add(titleLabel);
        add(manageButton);
        add(addButton);
        add(logoutButton);

        setVisible(true);

        //click manage record button
        manageButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                openManageLeavePage(); //open the Manage Record window
            }
        });

        //click add record button
        addButton.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				openAddLeavePage();
			}
		});

        //click log out button
        logoutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                logout(); 
            }
        });
    }
	
	//construtor with userID parameter
	public WelcomePage(String userID){
		this.userID = userID;
		name = Leave.getUserName(userID);
		initComponents();
	}
	
	//Welcome Page for user/employee
	private void initComponents(){
		
		setTitle("Leave Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(472, 421);
        setResizable(false);
        setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(173, 216, 230));
        setLayout(null); // Using null layout

        welcomeLabel = new JLabel("Welcome " + name + "!");
        welcomeLabel.setFont(new Font("Times Roman", 0, 26));
        welcomeLabel.setBounds(80, 60, 312, 30);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel("Leave Application");
        titleLabel.setFont(new Font("Times Roman", 1, 20));
        titleLabel.setBounds(80, 120, 312, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        manageButton = new JButton("Manage Leave");
		manageButton.setFont(new Font("Segoe UI",Font.BOLD,18));
		manageButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        manageButton.setBounds(136, 180, 200, 40);

        addButton = new JButton("Add Leave");
		addButton.setFont(new Font("Segoe UI",Font.BOLD,18));
		addButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        addButton.setBounds(136, 250, 200, 40);

        logoutButton = new JButton("Log out");
		logoutButton.setFont(new Font("Segoe UI",Font.BOLD,14));
        logoutButton.setBounds(342, 331, 100, 30);

        add(welcomeLabel);
        add(titleLabel);
        add(manageButton);
        add(addButton);
        add(logoutButton);

        setVisible(true);

		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				openAddLeavePage(userID);
			}
		});
		
		manageButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				openManageLeavePage(userID);
			}
		});

		logoutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
				logout(); 
            }
        });

	}

	// admin addleave
	private void openAddLeavePage() {
		if (addRecordFrame == null) {
			AddRecord addrecordpage = new AddRecord();
			addRecordFrame = addrecordpage.getFrame();
		} else {
			addRecordFrame.toFront(); // Brings it to the front
			addRecordFrame.repaint(); // refresh the frame's appearance
			addRecordFrame.setVisible(true);
		}
	}
	
	// user addleave (overloadding)
	private void openAddLeavePage(String userID) {
		if (addLeaveFrame == null) {
			AddLeave addleavepage = new AddLeave("", userID, name, "", "", "", "", "");
			addLeaveFrame = addleavepage.getFrame();
		} else {
			addLeaveFrame.toFront(); // Brings it to the front
			addLeaveFrame.repaint(); // refresh the frame's appearance
			addLeaveFrame.setVisible(true);
		}
	}
	
	// admin manageleave
	private void openManageLeavePage() {
		if (manageRecordFrame == null) {
			ManageRecord managerecordpage = new ManageRecord();
			manageRecordFrame = managerecordpage.getFrame();
		} else {
			manageRecordFrame.toFront(); // Brings it to the front
			manageRecordFrame.repaint(); // refresh the frame's appearance
			manageRecordFrame.setVisible(true);
		}
	}
	
	// user manageleave (overloadding)
	private void openManageLeavePage(String userID) {
		if (manageLeaveFrame == null) {
			ViewLeave1 manageleavepage = new ViewLeave1(userID);
			manageLeaveFrame = manageleavepage.getFrame();
		} else {
			manageLeaveFrame.toFront(); // Brings it to the front
			manageLeaveFrame.repaint(); // refresh the frame's appearance
			manageLeaveFrame.setVisible(true);
		}
	}
	
	//logout from welcomepage
	private void logout(){
		int confirmLogout = JOptionPane.showConfirmDialog(this,"Are you sure you want to log out?","Log Out",
        JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		
        // If the user confirms they want to log out
        if(confirmLogout == JOptionPane.YES_OPTION) {
        	 this.dispose();

			// Dispose of the potential frames if it exists
			if (addRecordFrame != null) {
				addRecordFrame.dispose();
			}
			if (manageRecordFrame != null) {
				manageRecordFrame.dispose();
			}
			if (addLeaveFrame != null) {
				addLeaveFrame.dispose();
			}
			if (manageLeaveFrame != null) {
				manageLeaveFrame.dispose();
			}

			ViewLeave1.disposeEditPage();

        	// Open the login page 
			Login loginPage = new Login();
			loginPage.setVisible(true);
        }
	}
}