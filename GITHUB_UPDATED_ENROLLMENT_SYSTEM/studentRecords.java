package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.Vector;
import java.awt.CardLayout;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;

public class studentRecords extends JFrame {

	static String sex[] = {"Male","Female"};
	static String grade[] = {"Grade 1","Grade 2","Grade 3","Grade 4","Grade 5","Grade 6"};

	static int totalRecords, downpayment, deletedRec;
	static String string_totalRecords, string_totalDeletedRecs;

	static Image Logo = new ImageIcon(dashboard.class.getResource("/Enrollment_img/sksuLogo.png")).getImage().getScaledInstance(100,55,Image.SCALE_SMOOTH);
	static Image dash = new ImageIcon(dashboard.class.getResource("/Enrollment_img/dash.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image search = new ImageIcon(dashboard.class.getResource("/Enrollment_img/find.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image add = new ImageIcon(dashboard.class.getResource("/Enrollment_img/add.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image update = new ImageIcon(dashboard.class.getResource("/Enrollment_img/edit.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image delete = new ImageIcon(dashboard.class.getResource("/Enrollment_img/remove.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image data = new ImageIcon(dashboard.class.getResource("/Enrollment_img/databaseRecords.png")).getImage().getScaledInstance(40,30,Image.SCALE_SMOOTH);
	static Image profileIcon = new ImageIcon(dashboard.class.getResource("/Enrollment_img/profile.png")).getImage().getScaledInstance(40,25,Image.SCALE_SMOOTH);
	static Image password = new ImageIcon(dashboard.class.getResource("/Enrollment_img/password.png")).getImage().getScaledInstance(40,25,Image.SCALE_SMOOTH);
	static Image payment = new ImageIcon(dashboard.class.getResource("/Enrollment_img/payment.png")).getImage().getScaledInstance(40,25,Image.SCALE_SMOOTH);
	static Image print = new ImageIcon(dashboard.class.getResource("/Enrollment_img/print.png")).getImage().getScaledInstance(40,32,Image.SCALE_SMOOTH);
	private JPanel contentPane;
	public static JTextField txtSearch;
	static JLabel createlbl;
	static JLabel updatelbl;
	static JLabel deletelbl;
	static JLabel signoutlbl;
	static JLabel dashlbl;
	static JPanel table_panel;
	static JPanel record;
	static JLabel lblRecord;
	static JPanel addRecord;
	static JPanel updateRecord;
	static JPanel deleteRecord;
	static JComboBox genderBox_upt;
	static JComboBox gradeBox_upt;
	static JLabel total_transaction;
	static JLabel lblDeletedRecord;
	static JPanel previousEnrollee;
	static JLabel total_records;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					studentRecords frame = new studentRecords();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public static void cardLayout(Component cardPanel) {
		table_panel.removeAll();
		table_panel.add(cardPanel); 
		table_panel.repaint();
		table_panel.revalidate();
	}


	//---DATABASE CONNECTION---//
	static Connection con, con1;
	static PreparedStatement pst, pst1;
	static ResultSet rs;
	private static JTable Record_table;
	private JTextField firstnametxt;
	private JTextField lastnametxt;
	private JTextField agetxt;
	private JTextField paytxt;
	private JPasswordField passwordtxt;
	private JTextField firstname_upt;
	private JTextField lastname_upt;
	private JPasswordField password_upt;
	private JTextField age_upt;
	private JTextField studentid_upt;
	private JTextField firstname_del;
	private JTextField lastname_del;
	private JPasswordField password_del;
	private JTextField age_del;
	private JTextField studentID_del;
	private JTable previousRecTable;

	//DATABASE CONNNECTION
	public void Connect(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/softwaredev_project","root","");
			con1 = DriverManager.getConnection("jdbc:mysql://localhost/softwaredev_project","root","");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void search() {
		try {

			int q;
			pst = con.prepareStatement("SELECT * FROM `student_credentials` WHERE `Student ID` = ? OR `Firstname` = ? OR `Lastname` = ? OR `Gender` = ? OR `Age` = ? OR `Grade` = ? OR `Date Enrolled` = ? ORDER BY `Lastname`");

			// Check if the age input is a valid integer
			int parseAge;
			try {
				parseAge = Integer.parseInt(txtSearch.getText());
			} catch (NumberFormatException ex) {
				parseAge = 0; 
			}

			pst.setString(1, txtSearch.getText());
			pst.setString(2, txtSearch.getText());
			pst.setString(3, txtSearch.getText());
			pst.setString(4, txtSearch.getText());
			pst.setInt(5, parseAge);
			pst.setString(6, txtSearch.getText());
			pst.setString(7, txtSearch.getText());


			rs = pst.executeQuery();
			ResultSetMetaData table = rs.getMetaData();
			q = table.getColumnCount();

			DefaultTableModel df = (DefaultTableModel)Record_table.getModel();
			df.setRowCount(0);

			while(rs.next()) {

				Vector v2 = new Vector();
				for(int a = 1; a <= q; a++) {
					v2.add(rs.getString("id"));
					v2.add(rs.getString("Student ID"));
					v2.add(rs.getString("Password"));
					v2.add(rs.getString("Firstname"));
					v2.add(rs.getString("Lastname"));
					v2.add(rs.getString("Gender"));
					v2.add(rs.getString("Age"));
					v2.add(rs.getString("Grade"));
					v2.add(rs.getString("Tuition"));
					v2.add(rs.getString("Date Enrolled"));
				}
				df.addRow(v2);
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public void responsiveTable(JTable pickTable) {
		try {

			int q;

			DefaultTableModel df = (DefaultTableModel)pickTable.getModel();
			pst = con.prepareStatement("SELECT * FROM `student_credentials` ORDER BY `Lastname`");
			rs = pst.executeQuery();
			ResultSetMetaData table = rs.getMetaData();
			q = table.getColumnCount();
			df.setRowCount(0);

			while(rs.next()) {

				Vector v2 = new Vector();
				for(int a = 1; a <= q; a++) {
					v2.add(rs.getString("id"));
					v2.add(rs.getString("Student ID"));
					v2.add(rs.getString("Password"));
					v2.add(rs.getString("Firstname"));
					v2.add(rs.getString("Lastname"));
					v2.add(rs.getString("Gender"));
					v2.add(rs.getString("Age"));
					v2.add(rs.getString("Grade"));
					v2.add(rs.getString("Tuition"));
					v2.add(rs.getString("Date Enrolled"));
				}
				df.addRow(v2);
			}


		}catch(SQLException e) {
			e.printStackTrace();
		}

	}


	//RECORDS IN DELETED RECORDS TABLE
	public void previousRec(JTable pickTable) {
		try {

			int q;

			DefaultTableModel df = (DefaultTableModel)pickTable.getModel();
			pst = con.prepareStatement("SELECT * FROM `deleted_records` ORDER BY `Lastname`");
			rs = pst.executeQuery();
			ResultSetMetaData table = rs.getMetaData();
			q = table.getColumnCount();
			df.setRowCount(0);

			while(rs.next()) {

				Vector v2 = new Vector();
				for(int a = 1; a <= q; a++) {
					v2.add(rs.getString("studentID"));
					v2.add(rs.getString("password"));
					v2.add(rs.getString("firstname"));
					v2.add(rs.getString("lastname"));
					v2.add(rs.getString("gender"));
					v2.add(rs.getString("age"));
					v2.add(rs.getString("grade"));
					v2.add(rs.getString("tuition"));
					v2.add(rs.getString("date_enrolled"));
				}
				df.addRow(v2);
			}


		}catch(SQLException e) {
			e.printStackTrace();
		}

	}





	public static void totalRecords() {
		try {
			pst = con.prepareStatement("SELECT COUNT(`Student ID`) FROM `student_credentials`");
			rs = pst.executeQuery();

			if (rs.next()) {
				totalRecords = rs.getInt(1); // Use index 1 to retrieve the value
				string_totalRecords = Integer.toString(totalRecords);
				total_transaction.setText("TOTAL RECORDS : " + string_totalRecords);
			} else {
				JOptionPane.showMessageDialog(null, "Getting total records failed!!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	public static void totalRecordsDeleted() {
		try {
			pst = con.prepareStatement("SELECT COUNT(`studentID`) FROM `deleted_records`");
			rs = pst.executeQuery();

			if (rs.next()) {
				deletedRec = rs.getInt(1); // Use index 1 to retrieve the value
				string_totalDeletedRecs = Integer.toString(deletedRec);
				total_records.setText("TOTAL DELETED RECORDS : " + string_totalDeletedRecs);
			} else {
				JOptionPane.showMessageDialog(null, "Getting total deleted records failed!!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	/**
	 * Create the frame.
	 */
	public studentRecords() {
		Connect();

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1300, 700);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(51, 153, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel nav_panel = new JPanel();
		nav_panel.setLayout(null);
		nav_panel.setBackground(Color.WHITE);
		nav_panel.setBounds(0, 0, 1300, 68);
		contentPane.add(nav_panel);

		JLabel studentLogo = new JLabel("");
		studentLogo.setHorizontalAlignment(SwingConstants.CENTER);
		studentLogo.setBounds(10, 0, 106, 68);
		studentLogo.setIcon(new ImageIcon(Logo));
		nav_panel.add(studentLogo);

		JLabel lblNewLabel = new JLabel("SKSU");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
		lblNewLabel.setBounds(126, 11, 178, 25);
		nav_panel.add(lblNewLabel);

		JLabel lblStudentPortal = new JLabel("STUDENT PORTAL ");
		lblStudentPortal.setFont(new Font("Arial", Font.BOLD, 18));
		lblStudentPortal.setBounds(126, 30, 178, 30);
		nav_panel.add(lblStudentPortal);

		JPanel dashboard_panel = new JPanel();
		dashboard_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				dashboard_panel.setBackground(Color.GRAY);
				dashlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				dashboard_panel.setBackground(Color.WHITE);
				dashlbl.setForeground(Color.BLACK);
			}
			@Override
			public void mouseClicked(MouseEvent e) {

				new dashboard().setVisible(true);
				dispose();	
			}
		});
		dashboard_panel.setLayout(null);
		dashboard_panel.setBackground(Color.WHITE);
		dashboard_panel.setBounds(1096, 15, 173, 40);
		nav_panel.add(dashboard_panel);

		dashlbl = new JLabel("Dashboard");
		dashlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				new dashboard().setVisible(true);
				dispose();	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				dashboard_panel.setBackground(Color.GRAY);
				dashlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				dashboard_panel.setBackground(Color.white);
				dashlbl.setForeground(Color.black);
			}
		});
		dashlbl.setBounds(46, 0, 127, 40);
		dashboard_panel.add(dashlbl);
		dashlbl.setHorizontalAlignment(SwingConstants.CENTER);
		dashlbl.setFont(new Font("Arial", Font.PLAIN, 18));

		JLabel dashboardlbl = new JLabel("");
		dashboardlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				new dashboard().setVisible(true);
				dispose();	
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				dashboard_panel.setBackground(Color.GRAY);
				dashlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				dashboard_panel.setBackground(Color.white);
				dashlbl.setForeground(Color.BLACK);
			}
		});
		dashboardlbl.setBounds(10, 0, 50, 40);
		dashboard_panel.add(dashboardlbl);
		dashboardlbl.setHorizontalAlignment(SwingConstants.CENTER);
		dashboardlbl.setIcon(new ImageIcon(dash));

		JLabel studentRecordlbl = new JLabel("Student Record");
		studentRecordlbl.setHorizontalAlignment(SwingConstants.CENTER);
		studentRecordlbl.setForeground(Color.WHITE);
		studentRecordlbl.setFont(new Font("Arial", Font.BOLD, 36));
		studentRecordlbl.setBounds(493, 110, 315, 46);
		contentPane.add(studentRecordlbl);

		table_panel = new JPanel();
		table_panel.setBounds(249, 189, 1051, 511);
		contentPane.add(table_panel);
		table_panel.setLayout(new CardLayout(0, 0));

		record = new JPanel();
		record.setBackground(Color.WHITE);
		table_panel.add(record, "name_398863194416300");
		record.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1051, 461);
		record.add(scrollPane);

		Record_table = new JTable();
		Record_table.setRowHeight(32);
		Record_table.setFont(new Font("Arial", Font.PLAIN, 16));
		scrollPane.setViewportView(Record_table);
		Record_table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"id", "Student ID", "Password", "Firstname", "Lastname", "Gender", "Age", "Grade", "Tuition", "Date Enrolled"
				}
				));
		Record_table.getColumnModel().getColumn(0).setPreferredWidth(70);
		Record_table.getColumnModel().getColumn(1).setPreferredWidth(80);
		Record_table.getColumnModel().getColumn(2).setPreferredWidth(100);
		Record_table.getColumnModel().getColumn(3).setPreferredWidth(100);
		Record_table.getColumnModel().getColumn(4).setPreferredWidth(100);
		Record_table.getColumnModel().getColumn(7).setPreferredWidth(80);
		Record_table.getColumnModel().getColumn(8).setPreferredWidth(90);
		Record_table.getColumnModel().getColumn(9).setPreferredWidth(100);

		//-- METHOD FOR RESPONSIVE TABLE
		responsiveTable(Record_table);

		total_transaction = new JLabel("TOTAL RECORDS: ");
		total_transaction.setHorizontalAlignment(SwingConstants.CENTER);
		total_transaction.setFont(new Font("Arial Black", Font.PLAIN, 16));
		total_transaction.setBounds(726, 465, 325, 46);
		record.add(total_transaction);

		//GET THE UPDATED TOTAL COUNT RECORDS
		totalRecords();

		addRecord = new JPanel();
		addRecord.setBackground(Color.WHITE);
		table_panel.add(addRecord, "name_398869859551000");
		addRecord.setLayout(null);

		JLabel lblAddRecord = new JLabel("Add Record");
		lblAddRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddRecord.setForeground(Color.BLACK);
		lblAddRecord.setFont(new Font("Arial", Font.BOLD, 36));
		lblAddRecord.setBounds(368, 45, 315, 46);
		addRecord.add(lblAddRecord);

		firstnametxt = new JTextField();
		firstnametxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				firstnametxt.setText("");
			}
		});
		firstnametxt.setText("Firstname");
		firstnametxt.setFont(new Font("Arial", Font.PLAIN, 16));
		firstnametxt.setBounds(352, 137, 347, 35);
		addRecord.add(firstnametxt);
		firstnametxt.setColumns(10);

		lastnametxt = new JTextField();
		lastnametxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lastnametxt.setText("");
			}
		});
		lastnametxt.setText("Lastname");
		lastnametxt.setFont(new Font("Arial", Font.PLAIN, 16));
		lastnametxt.setColumns(10);
		lastnametxt.setBounds(352, 172, 347, 35);
		addRecord.add(lastnametxt);

		agetxt = new JTextField();
		agetxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				agetxt.setText("");
			}
		});
		agetxt.setText("Age");
		agetxt.setFont(new Font("Arial", Font.PLAIN, 16));
		agetxt.setColumns(10);
		agetxt.setBounds(352, 242, 347, 35);
		addRecord.add(agetxt);

		JLabel icon1 = new JLabel("");
		icon1.setHorizontalAlignment(SwingConstants.CENTER);
		icon1.setBounds(296, 137, 56, 35);
		icon1.setIcon(new ImageIcon(profileIcon));
		addRecord.add(icon1);

		JLabel icon2 = new JLabel("");
		icon2.setHorizontalAlignment(SwingConstants.CENTER);
		icon2.setBounds(296, 172, 56, 35);
		icon2.setIcon(new ImageIcon(profileIcon));
		addRecord.add(icon2);

		JLabel icon3 = new JLabel("");
		icon3.setHorizontalAlignment(SwingConstants.CENTER);
		icon3.setBounds(296, 207, 56, 35);
		icon3.setIcon(new ImageIcon(password));
		addRecord.add(icon3);

		JComboBox genderBox = new JComboBox(sex);
		genderBox.setFont(new Font("Arial", Font.PLAIN, 16));
		genderBox.setBounds(352, 277, 347, 35);
		addRecord.add(genderBox);

		JComboBox gradeBox = new JComboBox(grade);
		gradeBox.setFont(new Font("Arial", Font.PLAIN, 16));
		gradeBox.setBounds(352, 312, 347, 35);
		addRecord.add(gradeBox);

		paytxt = new JTextField();
		paytxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				paytxt.setText("");
			}
		});
		paytxt.setText("Downpayment");
		paytxt.setFont(new Font("Arial", Font.PLAIN, 16));
		paytxt.setColumns(10);
		paytxt.setBounds(352, 358, 347, 35);
		addRecord.add(paytxt);

		JPanel register_panel = new JPanel();
		register_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String downpaymentText = paytxt.getText();
				try {
					if(firstnametxt.getText().isEmpty() || firstnametxt.getText().equals("Firstname")) {
						JOptionPane.showMessageDialog(null, "Please enter your firstname!");
					}else if(lastnametxt.getText().isEmpty() || lastnametxt.getText().equals("Lastname")) {
						JOptionPane.showMessageDialog(null, "Please enter your lastname!");
					}else if(passwordtxt.getText().isEmpty() || passwordtxt.getText().equals("Password")) {
						JOptionPane.showMessageDialog(null, "Please enter your password!");
					}else if(agetxt.getText().isEmpty() || agetxt.getText().equals("Age")) {
						JOptionPane.showMessageDialog(null, "Please enter your age!");
					}else if(!downpaymentText.equals("Downpayment")) {

						int downpayment = Integer.parseInt(downpaymentText);	
						if (downpayment <= 999) {
							JOptionPane.showMessageDialog(null, "Please provide a higher downpayment to complete your transaction.");
						}else {
							//Code to generate random numbers
							Random random = new Random();
							int min = 100000;
							int max = 999999;
							int randomNumber = random.nextInt(max - min + 1) + min;
							String studentID = Integer.toString(randomNumber);

							//DATABASE INSERT (SIGNUP)
							String firstname = firstnametxt.getText();
							String lastname = lastnametxt.getText();
							int age = Integer.parseInt(agetxt.getText());
							String sex = genderBox.getSelectedItem().toString();
							String gradeyear = gradeBox.getSelectedItem().toString();
							String password = passwordtxt.getText();
							downpayment = Integer.parseInt(paytxt.getText());
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

							//transfer downpayment value to dashboard
							dashboard.transferDownpayment = downpayment;
							dashboard.total_payment = downpayment;

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
									// Reset your text fields here
									firstnametxt.setText("Firstname");
									lastnametxt.setText("Lastname");
									agetxt.setText("Age");
									genderBox.setSelectedItem("Male");
									gradeBox.setSelectedItem("Grade 1");
									passwordtxt.setText("Password");
									passwordtxt.setEchoChar((char) 0);
									paytxt.setText("Downpayment");


									//-- METHOD FOR RESPONSIVE TABLE
									responsiveTable(Record_table);

									//-- METHOD FOR RESPONSIVE TABLE IN DELETED RECORDS
									previousRec(previousRecTable);

									//Execute the dashboard insertBalanceTable method
									new dashboard().insertBalanceTable(studentID, finalTuition, downpayment);


								} else {
									JOptionPane.showMessageDialog(null, "Record failed to save!");
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
							}

							//UPDATE THE TOTAL RECORDS IN THE TABLE
							totalRecords();

							//UPDATE THE TOTAL DELETD RECORDS IN THE TABLE
							totalRecordsDeleted();
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
				register_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				register_panel.setBackground(new Color(51, 153, 0));
			}
		});
		register_panel.setBackground(new Color(51, 153, 0));
		register_panel.setBounds(352, 404, 347, 46);
		addRecord.add(register_panel);
		register_panel.setLayout(null);

		JLabel registerlbl = new JLabel("Register");
		registerlbl.setForeground(Color.WHITE);
		registerlbl.setFont(new Font("Arial", Font.BOLD, 24));
		registerlbl.setHorizontalAlignment(SwingConstants.CENTER);
		registerlbl.setBounds(0, 0, 347, 46);
		register_panel.add(registerlbl);

		JLabel icon7 = new JLabel("");
		icon7.setHorizontalAlignment(SwingConstants.CENTER);
		icon7.setBounds(296, 358, 56, 35);
		icon7.setIcon(new ImageIcon(payment));
		addRecord.add(icon7);

		passwordtxt = new JPasswordField("Password");
		passwordtxt.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordtxt.setEchoChar((char) 0);
		passwordtxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				passwordtxt.setEchoChar('●');
				passwordtxt.setText("");
			}
		});
		passwordtxt.setBounds(352, 207, 347, 35);
		addRecord.add(passwordtxt);

		updateRecord = new JPanel();
		updateRecord.setBackground(Color.WHITE);
		table_panel.add(updateRecord, "name_398873527008300");
		updateRecord.setLayout(null);

		JLabel lblUpdateRecord = new JLabel("Update Record");
		lblUpdateRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdateRecord.setForeground(Color.BLACK);
		lblUpdateRecord.setFont(new Font("Arial", Font.BOLD, 36));
		lblUpdateRecord.setBounds(368, 45, 315, 46);
		updateRecord.add(lblUpdateRecord);

		firstname_upt = new JTextField();
		firstname_upt.setText("Firstname");
		firstname_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		firstname_upt.setColumns(10);
		firstname_upt.setBounds(352, 184, 347, 35);
		updateRecord.add(firstname_upt);

		lastname_upt = new JTextField();
		lastname_upt.setText("Lastname");
		lastname_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		lastname_upt.setColumns(10);
		lastname_upt.setBounds(352, 219, 347, 35);
		updateRecord.add(lastname_upt);

		password_upt = new JPasswordField("Password");
		password_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		password_upt.setEchoChar(' ');
		password_upt.setBounds(352, 253, 347, 35);
		updateRecord.add(password_upt);

		age_upt = new JTextField();
		age_upt.setText("Age");
		age_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		age_upt.setColumns(10);
		age_upt.setBounds(352, 288, 347, 35);
		updateRecord.add(age_upt);

		genderBox_upt = new JComboBox(sex);
		genderBox_upt.setEditable(true);
		genderBox_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		genderBox_upt.setBounds(352, 323, 347, 35);
		updateRecord.add(genderBox_upt);

		gradeBox_upt = new JComboBox(grade);
		gradeBox_upt.setEditable(true);
		gradeBox_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		gradeBox_upt.setBounds(352, 358, 347, 35);
		updateRecord.add(gradeBox_upt);

		JPanel update_panel = new JPanel();
		update_panel.setLayout(null);
		update_panel.setBackground(new Color(51, 153, 0));
		update_panel.setBounds(352, 414, 347, 46);
		updateRecord.add(update_panel);

		JLabel lblUpdate = new JLabel("Update");
		lblUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				//--UPDATING VALUE OF DATABASE
				try {				
					int age = Integer.parseInt(age_upt.getText());
					pst = con.prepareStatement("UPDATE `student_credentials` SET `Password` = ?,`Firstname` = ?,`Lastname` = ?,`Gender` = ?,`Age` = ?, `Grade` = ? WHERE `Student ID` = ?");

					pst.setString(1, password_upt.getText());
					pst.setString(2, firstname_upt.getText());
					pst.setString(3, lastname_upt.getText());
					pst.setString(4, genderBox_upt.getSelectedItem().toString());
					pst.setInt(5, age);
					pst.setString(6, gradeBox_upt.getSelectedItem().toString());
					pst.setString(7, studentid_upt.getText());

					int k = pst.executeUpdate();

					if(k == 1){
						JOptionPane.showMessageDialog(null, "Record has been successfully updated!!");
						firstname_upt.setText("Firstname");
						lastname_upt.setText("Lastname");
						password_upt.setText("Password");
						age_upt.setText("Age");
						genderBox_upt.setSelectedItem("Male");
						gradeBox_upt.setSelectedItem("Grade 1");

						//-- METHOD FOR RESPONSIVE TABLE
						responsiveTable(Record_table);

						//-- METHOD FOR RESPONSIVE TABLE IN DELETED RECORDS
						previousRec(previousRecTable);
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				update_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				update_panel.setBackground(new Color(51, 153, 0));
			}
		});
		lblUpdate.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdate.setForeground(Color.WHITE);
		lblUpdate.setFont(new Font("Arial", Font.BOLD, 24));
		lblUpdate.setBounds(0, 0, 347, 46);
		update_panel.add(lblUpdate);

		studentid_upt = new JTextField("Student ID");
		studentid_upt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				studentid_upt.setText("");
			}
		});
		studentid_upt.setFont(new Font("Arial", Font.PLAIN, 16));
		studentid_upt.setColumns(10);
		studentid_upt.setBounds(352, 127, 347, 35);
		updateRecord.add(studentid_upt);

		JPanel upt_panel = new JPanel();
		upt_panel.setBackground(Color.LIGHT_GRAY);
		upt_panel.setBounds(289, 127, 56, 35);
		updateRecord.add(upt_panel);
		upt_panel.setLayout(null);

		JLabel updateIcon = new JLabel("");
		updateIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					pst = con.prepareStatement("SELECT * FROM `student_credentials` WHERE `Student ID` = ?");
					pst.setString(1, studentid_upt.getText());

					rs = pst.executeQuery();
					if(rs.next()) {
						firstname_upt.setText(rs.getString("Firstname"));
						lastname_upt.setText(rs.getString("Lastname"));
						password_upt.setText(rs.getString("Password"));
						age_upt.setText(rs.getString("Age"));
						genderBox_upt.setSelectedItem(rs.getString("Gender"));
						gradeBox_upt.setSelectedItem(rs.getString("Grade"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				upt_panel.setBackground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				upt_panel.setBackground(Color.LIGHT_GRAY);
			}
		});
		updateIcon.setBounds(0, 0, 56, 35);
		upt_panel.add(updateIcon);
		updateIcon.setHorizontalAlignment(SwingConstants.CENTER);
		updateIcon.setIcon(new ImageIcon(search));

		deleteRecord = new JPanel();
		deleteRecord.setBackground(Color.WHITE);
		table_panel.add(deleteRecord, "name_398877309308200");
		deleteRecord.setLayout(null);

		JLabel lblDeleteRecord = new JLabel("Delete Record");
		lblDeleteRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeleteRecord.setForeground(Color.BLACK);
		lblDeleteRecord.setFont(new Font("Arial", Font.BOLD, 36));
		lblDeleteRecord.setBounds(368, 45, 315, 46);
		deleteRecord.add(lblDeleteRecord);

		firstname_del = new JTextField();
		firstname_del.setEditable(false);
		firstname_del.setText("Firstname");
		firstname_del.setFont(new Font("Arial", Font.PLAIN, 16));
		firstname_del.setColumns(10);
		firstname_del.setBounds(352, 184, 347, 35);
		deleteRecord.add(firstname_del);

		lastname_del = new JTextField();
		lastname_del.setEditable(false);
		lastname_del.setText("Lastname");
		lastname_del.setFont(new Font("Arial", Font.PLAIN, 16));
		lastname_del.setColumns(10);
		lastname_del.setBounds(352, 219, 347, 35);
		deleteRecord.add(lastname_del);

		password_del = new JPasswordField("Password");
		password_del.setEditable(false);
		password_del.setFont(new Font("Arial", Font.PLAIN, 16));
		password_del.setEchoChar(' ');
		password_del.setBounds(352, 254, 347, 35);
		deleteRecord.add(password_del);

		age_del = new JTextField();
		age_del.setEditable(false);
		age_del.setText("Age");
		age_del.setFont(new Font("Arial", Font.PLAIN, 16));
		age_del.setColumns(10);
		age_del.setBounds(352, 289, 347, 35);
		deleteRecord.add(age_del);

		JComboBox genderbox_del = new JComboBox(sex);
		genderbox_del.setForeground(Color.BLACK);
		genderbox_del.setBackground(Color.WHITE);
		genderbox_del.setEnabled(false);
		genderbox_del.setFont(new Font("Arial", Font.PLAIN, 16));
		genderbox_del.setBounds(352, 324, 347, 35);
		deleteRecord.add(genderbox_del);

		JComboBox gradeBox_del = new JComboBox(grade);
		gradeBox_del.setForeground(Color.BLACK);
		gradeBox_del.setBackground(Color.WHITE);
		gradeBox_del.setEnabled(false);
		gradeBox_del.setFont(new Font("Arial", Font.PLAIN, 16));
		gradeBox_del.setBounds(352, 359, 347, 35);
		deleteRecord.add(gradeBox_del);

		JPanel del_panel = new JPanel();
		del_panel.setLayout(null);
		del_panel.setBackground(new Color(51, 153, 0));
		del_panel.setBounds(352, 414, 347, 46);
		deleteRecord.add(del_panel);

		JLabel dellbl = new JLabel("Delete");
		dellbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				//GET THE TUITION & DATE OF STUDENT WHO WANT TO DELETE
				int deletedPayment = 0;
				String deletedDate = "";
				try {
					pst = con.prepareStatement("SELECT `Tuition`, `Date Enrolled` FROM `student_credentials` WHERE `Student ID` = ?");
					pst.setString(1, studentID_del.getText());

					rs = pst.executeQuery();
					if(rs.next()) {
						deletedPayment = rs.getInt("Tuition");
						deletedDate = rs.getString("Date Enrolled");
					}else {
						JOptionPane.showMessageDialog(null, "Failed in getting the tuition & dateEnrolled!");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



				//TRANSFER THE DELETED RECORDS OF STUDENTS TO A TEMPORARY HOLDER
				int deletedAge = Integer.parseInt(age_del.getText());
				try {
					pst = con.prepareStatement("INSERT INTO `deleted_records` (`studentID`,`password`,`firstname`,`lastname`,`gender`,`age`, `grade`, `tuition`, `date_enrolled`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
					pst.setString(1, studentID_del.getText());
					pst.setString(2, password_del.getText());
					pst.setString(3, firstname_del.getText());
					pst.setString(4, lastname_del.getText());
					pst.setString(5, genderbox_del.getSelectedItem().toString());
					pst.setInt(6, deletedAge);
					pst.setString(7, gradeBox_del.getSelectedItem().toString());
					pst.setInt(8, deletedPayment);
					pst.setString(9, deletedDate);

					int k = pst.executeUpdate();
					if (k == 1) {

						//-- METHOD FOR RESPONSIVE TABLE
						responsiveTable(Record_table);

						//-- METHOD FOR RESPONSIVE TABLE IN DELETED RECORDS
						previousRec(previousRecTable);

					} else {
						JOptionPane.showMessageDialog(null, "Record failed to save in deleted_records table!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}



				//--DELETING A DATA RECORD IN THE DATABASE
				try {
					pst = con.prepareStatement("DELETE FROM `student_credentials` WHERE `Student ID` = ?");
					pst.setString(1, studentID_del.getText());

					int deleteRecord = pst.executeUpdate();

					if(deleteRecord == 1){
						JOptionPane.showMessageDialog(null, "Student Record has been successfully deleted!!");
						firstname_del.setText("Firstname");
						lastname_del.setText("Lastname");
						password_del.setText("Password");
						age_del.setText("Age");
						genderbox_del.setSelectedItem("");
						gradeBox_del.setSelectedItem("");

						//-- METHOD FOR RESPONSIVE TABLE
						responsiveTable(Record_table);

						//-- METHOD FOR RESPONSIVE TABLE IN DELETED RECORDS
						previousRec(previousRecTable);

						//GET THE UPDATED TOTAL COUNT RECORDS
						totalRecords();

						//GET THE TOTAL DELETD RECORDS IN THE TABLE
						totalRecordsDeleted();

					}else {
						JOptionPane.showMessageDialog(null, "Student Record failed to delete!!" + studentID_del.getText());
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}



				//--DELETING A DATA PAYMENT IN THE DATABASE
				try {
					pst1 = con1.prepareStatement("DELETE FROM `student_payment` WHERE `studentID` = ?");
					pst1.setString(1, studentID_del.getText());

					int delPayment = pst1.executeUpdate();

					if(delPayment == 1){
						firstname_del.setText("Firstname");
						lastname_del.setText("Lastname");
						password_del.setText("Password");
						age_del.setText("Age");
						genderbox_del.setSelectedItem("");
						gradeBox_del.setSelectedItem("");

						//-- METHOD FOR RESPONSIVE TABLE
						responsiveTable(Record_table);

						//-- METHOD FOR RESPONSIVE TABLE IN DELETED RECORDS
						previousRec(previousRecTable);
					}

				} catch (SQLException e2) {
					e2.printStackTrace();
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				del_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				del_panel.setBackground(new Color(51, 153, 0));
			}
		});
		dellbl.setHorizontalAlignment(SwingConstants.CENTER);
		dellbl.setForeground(Color.WHITE);
		dellbl.setFont(new Font("Arial", Font.BOLD, 24));
		dellbl.setBounds(0, 0, 347, 46);
		del_panel.add(dellbl);

		studentID_del = new JTextField("Student ID");
		studentID_del.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				studentID_del.setText("");		}
		});
		studentID_del.setFont(new Font("Arial", Font.PLAIN, 16));
		studentID_del.setColumns(10);
		studentID_del.setBounds(352, 127, 347, 35);
		deleteRecord.add(studentID_del);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(292, 127, 56, 35);
		deleteRecord.add(panel_1);

		JLabel icon1_1_2 = new JLabel("");
		icon1_1_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					pst = con.prepareStatement("SELECT * FROM `student_credentials` WHERE `Student ID` = ?");
					pst.setString(1, studentID_del.getText());

					rs = pst.executeQuery();
					if(rs.next()) {
						firstname_del.setText(rs.getString("Firstname"));
						lastname_del.setText(rs.getString("Lastname"));
						password_del.setText(rs.getString("Password"));
						age_del.setText(rs.getString("Age"));
						genderbox_del.setSelectedItem(rs.getString("Gender"));
						gradeBox_del.setSelectedItem(rs.getString("Grade"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panel_1.setBackground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel_1.setBackground(Color.LIGHT_GRAY);
			}
		});
		icon1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		icon1_1_2.setBounds(0, 0, 56, 35);
		icon1_1_2.setIcon(new ImageIcon(search));
		panel_1.add(icon1_1_2);

		previousEnrollee = new JPanel();
		previousEnrollee.setBackground(Color.WHITE);
		table_panel.add(previousEnrollee, "name_718507985824600");
		previousEnrollee.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 1051, 459);
		previousEnrollee.add(scrollPane_1);

		previousRecTable = new JTable();
		previousRecTable.setFont(new Font("Arial", Font.PLAIN, 16));
		previousRecTable.setRowHeight(32);
		scrollPane_1.setViewportView(previousRecTable);
		previousRecTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Student ID", "Password", "Firstname", "Lastname", "Gender", "Age", "Grade", "Tuition", "Date Enrolled"
				}
				));

		previousRec(previousRecTable);

		total_records = new JLabel("TOTAL RECORDS: ");
		total_records.setHorizontalAlignment(SwingConstants.CENTER);
		total_records.setFont(new Font("Arial Black", Font.PLAIN, 16));
		total_records.setBounds(726, 459, 325, 52);
		previousEnrollee.add(total_records);

		//Get the total Deleted Records
		totalRecordsDeleted();

		JPanel menu_panel = new JPanel();
		menu_panel.setBackground(new Color(0, 255, 255));
		menu_panel.setBounds(0, 189, 248, 511);
		contentPane.add(menu_panel);
		menu_panel.setLayout(null);

		JPanel add_menu = new JPanel();
		add_menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(addRecord);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				add_menu.setBackground(Color.GRAY);
				createlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				add_menu.setBackground(new Color(0, 255, 255));
				createlbl.setForeground(Color.BLACK);
			}
		});

		JPanel record_card = new JPanel();
		record_card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(record);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				record_card.setBackground(Color.GRAY);
				lblRecord.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				record_card.setBackground(new Color(0, 255, 255));
				lblRecord.setForeground(Color.BLACK);
			}
		});
		record_card.setLayout(null);
		record_card.setBackground(Color.CYAN);
		record_card.setBounds(0, 75, 248, 60);
		menu_panel.add(record_card);

		lblRecord = new JLabel("Records");
		lblRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecord.setFont(new Font("Arial", Font.BOLD, 20));
		lblRecord.setBounds(0, 0, 248, 60);
		record_card.add(lblRecord);

		JLabel recordlbl = new JLabel("");
		recordlbl.setHorizontalAlignment(SwingConstants.CENTER);
		recordlbl.setBounds(0, 0, 56, 60);
		recordlbl.setIcon(new ImageIcon(dash));
		record_card.add(recordlbl);
		add_menu.setLayout(null);
		add_menu.setBackground(new Color(0, 255, 255));
		add_menu.setBounds(0, 132, 248, 60);
		menu_panel.add(add_menu);

		createlbl = new JLabel("Add Record");
		createlbl.setHorizontalAlignment(SwingConstants.CENTER);
		createlbl.setFont(new Font("Arial", Font.BOLD, 20));
		createlbl.setBounds(0, 0, 248, 60);
		add_menu.add(createlbl);

		JLabel addlbl = new JLabel("");
		addlbl.setHorizontalAlignment(SwingConstants.CENTER);
		addlbl.setBounds(0, 0, 56, 60);
		addlbl.setIcon(new ImageIcon(add));
		add_menu.add(addlbl);

		JPanel update_menu = new JPanel();
		update_menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(updateRecord);


			}
			@Override
			public void mouseEntered(MouseEvent e) {
				update_menu.setBackground(Color.GRAY);
				updatelbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				update_menu.setBackground(new Color(0, 255, 255));
				updatelbl.setForeground(Color.BLACK);
			}
		});
		update_menu.setLayout(null);
		update_menu.setBackground(new Color(0, 255, 255));
		update_menu.setBounds(0, 191, 248, 60);
		menu_panel.add(update_menu);

		updatelbl = new JLabel("Update Record");
		updatelbl.setHorizontalAlignment(SwingConstants.CENTER);
		updatelbl.setFont(new Font("Arial", Font.BOLD, 20));
		updatelbl.setBounds(21, 0, 227, 60);
		update_menu.add(updatelbl);

		JLabel updateicon = new JLabel("");
		updateicon.setHorizontalAlignment(SwingConstants.CENTER);
		updateicon.setBounds(0, 0, 66, 60);
		updateicon.setIcon(new ImageIcon(update));
		update_menu.add(updateicon);

		JPanel delete_menu = new JPanel();
		delete_menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(deleteRecord);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				delete_menu.setBackground(Color.GRAY);
				deletelbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				delete_menu.setBackground(new Color(0, 255, 255));
				deletelbl.setForeground(Color.BLACK);
			}
		});
		delete_menu.setLayout(null);
		delete_menu.setBackground(new Color(0, 255, 255));
		delete_menu.setBounds(0, 251, 248, 60);
		menu_panel.add(delete_menu);

		deletelbl = new JLabel("Delete Record");
		deletelbl.setHorizontalAlignment(SwingConstants.CENTER);
		deletelbl.setFont(new Font("Arial", Font.BOLD, 20));
		deletelbl.setBounds(20, 0, 228, 60);
		delete_menu.add(deletelbl);

		JLabel removeicon = new JLabel("");
		removeicon.setHorizontalAlignment(SwingConstants.CENTER);
		removeicon.setBounds(0, 0, 56, 60);
		removeicon.setIcon(new ImageIcon(delete));
		delete_menu.add(removeicon);

		JPanel signout_menu = new JPanel();
		signout_menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new logIn().setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				signout_menu.setBackground(Color.RED);
				signoutlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				signout_menu.setBackground(new Color(0, 255, 255));
				signoutlbl.setForeground(Color.BLACK);
			}
		});
		signout_menu.setLayout(null);
		signout_menu.setBackground(new Color(0, 255, 255));
		signout_menu.setBounds(0, 451, 248, 60);
		menu_panel.add(signout_menu);

		signoutlbl = new JLabel("Sign out");
		signoutlbl.setHorizontalAlignment(SwingConstants.CENTER);
		signoutlbl.setFont(new Font("Arial", Font.BOLD, 20));
		signoutlbl.setBounds(0, 0, 248, 60);
		signout_menu.add(signoutlbl);

		txtSearch = new JTextField();
		txtSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtSearch.setText("");
			}
		});
		txtSearch.setHorizontalAlignment(SwingConstants.CENTER);
		txtSearch.setText("Search Student ID");
		txtSearch.setFont(new Font("Arial", Font.PLAIN, 18));
		txtSearch.setBounds(0, 0, 190, 50);
		menu_panel.add(txtSearch);
		txtSearch.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(190, 0, 58, 50);
		panel.setBackground(new Color(0, 255, 255));
		menu_panel.add(panel);
		panel.setLayout(null);

		JLabel searchIcon = new JLabel("");
		searchIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				search();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(new Color(0, 255, 255));
			}
		});
		searchIcon.setHorizontalAlignment(SwingConstants.CENTER);
		searchIcon.setBounds(0, 0, 58, 50);
		searchIcon.setIcon(new ImageIcon(search));
		panel.add(searchIcon);

		JPanel previousStudents = new JPanel();
		previousStudents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(previousEnrollee);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				previousStudents.setBackground(Color.GRAY);
				lblDeletedRecord.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				previousStudents.setBackground(new Color(0, 255, 255));
				lblDeletedRecord.setForeground(Color.BLACK);
			}
		});
		previousStudents.setLayout(null);
		previousStudents.setBackground(Color.CYAN);
		previousStudents.setBounds(0, 311, 248, 60);
		menu_panel.add(previousStudents);

		lblDeletedRecord = new JLabel("Previous Enrollees");
		lblDeletedRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeletedRecord.setFont(new Font("Arial", Font.BOLD, 20));
		lblDeletedRecord.setBounds(41, 0, 207, 60);
		previousStudents.add(lblDeletedRecord);

		JLabel removeicon_1 = new JLabel("");
		removeicon_1.setHorizontalAlignment(SwingConstants.CENTER);
		removeicon_1.setBounds(0, 0, 56, 60);
		removeicon_1.setIcon(new ImageIcon(data));
		previousStudents.add(removeicon_1);
		setLocationRelativeTo(null);
	}
}
