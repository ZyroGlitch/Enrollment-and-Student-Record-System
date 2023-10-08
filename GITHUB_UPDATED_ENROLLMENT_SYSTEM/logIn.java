package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

public class logIn extends JFrame {

	private JPanel contentPane;

	static Image sksuLogo = new ImageIcon(logIn.class.getResource("/Enrollment_img/sksu.png")).getImage().getScaledInstance(340,300,Image.SCALE_SMOOTH);
	static Image userIcon = new ImageIcon(logIn.class.getResource("/Enrollment_img/username.png")).getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
	static Image passwordIcon = new ImageIcon(logIn.class.getResource("/Enrollment_img/padlock.png")).getImage().getScaledInstance(40,40,Image.SCALE_SMOOTH);
	private JTextField studentID_txt;
	private JPasswordField passwordField;
	private JLabel createAccountlbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logIn frame = new logIn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//---DATABASE CONNECTION---//
	static PreparedStatement pst;
	static ResultSet rs;
	static Connection con;
	private JLabel usernameIcon;
	private JLabel passIcon;
	public void Connect(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/softwaredev_project","root","");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}





	/**
	 * Create the frame.
	 */
	public logIn() {
		Connect();
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 549);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setBounds(456, 0, 40, 40);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel exit = new JLabel("X");
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				exit.setForeground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				exit.setForeground(Color.WHITE);
			}
		});
		exit.setForeground(Color.WHITE);
		exit.setFont(new Font("Arial Black", Font.PLAIN, 28));
		exit.setHorizontalAlignment(SwingConstants.CENTER);
		exit.setBounds(0, 0, 40, 40);
		panel.add(exit);

		JLabel sksu_logo = new JLabel("");
		sksu_logo.setHorizontalAlignment(SwingConstants.CENTER);
		sksu_logo.setBounds(0, 0, 496, 241);
		sksu_logo.setIcon(new ImageIcon(sksuLogo));
		contentPane.add(sksu_logo);

		studentID_txt = new JTextField("Student ID Number");
		studentID_txt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				studentID_txt.setText("");			
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				studentID_txt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				studentID_txt.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
			}
		});
		studentID_txt.setBackground(Color.WHITE);
		studentID_txt.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
		studentID_txt.setForeground(Color.BLACK);
		studentID_txt.setFont(new Font("Arial", Font.PLAIN, 18));
		studentID_txt.setBounds(91, 268, 314, 40);
		contentPane.add(studentID_txt);
		studentID_txt.setColumns(10);

		passwordField = new JPasswordField("Password");
		passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
		passwordField.setEchoChar((char) 0);
		passwordField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				passwordField.setEchoChar('●');
				passwordField.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				passwordField.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				passwordField.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		passwordField.setBorder(new LineBorder(Color.BLACK, 2, false));
		passwordField.setBounds(91, 319, 314, 40);
		contentPane.add(passwordField);

		JPanel login_panel = new JPanel();
		login_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, null, Color.DARK_GRAY, null));
		login_panel.setBackground(new Color(51, 204, 0));
		login_panel.setBounds(149, 383, 198, 50);
		contentPane.add(login_panel);
		login_panel.setLayout(null);

		JLabel login_label = new JLabel("LOGIN");
		login_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				//LOGIN VALIDATION
				if(studentID_txt.getText().isEmpty() || studentID_txt.getText().equals("Student ID Number")) {
					JOptionPane.showMessageDialog(null, "Please enter your Student ID!");
				}else if(passwordField.getText().isEmpty() || passwordField.getText().equals("Password")){
					JOptionPane.showMessageDialog(null, "Please enter your Password!");
				}else {
					String studentID = studentID_txt.getText();
					String password = passwordField.getText();

					try {
						pst = con.prepareStatement("SELECT * FROM `student_credentials` WHERE `Student ID` = ? AND `Password` = ?");
						pst.setString(1, studentID);
						pst.setString(2, password);

						rs = pst.executeQuery();
						if(rs.next()) {
							//Transfer the student id to dashboard that invisible
							dashboard.loginData_studentid = studentID;

							new loading().setVisible(true);
							dispose();

							//Reset all value input by user
							studentID_txt.setText("Student ID Number");
							passwordField.setText("Password");
							passwordField.setEchoChar((char) 0);
						}else {
							JOptionPane.showMessageDialog(null, studentID);
							JOptionPane.showMessageDialog(null, password);
						}

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					//Execute the dashboard insertBalanceTable method
					new dashboard().selectUpdated_totalBalance();
					new dashboard().selectUpdated_totalPayment();
					new dashboard().totalTransaction();
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				login_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				login_panel.setBackground(new Color(51, 204, 0));
			}
		});
		login_label.setForeground(Color.WHITE);
		login_label.setFont(new Font("Segoe UI Black", Font.BOLD, 25));
		login_label.setHorizontalAlignment(SwingConstants.CENTER);
		login_label.setBounds(0, 0, 198, 50);
		login_panel.add(login_label);

		createAccountlbl = new JLabel("Create Account");
		createAccountlbl.setForeground(Color.WHITE);
		createAccountlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new signup().setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				createAccountlbl.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				createAccountlbl.setForeground(Color.WHITE);
			}
		});
		createAccountlbl.setFont(new Font("Arial", Font.PLAIN, 16));
		createAccountlbl.setHorizontalAlignment(SwingConstants.CENTER);
		createAccountlbl.setBounds(149, 497, 198, 25);
		contentPane.add(createAccountlbl);

		usernameIcon = new JLabel("");
		usernameIcon.setHorizontalAlignment(SwingConstants.CENTER);
		usernameIcon.setBounds(41, 270, 40, 40);
		usernameIcon.setIcon(new ImageIcon(userIcon));
		contentPane.add(usernameIcon);

		passIcon = new JLabel("");
		passIcon.setHorizontalAlignment(SwingConstants.CENTER);
		passIcon.setBounds(41, 319, 40, 40);
		passIcon.setIcon(new ImageIcon(passwordIcon));
		contentPane.add(passIcon);
		
		JLabel forgotPass = new JLabel("Forgot Password?");
		forgotPass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new changePassword().setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				forgotPass.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				forgotPass.setForeground(Color.WHITE);
			}
		});
		forgotPass.setFont(new Font("Arial", Font.PLAIN, 16));
		forgotPass.setHorizontalAlignment(SwingConstants.CENTER);
		forgotPass.setForeground(Color.WHITE);
		forgotPass.setBounds(149, 447, 198, 30);
		contentPane.add(forgotPass);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setBounds(50, 480, 396, 11);
		contentPane.add(separator);
		setLocationRelativeTo(null);
	}
}
