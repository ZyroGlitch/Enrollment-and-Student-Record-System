package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changePassword extends JFrame {

	static Image sksuLogo = new ImageIcon(logIn.class.getResource("/Enrollment_img/sksu.png")).getImage().getScaledInstance(340,300,Image.SCALE_SMOOTH);

	private JPanel contentPane;
	private JTextField student_id;
	private JTextField txtNewPassword;
	private JTextField txtConfirmPassword;
	private JPanel panel;
	private JLabel exit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					changePassword frame = new changePassword();
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
	public changePassword() {
		Connect();
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 549);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel sksu_Logo = new JLabel("");
		sksu_Logo.setHorizontalAlignment(SwingConstants.CENTER);
		sksu_Logo.setBounds(0, 0, 496, 241);
		sksu_Logo.setIcon(new ImageIcon(sksuLogo));
		contentPane.add(sksu_Logo);

		student_id = new JTextField("Student ID Number");
		student_id.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				student_id.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				student_id.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				student_id.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
			}
		});
		student_id.setForeground(Color.BLACK);
		student_id.setFont(new Font("Arial", Font.PLAIN, 18));
		student_id.setColumns(10);
		student_id.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
		student_id.setBackground(Color.WHITE);
		student_id.setBounds(91, 278, 314, 40);
		contentPane.add(student_id);

		txtNewPassword = new JTextField("New Password");
		txtNewPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtNewPassword.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				txtNewPassword.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				txtNewPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
			}
		});
		txtNewPassword.setForeground(Color.BLACK);
		txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 18));
		txtNewPassword.setColumns(10);
		txtNewPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
		txtNewPassword.setBackground(Color.WHITE);
		txtNewPassword.setBounds(91, 329, 314, 40);
		contentPane.add(txtNewPassword);

		txtConfirmPassword = new JTextField("Confirm Password");
		txtConfirmPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtConfirmPassword.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				txtConfirmPassword.setBorder(new LineBorder(Color.CYAN, 2, true));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				txtConfirmPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
			}
		});
		txtConfirmPassword.setForeground(Color.BLACK);
		txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 18));
		txtConfirmPassword.setColumns(10);
		txtConfirmPassword.setBorder(new LineBorder(new Color(0, 0, 0), 2, false));
		txtConfirmPassword.setBackground(Color.WHITE);
		txtConfirmPassword.setBounds(91, 380, 314, 40);
		contentPane.add(txtConfirmPassword);

		JPanel login_panel = new JPanel();
		login_panel.setLayout(null);
		login_panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, null, Color.DARK_GRAY, null));
		login_panel.setBackground(new Color(51, 204, 0));
		login_panel.setBounds(149, 449, 198, 50);
		contentPane.add(login_panel);

		JLabel login_label = new JLabel("CHANGE PASSWORD");
		login_label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				String newPassword = txtNewPassword.getText();
				String confirmPassword = txtConfirmPassword.getText();

				if (!newPassword.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "New password and confirm password do not match. Please ensure they match before proceeding.");
				} else {
					try {
						pst = con.prepareStatement("UPDATE `student_credentials` SET `Password` = ? WHERE `Student ID` = ?");
						pst.setString(1, confirmPassword);
						pst.setString(2, student_id.getText());

						int k = pst.executeUpdate();

						if (k == 1) {
							JOptionPane.showMessageDialog(null, student_id.getText() + " password was successfully updated.");
							student_id.setText("Student ID Number");
							txtNewPassword.setText("New Password");
							txtConfirmPassword.setText("Confirm Password");

							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "The update for " + student_id.getText() + " password failed.");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
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
		login_label.setHorizontalAlignment(SwingConstants.CENTER);
		login_label.setForeground(Color.WHITE);
		login_label.setFont(new Font("Segoe UI Black", Font.BOLD, 16));
		login_label.setBounds(0, 0, 198, 50);
		login_panel.add(login_label);

		panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.BLACK);
		panel.setBounds(456, 0, 40, 40);
		contentPane.add(panel);

		exit = new JLabel("X");
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
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
