package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JComboBox;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;
import javax.swing.border.LineBorder;

public class signup extends JFrame {

	private JPanel contentPane;

	static Image sksuLogo = new ImageIcon(signup.class.getResource("/Enrollment_img/sksu.png")).getImage().getScaledInstance(340,300,Image.SCALE_SMOOTH);
	static JTextField firstnametxt;
	static JTextField lastnametxt;
	static JPasswordField passwordtxt;
	static JTextField agetxt;
	static JTextField downpaymenttxt;
	static JPanel message_panel = new JPanel();
	static JPanel amount_panel = new JPanel();
	static JComboBox genderCombobox = new JComboBox();
	static JComboBox gradeCombobox = new JComboBox();

	static String gender[] = {"Male","Female"};
	static String grade[] = {"Grade 1","Grade 2","Grade 3","Grade 4","Grade 5","Grade 6"};
	static int downpayment;
	static int dashboard_currentBal = new encapsulated_CurrentBal().getValue();

	public static void cardLayout(Component cardPanel) {
		message_panel.removeAll();
		message_panel.add(cardPanel); 
		message_panel.repaint();
		message_panel.revalidate();
	}

	static ResultSet rs;
	//---DATABASE CONNECTION---//
	static Connection con;
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

	//---DATABASE INSERT---//
	static PreparedStatement pst;
	static String firstname;
	static String lastname;
	static int age;
	static String sex;
	static String gradeyear;
	static String password;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					signup frame = new signup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public signup() {
		Connect();	//---DATABASE CONNECTION METHOD---//

		setUndecorated(true);
		setForeground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 720);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel sksu_logo = new JLabel("");
		sksu_logo.setHorizontalAlignment(SwingConstants.CENTER);
		sksu_logo.setBounds(0, 0, 496, 241);
		sksu_logo.setIcon(new ImageIcon(sksuLogo));
		contentPane.add(sksu_logo);

		JPanel signup_panel = new JPanel();
		signup_panel.setLayout(null);
		signup_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, null, Color.DARK_GRAY, null));
		signup_panel.setBackground(new Color(51, 204, 0));
		signup_panel.setBounds(149, 596, 198, 50);
		contentPane.add(signup_panel);

		JLabel signup_label = new JLabel("SIGNUP");
		signup_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String downpaymentText = downpaymenttxt.getText();
				try {
					if(firstnametxt.getText().isEmpty() || firstnametxt.getText().equals("Firstname")) {
						JOptionPane.showMessageDialog(null, "Please enter your Firstname!");
					}else if(lastnametxt.getText().isEmpty() || lastnametxt.getText().equals("Lastname")) {
						JOptionPane.showMessageDialog(null, "Please enter your Lastname!");
					}else if(passwordtxt.getText().isEmpty() || passwordtxt.getText().equals("Password")) {
						JOptionPane.showMessageDialog(null, "Please enter your Password!");
					}else if(agetxt.getText().isEmpty() || agetxt.getText().equals("Age")) {
						JOptionPane.showMessageDialog(null, "Please enter your Age!");
					}else if(!downpaymentText.equals("Downpayment")) {

						int downpayment = Integer.parseInt(downpaymentText);	
						if (downpayment <= 999) {
							JOptionPane.showMessageDialog(null, "Please provide a higher downpayment to complete your transaction.");
						}else {
							Random random = new Random();
							int min = 100000;
							int max = 999999;
							int randomNumber = random.nextInt(max - min + 1) + min;
							String studentID = Integer.toString(randomNumber);


							//DATABASE INSERT (SIGNUP)
							firstname = firstnametxt.getText();
							lastname = lastnametxt.getText();
							age = Integer.parseInt(agetxt.getText());
							sex = genderCombobox.getSelectedItem().toString();
							gradeyear = gradeCombobox.getSelectedItem().toString();
							password = passwordtxt.getText();
							downpayment = Integer.parseInt(downpaymenttxt.getText());

							//transfer downpayment value to dashboard
							dashboard.transferDownpayment = downpayment;

							int tuition = 0;
							switch(gradeyear) {
							case "Grade 1":
								tuition = 10000;
								break;
							case "Grade 2":
								tuition = 12000;
								break;
							case "Grade 3":
								tuition = 13000;
								break;
							case "Grade 4":
								tuition = 14000;
								break;
							case "Grade 5":
								tuition = 15000;
								break;
							case "Grade 6":
								tuition = 16000;
								break;
							}

							int finalTuition = tuition + 3340;
							LocalDate myObj = LocalDate.now(); // Create a date object
							String date= myObj.toString();


							try {
								pst = con.prepareStatement("INSERT INTO `student_credentials` (`Student ID`,`Password`,`Firstname`,`Lastname`,`Gender`,`Age`, `Grade`, `Tuition`, `Date Enrolled`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
								pst.setString(1, studentID);
								pst.setString(2, password);
								pst.setString(3, firstname);
								pst.setString(4, lastname);
								pst.setString(5, sex);
								pst.setInt(6, age);
								pst.setString(7, gradeyear);
								pst.setInt(8, tuition);
								pst.setString(9, date);

								int k = pst.executeUpdate();
								if (k == 1) {
									JOptionPane.showMessageDialog(null, "Record added successfully!");

									//Transfer the student id to dashboard that invisible
									dashboard.loginData_studentid = studentID;

									// Reset your text fields here
									firstnametxt.setText("Firstname");
									lastnametxt.setText("Lastname");
									agetxt.setText("Age");
									genderCombobox.setSelectedItem("Male");
									gradeCombobox.setSelectedItem("Grade 1");
									passwordtxt.setText("Password");
									passwordtxt.setEchoChar((char) 0);
									downpaymenttxt.setText("Downpayment");

									//Execute the dashboard insertBalanceTable method
									new dashboard().insertBalanceTable(studentID, finalTuition, downpayment);
									new dashboard().selectUpdated_totalBalance();
									new dashboard().selectUpdated_totalPayment();
									new dashboard().totalTransaction();


									new loading().setVisible(true);
									dispose();
								} else {
									JOptionPane.showMessageDialog(null, "Record failed to save!");
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}

					}else {
						JOptionPane.showMessageDialog(null, "Please enter a downpayment amount.");
					}

				}catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid downpayment amount. Please enter a valid number.");
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				signup_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				signup_panel.setBackground(new Color(51, 204, 0));
			}
		});
		signup_label.setHorizontalAlignment(SwingConstants.CENTER);
		signup_label.setForeground(Color.WHITE);
		signup_label.setFont(new Font("Segoe UI Black", Font.BOLD, 25));
		signup_label.setBounds(0, 0, 198, 50);
		signup_panel.add(signup_label);

		JLabel loglbl = new JLabel("Back to Login");
		loglbl.setForeground(Color.WHITE);
		loglbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new logIn().setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				loglbl.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				loglbl.setForeground(Color.WHITE);
			}
		});
		loglbl.setHorizontalAlignment(SwingConstants.CENTER);
		loglbl.setFont(new Font("Arial", Font.PLAIN, 16));
		loglbl.setBounds(149, 666, 198, 25);
		contentPane.add(loglbl);

		firstnametxt = new JTextField("Firstname");
		firstnametxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				firstnametxt.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				firstnametxt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				firstnametxt.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
			}
		});
		firstnametxt.setForeground(Color.BLACK);
		firstnametxt.setFont(new Font("Arial", Font.PLAIN, 18));
		firstnametxt.setColumns(10);
		firstnametxt.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		firstnametxt.setBackground(Color.WHITE);
		firstnametxt.setBounds(66, 296, 364, 40);
		contentPane.add(firstnametxt);

		lastnametxt = new JTextField("Lastname");
		lastnametxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lastnametxt.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lastnametxt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lastnametxt.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		lastnametxt.setForeground(Color.BLACK);
		lastnametxt.setFont(new Font("Arial", Font.PLAIN, 18));
		lastnametxt.setColumns(10);
		lastnametxt.setBorder(new LineBorder(Color.BLACK, 2, true));
		lastnametxt.setBackground(Color.WHITE);
		lastnametxt.setBounds(66, 347, 364, 40);
		contentPane.add(lastnametxt);

		passwordtxt = new JPasswordField("Password");
		passwordtxt.setEchoChar((char) 0);
		passwordtxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				passwordtxt.setEchoChar('●');
				passwordtxt.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				passwordtxt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				passwordtxt.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		passwordtxt.setFont(new Font("Arial", Font.PLAIN, 18));
		passwordtxt.setEchoChar(' ');
		passwordtxt.setBorder(new LineBorder(Color.BLACK, 2, true));
		passwordtxt.setBounds(66, 398, 364, 40);
		contentPane.add(passwordtxt);

		//Gender Combobox
		genderCombobox = new JComboBox(gender);
		genderCombobox.setBorder(new LineBorder(Color.BLACK, 2, true));
		genderCombobox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				genderCombobox.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				genderCombobox.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		genderCombobox.setFont(new Font("Arial", Font.PLAIN, 16));
		genderCombobox.setBounds(141, 449, 99, 34);
		contentPane.add(genderCombobox);

		JLabel genderlbl = new JLabel("Sex :");
		genderlbl.setForeground(Color.WHITE);
		genderlbl.setHorizontalAlignment(SwingConstants.LEFT);
		genderlbl.setFont(new Font("Arial", Font.PLAIN, 18));
		genderlbl.setBounds(66, 449, 70, 34);
		contentPane.add(genderlbl);

		agetxt = new JTextField("Age");
		agetxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				agetxt.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				agetxt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				agetxt.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		agetxt.setForeground(Color.BLACK);
		agetxt.setFont(new Font("Arial", Font.PLAIN, 18));
		agetxt.setColumns(10);
		agetxt.setBorder(new LineBorder(Color.BLACK, 2, true));
		agetxt.setBackground(Color.WHITE);
		agetxt.setBounds(66, 494, 174, 40);
		contentPane.add(agetxt);

		JLabel gradelbl = new JLabel("Grade :");
		gradelbl.setForeground(Color.WHITE);
		gradelbl.setHorizontalAlignment(SwingConstants.LEFT);
		gradelbl.setFont(new Font("Arial", Font.PLAIN, 18));
		gradelbl.setBounds(256, 449, 70, 34);
		contentPane.add(gradelbl);

		//GRADE COMBOBOX
		gradeCombobox = new JComboBox(grade);
		gradeCombobox.setBorder(new LineBorder(Color.BLACK, 2, true));
		gradeCombobox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				gradeCombobox.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				gradeCombobox.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		gradeCombobox.setFont(new Font("Arial", Font.PLAIN, 16));
		gradeCombobox.setBounds(331, 449, 99, 34);
		contentPane.add(gradeCombobox);

		downpaymenttxt = new JTextField("Downpayment");
		downpaymenttxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				downpaymenttxt.setText("");
				cardLayout(amount_panel);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				downpaymenttxt.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				downpaymenttxt.setBorder(new LineBorder(Color.BLACK, 2, true));
			}
		});
		downpaymenttxt.setForeground(Color.BLACK);
		downpaymenttxt.setFont(new Font("Arial", Font.PLAIN, 18));
		downpaymenttxt.setColumns(10);
		downpaymenttxt.setBorder(new LineBorder(Color.BLACK, 2, true));
		downpaymenttxt.setBackground(Color.WHITE);
		downpaymenttxt.setBounds(256, 494, 174, 40);
		contentPane.add(downpaymenttxt);

		message_panel = new JPanel();
		message_panel.setBounds(66, 545, 364, 34);
		contentPane.add(message_panel);
		message_panel.setLayout(new CardLayout(0, 0));

		amount_panel = new JPanel();
		amount_panel.setLocation(66, 0);
		amount_panel.setBackground(Color.BLACK);
		message_panel.add(amount_panel, "name_97978497072300");
		amount_panel.setLayout(null);

		JLabel minimumPaylbl = new JLabel("Enrollment Downpayment: Secure Your Spot with ₱1000");
		minimumPaylbl.setForeground(Color.WHITE);
		minimumPaylbl.setFont(new Font("Arial", Font.PLAIN, 14));
		minimumPaylbl.setHorizontalAlignment(SwingConstants.CENTER);
		minimumPaylbl.setBounds(0, 0, 364, 34);
		amount_panel.add(minimumPaylbl);

		JPanel lackPayment_panel = new JPanel();
		lackPayment_panel.setBackground(Color.GREEN);
		message_panel.add(lackPayment_panel, "name_98039401171400");

		JPanel lackInfo_panel = new JPanel();
		lackInfo_panel.setBackground(Color.PINK);
		message_panel.add(lackInfo_panel, "name_98137442532400");

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		panel.setBounds(456, 0, 40, 40);
		contentPane.add(panel);

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
		exit.setHorizontalAlignment(SwingConstants.CENTER);
		exit.setForeground(Color.WHITE);
		exit.setFont(new Font("Arial Black", Font.PLAIN, 28));
		exit.setBounds(0, 0, 40, 40);
		panel.add(exit);
		setLocationRelativeTo(null);
	}
}
