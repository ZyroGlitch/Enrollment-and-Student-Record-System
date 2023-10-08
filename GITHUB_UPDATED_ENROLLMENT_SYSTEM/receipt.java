package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class receipt extends JFrame {

	static Image Logo = new ImageIcon(dashboard.class.getResource("/Enrollment_img/bill1.png")).getImage().getScaledInstance(104,78,Image.SCALE_SMOOTH);
	public JPanel contentPane;
	public static String firstname, lastname, description, amountPay;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					receipt frame = new receipt();
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
	public receipt() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 476, 592);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 153, 0));
		panel.setBounds(0, 0, 476, 100);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setBounds(24, 11, 104, 78);
		logo.setIcon(new ImageIcon(Logo));
		panel.add(logo);

		JLabel lblNewLabel = new JLabel("SULTAN KUDARAT");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(124, 11, 352, 50);
		panel.add(lblNewLabel);

		JLabel lblStateUniversity = new JLabel("STATE UNIVERSITY");
		lblStateUniversity.setForeground(Color.WHITE);
		lblStateUniversity.setHorizontalAlignment(SwingConstants.CENTER);
		lblStateUniversity.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblStateUniversity.setBounds(124, 39, 352, 50);
		panel.add(lblStateUniversity);

		JLabel studName = new JLabel(firstname + " " + lastname);
		studName.setFont(new Font("Arial Black", Font.PLAIN, 22));
		studName.setHorizontalAlignment(SwingConstants.CENTER);
		studName.setBounds(10, 111, 456, 46);
		contentPane.add(studName);

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.DARK_GRAY);
		separator_1.setBackground(Color.DARK_GRAY);
		separator_1.setBounds(32, 320, 413, 1);
		contentPane.add(separator_1);

		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setForeground(Color.DARK_GRAY);
		separator_1_1.setBackground(Color.DARK_GRAY);
		separator_1_1.setBounds(32, 168, 413, 1);
		contentPane.add(separator_1_1);

		JLabel lblNewLabel_2 = new JLabel("Description");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(32, 180, 180, 33);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_2_1 = new JLabel("Amount");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2_1.setBounds(265, 180, 180, 33);
		contentPane.add(lblNewLabel_2_1);

		JLabel purpose = new JLabel(description);
		purpose.setHorizontalAlignment(SwingConstants.CENTER);
		purpose.setFont(new Font("Arial", Font.PLAIN, 18));
		purpose.setBounds(32, 224, 180, 33);
		contentPane.add(purpose);

		JLabel amount = new JLabel(amountPay);
		amount.setHorizontalAlignment(SwingConstants.CENTER);
		amount.setFont(new Font("Arial", Font.PLAIN, 18));
		amount.setBounds(265, 224, 180, 33);
		contentPane.add(amount);

		JLabel lblNewLabel_2_4 = new JLabel("Total Amount Pay");
		lblNewLabel_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2_4.setBounds(32, 332, 180, 33);
		contentPane.add(lblNewLabel_2_4);

		JLabel lblNewLabel_2_4_1 = new JLabel("P " + amountPay);
		lblNewLabel_2_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2_4_1.setBounds(265, 332, 180, 33);
		contentPane.add(lblNewLabel_2_4_1);

		JLabel lblNewLabel_2_4_2 = new JLabel("Reference No:");
		lblNewLabel_2_4_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4_2.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2_4_2.setBounds(32, 405, 180, 33);
		contentPane.add(lblNewLabel_2_4_2);

		LocalDateTime datetime = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String formatDate = datetime.format(myFormatObj);
		String formattedDatetime= formatDate.toString();

		JLabel lblNewLabel_2_4_3 = new JLabel(formattedDatetime);
		lblNewLabel_2_4_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4_3.setFont(new Font("Arial Black", Font.PLAIN, 15));
		lblNewLabel_2_4_3.setBounds(265, 405, 180, 33);
		contentPane.add(lblNewLabel_2_4_3);

		//Generate A referrence number
		Random random = new Random();
		char char1 = (char) (random.nextInt(26) + 'a');
		char char2 = (char) (random.nextInt(26) + 'a');
		char char3 = (char) (random.nextInt(26) + 'a');
		String myString = (String.valueOf(char1) + String.valueOf(char2) + String.valueOf(char3)).toUpperCase();

		int min = 1000;
		int max = 9999;
		int randomNumber = random.nextInt(max - min + 1) + min;
		String studentID = myString + Integer.toString(randomNumber);

		JLabel lblNewLabel_2_4_2_1 = new JLabel(studentID);
		lblNewLabel_2_4_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_4_2_1.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblNewLabel_2_4_2_1.setBounds(32, 434, 180, 33);
		contentPane.add(lblNewLabel_2_4_2_1);

		JPanel print_pnl = new JPanel();
		print_pnl.setBackground(new Color(51, 153, 0));
		print_pnl.setBounds(32, 539, 413, 42);
		contentPane.add(print_pnl);
		print_pnl.setLayout(null);

		JLabel printlbl = new JLabel("Print Receipt");
		printlbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				print_pnl.setVisible(false);
				printlbl.setVisible(false);

				try {				
					BufferedImage bi = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
					contentPane.paint(bi.getGraphics());

					String userHome = System.getProperty("user.home");
					String path = userHome + File.separator + "printIcon.png";
					File outputFile = new File(path);

					ImageIO.write(bi, "png", outputFile);
					Desktop.getDesktop().print(outputFile);

				}catch(IOException e1) {
					JOptionPane.showMessageDialog(null, "Print failed!");
				}
				print_pnl.setVisible(true);
				printlbl.setVisible(true);

				dispose();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				print_pnl.setBackground(Color.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				print_pnl.setBackground(new Color(51, 153, 0));
			}
		});
		printlbl.setForeground(Color.WHITE);
		printlbl.setHorizontalAlignment(SwingConstants.CENTER);
		printlbl.setFont(new Font("Arial Black", Font.PLAIN, 18));
		printlbl.setBounds(0, 0, 413, 42);
		print_pnl.add(printlbl);
		setLocationRelativeTo(null);
	}
}
