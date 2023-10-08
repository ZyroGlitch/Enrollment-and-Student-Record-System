package ENROLLMENT_SYSTEM;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JProgressBar;

public class loading extends JFrame {

	private JPanel contentPane;
	static Image profileIcon = new ImageIcon(dashboard.class.getResource("/Enrollment_img/sksu.png")).getImage().getScaledInstance(400,240,Image.SCALE_SMOOTH);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loading frame = new loading();
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
	public loading() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 592, 392);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel loadingIcon = new JLabel("");
		loadingIcon.setHorizontalAlignment(SwingConstants.CENTER);
		loadingIcon.setBounds(108, 35, 377, 207);
		loadingIcon.setIcon(new ImageIcon(profileIcon));
		contentPane.add(loadingIcon);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(51, 153, 0));
		progressBar.setBounds(10, 357, 572, 24);
		contentPane.add(progressBar);
		setLocationRelativeTo(null);

		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i <= 100; i++) {
					try {
						Thread.sleep(15);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					progressBar.setValue(i);
				}
				new dashboard().setVisible(true);
				dispose();
			}
		}) 
		.start();

	}
}
