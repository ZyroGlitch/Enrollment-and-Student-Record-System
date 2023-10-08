package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.awt.CardLayout;
import javax.swing.border.SoftBevelBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.MatteBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class dashboard extends JFrame {

	static Image Logo = new ImageIcon(dashboard.class.getResource("/Enrollment_img/sksuLogo.png")).getImage().getScaledInstance(100,55,Image.SCALE_SMOOTH);
	static Image banner = new ImageIcon(dashboard.class.getResource("/Enrollment_img/banner.jpg")).getImage().getScaledInstance(995,213,Image.SCALE_SMOOTH);
	static Image payIcon = new ImageIcon(dashboard.class.getResource("/Enrollment_img/creditcard.png")).getImage().getScaledInstance(230,130,Image.SCALE_SMOOTH);
	static Image profileIcon = new ImageIcon(dashboard.class.getResource("/Enrollment_img/profile.png")).getImage().getScaledInstance(45,30,Image.SCALE_SMOOTH);
	static Image record = new ImageIcon(dashboard.class.getResource("/Enrollment_img/edit.png")).getImage().getScaledInstance(45,30,Image.SCALE_SMOOTH);

	//AVATARS
	static Image avatar1 = new ImageIcon(dashboard.class.getResource("/avatar_IMG/son.png")).getImage().getScaledInstance(250,180,Image.SCALE_SMOOTH);

	public static String loginData_studentid;
	//variables that has a value of subquery
	static String studentID, firstname, lastname, stud_gender, stud_age, stud_grade, dateEnrolled;
	static int db_totalPayment, db_totalBalance, totalTransaction;
	static String string_DB_totalBalance, string_DB_totalPayment, string_totalTrans, eachExams;

	public static int age, tuition, transferDownpayment,tuitionPay, receiptFrame;
	static String uppercase, overall, current = "", parse_examPaid;
	static int examPaid, total_payment, current_balance, total_assessment;

	static String paymentOption[] = {"Downpayment","Tuition fee"};
	static String gender[] = {"Male","Female"};
	static String grade[] = {"Grade 1","Grade 2","Grade 3","Grade 4","Grade 5","Grade 6"};
	static String promiReason[] = {"Delayed Allowance", "Delayed Salary", "Insufficient Cash/Fund"};

	static JLabel english, math, science, AP, music, arts, health, PE;
	static JLabel english1, math1, science1, TLE, ESP, music1, health1, PE1;

	private static JPanel contentPane;
	static JPanel content_cardLayout = new JPanel();
	static JPanel vision_card = new JPanel();
	static JPanel dashboard_card = new JPanel();
	static JPanel onlinePayment_card = new JPanel();
	static JPanel schedule_card = new JPanel();
	static JPanel assessment_card = new JPanel();
	static JPanel promi_card = new JPanel();
	static JPanel payhistory_card = new JPanel();
	static JPanel withdrawal_card = new JPanel();
	private static JPanel profile_card;
	private JTextField textField;
	public static JTextField amounttxt;
	static JLabel profilenamelbl;
	private JTextField studentIDtxt;
	private JTextField lastnametxt;
	private JTextField agetxt;
	private JTextField firstnametxt;
	static JLabel changeslbl;
	static JLabel cancellbl;
	static JPanel scheduleMainCard;
	static JPanel grade123Card;
	static JPanel grade456Card;
	static JLabel tuitionlbl;
	static JLabel athletics_fee;
	static JLabel audio_fee;
	static JLabel guidance_fee;
	static JLabel internet_fee;
	static JLabel library_fee;
	static JLabel medDen_fee;
	static JLabel insurance_fee;
	static JLabel booklet_fee;
	static JComboBox genderBox;
	static JComboBox gradeBox;
	static JLabel examPaidlbl;
	static JLabel totalPaymentlbl;
	static JLabel latest;
	public static JLabel total_transaction;
	public static JLabel getIDfrom_loginFrame;
	public static JTable table;
	static JComboBox optionPay_combobox;

	public static void cardLayout(Component cardPanel) {
		content_cardLayout.removeAll();
		content_cardLayout.add(cardPanel); 
		content_cardLayout.repaint();
		content_cardLayout.revalidate();

		//Payment History Table in Database
		responsiveTable(table);
	}

	static String getCurrentDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime currentDateTime = LocalDateTime.now();
		return formatter.format(currentDateTime);
	}


	static int totalAssessment() {
		return tuition + 3340;
	}
	
	static int perExam() {
		return db_totalBalance / 4;
	}



	static void grade123_Subs(String num) {
		english.setText(num + " - A");
		math.setText(num + " - A");
		science.setText("SCIENCE LAB 1");
		AP.setText(num + " - B");
		music.setText("MUSIC LAB");
		arts.setText(num + " - B");
		health.setText(num + " - B");
		PE.setText("GYM");
	}

	static void grade456_Subs(String num) {
		//		static JLabel english1, math1, science1, TLE, ESP, music1, health1, PE1;
		english1.setText(num + " - A");
		math1.setText(num + " - A");
		science1.setText("SCIENCE LAB 2");
		TLE.setText(num + " - A");
		ESP.setText(num + " - B");
		music1.setText("MUSIC LAB");
		health1.setText(num + " - B");
		PE1.setText("GYM");
	}


	//---DATABASE CONNECTION---//
	static Connection con, con1;
	static PreparedStatement pst, pst1;
	static ResultSet rs, rs1;
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


	//---PAYMENT HISTORY TABLE---//
	public static void responsiveTable(JTable pickTable) {
		try {

			int q;

			DefaultTableModel df = (DefaultTableModel)pickTable.getModel();
			pst = con.prepareStatement("SELECT * FROM `student_payment` WHERE `studentID` = ?");
			pst.setString(1, loginData_studentid);
			rs = pst.executeQuery();
			ResultSetMetaData table = rs.getMetaData();
			q = table.getColumnCount();
			df.setRowCount(0);

			while(rs.next()) {

				Vector v2 = new Vector();
				for(int a = 1; a <= q; a++) {
					v2.add(rs.getString("id"));
					v2.add(rs.getString("totalPayment"));
					v2.add(rs.getString("totalBalance"));
					v2.add(rs.getString("Purpose"));
					v2.add(rs.getString("transaction_Datetime"));
				}
				df.addRow(v2);
			}


		}catch(SQLException e) {
			e.printStackTrace();
		}
	}


	//---PAYMENT HISTORY TABLE FROM STUDENT RECORD FRAME---//
	public static void studentRecordTable(String studentID_records) {
		try {

			int q;

			DefaultTableModel df = (DefaultTableModel)table.getModel();
			pst = con.prepareStatement("SELECT * FROM `student_payment` WHERE `studentID` = ?");
			pst.setString(1, studentID_records);
			rs = pst.executeQuery();
			ResultSetMetaData table = rs.getMetaData();
			q = table.getColumnCount();
			df.setRowCount(0);

			while(rs.next()) {

				Vector v2 = new Vector();
				for(int a = 1; a <= q; a++) {
					v2.add(rs.getString("id"));
					v2.add(rs.getString("totalPayment"));
					v2.add(rs.getString("totalBalance"));
					v2.add(rs.getString("Purpose"));
					v2.add(rs.getString("transaction_Datetime"));
				}
				df.addRow(v2);
			}


		}catch(SQLException e) {
			e.printStackTrace();
		}
	}




	public static void insertBalanceTable(String student_id, int assessment, int totalPayment) {
		//Add the downpayment to particular studentID in the studentPayment database
		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formatDate = datetime.format(myFormatObj);
		String formattedDatetime= formatDate.toString();

		//CURRENT BALANCE
		current_balance = assessment - totalPayment;
		current = Integer.toString(db_totalBalance);
		try {
			pst = con.prepareStatement("INSERT INTO `student_payment` (`studentID`, `totalPayment`, `totalBalance`, `purpose`, `transaction_Datetime`) VALUES(?, ?, ?, ?,?)");
			pst.setString(1, student_id);
			pst.setInt(2, transferDownpayment);
			pst.setInt(3, current_balance);
			pst.setString(4, "Downpayment");
			pst.setString(5, formattedDatetime);
			pst.executeUpdate();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	public static void selectUpdated_totalBalance() {
		try {
			pst = con.prepareStatement("SELECT totalBalance FROM `student_payment` WHERE `studentID` = ? ORDER BY totalBalance LIMIT 1");
			pst.setString(1, loginData_studentid);
			rs = pst.executeQuery();

			if (rs.next()) {
				db_totalBalance = rs.getInt(1); // Use index 1 to retrieve the value
				string_DB_totalBalance = Integer.toString(db_totalBalance);
				latest.setText(string_DB_totalBalance);
			} else {
				JOptionPane.showMessageDialog(null, "Getting student_balance failed!!");
			}
		}catch(SQLException e1) {
			e1.printStackTrace();
		}
	}


	public static void selectUpdated_totalPayment() {
		try {
			pst1 = con1.prepareStatement("SELECT SUM(totalPayment) FROM `student_payment` WHERE `studentID` = ?");
			pst1.setString(1, loginData_studentid);
			rs1 = pst1.executeQuery();

			if (rs1.next()) {
				db_totalPayment = rs1.getInt(1); // Use index 1 to retrieve the value
				string_DB_totalPayment = Integer.toString(db_totalPayment);
				totalPaymentlbl.setText(string_DB_totalPayment);
			} else {
				JOptionPane.showMessageDialog(null, "Getting total payment failed!!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	public static void getAllCredentials(String student_id) {
		try {
			pst = con.prepareStatement("SELECT * FROM `student_credentials` WHERE `Student ID` = ?");
			pst.setString(1, student_id);
			rs = pst.executeQuery();

			if(rs.next() == true){
				studentID = rs.getString(2);
				firstname = rs.getString(4);
				lastname = rs.getString(5);
				stud_gender = rs.getString(6);
				stud_age = rs.getString(7);
				stud_grade = rs.getString(8);
				tuition = rs.getInt(9);
				dateEnrolled = rs.getString(10);

			}else {
				JOptionPane.showMessageDialog(null, "Getting all credentials failed!!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}


	public static void totalTransaction() {
		try {
			pst = con.prepareStatement("SELECT COUNT(totalPayment) FROM `student_payment` WHERE `studentID` = ?");
			pst.setString(1, loginData_studentid);
			rs = pst.executeQuery();

			if (rs.next()) {
				totalTransaction = rs.getInt(1); // Use index 1 to retrieve the value
				string_totalTrans = Integer.toString(totalTransaction);
				total_transaction.setText("TOTAL TRANSACTIONS : " + string_totalTrans);

			} else {
				JOptionPane.showMessageDialog(null, "Getting total transaction failed!!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}


	public static void onlinePaymentProceed() {
		new receipt().firstname = firstname;
		new receipt().lastname = lastname;
		new receipt().description = optionPay_combobox.getSelectedItem().toString();
		new receipt().amountPay = amounttxt.getText();
		new receipt().setVisible(true);

		try {

			//Add the downpayment to particular studentID in the studentPayment database
			LocalDateTime datetime = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String formatDate = datetime.format(myFormatObj);
			String formattedDatetime= formatDate.toString();

			examPaidlbl.setText(amounttxt.getText()); //To set the latest exam paid
			//Transfer after parsing the amount textfield into a int variable
			// And then store it in a query statement to execute it to payment table in database
			tuitionPay = Integer.parseInt(amounttxt.getText());

			//CURRENT BALANCE
			current_balance = db_totalBalance - tuitionPay;


			pst = con.prepareStatement("INSERT INTO `student_payment` (`studentID`, `totalPayment`, `totalBalance`, `purpose`, `transaction_Datetime`) VALUES(?, ?, ?, ?,?)");
			pst.setString(1, loginData_studentid);
			pst.setInt(2, tuitionPay);
			pst.setInt(3, current_balance);
			pst.setString(4, optionPay_combobox.getSelectedItem().toString());
			pst.setString(5, formattedDatetime);
			pst.executeUpdate();

			//GET THE UPDATE COUNT TRANSACTION
			totalTransaction();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		amounttxt.setText("0");
		optionPay_combobox.setSelectedItem("Downpayment");

		//GET THE UPDATED TOTAL BALANCE
		selectUpdated_totalBalance();
	}



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dashboard frame = new dashboard();
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
	public dashboard() { 
		Connect();

		//CURRENT BALANCE
		current_balance = totalAssessment() - total_payment;
		current = Integer.toString(db_totalBalance);

		getAllCredentials(loginData_studentid);

		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1300, 730);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel nav_panel = new JPanel();
		nav_panel.setBackground(Color.WHITE);
		nav_panel.setBounds(0, 0, 1300, 68);
		contentPane.add(nav_panel);
		nav_panel.setLayout(null);

		JLabel logolbl = new JLabel("");
		logolbl.setHorizontalAlignment(SwingConstants.CENTER);
		logolbl.setBounds(10, 0, 106, 68);
		logolbl.setIcon(new ImageIcon(Logo));
		nav_panel.add(logolbl);

		JLabel lblNewLabel = new JLabel("SKSU");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
		lblNewLabel.setBounds(126, 11, 178, 25);
		nav_panel.add(lblNewLabel);

		JLabel lblStudentPortal = new JLabel("STUDENT PORTAL ");
		lblStudentPortal.setFont(new Font("Arial", Font.BOLD, 18));
		lblStudentPortal.setBounds(126, 30, 178, 30);
		nav_panel.add(lblStudentPortal);

		JPanel profile_panel = new JPanel();
		profile_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				profile_panel.setBackground(Color.LIGHT_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				profile_panel.setBackground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				genderBox.setSelectedItem(stud_gender);
				gradeBox.setSelectedItem(stud_grade);
				cardLayout(profile_card);
			}
		});
		profile_panel.setBackground(Color.WHITE);
		profile_panel.setBounds(1069, 15, 200, 40);
		nav_panel.add(profile_panel);
		profile_panel.setLayout(null);

		uppercase = firstname.toUpperCase();
		profilenamelbl = new JLabel(uppercase);
		profilenamelbl.setBounds(60, 0, 140, 40);
		profile_panel.add(profilenamelbl);
		profilenamelbl.setFont(new Font("Arial", Font.PLAIN, 18));
		profilenamelbl.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel profilelbl = new JLabel("");
		profilelbl.setBounds(28, 0, 50, 40);
		profile_panel.add(profilelbl);
		profilelbl.setHorizontalAlignment(SwingConstants.CENTER);
		profilelbl.setIcon(new ImageIcon(profileIcon));

		JPanel record_panel = new JPanel();
		record_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Payment History Table in Database
				responsiveTable(table);


				new studentRecords().setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				record_panel.setBackground(Color.LIGHT_GRAY);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				record_panel.setBackground(Color.WHITE);
			}
		});
		record_panel.setLayout(null);
		record_panel.setBackground(Color.WHITE);
		record_panel.setBounds(831, 15, 200, 40);
		nav_panel.add(record_panel);

		JLabel lblRecord = new JLabel("RECORD");
		lblRecord.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecord.setFont(new Font("Arial", Font.PLAIN, 18));
		lblRecord.setBounds(60, 0, 140, 40);
		record_panel.add(lblRecord);

		JLabel recordlbl = new JLabel("");
		recordlbl.setHorizontalAlignment(SwingConstants.CENTER);
		recordlbl.setBounds(21, 0, 50, 40);
		recordlbl.setIcon(new ImageIcon(record));
		record_panel.add(recordlbl);

		getIDfrom_loginFrame = new JLabel(loginData_studentid);
		getIDfrom_loginFrame.setVisible(false);
		getIDfrom_loginFrame.setHorizontalAlignment(SwingConstants.CENTER);
		getIDfrom_loginFrame.setBounds(654, 18, 86, 39);
		nav_panel.add(getIDfrom_loginFrame);

		JPanel menu_panel = new JPanel();
		menu_panel.setBorder(null);
		menu_panel.setBackground(Color.LIGHT_GRAY);
		menu_panel.setBounds(0, 68, 306, 662);
		contentPane.add(menu_panel);
		menu_panel.setLayout(null);

		JPanel vision_panel = new JPanel();
		vision_panel.setBackground(Color.LIGHT_GRAY);
		vision_panel.setBounds(0, 51, 306, 60);
		menu_panel.add(vision_panel);
		vision_panel.setLayout(null);

		JLabel visionlbl = new JLabel("Vision and Mission");
		visionlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(vision_card);
				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE
				selectUpdated_totalBalance();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				vision_panel.setBackground(Color.DARK_GRAY);
				visionlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				vision_panel.setBackground(Color.LIGHT_GRAY);
				visionlbl.setForeground(Color.BLACK);
			}
		});
		visionlbl.setFont(new Font("Arial", Font.PLAIN, 20));
		visionlbl.setHorizontalAlignment(SwingConstants.CENTER);
		visionlbl.setBounds(0, 0, 306, 60);
		vision_panel.add(visionlbl);

		JPanel dashboard_panel = new JPanel();
		dashboard_panel.setBorder(null);
		dashboard_panel.setBackground(Color.LIGHT_GRAY);
		dashboard_panel.setLayout(null);
		dashboard_panel.setBounds(0, 110, 306, 60);
		menu_panel.add(dashboard_panel);

		JLabel dashboardlbl = new JLabel("Dashboard");
		dashboardlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(dashboard_card);
				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE & PAYMENT
				selectUpdated_totalBalance();
				selectUpdated_totalPayment();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				dashboard_panel.setBackground(Color.DARK_GRAY);
				dashboardlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				dashboard_panel.setBackground(Color.LIGHT_GRAY);
				dashboardlbl.setForeground(Color.BLACK);
			}
		});
		dashboardlbl.setBorder(null);
		dashboardlbl.setHorizontalAlignment(SwingConstants.CENTER);
		dashboardlbl.setFont(new Font("Arial", Font.PLAIN, 20));
		dashboardlbl.setBounds(0, 0, 306, 60);
		dashboard_panel.add(dashboardlbl);

		JPanel payment_panel = new JPanel();
		payment_panel.setBackground(Color.LIGHT_GRAY);
		payment_panel.setLayout(null);
		payment_panel.setBounds(0, 168, 306, 60);
		menu_panel.add(payment_panel);

		JLabel paymentlbl = new JLabel("Online Payment");
		paymentlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(onlinePayment_card);
				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE
				selectUpdated_totalBalance();

				//UPDATE THE TOTAL COUNT TRANSACTION 
				totalTransaction();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				payment_panel.setBackground(Color.DARK_GRAY);
				paymentlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				payment_panel.setBackground(Color.LIGHT_GRAY);
				paymentlbl.setForeground(Color.BLACK);
			}
		});
		paymentlbl.setHorizontalAlignment(SwingConstants.CENTER);
		paymentlbl.setFont(new Font("Arial", Font.PLAIN, 20));
		paymentlbl.setBounds(0, 0, 306, 60);
		payment_panel.add(paymentlbl);

		JPanel classSchedule_panel = new JPanel();
		classSchedule_panel.setBackground(Color.LIGHT_GRAY);
		classSchedule_panel.setLayout(null);
		classSchedule_panel.setBounds(0, 228, 306, 60);
		menu_panel.add(classSchedule_panel);

		JLabel classlbl = new JLabel("Class Schedule");
		classlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(stud_grade.equals("Grade 1") || stud_grade.equals("Grade 2") || stud_grade.equals("Grade 3")) {
					scheduleMainCard.removeAll();
					scheduleMainCard.add(grade123Card); 
					scheduleMainCard.repaint();
					scheduleMainCard.revalidate();
				}else {
					scheduleMainCard.removeAll();
					scheduleMainCard.add(grade456Card); 
					scheduleMainCard.repaint();
					scheduleMainCard.revalidate();
				}
				cardLayout(schedule_card);

				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE
				selectUpdated_totalBalance();


				switch(stud_grade){
				case "Grade 1":
					grade123_Subs("1");
					break;
				case "Grade 2":
					grade123_Subs("2");
					break;
				case "Grade 3":
					grade123_Subs("3");
					break;
				case "Grade 4":
					grade456_Subs("4");
					break;
				case "Grade 5":
					grade456_Subs("5");
					break;
				case "Grade 6":
					grade456_Subs("6");
					break;
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				classSchedule_panel.setBackground(Color.DARK_GRAY);
				classlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				classSchedule_panel.setBackground(Color.LIGHT_GRAY);
				classlbl.setForeground(Color.BLACK);
			}
		});
		classlbl.setHorizontalAlignment(SwingConstants.CENTER);
		classlbl.setFont(new Font("Arial", Font.PLAIN, 20));
		classlbl.setBounds(0, 0, 306, 60);
		classSchedule_panel.add(classlbl);

		JPanel assessment_panel = new JPanel();
		assessment_panel.setBackground(Color.LIGHT_GRAY);
		assessment_panel.setLayout(null);
		assessment_panel.setBounds(0, 288, 306, 60);
		menu_panel.add(assessment_panel);



		JLabel assessmentlbl = new JLabel("Assessment");
		assessmentlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(assessment_card);

				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE
				selectUpdated_totalBalance();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				assessment_panel.setBackground(Color.DARK_GRAY);
				assessmentlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				assessment_panel.setBackground(Color.LIGHT_GRAY);
				assessmentlbl.setForeground(Color.BLACK);
			}
		});
		assessmentlbl.setHorizontalAlignment(SwingConstants.CENTER);
		assessmentlbl.setFont(new Font("Arial", Font.PLAIN, 20));
		assessmentlbl.setBounds(0, 0, 306, 60);
		assessment_panel.add(assessmentlbl);

		JPanel paymentHistory_panel = new JPanel();
		paymentHistory_panel.setBackground(Color.LIGHT_GRAY);
		paymentHistory_panel.setLayout(null);
		paymentHistory_panel.setBounds(0, 346, 306, 60);
		menu_panel.add(paymentHistory_panel);

		JLabel paymentHistolbl = new JLabel("Payment History");
		paymentHistolbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout(payhistory_card);

				//EXAM PAID
				examPaid = tuitionPay;
				parse_examPaid = Integer.toString(examPaid);

				//CURRENT BALANCE
				current_balance = total_assessment - total_payment;
				current = Integer.toString(current_balance);

				//Payment History Table in Database
				responsiveTable(table);

				//GET THE UPDATED TOTAL BALANCE
				selectUpdated_totalBalance();

				//UPDATED TOTAL COUNT OF TRANSACTION
				totalTransaction();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				paymentHistory_panel.setBackground(Color.DARK_GRAY);
				paymentHistolbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				paymentHistory_panel.setBackground(Color.LIGHT_GRAY);
				paymentHistolbl.setForeground(Color.BLACK);
			}
		});
		paymentHistolbl.setHorizontalAlignment(SwingConstants.CENTER);
		paymentHistolbl.setFont(new Font("Arial", Font.PLAIN, 20));
		paymentHistolbl.setBounds(0, 0, 306, 60);
		paymentHistory_panel.add(paymentHistolbl);

		JPanel signout_panel = new JPanel();
		signout_panel.setLayout(null);
		signout_panel.setBackground(Color.LIGHT_GRAY);
		signout_panel.setBounds(0, 602, 306, 60);
		menu_panel.add(signout_panel);

		JLabel signoutlbl = new JLabel("Sign out");
		signoutlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new logIn().setVisible(true);
				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				signout_panel.setBackground(Color.RED);
				signoutlbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				signout_panel.setBackground(Color.LIGHT_GRAY);
				signoutlbl.setForeground(Color.BLACK);
			}
		});
		signoutlbl.setHorizontalAlignment(SwingConstants.CENTER);
		signoutlbl.setFont(new Font("Arial", Font.PLAIN, 26));
		signoutlbl.setBounds(0, 0, 306, 60);
		signout_panel.add(signoutlbl);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 2, 2);
		signout_panel.add(scrollPane_1);

		content_cardLayout = new JPanel();
		content_cardLayout.setBackground(Color.LIGHT_GRAY);
		content_cardLayout.setBounds(305, 68, 995, 662);
		contentPane.add(content_cardLayout);
		content_cardLayout.setLayout(new CardLayout(0, 0));

		vision_card = new JPanel();
		vision_card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Payment History Table in Database
				responsiveTable(table);
			}
		});
		vision_card.setBackground(Color.WHITE);
		content_cardLayout.add(vision_card, "name_370408928796900");
		vision_card.setLayout(null);

		JLabel sksubg_lbl = new JLabel("");
		sksubg_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		sksubg_lbl.setBounds(0, 0, 995, 213);
		sksubg_lbl.setIcon(new ImageIcon(banner));
		vision_card.add(sksubg_lbl);

		JLabel vissionlbl = new JLabel("VISION");
		vissionlbl.setHorizontalAlignment(SwingConstants.CENTER);
		vissionlbl.setForeground(new Color(51, 153, 0));
		vissionlbl.setFont(new Font("Arial", Font.BOLD, 32));
		vissionlbl.setBounds(412, 240, 171, 40);
		vision_card.add(vissionlbl);

		JLabel visiondeslbl = new JLabel("A trailblazer in arts, science and technology in the region.");
		visiondeslbl.setFont(new Font("Arial", Font.ITALIC, 18));
		visiondeslbl.setHorizontalAlignment(SwingConstants.CENTER);
		visiondeslbl.setBounds(250, 290, 494, 40);
		vision_card.add(visiondeslbl);

		JLabel missionlbl = new JLabel("MISSION");
		missionlbl.setHorizontalAlignment(SwingConstants.CENTER);
		missionlbl.setForeground(new Color(51, 153, 0));
		missionlbl.setFont(new Font("Arial", Font.BOLD, 32));
		missionlbl.setBounds(412, 370, 171, 40);
		vision_card.add(missionlbl);

		JLabel des1lbl = new JLabel("The University shall primarily provide advanced");
		des1lbl.setHorizontalAlignment(SwingConstants.CENTER);
		des1lbl.setFont(new Font("Arial", Font.ITALIC, 18));
		des1lbl.setBounds(250, 420, 494, 40);
		vision_card.add(des1lbl);

		JLabel deslbl2 = new JLabel("instruction and professional training in science and");
		deslbl2.setHorizontalAlignment(SwingConstants.CENTER);
		deslbl2.setFont(new Font("Arial", Font.ITALIC, 18));
		deslbl2.setBounds(250, 445, 494, 40);
		vision_card.add(deslbl2);

		JLabel deslbl3 = new JLabel("technology, agriculture, fisheries, education and other");
		deslbl3.setHorizontalAlignment(SwingConstants.CENTER);
		deslbl3.setFont(new Font("Arial", Font.ITALIC, 18));
		deslbl3.setBounds(250, 470, 494, 40);
		vision_card.add(deslbl3);

		JLabel deslbl4 = new JLabel("relevant fields of study. It shall also undertake research");
		deslbl4.setHorizontalAlignment(SwingConstants.CENTER);
		deslbl4.setFont(new Font("Arial", Font.ITALIC, 18));
		deslbl4.setBounds(250, 495, 494, 40);
		vision_card.add(deslbl4);

		JLabel deslbl5 = new JLabel("and extension services and provide progressive");
		deslbl5.setHorizontalAlignment(SwingConstants.CENTER);
		deslbl5.setFont(new Font("Arial", Font.ITALIC, 18));
		deslbl5.setBounds(250, 520, 494, 40);
		vision_card.add(deslbl5);

		JLabel deslbl6 = new JLabel("leadership in its areas of specialization.");
		deslbl6.setHorizontalAlignment(SwingConstants.CENTER);
		deslbl6.setFont(new Font("Arial", Font.ITALIC, 18));
		deslbl6.setBounds(250, 545, 494, 40);
		vision_card.add(deslbl6);

		dashboard_card = new JPanel();
		dashboard_card.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		dashboard_card.setBackground(Color.WHITE);
		content_cardLayout.add(dashboard_card, "name_370464282725800");
		dashboard_card.setLayout(null);

		JLabel studentID_lbl = new JLabel(studentID);
		studentID_lbl.setForeground(new Color(51, 153, 0));
		studentID_lbl.setFont(new Font("Arial", Font.BOLD, 32));
		studentID_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		studentID_lbl.setBounds(385, 49, 224, 46);
		dashboard_card.add(studentID_lbl);

		String studentname = lastname + ", " + firstname;
		JLabel namelbl = new JLabel(studentname);
		namelbl.setHorizontalAlignment(SwingConstants.CENTER);
		namelbl.setForeground(Color.DARK_GRAY);
		namelbl.setFont(new Font("Arial", Font.BOLD, 32));
		namelbl.setBounds(340, 93, 315, 46);
		dashboard_card.add(namelbl);

		JLabel gradelbl = new JLabel(stud_grade);
		gradelbl.setHorizontalAlignment(SwingConstants.CENTER);
		gradelbl.setForeground(Color.DARK_GRAY);
		gradelbl.setFont(new Font("Arial", Font.BOLD, 24));
		gradelbl.setBounds(362, 138, 270, 46);
		dashboard_card.add(gradelbl);

		JPanel examPaid_panel = new JPanel();
		examPaid_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), null, Color.BLACK, null));
		examPaid_panel.setBackground(Color.LIGHT_GRAY);
		examPaid_panel.setBounds(10, 265, 289, 117);
		dashboard_card.add(examPaid_panel);
		examPaid_panel.setLayout(null);

		//EXAM PAID
		examPaid = transferDownpayment;
		parse_examPaid = Integer.toString(examPaid);

		examPaidlbl = new JLabel(parse_examPaid);
		examPaidlbl.setFont(new Font("Arial Black", Font.BOLD, 32));
		examPaidlbl.setBounds(64, 11, 215, 53);
		examPaid_panel.add(examPaidlbl);

		JLabel lblNewLabel_2 = new JLabel("Exams Paid");
		lblNewLabel_2.setForeground(Color.BLACK);
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 26));
		lblNewLabel_2.setBounds(10, 73, 269, 33);
		examPaid_panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_6 = new JLabel("P");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setFont(new Font("Arial Black", Font.BOLD, 32));
		lblNewLabel_6.setBounds(10, 11, 44, 53);
		examPaid_panel.add(lblNewLabel_6);

		JPanel totalAsses_panel = new JPanel();
		totalAsses_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), null, Color.BLACK, null));
		totalAsses_panel.setBackground(Color.LIGHT_GRAY);
		totalAsses_panel.setBounds(353, 265, 289, 117);
		dashboard_card.add(totalAsses_panel);
		totalAsses_panel.setLayout(null);

		String labeltotalAssessment = Integer.toString(totalAssessment());

		JLabel totalAssessmentlbl = new JLabel(labeltotalAssessment);
		totalAssessmentlbl.setFont(new Font("Arial Black", Font.BOLD, 32));
		totalAssessmentlbl.setBounds(64, 11, 215, 53);
		totalAsses_panel.add(totalAssessmentlbl);

		JLabel lblNewLabel_2_1 = new JLabel("Total Assessment");
		lblNewLabel_2_1.setForeground(Color.BLACK);
		lblNewLabel_2_1.setFont(new Font("Arial", Font.PLAIN, 26));
		lblNewLabel_2_1.setBounds(10, 73, 269, 33);
		totalAsses_panel.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_6_1 = new JLabel("P");
		lblNewLabel_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1.setFont(new Font("Arial Black", Font.BOLD, 32));
		lblNewLabel_6_1.setBounds(10, 11, 44, 53);
		totalAsses_panel.add(lblNewLabel_6_1);

		JPanel perExam_panel = new JPanel();
		perExam_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), null, Color.BLACK, null));
		perExam_panel.setBackground(Color.LIGHT_GRAY);
		perExam_panel.setBounds(696, 265, 289, 117);
		dashboard_card.add(perExam_panel);
		perExam_panel.setLayout(null);

//
//		switch(stud_grade) {
//		case "Grade 1":
//			eachExams = "3335";
//			break;
//		case "Grade 2":
//			eachExams = "3835";
//			break;
//		case "Grade 3":
//			eachExams = "4085";
//			break;
//		case "Grade 4":
//			eachExams = "4335";
//			break;
//		case "Grade 5":
//			eachExams = "4585";
//			break;
//		case "Grade 6":
//			eachExams = "4835";
//			break;	
//		}
		String parseExam = Integer.toString(perExam());
		
		JLabel perExam = new JLabel(parseExam);
		perExam.setFont(new Font("Arial Black", Font.BOLD, 32));
		perExam.setBounds(64, 11, 215, 53);
		perExam_panel.add(perExam);

		JLabel lblNewLabel_2_1_1 = new JLabel("Quartely Assessment");
		lblNewLabel_2_1_1.setForeground(Color.BLACK);
		lblNewLabel_2_1_1.setFont(new Font("Arial", Font.PLAIN, 26));
		lblNewLabel_2_1_1.setBounds(10, 75, 269, 33);
		perExam_panel.add(lblNewLabel_2_1_1);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("P");
		lblNewLabel_6_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1_1.setFont(new Font("Arial Black", Font.BOLD, 32));
		lblNewLabel_6_1_1.setBounds(10, 11, 44, 53);
		perExam_panel.add(lblNewLabel_6_1_1);

		JPanel totalPayment_panel = new JPanel();
		totalPayment_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), null, Color.BLACK, null));
		totalPayment_panel.setBackground(Color.LIGHT_GRAY);
		totalPayment_panel.setBounds(178, 434, 289, 117);
		dashboard_card.add(totalPayment_panel);
		totalPayment_panel.setLayout(null);

		//TOTAL PAYMENT
		total_payment = transferDownpayment + tuitionPay;

		totalPaymentlbl = new JLabel(string_DB_totalPayment);
		totalPaymentlbl.setFont(new Font("Arial Black", Font.BOLD, 32));
		totalPaymentlbl.setBounds(64, 11, 215, 53);
		totalPayment_panel.add(totalPaymentlbl);

		JLabel lblNewLabel_2_1_1_3 = new JLabel("Total Payment");
		lblNewLabel_2_1_1_3.setForeground(Color.BLACK);
		lblNewLabel_2_1_1_3.setFont(new Font("Arial", Font.PLAIN, 26));
		lblNewLabel_2_1_1_3.setBounds(10, 73, 269, 33);
		totalPayment_panel.add(lblNewLabel_2_1_1_3);
		
		JLabel lblNewLabel_6_1_3 = new JLabel("P");
		lblNewLabel_6_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1_3.setFont(new Font("Arial Black", Font.BOLD, 32));
		lblNewLabel_6_1_3.setBounds(10, 11, 44, 53);
		totalPayment_panel.add(lblNewLabel_6_1_3);

		JPanel currentBal_panel = new JPanel();
		currentBal_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), null, Color.BLACK, null));
		currentBal_panel.setBackground(Color.LIGHT_GRAY);
		currentBal_panel.setBounds(530, 434, 289, 117);
		dashboard_card.add(currentBal_panel);
		currentBal_panel.setLayout(null);

		latest = new JLabel(string_DB_totalBalance);
		latest.setFont(new Font("Arial Black", Font.BOLD, 32));
		latest.setBounds(64, 11, 215, 53);
		currentBal_panel.add(latest);

		JLabel lblNewLabel_2_1_1_2 = new JLabel("Current Balance");
		lblNewLabel_2_1_1_2.setForeground(Color.BLACK);
		lblNewLabel_2_1_1_2.setFont(new Font("Arial", Font.PLAIN, 26));
		lblNewLabel_2_1_1_2.setBounds(10, 73, 269, 33);
		currentBal_panel.add(lblNewLabel_2_1_1_2);
		
		JLabel lblNewLabel_6_1_4 = new JLabel("P");
		lblNewLabel_6_1_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1_4.setFont(new Font("Arial Black", Font.BOLD, 32));
		lblNewLabel_6_1_4.setBounds(10, 11, 44, 53);
		currentBal_panel.add(lblNewLabel_6_1_4);

		onlinePayment_card = new JPanel();
		onlinePayment_card.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		onlinePayment_card.setBackground(Color.WHITE);
		content_cardLayout.add(onlinePayment_card, "name_370499054866600");
		onlinePayment_card.setLayout(null);

		JLabel lblOlinePayment = new JLabel("Online Payment");
		lblOlinePayment.setHorizontalAlignment(SwingConstants.CENTER);
		lblOlinePayment.setForeground(Color.DARK_GRAY);
		lblOlinePayment.setFont(new Font("Arial", Font.BOLD, 36));
		lblOlinePayment.setBounds(340, 49, 315, 46);
		onlinePayment_card.add(lblOlinePayment);

		JLabel lblPaymentDetails = new JLabel("Payment Details");
		lblPaymentDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblPaymentDetails.setForeground(Color.DARK_GRAY);
		lblPaymentDetails.setFont(new Font("Arial", Font.PLAIN, 22));
		lblPaymentDetails.setBounds(362, 106, 270, 46);
		onlinePayment_card.add(lblPaymentDetails);

		JPanel paymentForm_panel = new JPanel();
		paymentForm_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.GRAY, null, Color.GRAY, null));
		paymentForm_panel.setBackground(Color.DARK_GRAY);
		paymentForm_panel.setBounds(227, 163, 540, 458);
		onlinePayment_card.add(paymentForm_panel);
		paymentForm_panel.setLayout(null);

		JLabel payIconlbl = new JLabel("");
		payIconlbl.setHorizontalAlignment(SwingConstants.CENTER);
		payIconlbl.setBounds(0, 0, 540, 174);
		payIconlbl.setIcon(new ImageIcon(payIcon));
		paymentForm_panel.add(payIconlbl);

		JLabel lblNewLabel_3 = new JLabel("Student ID:");
		lblNewLabel_3.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(29, 176, 482, 34);
		paymentForm_panel.add(lblNewLabel_3);

		textField = new JTextField(studentID);
		textField.setFont(new Font("Arial", Font.PLAIN, 22));
		textField.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		textField.setBounds(29, 210, 482, 34);
		paymentForm_panel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_3_1 = new JLabel("Item:");
		lblNewLabel_3_1.setForeground(Color.WHITE);
		lblNewLabel_3_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_3_1.setBounds(29, 255, 482, 34);
		paymentForm_panel.add(lblNewLabel_3_1);

		optionPay_combobox = new JComboBox(paymentOption);
		optionPay_combobox.setBorder(null);
		optionPay_combobox.setFont(new Font("Arial", Font.PLAIN, 16));
		optionPay_combobox.setForeground(Color.BLACK);
		optionPay_combobox.setBackground(Color.WHITE);
		optionPay_combobox.setBounds(29, 288, 482, 34);
		paymentForm_panel.add(optionPay_combobox);

		JLabel lblNewLabel_3_1_1 = new JLabel("Amount:");
		lblNewLabel_3_1_1.setForeground(Color.WHITE);
		lblNewLabel_3_1_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_3_1_1.setBounds(29, 329, 482, 34);
		paymentForm_panel.add(lblNewLabel_3_1_1);

		amounttxt = new JTextField("0");
		amounttxt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				amounttxt.setText("");
			}
		});
		amounttxt.setFont(new Font("Arial", Font.PLAIN, 22));
		amounttxt.setColumns(10);
		amounttxt.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		amounttxt.setBounds(29, 361, 482, 34);
		paymentForm_panel.add(amounttxt);

		JPanel proceed_panel = new JPanel();
		proceed_panel.setBounds(126, 406, 288, 41);
		proceed_panel.setBackground(new Color(51, 153, 0));
		paymentForm_panel.add(proceed_panel);
		proceed_panel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("PROCEED");
		lblNewLabel_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					int value = Integer.parseInt(amounttxt.getText());
					if(stud_grade.equals("Grade 1") && value >= 1234) {
						onlinePaymentProceed();
					}else if(stud_grade.equals("Grade 2") && value >= 1434) {
						onlinePaymentProceed();
					}else if(stud_grade.equals("Grade 3") && value >= 1534) {
						onlinePaymentProceed();
					}else if(stud_grade.equals("Grade 4") && value >= 1634) {
						onlinePaymentProceed();
					}else if(stud_grade.equals("Grade 5") && value >= 1734) {
						onlinePaymentProceed();
					}else if(stud_grade.equals("Grade 6") && value >= 1834) {
						onlinePaymentProceed();

					}else {
						int minimumPay = 0;
						switch(stud_grade) {
						case "Grade 1":
							minimumPay = 1234;
							break;
						case "Grade 2":
							minimumPay = 1434;
							break;
						case "Grade 3":
							minimumPay = 1534;
							break;
						case "Grade 4":
							minimumPay = 1634;
							break;
						case "Grade 5":
							minimumPay = 1734;
							break;
						case "Grade 6":
							minimumPay = 1834;
							break;
						}
						JOptionPane.showMessageDialog(null, "The minimum payment is " + minimumPay);

					}
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "Please put some amount!");
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				proceed_panel.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				proceed_panel.setBackground(new Color(51, 153, 0));
			}
		});
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 0, 288, 41);
		proceed_panel.add(lblNewLabel_1);

		schedule_card = new JPanel();
		schedule_card.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		schedule_card.setBackground(Color.WHITE);
		content_cardLayout.add(schedule_card, "name_370541831207900");
		schedule_card.setLayout(null);

		JLabel studentID_sched = new JLabel(studentID);
		studentID_sched.setHorizontalAlignment(SwingConstants.CENTER);
		studentID_sched.setForeground(new Color(51, 153, 0));
		studentID_sched.setFont(new Font("Arial", Font.BOLD, 32));
		studentID_sched.setBounds(385, 49, 224, 46);
		schedule_card.add(studentID_sched);

		JLabel name_schedulelbl = new JLabel(studentname);
		name_schedulelbl.setHorizontalAlignment(SwingConstants.CENTER);
		name_schedulelbl.setForeground(Color.DARK_GRAY);
		name_schedulelbl.setFont(new Font("Arial", Font.BOLD, 32));
		name_schedulelbl.setBounds(340, 93, 315, 46);
		schedule_card.add(name_schedulelbl);

		JLabel gradelbl_1 = new JLabel(stud_grade);
		gradelbl_1.setHorizontalAlignment(SwingConstants.CENTER);
		gradelbl_1.setForeground(Color.DARK_GRAY);
		gradelbl_1.setFont(new Font("Arial", Font.BOLD, 24));
		gradelbl_1.setBounds(362, 138, 270, 46);
		schedule_card.add(gradelbl_1);

		JLabel gradelbl_1_1 = new JLabel("Official Class Schedule");
		gradelbl_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		gradelbl_1_1.setForeground(Color.DARK_GRAY);
		gradelbl_1_1.setFont(new Font("Arial", Font.BOLD, 24));
		gradelbl_1_1.setBounds(10, 196, 270, 46);
		schedule_card.add(gradelbl_1_1);

		JPanel schedule_panel = new JPanel();
		schedule_panel.setBounds(10, 237, 975, 384);
		schedule_card.add(schedule_panel);
		schedule_panel.setLayout(null);

		JPanel scheduleMenu_panel = new JPanel();
		scheduleMenu_panel.setBackground(new Color(51, 153, 0));
		scheduleMenu_panel.setBounds(0, 0, 975, 55);
		schedule_panel.add(scheduleMenu_panel);
		scheduleMenu_panel.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel("Subject");
		lblNewLabel_4.setForeground(Color.WHITE);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Arial Black", Font.PLAIN, 26));
		lblNewLabel_4.setBounds(0, 0, 230, 55);
		scheduleMenu_panel.add(lblNewLabel_4);

		JLabel lblNewLabel_4_3 = new JLabel("Status");
		lblNewLabel_4_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_3.setForeground(Color.WHITE);
		lblNewLabel_4_3.setFont(new Font("Arial Black", Font.PLAIN, 26));
		lblNewLabel_4_3.setBounds(745, 0, 230, 55);
		scheduleMenu_panel.add(lblNewLabel_4_3);

		JLabel lblNewLabel_4_1 = new JLabel("Time");
		lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_1.setForeground(Color.WHITE);
		lblNewLabel_4_1.setFont(new Font("Arial Black", Font.PLAIN, 26));
		lblNewLabel_4_1.setBounds(240, 0, 230, 55);
		scheduleMenu_panel.add(lblNewLabel_4_1);

		JLabel lblNewLabel_4_2 = new JLabel("Room");
		lblNewLabel_4_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_2.setForeground(Color.WHITE);
		lblNewLabel_4_2.setFont(new Font("Arial Black", Font.PLAIN, 26));
		lblNewLabel_4_2.setBounds(505, 0, 230, 55);
		scheduleMenu_panel.add(lblNewLabel_4_2);


		scheduleMainCard = new JPanel();
		scheduleMainCard.setBounds(0, 54, 975, 330);
		schedule_panel.add(scheduleMainCard);
		scheduleMainCard.setLayout(new CardLayout(0, 0));


		grade123Card = new JPanel();
		scheduleMainCard.add(grade123Card, "name_460033522865900");
		grade123Card.setLayout(null);

		JPanel englishCard = new JPanel();
		englishCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(128, 128, 128)));
		englishCard.setLayout(null);
		englishCard.setBounds(0, 0, 975, 40);
		grade123Card.add(englishCard);

		JLabel lblNewLabel_5 = new JLabel("English");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5.setBounds(0, 0, 230, 40);
		englishCard.add(lblNewLabel_5);

		JLabel lblNewLabel_5_8 = new JLabel("8M - 9M");
		lblNewLabel_5_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8.setBounds(240, 0, 230, 40);
		englishCard.add(lblNewLabel_5_8);

		english = new JLabel("1 - A");
		english.setHorizontalAlignment(SwingConstants.CENTER);
		english.setFont(new Font("Arial", Font.PLAIN, 18));
		english.setBounds(505, 0, 230, 40);
		englishCard.add(english);

		JLabel lblNewLabel_5_24 = new JLabel("Official");
		lblNewLabel_5_24.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24.setBounds(745, 0, 230, 40);
		englishCard.add(lblNewLabel_5_24);

		JPanel mathCard = new JPanel();
		mathCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(128, 128, 128)));
		mathCard.setLayout(null);
		mathCard.setBounds(0, 40, 975, 40);
		grade123Card.add(mathCard);

		JLabel lblNewLabel_5_1 = new JLabel("Math");
		lblNewLabel_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_1.setBounds(0, 0, 230, 40);
		mathCard.add(lblNewLabel_5_1);

		JLabel lblNewLabel_5_8_1 = new JLabel("9M - 10M");
		lblNewLabel_5_8_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_1.setBounds(240, 0, 230, 40);
		mathCard.add(lblNewLabel_5_8_1);

		math = new JLabel("1 - A");
		math.setHorizontalAlignment(SwingConstants.CENTER);
		math.setFont(new Font("Arial", Font.PLAIN, 18));
		math.setBounds(505, 0, 230, 40);
		mathCard.add(math);

		JLabel lblNewLabel_5_24_1 = new JLabel("Official");
		lblNewLabel_5_24_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_1.setBounds(745, 0, 230, 40);
		mathCard.add(lblNewLabel_5_24_1);

		JPanel scienceCard = new JPanel();
		scienceCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		scienceCard.setLayout(null);
		scienceCard.setBounds(0, 80, 975, 40);
		grade123Card.add(scienceCard);

		JLabel lblNewLabel_5_2 = new JLabel("Science");
		lblNewLabel_5_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_2.setBounds(0, 0, 230, 40);
		scienceCard.add(lblNewLabel_5_2);

		JLabel lblNewLabel_5_8_2 = new JLabel("10M - 11M");
		lblNewLabel_5_8_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_2.setBounds(240, 0, 230, 40);
		scienceCard.add(lblNewLabel_5_8_2);

		science = new JLabel("SCIENCE LAB 1");
		science.setHorizontalAlignment(SwingConstants.CENTER);
		science.setFont(new Font("Arial", Font.PLAIN, 18));
		science.setBounds(505, 0, 230, 40);
		scienceCard.add(science);

		JLabel lblNewLabel_5_24_2 = new JLabel("Official");
		lblNewLabel_5_24_2.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_2.setBounds(745, 0, 230, 40);
		scienceCard.add(lblNewLabel_5_24_2);

		JPanel apCard = new JPanel();
		apCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		apCard.setLayout(null);
		apCard.setBounds(0, 120, 975, 40);
		grade123Card.add(apCard);

		JLabel lblNewLabel_5_3 = new JLabel("Araling Panlipunan");
		lblNewLabel_5_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_3.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_3.setBounds(0, 0, 230, 40);
		apCard.add(lblNewLabel_5_3);

		JLabel lblNewLabel_5_8_3 = new JLabel("11M - 12PM");
		lblNewLabel_5_8_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_3.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_3.setBounds(240, 0, 230, 40);
		apCard.add(lblNewLabel_5_8_3);

		AP = new JLabel("1 - B");
		AP.setHorizontalAlignment(SwingConstants.CENTER);
		AP.setFont(new Font("Arial", Font.PLAIN, 18));
		AP.setBounds(505, 0, 230, 40);
		apCard.add(AP);

		JLabel lblNewLabel_5_24_3 = new JLabel("Official");
		lblNewLabel_5_24_3.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_3.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_3.setBounds(745, 0, 230, 40);
		apCard.add(lblNewLabel_5_24_3);

		JPanel musicCard = new JPanel();
		musicCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		musicCard.setLayout(null);
		musicCard.setBounds(0, 160, 975, 40);
		grade123Card.add(musicCard);

		JLabel lblNewLabel_5_4 = new JLabel("Music");
		lblNewLabel_5_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_4.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_4.setBounds(0, 0, 230, 40);
		musicCard.add(lblNewLabel_5_4);

		JLabel lblNewLabel_5_8_4 = new JLabel("12PM - 1PM");
		lblNewLabel_5_8_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_4.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_4.setBounds(240, 0, 230, 40);
		musicCard.add(lblNewLabel_5_8_4);

		music = new JLabel("MUSIC LAB");
		music.setHorizontalAlignment(SwingConstants.CENTER);
		music.setFont(new Font("Arial", Font.PLAIN, 18));
		music.setBounds(505, 0, 230, 40);
		musicCard.add(music);

		JLabel lblNewLabel_5_24_4 = new JLabel("Official");
		lblNewLabel_5_24_4.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_4.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_4.setBounds(745, 0, 230, 40);
		musicCard.add(lblNewLabel_5_24_4);

		JPanel artsCard = new JPanel();
		artsCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		artsCard.setLayout(null);
		artsCard.setBounds(0, 200, 975, 40);
		grade123Card.add(artsCard);

		JLabel lblNewLabel_5_5 = new JLabel("Arts");
		lblNewLabel_5_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_5.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_5.setBounds(0, 0, 230, 40);
		artsCard.add(lblNewLabel_5_5);

		JLabel lblNewLabel_5_8_5 = new JLabel("1PM - 2PM");
		lblNewLabel_5_8_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_5.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_5.setBounds(240, 0, 230, 40);
		artsCard.add(lblNewLabel_5_8_5);

		arts = new JLabel("1 - B");
		arts.setHorizontalAlignment(SwingConstants.CENTER);
		arts.setFont(new Font("Arial", Font.PLAIN, 18));
		arts.setBounds(505, 0, 230, 40);
		artsCard.add(arts);

		JLabel lblNewLabel_5_24_5 = new JLabel("Official");
		lblNewLabel_5_24_5.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_5.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_5.setBounds(745, 0, 230, 40);
		artsCard.add(lblNewLabel_5_24_5);

		JPanel healthCard = new JPanel();
		healthCard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		healthCard.setLayout(null);
		healthCard.setBounds(0, 240, 975, 40);
		grade123Card.add(healthCard);

		JLabel lblNewLabel_5_6 = new JLabel("Health");
		lblNewLabel_5_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_6.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_6.setBounds(0, 0, 230, 40);
		healthCard.add(lblNewLabel_5_6);

		JLabel lblNewLabel_5_8_6 = new JLabel("2PM - 3PM");
		lblNewLabel_5_8_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_6.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_6.setBounds(240, 0, 230, 40);
		healthCard.add(lblNewLabel_5_8_6);

		health = new JLabel("1 - B");
		health.setHorizontalAlignment(SwingConstants.CENTER);
		health.setFont(new Font("Arial", Font.PLAIN, 18));
		health.setBounds(505, 0, 230, 40);
		healthCard.add(health);

		JLabel lblNewLabel_5_24_6 = new JLabel("Official");
		lblNewLabel_5_24_6.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_6.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_6.setBounds(745, 0, 230, 40);
		healthCard.add(lblNewLabel_5_24_6);

		JPanel PECard = new JPanel();
		PECard.setBorder(null);
		PECard.setLayout(null);
		PECard.setBounds(0, 280, 975, 40);
		grade123Card.add(PECard);

		JLabel lblNewLabel_5_7 = new JLabel("Physical Education");
		lblNewLabel_5_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_7.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_7.setBounds(0, 0, 230, 40);
		PECard.add(lblNewLabel_5_7);

		JLabel lblNewLabel_5_8_7 = new JLabel("3PM - 4:30PM");
		lblNewLabel_5_8_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_7.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_7.setBounds(240, 0, 230, 40);
		PECard.add(lblNewLabel_5_8_7);

		PE = new JLabel("GYM");
		PE.setHorizontalAlignment(SwingConstants.CENTER);
		PE.setFont(new Font("Arial", Font.PLAIN, 18));
		PE.setBounds(505, 0, 230, 40);
		PECard.add(PE);

		JLabel lblNewLabel_5_24_7 = new JLabel("Official");
		lblNewLabel_5_24_7.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_7.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_7.setBounds(745, 0, 230, 40);
		PECard.add(lblNewLabel_5_24_7);

		grade456Card = new JPanel();
		scheduleMainCard.add(grade456Card, "name_460114370477500");
		grade456Card.setLayout(null);

		JPanel englishCard_1 = new JPanel();
		englishCard_1.setLayout(null);
		englishCard_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(128, 128, 128)));
		englishCard_1.setBounds(0, 0, 975, 40);
		grade456Card.add(englishCard_1);

		JLabel lblNewLabel_5_9 = new JLabel("English");
		lblNewLabel_5_9.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_9.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_9.setBounds(0, 0, 230, 40);
		englishCard_1.add(lblNewLabel_5_9);

		JLabel lblNewLabel_5_8_8 = new JLabel("8M - 9M");
		lblNewLabel_5_8_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_8.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_8.setBounds(240, 0, 230, 40);
		englishCard_1.add(lblNewLabel_5_8_8);

		english1 = new JLabel("4 - A");
		english1.setHorizontalAlignment(SwingConstants.CENTER);
		english1.setFont(new Font("Arial", Font.PLAIN, 18));
		english1.setBounds(505, 0, 230, 40);
		englishCard_1.add(english1);

		JLabel lblNewLabel_5_24_8 = new JLabel("Official");
		lblNewLabel_5_24_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_8.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_8.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_8.setBounds(745, 0, 230, 40);
		englishCard_1.add(lblNewLabel_5_24_8);

		JPanel mathCard_1 = new JPanel();
		mathCard_1.setLayout(null);
		mathCard_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(128, 128, 128)));
		mathCard_1.setBounds(0, 40, 975, 40);
		grade456Card.add(mathCard_1);

		JLabel lblNewLabel_5_1_1 = new JLabel("Math");
		lblNewLabel_5_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_1_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_1_1.setBounds(0, 0, 230, 40);
		mathCard_1.add(lblNewLabel_5_1_1);

		JLabel lblNewLabel_5_8_1_1 = new JLabel("9M - 10M");
		lblNewLabel_5_8_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_1_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_1_1.setBounds(240, 0, 230, 40);
		mathCard_1.add(lblNewLabel_5_8_1_1);

		math1 = new JLabel("4 - A");
		math1.setHorizontalAlignment(SwingConstants.CENTER);
		math1.setFont(new Font("Arial", Font.PLAIN, 18));
		math1.setBounds(505, 0, 230, 40);
		mathCard_1.add(math1);

		JLabel lblNewLabel_5_24_1_1 = new JLabel("Official");
		lblNewLabel_5_24_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_1_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_1_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_1_1.setBounds(745, 0, 230, 40);
		mathCard_1.add(lblNewLabel_5_24_1_1);

		JPanel scienceCard_1 = new JPanel();
		scienceCard_1.setLayout(null);
		scienceCard_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		scienceCard_1.setBounds(0, 80, 975, 40);
		grade456Card.add(scienceCard_1);

		JLabel lblNewLabel_5_2_1 = new JLabel("Science");
		lblNewLabel_5_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_2_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_2_1.setBounds(0, 0, 230, 40);
		scienceCard_1.add(lblNewLabel_5_2_1);

		JLabel lblNewLabel_5_8_2_1 = new JLabel("10M - 11M");
		lblNewLabel_5_8_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_2_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_2_1.setBounds(240, 0, 230, 40);
		scienceCard_1.add(lblNewLabel_5_8_2_1);

		science1 = new JLabel("SCIENCE LAB 2");
		science1.setHorizontalAlignment(SwingConstants.CENTER);
		science1.setFont(new Font("Arial", Font.PLAIN, 18));
		science1.setBounds(505, 0, 230, 40);
		scienceCard_1.add(science1);

		JLabel lblNewLabel_5_24_2_1 = new JLabel("Official");
		lblNewLabel_5_24_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_2_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_2_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_2_1.setBounds(745, 0, 230, 40);
		scienceCard_1.add(lblNewLabel_5_24_2_1);

		JPanel TLEcard = new JPanel();
		TLEcard.setLayout(null);
		TLEcard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		TLEcard.setBounds(0, 120, 975, 40);
		grade456Card.add(TLEcard);

		JLabel lblNewLabel_5_3_1 = new JLabel("TLE");
		lblNewLabel_5_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_3_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_3_1.setBounds(0, 0, 230, 40);
		TLEcard.add(lblNewLabel_5_3_1);

		JLabel lblNewLabel_5_8_3_1 = new JLabel("11M - 12PM");
		lblNewLabel_5_8_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_3_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_3_1.setBounds(240, 0, 230, 40);
		TLEcard.add(lblNewLabel_5_8_3_1);

		TLE = new JLabel("4 - A");
		TLE.setHorizontalAlignment(SwingConstants.CENTER);
		TLE.setFont(new Font("Arial", Font.PLAIN, 18));
		TLE.setBounds(505, 0, 230, 40);
		TLEcard.add(TLE);

		JLabel lblNewLabel_5_24_3_1 = new JLabel("Official");
		lblNewLabel_5_24_3_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_3_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_3_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_3_1.setBounds(745, 0, 230, 40);
		TLEcard.add(lblNewLabel_5_24_3_1);

		JPanel ESPcard = new JPanel();
		ESPcard.setLayout(null);
		ESPcard.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		ESPcard.setBounds(0, 160, 975, 40);
		grade456Card.add(ESPcard);

		JLabel lblNewLabel_5_4_1 = new JLabel("ESP");
		lblNewLabel_5_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_4_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_4_1.setBounds(0, 0, 230, 40);
		ESPcard.add(lblNewLabel_5_4_1);

		JLabel lblNewLabel_5_8_4_1 = new JLabel("12PM - 1PM");
		lblNewLabel_5_8_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_4_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_4_1.setBounds(240, 0, 230, 40);
		ESPcard.add(lblNewLabel_5_8_4_1);

		ESP = new JLabel("4 - B");
		ESP.setHorizontalAlignment(SwingConstants.CENTER);
		ESP.setFont(new Font("Arial", Font.PLAIN, 18));
		ESP.setBounds(505, 0, 230, 40);
		ESPcard.add(ESP);

		JLabel lblNewLabel_5_24_4_1 = new JLabel("Official");
		lblNewLabel_5_24_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_4_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_4_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_4_1.setBounds(745, 0, 230, 40);
		ESPcard.add(lblNewLabel_5_24_4_1);

		JPanel musicCard2 = new JPanel();
		musicCard2.setLayout(null);
		musicCard2.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		musicCard2.setBounds(0, 200, 975, 40);
		grade456Card.add(musicCard2);

		JLabel lblNewLabel_5_5_1 = new JLabel("Music");
		lblNewLabel_5_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_5_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_5_1.setBounds(0, 0, 230, 40);
		musicCard2.add(lblNewLabel_5_5_1);

		JLabel lblNewLabel_5_8_5_1 = new JLabel("1PM - 2PM");
		lblNewLabel_5_8_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_5_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_5_1.setBounds(240, 0, 230, 40);
		musicCard2.add(lblNewLabel_5_8_5_1);

		music1 = new JLabel("MUSIC LAB");
		music1.setHorizontalAlignment(SwingConstants.CENTER);
		music1.setFont(new Font("Arial", Font.PLAIN, 18));
		music1.setBounds(505, 0, 230, 40);
		musicCard2.add(music1);

		JLabel lblNewLabel_5_24_5_1 = new JLabel("Official");
		lblNewLabel_5_24_5_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_5_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_5_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_5_1.setBounds(745, 0, 230, 40);
		musicCard2.add(lblNewLabel_5_24_5_1);

		JPanel healthCard_1 = new JPanel();
		healthCard_1.setLayout(null);
		healthCard_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.GRAY));
		healthCard_1.setBounds(0, 240, 975, 40);
		grade456Card.add(healthCard_1);

		JLabel lblNewLabel_5_6_1 = new JLabel("Health");
		lblNewLabel_5_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_6_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_6_1.setBounds(0, 0, 230, 40);
		healthCard_1.add(lblNewLabel_5_6_1);

		JLabel lblNewLabel_5_8_6_1 = new JLabel("2PM - 3PM");
		lblNewLabel_5_8_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_6_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_6_1.setBounds(240, 0, 230, 40);
		healthCard_1.add(lblNewLabel_5_8_6_1);

		health1 = new JLabel("4 - B");
		health1.setHorizontalAlignment(SwingConstants.CENTER);
		health1.setFont(new Font("Arial", Font.PLAIN, 18));
		health1.setBounds(505, 0, 230, 40);
		healthCard_1.add(health1);

		JLabel lblNewLabel_5_24_6_1 = new JLabel("Official");
		lblNewLabel_5_24_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_6_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_6_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_6_1.setBounds(745, 0, 230, 40);
		healthCard_1.add(lblNewLabel_5_24_6_1);

		JPanel PECard_1 = new JPanel();
		PECard_1.setLayout(null);
		PECard_1.setBorder(null);
		PECard_1.setBounds(0, 280, 975, 40);
		grade456Card.add(PECard_1);

		JLabel lblNewLabel_5_7_1 = new JLabel("Physical Education");
		lblNewLabel_5_7_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_7_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_7_1.setBounds(0, 0, 230, 40);
		PECard_1.add(lblNewLabel_5_7_1);

		JLabel lblNewLabel_5_8_7_1 = new JLabel("3PM - 4:30PM");
		lblNewLabel_5_8_7_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_8_7_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_8_7_1.setBounds(240, 0, 230, 40);
		PECard_1.add(lblNewLabel_5_8_7_1);

		PE1 = new JLabel("GYM");
		PE1.setHorizontalAlignment(SwingConstants.CENTER);
		PE1.setFont(new Font("Arial", Font.PLAIN, 18));
		PE1.setBounds(505, 0, 230, 40);
		PECard_1.add(PE1);

		JLabel lblNewLabel_5_24_7_1 = new JLabel("Official");
		lblNewLabel_5_24_7_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5_24_7_1.setForeground(new Color(51, 153, 0));
		lblNewLabel_5_24_7_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_5_24_7_1.setBounds(745, 0, 230, 40);
		PECard_1.add(lblNewLabel_5_24_7_1);

		assessment_card = new JPanel();
		assessment_card.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		assessment_card.setBackground(Color.WHITE);
		content_cardLayout.add(assessment_card, "name_370562724073300");
		assessment_card.setLayout(null);

		JPanel list_panel = new JPanel();
		list_panel.setBounds(150, 11, 695, 571);
		assessment_card.add(list_panel);
		list_panel.setLayout(null);

		JPanel listMenu = new JPanel();
		listMenu.setBackground(new Color(51, 153, 0));
		listMenu.setBounds(0, 0, 695, 78);
		list_panel.add(listMenu);
		listMenu.setLayout(null);

		JLabel accountlbl = new JLabel("Account No:");
		accountlbl.setForeground(Color.WHITE);
		accountlbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		accountlbl.setHorizontalAlignment(SwingConstants.CENTER);
		accountlbl.setBounds(10, 11, 138, 39);
		listMenu.add(accountlbl);

		JLabel studlbl = new JLabel("Student's Name:");
		studlbl.setHorizontalAlignment(SwingConstants.CENTER);
		studlbl.setForeground(Color.WHITE);
		studlbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		studlbl.setBounds(262, 11, 171, 39);
		listMenu.add(studlbl);

		JLabel datelbl = new JLabel("Date/Time");
		datelbl.setHorizontalAlignment(SwingConstants.CENTER);
		datelbl.setForeground(Color.WHITE);
		datelbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		datelbl.setBounds(518, 11, 138, 39);
		listMenu.add(datelbl);

		JLabel studIDlbl = new JLabel(studentID);
		studIDlbl.setHorizontalAlignment(SwingConstants.CENTER);
		studIDlbl.setForeground(Color.WHITE);
		studIDlbl.setFont(new Font("Arial Black", Font.PLAIN, 15));
		studIDlbl.setBounds(10, 39, 138, 39);
		listMenu.add(studIDlbl);

		JLabel dateTimelbl = new JLabel(getCurrentDate());
		dateTimelbl.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimelbl.setForeground(Color.WHITE);
		dateTimelbl.setFont(new Font("Arial Black", Font.PLAIN, 15));
		dateTimelbl.setBounds(489, 39, 196, 39);
		listMenu.add(dateTimelbl);

		String assessName = firstname + " " + lastname;
		JLabel studnamelbl = new JLabel(assessName);
		studnamelbl.setHorizontalAlignment(SwingConstants.CENTER);
		studnamelbl.setForeground(Color.WHITE);
		studnamelbl.setFont(new Font("Arial Black", Font.PLAIN, 15));
		studnamelbl.setBounds(262, 39, 171, 39);
		listMenu.add(studnamelbl);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(128, 128, 128)));
		panel_1_1.setBounds(0, 76, 695, 45);
		list_panel.add(panel_1_1);
		panel_1_1.setLayout(null);

		JLabel lblNewLabel_7 = new JLabel("Charge Description");
		lblNewLabel_7.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(0, 0, 238, 45);
		panel_1_1.add(lblNewLabel_7);

		JLabel lblNewLabel_7_1 = new JLabel("Amount");
		lblNewLabel_7_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_7_1.setBounds(457, 0, 238, 45);
		panel_1_1.add(lblNewLabel_7_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_1.setBounds(0, 121, 695, 45);
		list_panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel_7_2 = new JLabel("Subject fee");
		lblNewLabel_7_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2.setBounds(0, 0, 238, 45);
		panel_1.add(lblNewLabel_7_2);

		String parseTuition = Integer.toString(tuition);
		tuitionlbl = new JLabel("₱ " + parseTuition);
		tuitionlbl.setHorizontalAlignment(SwingConstants.CENTER);
		tuitionlbl.setFont(new Font("Arial", Font.PLAIN, 18));
		tuitionlbl.setBounds(457, 0, 238, 45);
		panel_1.add(tuitionlbl);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_2.setBounds(0, 166, 695, 45);
		list_panel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblNewLabel_7_2_1 = new JLabel("Athletics Fee");
		lblNewLabel_7_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_1.setBounds(0, 0, 238, 45);
		panel_2.add(lblNewLabel_7_2_1);

		athletics_fee = new JLabel("₱ 300");
		athletics_fee.setHorizontalAlignment(SwingConstants.CENTER);
		athletics_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		athletics_fee.setBounds(457, 0, 238, 45);
		panel_2.add(athletics_fee);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_3.setBounds(0, 211, 695, 45);
		list_panel.add(panel_3);
		panel_3.setLayout(null);

		JLabel lblNewLabel_7_2_2 = new JLabel("Audio Visual Fee");
		lblNewLabel_7_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_2.setBounds(0, 0, 238, 45);
		panel_3.add(lblNewLabel_7_2_2);

		audio_fee = new JLabel("₱ 410");
		audio_fee.setHorizontalAlignment(SwingConstants.CENTER);
		audio_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		audio_fee.setBounds(457, 0, 238, 45);
		panel_3.add(audio_fee);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_4.setBounds(0, 256, 695, 45);
		list_panel.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblNewLabel_7_2_3 = new JLabel("Guidance Fee");
		lblNewLabel_7_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_3.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_3.setBounds(0, 0, 238, 45);
		panel_4.add(lblNewLabel_7_2_3);

		guidance_fee = new JLabel("₱ 350");
		guidance_fee.setHorizontalAlignment(SwingConstants.CENTER);
		guidance_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		guidance_fee.setBounds(457, 0, 238, 45);
		panel_4.add(guidance_fee);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_5.setBounds(0, 301, 695, 45);
		list_panel.add(panel_5);
		panel_5.setLayout(null);

		JLabel lblNewLabel_7_2_4 = new JLabel("Internet Fee");
		lblNewLabel_7_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_4.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_4.setBounds(0, 0, 238, 45);
		panel_5.add(lblNewLabel_7_2_4);

		internet_fee = new JLabel("₱ 350");
		internet_fee.setHorizontalAlignment(SwingConstants.CENTER);
		internet_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		internet_fee.setBounds(457, 0, 238, 45);
		panel_5.add(internet_fee);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_6.setBounds(0, 346, 695, 45);
		list_panel.add(panel_6);
		panel_6.setLayout(null);

		JLabel lblNewLabel_7_2_5 = new JLabel("Library Fee");
		lblNewLabel_7_2_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_5.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_5.setBounds(0, 0, 238, 45);
		panel_6.add(lblNewLabel_7_2_5);

		library_fee = new JLabel("₱ 1,250");
		library_fee.setHorizontalAlignment(SwingConstants.CENTER);
		library_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		library_fee.setBounds(457, 0, 238, 45);
		panel_6.add(library_fee);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_7.setBounds(0, 391, 695, 45);
		list_panel.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblNewLabel_7_2_6 = new JLabel("Medical and Dental Fee");
		lblNewLabel_7_2_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_6.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_6.setBounds(0, 0, 238, 45);
		panel_7.add(lblNewLabel_7_2_6);

		medDen_fee = new JLabel("₱ 340");
		medDen_fee.setHorizontalAlignment(SwingConstants.CENTER);
		medDen_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		medDen_fee.setBounds(457, 0, 238, 45);
		panel_7.add(medDen_fee);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_8.setBounds(0, 436, 695, 45);
		list_panel.add(panel_8);
		panel_8.setLayout(null);

		JLabel lblNewLabel_7_2_7 = new JLabel("Student Insurance");
		lblNewLabel_7_2_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_7.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_7.setBounds(0, 0, 238, 45);
		panel_8.add(lblNewLabel_7_2_7);

		insurance_fee = new JLabel("₱ 200");
		insurance_fee.setHorizontalAlignment(SwingConstants.CENTER);
		insurance_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		insurance_fee.setBounds(457, 0, 238, 45);
		panel_8.add(insurance_fee);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new MatteBorder(0, 0, 1, 0, (Color) Color.GRAY));
		panel_9.setBounds(0, 481, 695, 45);
		list_panel.add(panel_9);
		panel_9.setLayout(null);

		JLabel lblNewLabel_7_2_8 = new JLabel("Examination Booklet Fee");
		lblNewLabel_7_2_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_8.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_8.setBounds(0, 0, 238, 45);
		panel_9.add(lblNewLabel_7_2_8);

		booklet_fee = new JLabel("₱ 140");
		booklet_fee.setHorizontalAlignment(SwingConstants.CENTER);
		booklet_fee.setFont(new Font("Arial", Font.PLAIN, 18));
		booklet_fee.setBounds(457, 0, 238, 45);
		panel_9.add(booklet_fee);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(null);
		panel_10.setBounds(0, 526, 695, 45);
		list_panel.add(panel_10);
		panel_10.setLayout(null);

		JLabel lblNewLabel_7_2_9 = new JLabel("Total Assessment:");
		lblNewLabel_7_2_9.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7_2_9.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewLabel_7_2_9.setBounds(0, 0, 238, 45);
		panel_10.add(lblNewLabel_7_2_9);

		JLabel total_assessmentlbl = new JLabel("₱ " + totalAssessment());
		total_assessmentlbl.setHorizontalAlignment(SwingConstants.CENTER);
		total_assessmentlbl.setFont(new Font("Arial", Font.PLAIN, 18));
		total_assessmentlbl.setBounds(457, 0, 238, 45);
		panel_10.add(total_assessmentlbl);

		JPanel panel = new JPanel();
		panel.setBounds(150, 88, 695, 45);
		assessment_card.add(panel);

		//PROMI 30% DEDUCTION IN CURRENT BALANCE
		double balance1 = (double) current_balance;
		int promi = (int) (current_balance * 0.30);
		int promiBal = current_balance - promi;

		payhistory_card = new JPanel();
		payhistory_card.setBackground(Color.WHITE);
		content_cardLayout.add(payhistory_card, "name_370622586313800");
		payhistory_card.setLayout(null);

		JLabel lblPayments = new JLabel("Payments");
		lblPayments.setHorizontalAlignment(SwingConstants.CENTER);
		lblPayments.setForeground(Color.BLACK);
		lblPayments.setFont(new Font("Arial", Font.BOLD, 36));
		lblPayments.setBounds(340, 25, 315, 46);
		payhistory_card.add(lblPayments);

		JPanel payHistory = new JPanel();
		payHistory.setBackground(new Color(51, 153, 0));
		payHistory.setBounds(10, 92, 975, 523);
		payhistory_card.add(payHistory);
		payHistory.setLayout(null);

		JPanel panel_12 = new JPanel();
		panel_12.setBounds(0, 0, 975, 92);
		panel_12.setBackground(new Color(51, 153, 0));
		payHistory.add(panel_12);
		panel_12.setLayout(null);

		JLabel lblNewLabel_6_4 = new JLabel("Account No:");
		lblNewLabel_6_4.setBounds(29, 11, 138, 39);
		panel_12.add(lblNewLabel_6_4);
		lblNewLabel_6_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_4.setForeground(Color.WHITE);
		lblNewLabel_6_4.setFont(new Font("Arial Black", Font.PLAIN, 18));

		JLabel receiptStudID = new JLabel(studentID);
		receiptStudID.setBounds(29, 42, 138, 39);
		panel_12.add(receiptStudID);
		receiptStudID.setHorizontalAlignment(SwingConstants.CENTER);
		receiptStudID.setForeground(Color.WHITE);
		receiptStudID.setFont(new Font("Arial Black", Font.PLAIN, 15));

		JLabel lblNewLabel_6_1_2 = new JLabel("Student's Name:");
		lblNewLabel_6_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1_2.setForeground(Color.WHITE);
		lblNewLabel_6_1_2.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNewLabel_6_1_2.setBounds(402, 11, 171, 39);
		panel_12.add(lblNewLabel_6_1_2);

		JLabel receiptNamelbl = new JLabel(firstname + " " + lastname);
		receiptNamelbl.setHorizontalAlignment(SwingConstants.CENTER);
		receiptNamelbl.setForeground(Color.WHITE);
		receiptNamelbl.setFont(new Font("Arial Black", Font.PLAIN, 15));
		receiptNamelbl.setBounds(402, 42, 171, 39);
		panel_12.add(receiptNamelbl);

		JLabel lblNewLabel_6_2_1 = new JLabel("Date/Time");
		lblNewLabel_6_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_2_1.setForeground(Color.WHITE);
		lblNewLabel_6_2_1.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNewLabel_6_2_1.setBounds(794, 11, 138, 39);
		panel_12.add(lblNewLabel_6_2_1);

		JLabel dateTimelbl_2 = new JLabel(getCurrentDate());
		dateTimelbl_2.setHorizontalAlignment(SwingConstants.CENTER);
		dateTimelbl_2.setForeground(Color.WHITE);
		dateTimelbl_2.setFont(new Font("Arial Black", Font.PLAIN, 15));
		dateTimelbl_2.setBounds(765, 42, 196, 39);
		panel_12.add(dateTimelbl_2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 91, 975, 438);
		payHistory.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 18));
		table.setRowHeight(32);
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"ID", "Total Payment", "Total Balance", "Purpose", "DateTime"
				}
				));
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);

		//Payment History Table in Database
		responsiveTable(table);

		total_transaction = new JLabel("TOTAL TRANSACTIONS : " + string_totalTrans);
		total_transaction.setBounds(660, 616, 325, 46);
		payhistory_card.add(total_transaction);
		total_transaction.setFont(new Font("Arial Black", Font.PLAIN, 16));
		total_transaction.setHorizontalAlignment(SwingConstants.CENTER);

		//GET THE UPDATED TOTAL TRANSACTION
		totalTransaction();

		profile_card = new JPanel();
		profile_card.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, null, Color.LIGHT_GRAY, null));
		profile_card.setBackground(Color.WHITE);
		content_cardLayout.add(profile_card, "name_370657810447400");
		profile_card.setLayout(null);

		JLabel lblStudentProfile = new JLabel("Student Profile");
		lblStudentProfile.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudentProfile.setForeground(Color.DARK_GRAY);
		lblStudentProfile.setFont(new Font("Arial", Font.BOLD, 36));
		lblStudentProfile.setBounds(340, 49, 315, 46);
		profile_card.add(lblStudentProfile);

		JPanel avatar_panel = new JPanel();
		avatar_panel.setBackground(new Color(204, 153, 204));
		avatar_panel.setBounds(10, 161, 321, 238);
		profile_card.add(avatar_panel);
		avatar_panel.setLayout(null);

		JLabel avatarlbl = new JLabel("");
		avatarlbl.setBounds(10, 11, 298, 216);
		avatar_panel.add(avatarlbl);
		avatarlbl.setBorder(null);
		avatarlbl.setHorizontalAlignment(SwingConstants.CENTER);
		avatarlbl.setIcon(new ImageIcon(avatar1));

		JPanel info_panel = new JPanel();
		info_panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		info_panel.setBackground(Color.WHITE);
		info_panel.setBounds(339, 161, 646, 460);
		profile_card.add(info_panel);
		info_panel.setLayout(null);

		JLabel lblNewLabel_12 = new JLabel("General Information");
		lblNewLabel_12.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNewLabel_12.setBounds(10, 11, 266, 36);
		info_panel.add(lblNewLabel_12);

		JLabel lblNewLabel_13 = new JLabel("Student ID :");
		lblNewLabel_13.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13.setBounds(10, 58, 147, 30);
		info_panel.add(lblNewLabel_13);

		JLabel lblNewLabel_13_1 = new JLabel("Firstname :");
		lblNewLabel_13_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13_1.setBounds(10, 99, 147, 30);
		info_panel.add(lblNewLabel_13_1);

		JLabel lblNewLabel_13_2 = new JLabel("Lastname :");
		lblNewLabel_13_2.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13_2.setBounds(10, 140, 147, 30);
		info_panel.add(lblNewLabel_13_2);

		JLabel lblNewLabel_13_3 = new JLabel("Age :");
		lblNewLabel_13_3.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13_3.setBounds(10, 181, 147, 30);
		info_panel.add(lblNewLabel_13_3);

		JLabel lblNewLabel_13_4 = new JLabel("Gender :");
		lblNewLabel_13_4.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13_4.setBounds(10, 222, 147, 30);
		info_panel.add(lblNewLabel_13_4);

		JLabel lblNewLabel_13_4_1 = new JLabel("Grade : ");
		lblNewLabel_13_4_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_13_4_1.setBounds(10, 263, 147, 30);
		info_panel.add(lblNewLabel_13_4_1);

		studentIDtxt = new JTextField(studentID);
		studentIDtxt.setFont(new Font("Arial", Font.PLAIN, 18));
		studentIDtxt.setBounds(178, 58, 458, 28);
		info_panel.add(studentIDtxt);
		studentIDtxt.setColumns(10);

		lastnametxt = new JTextField(lastname);
		lastnametxt.setFont(new Font("Arial", Font.PLAIN, 18));
		lastnametxt.setColumns(10);
		lastnametxt.setBounds(178, 140, 458, 28);
		info_panel.add(lastnametxt);

		agetxt = new JTextField(stud_age);
		agetxt.setFont(new Font("Arial", Font.PLAIN, 18));
		agetxt.setColumns(10);
		agetxt.setBounds(178, 181, 458, 28);
		info_panel.add(agetxt);

		firstnametxt = new JTextField(firstname);
		firstnametxt.setFont(new Font("Arial", Font.PLAIN, 18));
		firstnametxt.setColumns(10);
		firstnametxt.setBounds(178, 99, 458, 28);
		info_panel.add(firstnametxt);

		genderBox = new JComboBox(gender);
		genderBox.setBackground(Color.WHITE);
		genderBox.setFont(new Font("Arial", Font.PLAIN, 18));
		genderBox.setBounds(178, 222, 458, 28);
		info_panel.add(genderBox);

		gradeBox = new JComboBox(grade);
		gradeBox.setBackground(Color.WHITE);
		gradeBox.setFont(new Font("Arial", Font.PLAIN, 18));
		gradeBox.setBounds(178, 263, 458, 28);
		info_panel.add(gradeBox);

		JPanel changes_panel = new JPanel();
		changes_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//--UPDATING VALUE OF DATABASE
				try {				
					int age = Integer.parseInt(agetxt.getText());
					pst = con.prepareStatement("UPDATE `student_credentials` SET `Firstname` = ?,`Lastname` = ?,`Gender` = ?,`Age` = ?, `Grade` = ? WHERE `Student ID` = ?");
					pst.setString(1, firstnametxt.getText());
					pst.setString(2, lastnametxt.getText());
					pst.setString(3, genderBox.getSelectedItem().toString());
					pst.setInt(4, age);
					pst.setString(5, gradeBox.getSelectedItem().toString());
					pst.setString(6, studentIDtxt.getText());

					int k = pst.executeUpdate();

					if(k == 1){
						JOptionPane.showMessageDialog(null, "Record has been successfully updated!!");
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				changes_panel.setBackground(Color.GREEN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				changes_panel.setBackground(Color.WHITE);
			}
		});

		//UPDATE ALL THE INFORMATION INSIDE THE SYSTEM
		firstnametxt.setText(firstname);
		lastnametxt.setText(lastname);
		agetxt.setText(stud_age);
		genderBox.setSelectedItem(stud_gender);
		gradeBox.setSelectedItem(stud_grade);
		profilenamelbl.setText(firstname.toUpperCase());

		changes_panel.setBounds(327, 411, 147, 38);
		info_panel.add(changes_panel);
		changes_panel.setLayout(null);

		changeslbl = new JLabel("Save Changes");
		changeslbl.setFont(new Font("Arial", Font.PLAIN, 18));
		changeslbl.setHorizontalAlignment(SwingConstants.CENTER);
		changeslbl.setBounds(0, 0, 147, 38);
		changes_panel.add(changeslbl);

		JPanel cancel_panel = new JPanel();
		cancel_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//RESET TO DEFAULT ALL THE INFORMATION INSIDE THE SYSTEM
				firstnametxt.setText(firstname);
				lastnametxt.setText(lastname);
				agetxt.setText(stud_age);
				genderBox.setSelectedItem(stud_gender);
				gradeBox.setSelectedItem(stud_grade);
				uppercase = firstname.toUpperCase();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				cancel_panel.setBackground(Color.RED);
				cancellbl.setForeground(Color.WHITE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				cancel_panel.setBackground(Color.WHITE);
				cancellbl.setForeground(Color.BLACK);
			}
		});
		cancel_panel.setLayout(null);
		cancel_panel.setBounds(489, 411, 147, 38);
		info_panel.add(cancel_panel);

		cancellbl = new JLabel("Cancel");
		cancellbl.setHorizontalAlignment(SwingConstants.CENTER);
		cancellbl.setFont(new Font("Arial", Font.PLAIN, 18));
		cancellbl.setBounds(0, 0, 147, 38);
		cancel_panel.add(cancellbl);
		setLocationRelativeTo(null);


	}
}
